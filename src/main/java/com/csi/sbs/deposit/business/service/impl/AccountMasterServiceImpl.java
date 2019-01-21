package com.csi.sbs.deposit.business.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.constant.CommonConstant;
import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.deposit.business.clientmodel.AccountEnquiryModel;
import com.csi.sbs.deposit.business.clientmodel.ChequeBookModel;
import com.csi.sbs.deposit.business.clientmodel.CloseAccountModel;
import com.csi.sbs.deposit.business.clientmodel.CurrencyModel;
import com.csi.sbs.deposit.business.clientmodel.DepositModel;
import com.csi.sbs.deposit.business.clientmodel.TransactionModel;
import com.csi.sbs.deposit.business.clientmodel.WithDrawalModel;
import com.csi.sbs.deposit.business.constant.SysConstant;
import com.csi.sbs.deposit.business.dao.AccountMasterDao;
import com.csi.sbs.deposit.business.dao.CustomerMasterDao;
import com.csi.sbs.deposit.business.entity.AccountMasterEntity;
import com.csi.sbs.deposit.business.entity.CustomerMasterEntity;
import com.csi.sbs.deposit.business.service.AccountMasterService;
import com.csi.sbs.deposit.business.util.LogUtil;
import com.csi.sbs.deposit.business.util.PostUtil;

@Service("AccountMasterService")
public class AccountMasterServiceImpl implements AccountMasterService {

	@SuppressWarnings("rawtypes")
	@Resource
	private AccountMasterDao accountMasterDao;
	
	@SuppressWarnings("rawtypes")
	@Resource
	private CustomerMasterDao customerMasterDao;

	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	@SuppressWarnings("unchecked")
	@Override
	public List<AccountMasterEntity> findAccountByParams(AccountMasterEntity ame) {
		return accountMasterDao.findAccountByParams(ame);
	}

	@Override
	@Transactional
	public Map<String,Object> closeAccount(CloseAccountModel cam,RestTemplate restTemplate) throws ParseException {
		Map<String,Object> map = new HashMap<String,Object>();
		AccountMasterEntity ame = new AccountMasterEntity();
		ame.setAccountnumber(cam.getAccountNumber());
		ame.setAccounttype(cam.getAccountType());
		// 根据accountNumber 和 accountType查询账号
		@SuppressWarnings("unchecked")
		List<AccountMasterEntity> accountList = accountMasterDao.findAccountByParams(ame);
		if (accountList == null || accountList.size() == 0) {
			map.put("msg", "Record Not Found");
			map.put("code", "0");
			return map;
		}
		// 校验账户余额是否大于0
		int r = accountList.get(0).getBalance().compareTo(BigDecimal.ZERO);
		if (r != 0) {
			map.put("msg", "Account Balance Not Zero");
			map.put("code", "0");
			return map;
		}

		ame.setAccountstatus(SysConstant.ACCOUNT_STATE1);
		ame.setLastupdateddate(sf.parse(sf.format(new Date())));
		accountMasterDao.closeAccount(ame);
		
		map.put("msg", "Account Deleted Success");
		map.put("code", "1");
		String logstr = "close accountNumber:" + ame.getAccountnumber() + " success!";
		LogUtil.saveLog(
				restTemplate, 
				SysConstant.OPERATION_UPDATE, 
				SysConstant.LOCAL_SERVICE_NAME, 
				SysConstant.OPERATION_SUCCESS, 
				logstr);
		
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int update(AccountMasterEntity ame) {
		return accountMasterDao.update(ame);
	}

	@Override
	@Transactional
	public Map<String, Object> deposit(DepositModel depositModel, RestTemplate restTemplate) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验必填字段
		if (!validateMandatoryField(depositModel)) {
			map.put("msg", "Required field incomplete");
			map.put("code", "0");

			return map;
		}
		// 校验是否支持输入的ccy
		CurrencyModel currency = new CurrencyModel();
		currency.setCcycode(depositModel.getDepositCCyCode());
		ResponseEntity<String> result = restTemplate.postForEntity("http://" + CommonConstant.getSYSADMIN() + "/sysadmin/currency/isSupportbyccy", PostUtil.getRequestEntity(JsonProcess.changeEntityTOJSON(currency)), String.class);
		if (result.getBody().equals("false")) {
			map.put("msg", "Currency Not Supported");
			map.put("code", "1");

			return map;
		}
		// 根据accountNumber 和 ccy 查询
		AccountMasterEntity account = new AccountMasterEntity();
		account.setAccountnumber(depositModel.getAccountNumber());
		account.setCurrencycode(depositModel.getDepositCCyCode());

		@SuppressWarnings("unchecked")
		List<AccountMasterEntity> accountList = accountMasterDao.findAccountByParams(account);
		if (accountList == null || accountList.size() == 0) {
			map.put("msg", "Record Not Found");
			map.put("code", "0");

			return map;
		}
		// 校验账号状态是否是Active的
		if (!accountList.get(0).getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)) {
			map.put("msg", "Account is not active");
			map.put("code", "0");

			return map;
		}

