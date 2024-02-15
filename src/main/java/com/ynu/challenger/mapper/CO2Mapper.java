package com.ynu.challenger.mapper;

import com.ynu.challenger.PO.CO2;
import com.ynu.challenger.PO.CO2Example;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CO2Mapper {
    long countByExample(CO2Example example);

    int deleteByExample(CO2Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(CO2 record);

    int insertSelective(CO2 record);

    List<CO2> selectByExample(CO2Example example);

    CO2 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CO2 record, @Param("example") CO2Example example);

    int updateByExample(@Param("record") CO2 record, @Param("example") CO2Example example);

    int updateByPrimaryKeySelective(CO2 record);

    int updateByPrimaryKey(CO2 record);

    List<CO2> selectByAccountAndYear(@Param("accountID") int accountID,@Param("year") int year);
    List<CO2> selectByAccountAndYearAndMonth(
            @Param("accountID") int accountID,@Param("year") int year,
             @Param("month") int month);
    List<CO2> selectByAccountAndYearAndMonthAndDay(
            @Param("accountID") int accountID,@Param("year") int year,
            @Param("month") int month,@Param("day") int day);
}