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

import com.csi.sbs.deposit.business.service.TermDepositEnquiryService;
import com.csi.sbs.deposit.business.clientmodel.TermDepositEnquiryModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin // 解决跨域请求
@Controller
@RequestMapping("/deposit/enquiry")
@Api(value = "Then controller is termDepositEnquiry")
public class TermDepositEnquiryController {

	@Resource
	private RestTemplate restTemplate;

	@Resource
	private TermDepositEnquiryService termDepositEnquiryService;

	ObjectMapper objectMapper = new ObjectMapper();

	@RequestMapping(value = "/termDepositEnquiry", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "This api is for termDepositEnquiry", notes = "version 0.0.1")
	public String termDepositEnquiry(@RequestBody TermDepositEnquiryModel termDepositEnquiryModel) throws JsonProcessingException{
		Map<String,Object> map = null;
		try{
			map = termDepositEnquiryService.termDepositEnquiry(termDepositEnquiryModel,restTemplate);
    	}catch(Exception e){
    		map.put("msg", "查询失败");
    		map.put("code", "0");
    	}
		return objectMapper.writeValueAsString(map);
	}

}
