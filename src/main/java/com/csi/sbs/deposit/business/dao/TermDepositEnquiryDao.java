package com.csi.sbs.deposit.business.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.csi.sbs.deposit.business.base.BaseDao;
import com.csi.sbs.deposit.business.entity.TermDepositEnquiryEntity;


@Mapper
public interface TermDepositEnquiryDao<T> extends BaseDao<T>{
	
	public List<TermDepositEnquiryEntity> termDepositEnquiry(TermDepositEnquiryEntity tde);
}