package com.csi.sbs.deposit.business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.csi.sbs.deposit.business.clientmodel.TermDepositEnquiryModel;
import com.csi.sbs.deposit.business.constant.SysConstant;
import com.csi.sbs.deposit.business.dao.TermDepositEnquiryDao;
import com.csi.sbs.deposit.business.entity.TermDepositEnquiryEntity;
import com.csi.sbs.deposit.business.service.TermDepositEnquiryService;
import com.csi.sbs.deposit.business.util.LogUtil;

@Service("TermDepositEnquiryService")
public  class TermDepositEnquiryServiceImpl implements TermDepositEnquiryService{
	
	
	@SuppressWarnings("rawtypes")
	@Resource
	private TermDepositEnquiryDao termDepositeEnquiryDao;
	

	@Override
	@Transactional
	public Map<String,Object> termDepositEnquiry(TermDepositEnquiryModel tdem,RestTemplate restTemplate) {
		Map<String,Object> map = new HashMap<String,Object>();
		//校验必填字段
		if(!validateField(tdem)){
			map.put("msg", "Record Not Found");
			map.put("code", "0");
			
			return map;
		}
		TermDepositEnquiryEntity tde = new TermDepositEnquiryEntity();
		tde.setAccountnumber(tdem.getAccountnumber());
		tde.setDepositnumber(tdem.getTdnumber());
		
		@SuppressWarnings("unchecked")
		List<TermDepositEnquiryEntity> tdlist = termDepositeEnquiryDao.termDepositEnquiry(tde);
		//写入日志
		String logstr = "Query success based on acount Number and tdnumber:accountNumber:"+tdem.getAccountnumber()+"tdNumber:"+tdem.getTdnumber();
		LogUtil.saveLog(
				restTemplate, 
				SysConstant.OPERATION_QUERY, 
				SysConstant.LOCAL_SERVICE_NAME, 
				SysConstant.OPERATION_SUCCESS, 
				logstr);
		
		map.put("list", tdlist);
		map.put("code", "1");
		
		return map;
	}
	
	private boolean validateField(TermDepositEnquiryModel tdem){
		if(tdem.getAccountnumber()==null || "".equals(tdem.getAccountnumber())){
			return false;
		}
		if(tdem.getTdnumber()==null || "".equals(tdem.getTdnumber())){
			return false;
		}
		return true;
	}
	
}
