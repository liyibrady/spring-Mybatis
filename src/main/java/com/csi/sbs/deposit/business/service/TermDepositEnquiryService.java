package com.csi.sbs.deposit.business.service;

import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.csi.sbs.deposit.business.clientmodel.TermDepositEnquiryModel;

public interface TermDepositEnquiryService {
	
	
	public Map<String,Object> termDepositEnquiry(TermDepositEnquiryModel tdem,RestTemplate restTemplate);
}
