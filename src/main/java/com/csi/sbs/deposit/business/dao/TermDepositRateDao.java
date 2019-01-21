package com.csi.sbs.deposit.business.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.csi.sbs.deposit.business.base.BaseDao;
import com.csi.sbs.deposit.business.entity.TermDepositRateEntity;



@Mapper
public interface TermDepositRateDao<T> extends BaseDao<T> {
    
	
	int deleteByPrimaryKey(String id);

    int insert(TermDepositRateEntity record);

    int insertSelective(TermDepositRateEntity record);

    List<TermDepositRateEntity> selectAll();
    
    TermDepositRateEntity queryRate(TermDepositRateEntity tdr);
        
    int updateByPrimaryKey(TermDepositRateEntity record);
}