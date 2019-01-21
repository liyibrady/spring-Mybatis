package com.csi.sbs.deposit.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.csi.sbs.deposit.business.dao.TermDepositRateDao;
import com.csi.sbs.deposit.business.entity.TermDepositRateEntity;
import com.csi.sbs.deposit.business.service.TermDepositRateService;


@Service
public class TermDepositRateServiceImpl implements TermDepositRateService {

	
	@SuppressWarnings("rawtypes")
	@Autowired 
	private TermDepositRateDao tdrDao;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TermDepositRateEntity> selectAll() {
					
		return tdrDao.selectAll();
	}

}
