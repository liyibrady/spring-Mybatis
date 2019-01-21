package com.csi.sbs.deposit.business.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.csi.sbs.deposit.business.base.BaseDao;
import com.csi.sbs.deposit.business.entity.TermDepositMasterEntity;


@Mapper
public interface TermDepositMasterDao<T> extends BaseDao<T> {
   
	public List<TermDepositMasterEntity>  getTDMaster(TermDepositMasterEntity tdm);
	
	public int updateMatuirtyStatus(@Param("status")String status,@Param("accountNumber")String accountNumber,@Param("depositNumber")String depositNumber);
	
}