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

import com.csi.sbs.deposit.business.clientmodel.ChequeBookModel;
import com.csi.sbs.deposit.business.service.AccountMasterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin // 解决跨域请求
@Controller
@RequestMapping("/deposit/chequebook")
@Api(value = "Then controller is deposit chequebook")
public class ChequeBookController {
	
	
	@Resource
	private AccountMasterService accountMasterService;
	
	@Resource
	private RestTemplate restTemplate;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	
	@RequestMapping(value = "/chequeBookRequest", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This api is for chequeBookRequest", notes = "version 0.0.1")
	public String chequeBookRequest(@RequestBody ChequeBookModel chequeBookModel)
			throws JsonProcessingException {
		Map<String, Object> map = null;
		try {
			map = accountMasterService.chequeBookRequest(chequeBookModel,restTemplate);
		} catch (Exception e) {
			map.put("msg", "chequeBookRequest fail");
			map.put("code", "0");
		}

		return objectMapper.writeValueAsString(map);
	}

}
