package com.csi.sbs.deposit.business.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.csi.sbs.deposit.business.clientmodel.CloseAccountModel;
import com.csi.sbs.deposit.business.clientmodel.CustomerAndAccountModel;
import com.csi.sbs.deposit.business.clientmodel.CustomerMaintenanceModel;
import com.csi.sbs.deposit.business.clientmodel.AccountEnquiryModel;
import com.csi.sbs.deposit.business.clientmodel.AccountNumber;
import com.csi.sbs.deposit.business.clientmodel.DepositModel;
import com.csi.sbs.deposit.business.clientmodel.WithDrawalModel;
import com.csi.sbs.deposit.business.constant.SysConstant;
import com.csi.sbs.deposit.business.entity.AccountMasterEntity;
import com.csi.sbs.deposit.business.service.AccountMasterService;
import com.csi.sbs.deposit.business.service.CustomerMasterService;
import com.csi.sbs.deposit.business.service.OpeningCurrentAccountService;
import com.csi.sbs.deposit.business.service.OpeningFEAccountService;
import com.csi.sbs.deposit.business.service.OpeningSavingAccountService;
import com.csi.sbs.deposit.business.service.OpeningTDAccountService;
import com.csi.sbs.deposit.business.util.LogUtil;

@CrossOrigin // 解决跨域请求
@Controller
@RequestMapping("/deposit/account")
@Api(value = "Then controller is deposit account")
public class CustomerMasterController {

	@Resource
	private CustomerMasterService customerMasterService;

	@Resource
	private AccountMasterService accountMasterService;

	@Resource
	private OpeningSavingAccountService openingSavingAccountService;
	
	@Resource
	private OpeningCurrentAccountService openingCurrentAccountService;
	
	@Resource
	private OpeningFEAccountService openingFEAccountService;
	
	@Resource
	private OpeningTDAccountService openingTDAccountService;

