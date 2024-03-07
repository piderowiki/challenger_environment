package com.ynu.challenger.mapper;

import com.ynu.challenger.PO.Error;
import com.ynu.challenger.PO.ErrorExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ErrorMapper {
    long countByExample(ErrorExample example);

    int deleteByExample(ErrorExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Error record);

    int insertSelective(Error record);

    List<Error> selectByExample(ErrorExample example);

    Error selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Error record, @Param("example") ErrorExample example);

    int updateByExample(@Param("record") Error record, @Param("example") ErrorExample example);

    int updateByPrimaryKeySelective(Error record);

    int updateByPrimaryKey(Error record);
}