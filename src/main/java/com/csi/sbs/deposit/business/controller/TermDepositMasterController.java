package com.csi.sbs.deposit.business.controller;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.csi.sbs.common.business.util.UUIDUtil;
import com.csi.sbs.deposit.business.clientmodel.TermDepositDrawDownModel;
import com.csi.sbs.deposit.business.clientmodel.TermDepositMasterModel;
import com.csi.sbs.deposit.business.service.TermDepositMasterService;
import com.csi.sbs.deposit.business.validator.TDMasterValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@CrossOrigin//解决跨域请求
@Controller
@RequestMapping("/deposit/term")
@Api(value = "Then controller is term deposit")
public class TermDepositMasterController {
	
		

	    @Resource
	    private TermDepositMasterService tdmService;
	   
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    @RequestMapping(value = "/termDepositApplication", method = RequestMethod.POST)
	    @ResponseBody
	    @ApiOperation(value = "This api is for term deposit", notes = "version 0.0.1")		
		@ApiImplicitParam(paramType = "body", name = "tdm", required = true, value = "TermDepositMasterModel")
		public String termDepositApplication(@RequestBody TermDepositMasterModel tdm) throws JsonProcessingException{
	        Map<String,Object> map = new HashMap<String,Object>();
	        try{
	        	tdm.setId(UUIDUtil.generateUUID());	        	
	        	TDMasterValidator.validateTDMaster(tdm, map);
	     	   	if(map.size()>0){
	     	   		return objectMapper.writeValueAsString(map);
	     	   	}     
	     	    map = tdmService.termDepositApplication(tdm);
	     	    return objectMapper.writeValueAsString(map);	           
	        }catch(Exception e){
		     	map.put("msg", "创建失败");
		        map.put("code", "0");
		    }
	        			  
			return objectMapper.writeValueAsString(map);
		}
	
	    
	    
	    
	    @RequestMapping(value = "/termDepositDrawDown", method = RequestMethod.POST)
	    @ResponseBody
	    @ApiOperation(value = "This api is for term deposit dranwdown Function", notes = "version 0.0.1")		
		@ApiImplicitParam(paramType = "body", name = "tddm", required = true, value = "TermDepositDrawDownModel")
		public String termDepositDrawDown(@RequestBody TermDepositDrawDownModel tddm) throws JsonProcessingException{
	       
	    	Map<String,Object> map = new HashMap<String,Object>();
	        try{	        	        	
	        	TDMasterValidator.validateTDDrawDown(tddm, map);
	     	   	if(map.size()>0){
	     	   		return objectMapper.writeValueAsString(map);
	     	   	}     
	     	    map = tdmService.termDepositDrawDown(tddm);
	     	    return objectMapper.writeValueAsString(map);	           
	        }catch(Exception e){
		     	map.put("msg", "创建失败");
		        map.put("code", "0");
		    }
	        			  
			return objectMapper.writeValueAsString(map);
		}
	    
	    
	    
	    @RequestMapping(value = "/termDepositRenewal", method = RequestMethod.POST)
	    @ResponseBody
	    @ApiOperation(value = "This api is for term deposit renewal Function", notes = "version 0.0.1")		
		@ApiImplicitParam(paramType = "body", name = "tddm", required = true, value = "TermDepositDrawDownModel")
		public String termDepositRenewal(@RequestBody TermDepositDrawDownModel tddm) throws JsonProcessingException{
	       
	    	Map<String,Object> map = new HashMap<String,Object>();
	        try{	        	        	
	        	TDMasterValidator.validateTDDrawDown(tddm, map);
	     	   	if(map.size()>0){
	     	   		return objectMapper.writeValueAsString(map);
	     	   	}     
	     	    map = tdmService.termDepositDrawDown(tddm);
	     	    return objectMapper.writeValueAsString(map);	           
	        }catch(Exception e){
		     	map.put("msg", "创建失败");
		        map.put("code", "0");
		    }
	        			  
			return objectMapper.writeValueAsString(map);
		}
	    
	    
	    
	    
	

}
