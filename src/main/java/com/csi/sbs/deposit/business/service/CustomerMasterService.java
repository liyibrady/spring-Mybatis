package com.csi.sbs.deposit.business.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.csi.sbs.deposit.business.clientmodel.CustomerMaintenanceModel;
import com.csi.sbs.deposit.business.entity.CustomerMasterEntity;

public interface CustomerMasterService {
	
	
	   public List<CustomerMasterEntity> queryAll();
	   
	   public Map<String,Object> contactInformationUpdate(CustomerMaintenanceModel cmm,RestTemplate restTemplate);
	   
	   public CustomerMasterEntity findCustomerByCustomerID(CustomerMasterEntity cme);

}
