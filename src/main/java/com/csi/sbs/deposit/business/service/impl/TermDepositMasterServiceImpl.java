package com.csi.sbs.deposit.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.deposit.business.clientmodel.TermDepositDrawDownModel;
import com.csi.sbs.deposit.business.clientmodel.TermDepositMasterModel;
import com.csi.sbs.deposit.business.constant.SysConstant;
import com.csi.sbs.deposit.business.dao.AccountMasterDao;
import com.csi.sbs.deposit.business.dao.TermDepositMasterDao;
import com.csi.sbs.deposit.business.dao.TermDepositRateDao;
import com.csi.sbs.deposit.business.entity.AccountMasterEntity;
import com.csi.sbs.deposit.business.entity.TermDepositMasterEntity;
import com.csi.sbs.deposit.business.entity.TermDepositRateEntity;
import com.csi.sbs.deposit.business.service.TermDepositMasterService;
import com.csi.sbs.deposit.business.util.AvailableNumberUtil;
import com.csi.sbs.deposit.business.util.LogUtil;


@Service("TermDepositMasterService")
public class TermDepositMasterServiceImpl implements TermDepositMasterService{
	
	
	@Autowired
	RestTemplate restTemplate;
	
	@SuppressWarnings("rawtypes")
	@Resource
	private TermDepositMasterDao tdmDao;
	
	
	@SuppressWarnings("rawtypes")
	@Resource
	private AccountMasterDao amDao;  
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private TermDepositRateDao  tdrDao;
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String,Object> termDepositApplication(TermDepositMasterModel tdm) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		AccountMasterEntity  amentiry = new AccountMasterEntity();
		amentiry.setAccountnumber(tdm.getAccountnumber());		
		List<AccountMasterEntity> amList = amDao.findAccountByParams(amentiry);
		if(amList.size()==0){
			map.put("msg", "Record Not Found");
			map.put("code", "0");
			return map;
		}
		if(amList.get(0)!= null){
						
			if(!amList.get(0).getAccounttype().equals(SysConstant.ACCOUNT_TYPE4)){
				map.put("msg", "Record Not Found");
				map.put("code", "0");	
				return map;
			}
			if(amList.get(0).getBalance()==null || amList.get(0).getBalance().compareTo(tdm.getDepositamount())==-1){
				map.put("msg", "Insufficient Fund");
				map.put("code", "0");				
				return map;
			}
			
		}	
		
		String response = restTemplate.getForEntity("http://SYSADMIN/sysadmin/generate/getNextAvailableNumber/NextAvailableTDNumber", String.class).getBody();
		String tdNumber = JsonProcess.changeToJSONObject(response).getString("nextAvailableNumber");
		AvailableNumberUtil.availableNumberIncrease(restTemplate, "NextAvailableTDNumber");
		List<TermDepositRateEntity> tdrList = tdrDao.selectAll();
		BigDecimal rate = getRate(tdrList,tdm.getTermperiod(),tdm.getDepositamount(),map);
		if(rate==null){
			rate=new BigDecimal(0);
		}
		BigDecimal days = new BigDecimal(getMaturityDate(tdm.getMaturitydate()));
		BigDecimal tdinterest = tdm.getDepositamount().multiply(days).multiply(rate);
		BigDecimal tdMaturityAmount = tdm.getDepositamount().add(tdinterest);		
		tdm.setDepositnumber(tdNumber);
		tdm.setTerminterestrate(rate);
		tdm.setMaturityinterest(tdinterest);
		tdm.setMaturityamount(tdMaturityAmount);	
		tdmDao.insert(tdm);		
		//写入日志
		String logstr = "Transaction Accepted :" + tdm.getAccountnumber(); 
		LogUtil.saveLog(
				restTemplate, 
				SysConstant.OPERATION_UPDATE, 
				SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, 
				logstr);

