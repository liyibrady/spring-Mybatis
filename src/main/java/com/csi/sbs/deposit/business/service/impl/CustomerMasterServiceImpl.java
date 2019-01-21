package com.csi.sbs.deposit.business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.csi.sbs.deposit.business.clientmodel.CustomerMaintenanceModel;
import com.csi.sbs.deposit.business.constant.SysConstant;
import com.csi.sbs.deposit.business.dao.AccountMasterDao;
import com.csi.sbs.deposit.business.dao.CustomerMasterDao;
import com.csi.sbs.deposit.business.entity.AccountMasterEntity;
import com.csi.sbs.deposit.business.entity.CustomerMasterEntity;
import com.csi.sbs.deposit.business.service.CustomerMasterService;
import com.csi.sbs.deposit.business.util.LogUtil;


@Service("CustomerMasterService")
public class CustomerMasterServiceImpl implements CustomerMasterService{
	
	
	@SuppressWarnings("rawtypes")
	@Resource
	private CustomerMasterDao customerMasterDao;
	
	@SuppressWarnings("rawtypes")
	@Resource
	private AccountMasterDao accountMasterDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerMasterEntity> queryAll() {
		return customerMasterDao.queryAll();
	}

	@Override
	@Transactional
	public Map<String,Object> contactInformationUpdate(CustomerMaintenanceModel cmm,RestTemplate restTemplate) {
		Map<String,Object> map = new HashMap<String,Object>();
		AccountMasterEntity ame = new AccountMasterEntity();
		ame.setAccountnumber(cmm.getAccountNumber());
		// 根据accountNumber查询账号
		@SuppressWarnings("unchecked")
		List<AccountMasterEntity> accountList = accountMasterDao.findAccountByParams(ame);
		if (accountList == null || accountList.size() == 0) {
			map.put("msg", "Record Not Found");
			map.put("code", "0");
			return map;
		}
		CustomerMasterEntity customer = new CustomerMasterEntity();
		customer.setMailingaddress(cmm.getMailingAddress());
		customer.setMobilephonenumber(cmm.getMobilePhoneNumber());
		customer.setCustomerid(cmm.getCustomerID());
		customerMasterDao.contactInformationUpdate(customer);
		String logstr = "update accountNumber:" + cmm.getAccountNumber()
		+ " contact information success!";
		LogUtil.saveLog(
				restTemplate, 
				SysConstant.OPERATION_UPDATE, 
				SysConstant.LOCAL_SERVICE_NAME, 
				SysConstant.OPERATION_SUCCESS, 
				logstr);
		
		map.put("msg", cmm.getAccountNumber()
					+ "-Customer Contact Information already Record Changed");
		map.put("code", "1");
		return map;
	}

	@Override
	public CustomerMasterEntity findCustomerByCustomerID(CustomerMasterEntity cme) {
		return customerMasterDao.findCustomerByCustomerID(cme);
	}

}
