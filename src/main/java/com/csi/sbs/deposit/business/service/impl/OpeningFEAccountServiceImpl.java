package com.csi.sbs.deposit.business.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.csi.sbs.common.business.constant.CommonConstant;
import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.common.business.util.UUIDUtil;
import com.csi.sbs.deposit.business.clientmodel.CustomerAndAccountModel;
import com.csi.sbs.deposit.business.constant.SysConstant;
import com.csi.sbs.deposit.business.dao.AccountMasterDao;
import com.csi.sbs.deposit.business.dao.CustomerMasterDao;
import com.csi.sbs.deposit.business.entity.CustomerMasterEntity;
import com.csi.sbs.deposit.business.service.OpeningFEAccountService;
import com.csi.sbs.deposit.business.util.AvailableNumberUtil;
import com.csi.sbs.deposit.business.util.LogUtil;
import com.csi.sbs.deposit.business.util.PostUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service("OpeningFEAccountService")
public class OpeningFEAccountServiceImpl implements OpeningFEAccountService{

	@SuppressWarnings("rawtypes")
	@Resource
	private CustomerMasterDao customerMasterDao;

	@SuppressWarnings("rawtypes")
	@Resource
	private AccountMasterDao accountMasterDao;
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> openingFEAccount(CustomerAndAccountModel cam, RestTemplate restTemplate)
			throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验公共逻辑处理是否成功
		if (!commonBusinessProcess(cam, restTemplate).get("code").equals("1")) {
			return commonBusinessProcess(cam, restTemplate);
		}
		// 校验客户是否存在
		CustomerMasterEntity cme = new CustomerMasterEntity();
		cme.setCustomerid(cam.getCustomer().getCustomerid());
		CustomerMasterEntity recustomer = customerMasterDao.findCustomerByCustomerID(cme);
		boolean flag = false;
		if (recustomer != null) {
			flag = true;
		}
		cam.getAccount().setCurrencycode(commonBusinessProcess(cam, restTemplate).get("localCCy").toString());
		cam.getAccount().setAccounttype(SysConstant.ACCOUNT_TYPE3);

		if (flag) {
			cam.getAccount().setCustomerprimarykeyid(recustomer.getId());
			accountMasterDao.insert(cam.getAccount());
		} else {
			customerMasterDao.insert(cam.getCustomer());
			cam.getAccount().setCustomerprimarykeyid(cam.getCustomer().getId());
			accountMasterDao.insert(cam.getAccount());
		}
		// 写入日志
		String logstr = "create accountNumber:" + cam.getAccount().getAccountnumber() + " success!";
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_CREATE, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr);
		AvailableNumberUtil.availableNumberIncrease(restTemplate, SysConstant.NEXT_AVAILABLE_CUSTOMERNUMBER);
		
		map.put("msg", "创建成功");
		map.put("accountNumber", cam.getAccount().getAccountnumber());
		map.put("code", "1");
		
		return map;
	}
	
	/**
	 * 创建账号公共业务处理
	 * 
	 * @param cam
	 * @return
	 * @throws JsonProcessingException
	 */
	private Map<String, Object> commonBusinessProcess(CustomerAndAccountModel cam, RestTemplate restTemplate)
			throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 先校验字段
		boolean validateFlag = validateField(cam);
		if (!validateFlag) {
			map.put("msg", "必填字段不全");
			map.put("code", "0");

			return map;
		}

		// 调用服务接口地址
		String param1 = "{\"apiname\":\"getSystemParameter\"}";
		ResponseEntity<String> result1 = restTemplate.postForEntity(
				"http://" + CommonConstant.getSYSADMIN() + SysConstant.SERVICE_INTERNAL_URL + "",
				PostUtil.getRequestEntity(param1), String.class);
		if (result1 == null) {
			map.put("msg", "调用服务接口地址失败");
			map.put("code", "0");
		}
		String path = JsonProcess.returnValue(JsonProcess.changeToJSONObject(result1.getBody()), "internaURL");

		// 调用系统参数服务接口
		String param2 = "{\"item\":\"ClearingCode,BranchNumber,LocalCcy,NextAvailableCustomerNumber\"}";
		ResponseEntity<String> result2 = restTemplate.postForEntity(path, PostUtil.getRequestEntity(param2),
				String.class);
		if (result2 == null) {
			map.put("msg", "调用系统参数失败");
			map.put("code", "0");
		}

		// 返回数据处理
		String clearcode = "";
		String branchnumber = "";
		String localCCy = "";
		JSONObject jsonObject1 = null;
		String revalue = null;
		String temp = null;
		for (int i = 0; i < JsonProcess.changeToJSONArray(result2.getBody()).size(); i++) {
			jsonObject1 = JsonProcess
					.changeToJSONObject(JsonProcess.changeToJSONArray(result2.getBody()).get(i).toString());
			revalue = JsonProcess.returnValue(jsonObject1, "item");
			temp = JsonProcess.returnValue(jsonObject1, "value");
			if (revalue.equals("BranchNumber")) {
				branchnumber = temp;
			}
			if (revalue.equals("ClearingCode")) {
				clearcode = temp;
			}
			if (revalue.equals("LocalCcy")) {
				localCCy = temp;
			}
		}

		// 调用可用customerNumber服务接口
		String result3 = restTemplate
				.getForEntity("http://SYSADMIN/sysadmin/generate/getNextAvailableNumber/NextAvailableCustomerNumber",
						String.class)
				.getBody();
		String customerNumber = "";
		if (result3 == null) {
			map.put("msg", "调用系统参数失败");
			map.put("code", "0");
		}

		// 返回数据处理
		customerNumber = JsonProcess.returnValue(JsonProcess.changeToJSONObject(result3), "nextAvailableNumber");

		cam.getCustomer()
				.setCustomernumber(clearcode + branchnumber + customerNumber + cam.getAccount().getAccounttype());
		cam.getCustomer().setId(UUIDUtil.generateUUID());

		cam.getAccount().setBalance(new BigDecimal(0));
		cam.getAccount().setId(UUIDUtil.generateUUID());
		cam.getAccount()
				.setAccountnumber(clearcode + branchnumber + customerNumber + cam.getAccount().getAccounttype());

		map.put("msg", "处理成功");
		map.put("localCCy", localCCy);
		map.put("code", "1");

		return map;
	}

	/**
	 * 字段校验
	 */
	private boolean validateField(CustomerAndAccountModel cam) {
		/**
		 * 校验Customer
		 */
		if (cam.getCustomer().getCustomername() == null || "".equals(cam.getCustomer().getCustomername())) {
			return false;
		}
		if (cam.getCustomer().getCustomerid() == null || "".equals(cam.getCustomer().getCustomerid())) {
			return false;
		}
		if (cam.getCustomer().getIssuecountry() == null || "".equals(cam.getCustomer().getIssuecountry())) {
			return false;
		}
		if (cam.getCustomer().getDateofbirth() == null || "".equals(cam.getCustomer().getDateofbirth())) {
			return false;
		}
		if (cam.getCustomer().getMailingaddress() == null || "".equals(cam.getCustomer().getMailingaddress())) {
			return false;
		}
		if (cam.getCustomer().getMobilephonenumber() == null || "".equals(cam.getCustomer().getMobilephonenumber())) {
			return false;
		}

		/**
		 * 校验account
		 */
		if (cam.getAccount().getCurrencycode() == null || "".equals(cam.getAccount().getCurrencycode())) {
			return false;
		}
		if (cam.getAccount().getAccounttype() == null || "".equals(cam.getAccount().getAccounttype())) {
			return false;
		}

		return true;
	}

}
