package com.csi.sbs.deposit.business.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.csi.sbs.deposit.business.entity.TermDepositRateEntity;
import com.csi.sbs.deposit.business.service.TermDepositRateService;


@CrossOrigin//解决跨域请求
@Controller
@RequestMapping("/deposit/rate")
public class TermDepositRateController {
	
	
	@Autowired
	private TermDepositRateService  tdrService;
		
	@RequestMapping(value = "/getTDRateDetails", method = RequestMethod.GET)
	@ResponseBody
	public List<TermDepositRateEntity> getTDRate(){
						
		 Map<String,Object> map = new HashMap<String,Object>();
		 List<TermDepositRateEntity>   tdrList = new ArrayList<TermDepositRateEntity>();
		 try{	        
			    tdrList = tdrService.selectAll();	
			    map.put("msg", "创建成功");
	            map.put("code", "1");
	        }catch(Exception e){
		     	map.put("msg", "创建失败");
		        map.put("code", "0");
		 }
	        			  				
		return tdrList; 
		
	}
	

}
