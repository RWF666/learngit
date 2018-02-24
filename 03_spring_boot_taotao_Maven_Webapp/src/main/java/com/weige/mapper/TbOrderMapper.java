package com.weige.mapper;

import com.weige.model.TbOrder;
import com.weige.model.TbOrderExample;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TbOrderMapper {
    long countByExample(TbOrderExample example);

    int deleteByExample(TbOrderExample example);

    int insert(TbOrder record);

    int insertSelective(TbOrder record);

    List<TbOrder> selectByExample(TbOrderExample example);

    int updateByExampleSelective(@Param("record") TbOrder record, @Param("example") TbOrderExample example);

    int updateByExample(@Param("record") TbOrder record, @Param("example") TbOrderExample example);

	void paymentOrderScan(Date date);
}