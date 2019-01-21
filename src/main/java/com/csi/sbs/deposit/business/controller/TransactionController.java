package com.csi.sbs.deposit.business.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.csi.sbs.deposit.business.clientmodel.TransactionModel;
import com.csi.sbs.deposit.business.service.AccountMasterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin // 解决跨域请求
@Controller
@RequestMapping("/deposit/transaction")
@Api(value = "Then controller is deposit transaction")
public class TransactionController {
	
	@Resource
	private AccountMasterService accountMasterService;
	
	@Resource
	private RestTemplate restTemplate;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for transfer", notes = "version 0.0.1")
	public String transfer(@RequestBody TransactionModel transactionModel)
			throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = accountMasterService.transfer(transactionModel, restTemplate);
		} catch (Exception e) {
			map.put("msg", "转账失败");
			map.put("code", "0");
		}

		return objectMapper.writeValueAsString(map);
	}

}