		map.put("msg", "transaction accepted");
		map.put("TD Details", tdm);
		map.put("code", 0);		
		return map; 
			     			
	}	
	
	
	//validate termperiod and get rate
	private static BigDecimal getRate(List<TermDepositRateEntity>  tdrList,String termperiod , BigDecimal depositAmount,Map<String,Object> map){
			
		BigDecimal rate = null;
		
		List<String> surpportPeriods = new ArrayList<String>();		
		for(TermDepositRateEntity tdr : tdrList){
			surpportPeriods.add(tdr.getTdperiod().trim());
		}				
		if(!surpportPeriods.contains(termperiod)){
			map.put("msg", "not surpport termperiod");
		}
		
		String range = getAmountRange(depositAmount);
		for(TermDepositRateEntity tdr : tdrList){
			if(tdr.getTdperiod().equals(termperiod) && tdr.getDepositrange().equals(range)){
				
				rate = tdr.getTdinterestrate();
			}
		}
				
		return rate;
	}
	
	
	private static String getAmountRange(BigDecimal depositAmount){
			   
	   String[] ranges = {SysConstant.RANGE1,SysConstant.RANGE2,SysConstant.RANGE3,
			   SysConstant.RANGE4};
	   for(String range : ranges){
		    String[] amount = range.split("-");
		    if(amount.length==1 && depositAmount.compareTo(new BigDecimal(amount[0]))==1){
		    	return range;
		    }
		    if(amount.length==2){
		    	if(depositAmount.compareTo(new BigDecimal(amount[0]))==1
						&& depositAmount.compareTo(new BigDecimal(amount[1]))==-1){
					return range;
				}
		    }
			
	   }
		
	   return "";
	}
			
	private static long getMaturityDate(Date maturityDate){
		
		long timeMillis =maturityDate.getTime()-System.currentTimeMillis();
		long day = timeMillis/(1000*60*60*24);
		
		return day;
	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> termDepositDrawDown(TermDepositDrawDownModel tddm) {
         
		Map<String,Object> map = new HashMap<String,Object>();		
		AccountMasterEntity  amentiry = new AccountMasterEntity();
		amentiry.setAccountnumber(tddm.getAccountnumber());	
		List<AccountMasterEntity> amList = amDao.findAccountByParams(amentiry);
		if(amList.size()==0){
			map.put("msg", "Record Not Found");
			map.put("code", "0");
			return map;
		}
		
		TermDepositMasterEntity tdmentiry = new TermDepositMasterEntity();
		tdmentiry.setAccountnumber(tddm.getAccountnumber());
		tdmentiry.setDepositnumber(tddm.getDepositnumber());
		List<TermDepositMasterEntity> tdmList = tdmDao.getTDMaster(tdmentiry);
		if(tdmList.size()==0){
			map.put("msg", "Record Not Found");
			map.put("code", "0");
			return map;
		}
		TermDepositMasterEntity tdmdetails = tdmList.get(0);
		if(tdmdetails.getMaturitystatus().equals(SysConstant.STATUS_D)){
			map.put("msg", "TD record has been drawn down");
			map.put("code", "0");
			return map;
		}
		
	    int futureTime = tdmdetails.getMaturitydate().compareTo(new Date(System.currentTimeMillis()));
		if(futureTime != 1){
			map.put("msg", "Transaction is  not matured for drawdown");
			map.put("code", "0");
			return map;
			
		}
				
		amList.get(0).setBalance(tdmdetails.getMaturityamount());
		amDao.update(amList.get(0));		
		tdmDao.updateMatuirtyStatus(SysConstant.STATUS_D, tdmdetails.getAccountnumber(), tdmdetails.getDepositnumber());
				
		//写入日志
		String logstr = "Transaction Accepted :" + tddm.getAccountnumber(); 
		LogUtil.saveLog(
				restTemplate, 
				SysConstant.OPERATION_UPDATE, 
				SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, 
				logstr);

		map.put("msg", "transaction accepted");
		map.put("code", 0);		
		return map; 		
	}
}
