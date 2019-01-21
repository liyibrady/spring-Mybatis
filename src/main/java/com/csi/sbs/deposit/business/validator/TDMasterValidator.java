package com.csi.sbs.deposit.business.validator;

import java.util.Map;

import com.csi.sbs.deposit.business.clientmodel.TermDepositDrawDownModel;
import com.csi.sbs.deposit.business.clientmodel.TermDepositMasterModel;
import com.mysql.jdbc.StringUtils;

public class TDMasterValidator {
	
	
	 public static Map<String,Object> validateTDMaster(TermDepositMasterModel tdm
			 ,Map<String,Object> map){
		 
		 if(tdm!=null){
			 if(StringUtils.isNullOrEmpty(tdm.getAccountnumber())){ 	            
				 map.put("code", "409");
				 map.put("msg", "account number is mandatory");	
				 return map;
			 }
			 			
			 if(validateObjectIfnull(tdm.getDepositamount())){ 	            
				 map.put("code", "409");
				 map.put("msg", "deposit amount is mandatory");	
				 return map;
			 }	
			 if(StringUtils.isNullOrEmpty(tdm.getTdccy())){ 	            
				 map.put("code", "409");
				 map.put("msg", "deposit amount ccy is mandatory");	
				 return map;
			 }			
			 if(StringUtils.isNullOrEmpty(tdm.getTermperiod())){ 	            
				 map.put("code", "409");
				 map.put("msg", "term period is mandatory");	
				 return map;
			 }			 
		 }
		 
		 return map;
		 
	 }
	 
	 
	 public static Map<String,Object> validateTDDrawDown(TermDepositDrawDownModel tddm
			 ,Map<String,Object> map){
		 
		 if(tddm!=null){
			 if(StringUtils.isNullOrEmpty(tddm.getAccountnumber())){ 	            
				 map.put("code", "409");
				 map.put("msg", "account number is mandatory");	
				 return map;
			 }
			 
			 if(StringUtils.isNullOrEmpty(tddm.getDepositnumber())){ 	            
				 map.put("code", "409");
				 map.put("msg", "deposit number is mandatory");	
				 return map;
			 }
			 if(validateObjectIfnull(tddm.getDepositamount())){ 	            
				 map.put("code", "409");
				 map.put("msg", "deposit amount is mandatory");	
				 return map;
			 }		 
			 if(StringUtils.isNullOrEmpty(tddm.getTdccy())){ 	            
				 map.put("code", "409");
				 map.put("msg", "td ccy is mandatory");	
				 return map;
			 }
			 
			 if(StringUtils.isNullOrEmpty(tddm.getCreditAccountNumber())){ 	            
				 map.put("code", "409");
				 map.put("msg", "credit account number is mandatory");	
				 return map;
			 }			
			 	 
		 }
		 
		 return map;
		 
	 }
	 
	 
	 
	 private static boolean validateObjectIfnull(Object obj){
		
		 boolean empty = false;		 
		 if( null ==obj || (null!=obj && StringUtils.isNullOrEmpty(obj.toString()))){
			 empty = true;
		 }
		 
		 return empty;
		 
	 }
	 
	

}
