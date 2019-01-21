package com.csi.sbs.deposit.business.service;

import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.csi.sbs.deposit.business.clientmodel.CustomerAndAccountModel;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OpeningCurrentAccountService {
	
	
	public Map<String,Object> openingCurrentAccount(CustomerAndAccountModel cam,RestTemplate restTemplate) throws JsonProcessingException;

}
