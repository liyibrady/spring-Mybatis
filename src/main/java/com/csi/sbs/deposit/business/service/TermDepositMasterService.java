package com.csi.sbs.deposit.business.service;


import java.util.Map;

import com.csi.sbs.deposit.business.clientmodel.TermDepositDrawDownModel;
import com.csi.sbs.deposit.business.clientmodel.TermDepositMasterModel;


public interface TermDepositMasterService {
	
	
	
	 public Map<String,Object> termDepositApplication(TermDepositMasterModel tdm);

		 
	 public Map<String,Object> termDepositDrawDown(TermDepositDrawDownModel tddm);
	 	 
	
}
