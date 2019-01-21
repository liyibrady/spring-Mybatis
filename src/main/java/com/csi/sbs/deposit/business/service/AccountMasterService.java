package com.csi.sbs.deposit.business.service;


import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.csi.sbs.deposit.business.clientmodel.AccountEnquiryModel;
import com.csi.sbs.deposit.business.clientmodel.ChequeBookModel;
import com.csi.sbs.deposit.business.clientmodel.CloseAccountModel;
import com.csi.sbs.deposit.business.clientmodel.DepositModel;
import com.csi.sbs.deposit.business.clientmodel.TransactionModel;
import com.csi.sbs.deposit.business.clientmodel.WithDrawalModel;
import com.csi.sbs.deposit.business.entity.AccountMasterEntity;

public interface AccountMasterService {
	
	
	   public Map<String,Object> accountEnquiry(AccountEnquiryModel accountEnquiryModel,RestTemplate restTemplate);
	   
	   public List<AccountMasterEntity> findAccountByParams(AccountMasterEntity ame);
	   
	   public Map<String,Object> closeAccount(CloseAccountModel cam,RestTemplate restTemplate) throws ParseException;
	   
	   public Map<String,Object> deposit(DepositModel depositModel,RestTemplate restTemplate) throws ParseException;
	   
	   public int update(AccountMasterEntity ame);
	   
	   public Map<String,Object> withdrawal(WithDrawalModel withDrawalModel,RestTemplate restTemplate) throws ParseException;

	   public Map<String,Object> transfer(TransactionModel transactionModel,RestTemplate restTemplate) throws ParseException;
	   
	   public Map<String,Object> chequeBookRequest(ChequeBookModel cbm,RestTemplate restTemplate);
	   
}