	@Resource
	private RestTemplate restTemplate;

	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 开普通账号
	 * 
	 * @param cam
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/openingSavingAccount", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for create savingaccount", notes = "version 0.0.1")
	public String openingSavingAccount(@RequestBody CustomerAndAccountModel customerAndAccountModel)
			throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = openingSavingAccountService.openingSavingAccount(customerAndAccountModel, restTemplate);
		} catch (Exception e) {
			map.put("msg", "创建失败");
			map.put("code", "0");
		}
		return objectMapper.writeValueAsString(map);
	}

	/**
	 * 开支票账号
	 * 
	 * @param cam
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/openingCurrentAccount", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for create currentaccount", notes = "version 0.0.1")
	public String openingCurrentAccount(@RequestBody CustomerAndAccountModel customerAndAccountModel)
			throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = openingCurrentAccountService.openingCurrentAccount(customerAndAccountModel, restTemplate);
		} catch (Exception e) {
			map.put("msg", "创建失败");
			map.put("code", "0");
		}
		return objectMapper.writeValueAsString(map);
	}

	/**
	 * 开外汇账号
	 * 
	 * @param cam
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/openingFEAccount", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for create feaccount", notes = "version 0.0.1")
	public String openingFEAccount(@RequestBody CustomerAndAccountModel customerAndAccountModel)
			throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = openingFEAccountService.openingFEAccount(customerAndAccountModel, restTemplate);
		} catch (Exception e) {
			map.put("msg", "创建失败");
			map.put("code", "0");
		}
		return objectMapper.writeValueAsString(map);
	}

	/**
	 * 开定存账号
	 * 
	 * @param cam
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/openingTDAccount", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for create tdaccount", notes = "version 0.0.1")
	public String openingTDAccount(@RequestBody CustomerAndAccountModel customerAndAccountModel)
			throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = openingTDAccountService.openingTDAccount(customerAndAccountModel, restTemplate);
		} catch (Exception e) {
			map.put("msg", "创建失败");
			map.put("code", "0");
		}
		return objectMapper.writeValueAsString(map);
	}

	/**
	 * 账号关闭
	 * 
	 * @param cam
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/accountClosure", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for close account", notes = "version 0.0.1")
	public String accountClosure(@RequestBody CloseAccountModel closeAccountModel) throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = accountMasterService.closeAccount(closeAccountModel, restTemplate);
		} catch (Exception e) {
			map.put("msg", "Account Deleted Fail");
			map.put("code", "0");
		}
		return objectMapper.writeValueAsString(map);
	}

	/**
	 * 账号维护
	 * 
	 * @param cam
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/updateCustContactInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for update customer contact Information", notes = "version 0.0.1")
	public String updateCustContactInfo(@RequestBody CustomerMaintenanceModel customerMaintenanceModel)
			throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = customerMasterService.contactInformationUpdate(customerMaintenanceModel, restTemplate);
		} catch (Exception e) {
			map.put("msg", "Record Change Fail");
			map.put("code", "0");
		}
		return objectMapper.writeValueAsString(map);
	}

	/**
	 * 存款
	 * 
	 * @param cam
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for deposit", notes = "version 0.0.1")
	public String deposit(@RequestBody DepositModel depositModel) throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = accountMasterService.deposit(depositModel, restTemplate);
		} catch (Exception e) {
			map.put("msg", "Transaction Fail");
			map.put("code", "0");
		}
		return objectMapper.writeValueAsString(map);
	}

	/**
	 * 取款
	 * 
	 * @param cam
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/withdrawal", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for withdrawal", notes = "version 0.0.1")
	public String withdrawal(@RequestBody WithDrawalModel withDrawalModel) throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = accountMasterService.withdrawal(withDrawalModel, restTemplate);
		} catch (Exception e) {
			map.put("msg", "Transaction Fail");
			map.put("code", "0");
		}
		return objectMapper.writeValueAsString(map);
	}
	
	/**
	 * 返回account and customer信息
	 * 
	 * @param cam
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/retDepositAccountDetails", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for retDepositAccountDetails", notes = "version 0.0.1")
	public String retDepositAccountDetails(@RequestBody AccountEnquiryModel accountEnquiryModel) throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = accountMasterService.accountEnquiry(accountEnquiryModel, restTemplate);
		} catch (Exception e) {
			map.put("msg", "query Fail");
			map.put("code", "0");
		}
		return objectMapper.writeValueAsString(map);
	}

	/**
	 * 查找账号 Alina
	 * 
	 * @param accountNumModel
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/accountSearch", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for account search", notes = "version 0.0.1")
	public String accountSearch(@RequestBody AccountNumber accountNumModel) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		AccountMasterEntity ame = new AccountMasterEntity();
		ame.setAccountnumber(accountNumModel.getAccountNumber());
		ame.setAccounttype(accountNumModel.getAccountType());
		ame.setCurrencycode(accountNumModel.getCcycode());
		// 根据accountNumber查询账号
		List<AccountMasterEntity> accountList = accountMasterService.findAccountByParams(ame);
		if (accountList == null || accountList.size() == 0) {
			map.put("msg", "Record Not Found");
			map.put("code", "0");
			return objectMapper.writeValueAsString(map);
		} else {
			return objectMapper.writeValueAsString(accountList.get(0));
		}
	}

	/**
	 * Alina 查找账号
	 * 
	 * @param accountNumModel
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/updateAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for update account master information", notes = "version 0.0.1")
	public String updateAccountInfo(@RequestBody AccountMasterEntity accountInfo) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Date lastupdateddate = null;
		try {
			lastupdateddate = sf.parse(sf.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		accountInfo.setLastupdateddate(lastupdateddate);
		try {
			int a = accountMasterService.update(accountInfo);
			if (a > 0) {
				map.put("msg", "update success");
				map.put("code", "1");
				String logstr = "update accountNumber:" + accountInfo.getAccountnumber()
						+ " account infomation success!";
				LogUtil.saveLog(restTemplate, SysConstant.OPERATION_UPDATE, SysConstant.LOCAL_SERVICE_NAME,
						SysConstant.OPERATION_SUCCESS, logstr);
			} else {
				map.put("msg", "Record Change Fail");
				map.put("code", "0");
			}
		} catch (Exception e) {
			map.put("msg", "Record Change Fail");
			map.put("code", "0");
		}
		return objectMapper.writeValueAsString(map);
	}
	
}