		// 账号余额增加
		// 原来余额
		BigDecimal balance1 = BigDecimal.valueOf(Double.parseDouble(accountList.get(0).getBalance().toString()));
		// 存款余额
		BigDecimal balance2 = BigDecimal.valueOf(Double.parseDouble(depositModel.getDepositAmount()));
		// 总余额
		BigDecimal balance3 = balance1.add(balance2);
		account.setBalance(balance3);
		account.setLastupdateddate(sf.parse(sf.format(new Date())));

		// 存款
		accountMasterDao.deposit(account);
		// 写入日志
		String logstr = "Transaction Accepted:" + depositModel.getAccountNumber();
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_UPDATE, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr);
		map.put("msg", "Transaction Accepted:" + depositModel.getAccountNumber());
		map.put("code", "1");

		return map;
	}

	/**
	 * 存款必填字段校验
	 * 
	 * @param depositModel
	 * @return
	 */
	private boolean validateMandatoryField(DepositModel depositModel) {
		if (depositModel.getAccountNumber() == null || "".equals(depositModel.getAccountNumber())) {
			return false;
		}
		if (depositModel.getDepositAmount() == null || "".equals(depositModel.getDepositAmount())
				|| depositModel.getDepositAmount().equals("0")) {
			return false;
		}
		if (depositModel.getDepositCCyCode() == null || "".equals(depositModel.getDepositCCyCode())) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Map<String, Object> withdrawal(WithDrawalModel withDrawalModel, RestTemplate restTemplate)
			throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验必填字段
		if (!validateMandatoryField(withDrawalModel)) {
			map.put("msg", "Required field incomplete");
			map.put("code", "0");

			return map;
		}
		// 校验是否支持输入的ccy
		CurrencyModel currency = new CurrencyModel();
		currency.setCcycode(withDrawalModel.getWithDrawalCCyCode());
		ResponseEntity<String> result = restTemplate.postForEntity("http://" + CommonConstant.getSYSADMIN() + "/sysadmin/currency/isSupportbyccy", PostUtil.getRequestEntity(JsonProcess.changeEntityTOJSON(currency)), String.class);
		if (result.getBody().equals("false")) {
			map.put("msg", "Currency Not Supported");
			map.put("code", "1");

			return map;
		}
		// 根据accountNumber 和 ccy 查询
		AccountMasterEntity account = new AccountMasterEntity();
		account.setAccountnumber(withDrawalModel.getAccountNumber());
		account.setCurrencycode(withDrawalModel.getWithDrawalCCyCode());

		@SuppressWarnings("unchecked")
		List<AccountMasterEntity> accountList = accountMasterDao.findAccountByParams(account);
		if (accountList == null || accountList.size() == 0) {
			map.put("msg", "Record Not Found");
			map.put("code", "0");

			return map;
		}
		// 校验账号状态是否是Active的
		if (!accountList.get(0).getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)) {
			map.put("msg", "Account is not active");
			map.put("code", "0");

			return map;
		}
		// check账户余额
		if (accountList.get(0).getBalance()
				.compareTo(BigDecimal.valueOf(Double.parseDouble(withDrawalModel.getWithDrawalAmount()))) == -1) {
			map.put("msg", "Insufficient Fund");
			map.put("code", "0");

			return map;
		}

		// 减账户余额
		// 原来余额
		BigDecimal balance1 = BigDecimal.valueOf(Double.parseDouble(accountList.get(0).getBalance().toString()));
		// 取款余额
		BigDecimal balance2 = BigDecimal.valueOf(Double.parseDouble(withDrawalModel.getWithDrawalAmount()));
		// 总余额
		BigDecimal balance3 = balance1.subtract(balance2);
		account.setBalance(balance3);
		account.setLastupdateddate(sf.parse(sf.format(new Date())));

		// 取款
		accountMasterDao.withdrawal(account);
		// 写入日志
		String logstr = "Transaction Accepted:" + withDrawalModel.getAccountNumber();
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_UPDATE, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr);
		map.put("msg", "Transaction Accepted:" + withDrawalModel.getAccountNumber());
		map.put("code", "1");

		return map;
	}

	/**
	 * 存款必填字段校验
	 * 
	 * @param depositModel
	 * @return
	 */
	private boolean validateMandatoryField(WithDrawalModel withDrawalModel) {
		if (withDrawalModel.getAccountNumber() == null || "".equals(withDrawalModel.getAccountNumber())) {
			return false;
		}
		if (withDrawalModel.getWithDrawalAmount() == null || "".equals(withDrawalModel.getWithDrawalAmount())
				|| withDrawalModel.getWithDrawalAmount().equals("0")) {
			return false;
		}
		if (withDrawalModel.getWithDrawalCCyCode() == null || "".equals(withDrawalModel.getWithDrawalCCyCode())) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Map<String, Object> transfer(TransactionModel transactionModel, RestTemplate restTemplate)
			throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 校验必填字段
		if (!validateMandatoryField(transactionModel)) {
			map.put("msg", "Required field incomplete");
			map.put("code", "0");
		}
		// 校验ccy是否支持
		// 校验是否支持输入的ccy
		CurrencyModel currency = new CurrencyModel();
		currency.setCcycode(transactionModel.getTransferCcy());
		ResponseEntity<String> result = restTemplate.postForEntity("http://" + CommonConstant.getSYSADMIN() + "/sysadmin/currency/isSupportbyccy", PostUtil.getRequestEntity(JsonProcess.changeEntityTOJSON(currency)), String.class);
		if (result.getBody().equals("false")) {
			map.put("msg", "Currency Not Supported");
			map.put("code", "1");

			return map;
		}
		// 校验转入账号
		AccountMasterEntity account = new AccountMasterEntity();
		account.setAccountnumber(transactionModel.getTransferInAccountNumber());
		@SuppressWarnings("unchecked")
		List<AccountMasterEntity> accountList = accountMasterDao.findAccountByParams(account);
		if (accountList == null || accountList.size() == 0) {
			map.put("msg", "Record Not Found");
			map.put("code", "0");

			return map;
		}
		// 校验账号状态是否是Active的
		if (!accountList.get(0).getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)) {
			map.put("msg", "Account is not active");
			map.put("code", "0");

			return map;
		}
		// 校验转出账号
		account.setAccountnumber(transactionModel.getTransferOutAccountNumber());
		@SuppressWarnings("unchecked")
		List<AccountMasterEntity> accountList2 = accountMasterDao.findAccountByParams(account);
		if (accountList2 == null || accountList2.size() == 0) {
			map.put("msg", "Record Not Found");
			map.put("code", "0");

			return map;
		}
		// 校验账号状态是否是Active的
		if (!accountList2.get(0).getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)) {
			map.put("msg", "Account is not active");
			map.put("code", "0");

			return map;
		}
		// 校验余额
		if (accountList2.get(0).getBalance()
				.compareTo(BigDecimal.valueOf(Double.parseDouble(transactionModel.getTransferAmount()))) == -1) {
			map.put("msg", "Insufficient Fund");
			map.put("code", "0");

			return map;
		}

		// 减
		AccountMasterEntity minus = new AccountMasterEntity();
		minus.setAccountnumber(transactionModel.getTransferOutAccountNumber());
		minus.setLastupdateddate(sf.parse(sf.format(new Date())));
		// 转出金额
		BigDecimal balance1 = BigDecimal.valueOf(Double.parseDouble(transactionModel.getTransferAmount()));
		// 总额
		BigDecimal balance2 = accountList2.get(0).getBalance();
		// 余额
		BigDecimal balance3 = balance2.subtract(balance1);
		minus.setBalance(balance3);
		accountMasterDao.withdrawal(minus);
		// 加
		AccountMasterEntity add = new AccountMasterEntity();
		add.setAccountnumber(transactionModel.getTransferInAccountNumber());
		add.setLastupdateddate(sf.parse(sf.format(new Date())));
		// 转入金额
		BigDecimal balance4 = BigDecimal.valueOf(Double.parseDouble(transactionModel.getTransferAmount()));
		// 总额
		BigDecimal balance5 = accountList.get(0).getBalance();
		// 余额
		BigDecimal balance6 = balance5.add(balance4);
		add.setBalance(balance6);
		accountMasterDao.deposit(add);

		map.put("msg", transactionModel.getTransferOutAccountNumber() + ":Transaction Accepted");
		map.put("code", "1");

		// 写入日志
		String logstr = "transfer success";
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_TRANSFER, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr);
		return map;
	}

	private boolean validateMandatoryField(TransactionModel transactionModel) {
		if (transactionModel.getTransferInAccountNumber() == null
				|| "".equals(transactionModel.getTransferInAccountNumber())) {
			return false;
		}
		if (transactionModel.getTransferOutAccountNumber() == null
				|| "".equals(transactionModel.getTransferOutAccountNumber())) {
			return false;
		}
		if (transactionModel.getTransferCcy() == null || "".equals(transactionModel.getTransferCcy())) {
			return false;
		}
		if (transactionModel.getTransferAmount() == null || "".equals(transactionModel.getTransferAmount())
				|| transactionModel.getTransferAmount().equals("0")) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Map<String, Object> chequeBookRequest(ChequeBookModel cbm, RestTemplate restTemplate) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 字段校验
		if (!validateMandatoryField(cbm)) {
			map.put("msg", "Required field incomplete");
			map.put("code", "0");

			return map;
		}
		// 校验是否存在
		AccountMasterEntity account = new AccountMasterEntity();
		account.setAccountnumber(cbm.getAccountNumber());
		@SuppressWarnings("unchecked")
		List<AccountMasterEntity> reaccount = accountMasterDao.findAccountByParams(account);
		if (reaccount == null || reaccount.size() == 0) {
			map.put("msg", "Record Not Found");
			map.put("code", "0");

			return map;
		}
		//
		account.setChequebooksize(BigDecimal.valueOf(Double.parseDouble(cbm.getChequeBookSize())));
		account.setChequebooktype(cbm.getChequeBookType());
		accountMasterDao.chequeBookRequest(account);
		// 写入日志
		String logstr = account.getAccountnumber() + "chequeBookRequest success";
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_UPDATE, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr);

		map.put("msg", "Transaction Accepted");
		map.put("code", "1");

		return map;
	}

	private boolean validateMandatoryField(ChequeBookModel cbm) {
		if (cbm.getAccountNumber() == null || "".equals(cbm.getAccountNumber())) {
			return false;
		}
		if (cbm.getChequeBookSize() == null || "".equals(cbm.getChequeBookSize())) {
			return false;
		}
		if (cbm.getChequeBookType() == null || "".equals(cbm.getChequeBookType())) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Map<String, Object> accountEnquiry(AccountEnquiryModel accountEnquiryModel,RestTemplate restTemplate) {
		Map<String,Object> map = new HashMap<String,Object>();
		//search account
		AccountMasterEntity account = new AccountMasterEntity();
		account.setAccountnumber(accountEnquiryModel.getAccountNumber());
		account.setAccounttype(accountEnquiryModel.getAccountType());
		@SuppressWarnings("unchecked")
		List<AccountMasterEntity> reaccount = accountMasterDao.findAccountByParams(account);
		if(reaccount==null || reaccount.size()==0){
			map.put("msg", "Record Not Found");
			map.put("code", "0");
			
			return map;
		}
		//search customer
		CustomerMasterEntity customer = new CustomerMasterEntity();
		customer.setId(reaccount.get(0).getCustomerprimarykeyid());
		CustomerMasterEntity recustomer = customerMasterDao.findCustomerByCustomerID(customer);
		//write log
		String logstr = "Query success based on acount Number and acount Type:accountNumber:"+account.getAccountnumber()+"accountType:"+account.getAccounttype();
		LogUtil.saveLog(
				restTemplate, 
				SysConstant.OPERATION_QUERY, 
				SysConstant.LOCAL_SERVICE_NAME, 
				SysConstant.OPERATION_SUCCESS, 
				logstr);
		
		map.put("msg", "search success");
		map.put("account", reaccount);
		map.put("customer", recustomer);
		map.put("code", "1");
		
		return map;
	}
}
