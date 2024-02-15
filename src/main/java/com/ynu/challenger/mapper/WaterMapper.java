package com.ynu.challenger.mapper;

import com.ynu.challenger.PO.CO2;
import com.ynu.challenger.PO.Water;
import com.ynu.challenger.PO.WaterExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WaterMapper {
    long countByExample(WaterExample example);

    int deleteByExample(WaterExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Water record);

    int insertSelective(Water record);

    List<Water> selectByExample(WaterExample example);

    Water selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Water record, @Param("example") WaterExample example);

    int updateByExample(@Param("record") Water record, @Param("example") WaterExample example);

    int updateByPrimaryKeySelective(Water record);

    int updateByPrimaryKey(Water record);

    List<Water> selectByAccountAndYear(@Param("accountID") int accountID, @Param("year") int year);
    List<Water> selectByAccountAndYearAndMonth(
            @Param("accountID") int accountID,@Param("year") int year,
            @Param("month") int month);
    List<Water> selectByAccountAndYearAndMonthAndDay(
            @Param("accountID") int accountID,@Param("year") int year,
            @Param("month") int month,@Param("day") int day);
}