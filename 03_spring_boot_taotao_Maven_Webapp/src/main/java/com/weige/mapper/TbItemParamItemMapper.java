package com.weige.mapper;

import com.weige.model.TbItemParamItem;
import com.weige.model.TbItemParamItemExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TbItemParamItemMapper {
    long countByExample(TbItemParamItemExample example);

    int deleteByExample(TbItemParamItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbItemParamItem record);

    int insertSelective(TbItemParamItem record);

    List<TbItemParamItem> selectByExample(TbItemParamItemExample example);

    TbItemParamItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbItemParamItem record, @Param("example") TbItemParamItemExample example);

    int updateByExample(@Param("record") TbItemParamItem record, @Param("example") TbItemParamItemExample example);

    int updateByPrimaryKeySelective(TbItemParamItem record);

    int updateByPrimaryKey(TbItemParamItem record);

}