package com.ynu.challenger.mapper;

import com.ynu.challenger.PO.CO2;
import com.ynu.challenger.PO.Power;
import com.ynu.challenger.PO.PowerExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PowerMapper {
    long countByExample(PowerExample example);

    int deleteByExample(PowerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Power record);

    int insertSelective(Power record);

    List<Power> selectByExample(PowerExample example);

    Power selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Power record, @Param("example") PowerExample example);

    int updateByExample(@Param("record") Power record, @Param("example") PowerExample example);

    int updateByPrimaryKeySelective(Power record);

    int updateByPrimaryKey(Power record);

    List<Power> selectByAccountAndYear(@Param("accountID") int accountID, @Param("year") int year);
    List<Power> selectByAccountAndYearAndMonth(
            @Param("accountID") int accountID,@Param("year") int year,
            @Param("month") int month);
    List<Power> selectByAccountAndYearAndMonthAndDay(
            @Param("accountID") int accountID,@Param("year") int year,
            @Param("month") int month,@Param("day") int day);
}