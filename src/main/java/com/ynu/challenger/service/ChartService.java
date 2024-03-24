package com.ynu.challenger.service;

import com.ynu.challenger.PO.CO2;
import com.ynu.challenger.PO.Power;
import com.ynu.challenger.PO.Water;
import com.ynu.challenger.VO.*;
import com.ynu.challenger.mapper.CO2Mapper;
import com.ynu.challenger.mapper.PowerMapper;
import com.ynu.challenger.mapper.WaterMapper;
import com.ynu.challenger.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.logging.XMLFormatter;

@Service
public class ChartService {
    boolean debug = false;
    @Autowired
    private CO2Mapper co2Mapper;
    @Autowired
    private PowerMapper powerMapper;
    @Autowired
    private WaterMapper waterMapper;

    // 0代表co2,1代表电,2代表水
    public ChartData onlyYear(ChartReceiveData chartReceiveData){
        ChartData chartData = new ChartData();
        // 按下year时对应的年份
        int year = chartReceiveData.getThisDate().getYear();
        int type = chartReceiveData.getType();
        int account = chartReceiveData.getAccount();
        // pieData
        List<PieData> pieDataList = new ArrayList<>();
        chartData.setPieData(pieDataList);
        // areaPercent
        List<AreaPercent> areaPercentList = new ArrayList<>();
        chartData.setAreaPercent(areaPercentList);
        // ColumnData
        List<ColumnData> columnDataList = new ArrayList<>();
        chartData.setColumnData(columnDataList);

        switch (type){
            case 0:
                // 最近三年内的数据
                List<CO2> co2List1 = co2Mapper.selectByAccountAndYear(account,year - 2);
                List<CO2> co2List2 = co2Mapper.selectByAccountAndYear(account,year - 1);
                List<CO2> co2List3 = co2Mapper.selectByAccountAndYear(account,year);
                // 搞合并吧
                co2List1.addAll(co2List2);
                co2List1.addAll(co2List3);
                // 这部分是PieData的部分
                for (int i = 0; i < 3; i++) {
                    PieData perYear = new PieData();
                    perYear.setName("" + (year - 2 + i) + "年");
                    pieDataList.add(perYear);
                    // 这里还得补上
                    ColumnData columnDataPerYear = new ColumnData();
                    columnDataPerYear.setName("" + (year - 2 + i) + "年");
                    columnDataList.add(columnDataPerYear);
                }
                // 统计已经出现过的area
                Map<String,Integer> areaMap = new HashMap<>();

                // 接下来把三个部分统计
                int total = 0;                           // 为最后计算百分比作准备
                for (CO2 co2 : co2List1) {
                    if(debug)System.out.println(co2.getName());
                    int co2Year = DateUtil.getDateYear(co2.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(2 + co2Year - year);
                    pieData.setValue(pieData.getValue() + co2.getAmount());
                    ColumnData columnData = columnDataList.get(2 + co2Year - year);
                    columnData.setData(columnData.getData() + co2.getAmount());
                    // 这是area的部分
                    total += co2.getAmount();
                    String areaName = co2.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = areaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + co2.getAmount());
                    // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(co2.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        areaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / total);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);

                break;
            case 1:
                // 最近三年内的数据
                List<Power> powerList1 = powerMapper.selectByAccountAndYear(account,year - 2);
                List<Power> powerList2 = powerMapper.selectByAccountAndYear(account,year - 1);
                List<Power> powerList3 = powerMapper.selectByAccountAndYear(account,year);
                // 搞合并吧
                powerList1.addAll(powerList2);
                powerList1.addAll(powerList3);
                // 这部分是PieData的部分
                for (int i = 0; i < 3; i++) {
                    PieData perYear = new PieData();
                    perYear.setName("" + (year - 2 + i) + "年");
                    pieDataList.add(perYear);
                    // 这里还得补上
                    ColumnData columnDataPerYear = new ColumnData();
                    columnDataPerYear.setName("" + (year - 2 + i) + "年");
                    columnDataList.add(columnDataPerYear);
                }
                // 统计已经出现过的area
                Map<String,Integer> powerAreaMap = new HashMap<>();

                // 接下来把三个部分统计
                int powerTotal = 0;                           // 为最后计算百分比作准备
                for (Power power : powerList1) {
                    if(debug)System.out.println(power.getName());
                    int co2Year = DateUtil.getDateYear(power.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(2 + co2Year - year);
                    pieData.setValue(pieData.getValue() + power.getAmount());
                    ColumnData columnData = columnDataList.get(2 + co2Year - year);
                    columnData.setData(columnData.getData() + power.getAmount());
                    // 这是area的部分
                    powerTotal += power.getAmount();
                    String areaName = power.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = powerAreaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + power.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(power.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        powerAreaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / powerTotal);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);

                break;
            default:
                // 最近三年内的数据
                List<Water> waterList1 = waterMapper.selectByAccountAndYear(account,year - 2);
                List<Water> waterList2 = waterMapper.selectByAccountAndYear(account,year - 1);
                List<Water> waterList3 = waterMapper.selectByAccountAndYear(account,year);
                // 搞合并吧
                waterList1.addAll(waterList2);
                waterList1.addAll(waterList3);
                // 这部分是PieData的部分
                for (int i = 0; i < 3; i++) {
                    PieData perYear = new PieData();
                    perYear.setName("" + (year - 2 + i) + "年");
                    pieDataList.add(perYear);
                    // 这里还得补上
                    ColumnData columnDataPerYear = new ColumnData();
                    columnDataPerYear.setName("" + (year - 2 + i) + "年");
                    columnDataList.add(columnDataPerYear);
                }
                // 统计已经出现过的area
                Map<String,Integer> waterAreaMap = new HashMap<>();

                // 接下来把三个部分统计
                int waterTotal = 0;                           // 为最后计算百分比作准备
                for (Water water : waterList1) {
                    if(debug)System.out.println(water.getName());
                    int co2Year = DateUtil.getDateYear(water.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(2 + co2Year - year);
                    pieData.setValue(pieData.getValue() + water.getAmount());
                    ColumnData columnData = columnDataList.get(2 + co2Year - year);
                    columnData.setData(columnData.getData() + water.getAmount());
                    // 这是area的部分
                    waterTotal += water.getAmount();
                    String areaName = water.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = waterAreaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + water.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(water.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        waterAreaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / waterTotal);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);

                break;
        }

        return chartData;
    }

    public ChartData yearAndMonth(ChartReceiveData chartReceiveData){
        ChartData chartData = new ChartData();
        int year = chartReceiveData.getThisDate().getYear();
        int month = chartReceiveData.getThisDate().getMonth();
        int type = chartReceiveData.getType();
        int account = chartReceiveData.getAccount();

        boolean isThisYear = false;
        int thisSelectMonth = 0;

        // pieData
        List<PieData> pieDataList = new ArrayList<>();
        chartData.setPieData(pieDataList);
        // areaPercent
        List<AreaPercent> areaPercentList = new ArrayList<>();
        chartData.setAreaPercent(areaPercentList);
        // ColumnData
        List<ColumnData> columnDataList = new ArrayList<>();
        chartData.setColumnData(columnDataList);

        // 这里要作一个区分,如果是今年,那数据到本月即可
        if(year == DateUtil.getThisYear()){
            thisSelectMonth = DateUtil.getThisMonth();
        }else {
            thisSelectMonth = 12;
        }

        switch (type){
            case 0:
                List<CO2> co2List = new ArrayList<>();
                for (int i = 0; i < thisSelectMonth; i++) {
                    co2List.addAll(co2Mapper.selectByAccountAndYearAndMonth(account,year,i+1));
                    // 这里也合并掉,包括PieData和ColumnData
                    PieData perData= new PieData();
                    perData.setName("" + (i + 1) + "月");
                    pieDataList.add(perData);
                    ColumnData columnDataPerData = new ColumnData();
                    columnDataPerData.setName("" + (i + 1) + "月");
                    columnDataList.add(columnDataPerData);
                }
                // 统计已经出现过的area
                Map<String,Integer> areaMap = new HashMap<>();
                // 接下来把三个部分统计
                int total = 0;                           // 为最后计算百分比作准备
                for (CO2 co2 : co2List) {
                    if(debug)System.out.println(co2.getName());
                    int co2Month = DateUtil.getDateMonth(co2.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(co2Month);
                    pieData.setValue(pieData.getValue() + co2.getAmount());
                    ColumnData columnData = columnDataList.get(co2Month);
                    columnData.setData(columnData.getData() + co2.getAmount());
                    // 这是area的部分
                    total += co2.getAmount();
                    String areaName = co2.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = areaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + co2.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(co2.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        areaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / total);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);
                break;

            case 1:
                List<Power> powerList = new ArrayList<>();
                for (int i = 0; i < thisSelectMonth; i++) {
                    powerList.addAll(powerMapper.selectByAccountAndYearAndMonth(account,year,i+1));
                    // 这里也合并掉,包括PieData和ColumnData
                    PieData perData= new PieData();
                    perData.setName("" + (i + 1) + "月");
                    pieDataList.add(perData);
                    ColumnData columnDataPerData = new ColumnData();
                    columnDataPerData.setName("" + (i + 1) + "月");
                    columnDataList.add(columnDataPerData);
                }
                // 统计已经出现过的area
                Map<String,Integer> powerAreaMap = new HashMap<>();
                // 接下来把三个部分统计
                int powerTotal = 0;                           // 为最后计算百分比作准备
                for (Power power : powerList) {
                    if(debug)System.out.println(power.getName());
                    int co2Month = DateUtil.getDateMonth(power.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(co2Month);
                    pieData.setValue(pieData.getValue() + power.getAmount());
                    ColumnData columnData = columnDataList.get(co2Month);
                    columnData.setData(columnData.getData() + power.getAmount());
                    // 这是area的部分
                    powerTotal += power.getAmount();
                    String areaName = power.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = powerAreaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + power.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(power.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        powerAreaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / powerTotal);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);

                break;
            default:
                List<Water> waterList = new ArrayList<>();
                for (int i = 0; i < thisSelectMonth; i++) {
                    waterList.addAll(waterMapper.selectByAccountAndYearAndMonth(account,year,i+1));
                    // 这里也合并掉,包括PieData和ColumnData
                    PieData perData= new PieData();
                    perData.setName("" + (i + 1) + "月");
                    pieDataList.add(perData);
                    ColumnData columnDataPerData = new ColumnData();
                    columnDataPerData.setName("" + (i + 1) + "月");
                    columnDataList.add(columnDataPerData);
                }
                // 统计已经出现过的area
                Map<String,Integer> waterAreaMap = new HashMap<>();
                // 接下来把三个部分统计
                int waterTotal = 0;                           // 为最后计算百分比作准备
                for (Water water : waterList) {
                    if(debug)System.out.println(water.getName());
                    int co2Month = DateUtil.getDateMonth(water.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(co2Month);
                    pieData.setValue(pieData.getValue() + water.getAmount());
                    ColumnData columnData = columnDataList.get(co2Month);
                    columnData.setData(columnData.getData() + water.getAmount());
                    // 这是area的部分
                    waterTotal += water.getAmount();
                    String areaName = water.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = waterAreaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + water.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(water.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        waterAreaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / waterTotal);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);
                break;
        }

        return chartData;
    }

    private void columnDataGetGrowRate(List<ColumnData> columnDataList) {
        for (int i = 0; i < columnDataList.size(); i++) {
            ColumnData columnData = columnDataList.get(i);
            // 第一个或者前一项是0
            if(i == 0 || (columnDataList.get(i - 1).getData() == 0)){
                columnData.setGrowRate(0);
                continue;
            }
            ColumnData perData = columnDataList.get(i - 1);
            int growRate = (columnData.getData() - perData.getData()) * 100
                    /(perData.getData());
            columnData.setGrowRate(growRate);
        }
    }

    public ChartData yearAndMonthAndWeek(ChartReceiveData chartReceiveData){
        // week本质上也是用day
        ChartData chartData = new ChartData();
        int year = chartReceiveData.getThisDate().getYear();
        int month = chartReceiveData.getThisDate().getMonth();
        int day = chartReceiveData.getThisDate().getDay();
        int type = chartReceiveData.getType();
        int account = chartReceiveData.getAccount();

        // pieData
        List<PieData> pieDataList = new ArrayList<>();
        chartData.setPieData(pieDataList);
        // areaPercent
        List<AreaPercent> areaPercentList = new ArrayList<>();
        chartData.setAreaPercent(areaPercentList);
        // ColumnData
        List<ColumnData> columnDataList = new ArrayList<>();
        chartData.setColumnData(columnDataList);
        int total = 0;                           // 为最后计算百分比作准备
        switch (type){
            case 0:
                // 统计已经出现过的area
                Map<String,Integer> areaMap = new HashMap<>();
                // 这里直接设置成近七天
                for (int i = 0; i < 7; i++) {
                    // 这里我们要重新计算year,month,day
                    LocalDate dayBeforeXday = DateUtil.getDayBeforeXday(year,month,day, 6 - i);
                    int beforeYear = dayBeforeXday.getYear();
                    int beforeMonth = dayBeforeXday.getMonthValue();
                    int beforeDay  = dayBeforeXday.getDayOfMonth();
                    List<CO2> co2List = co2Mapper.selectByAccountAndYearAndMonthAndDay(account,beforeYear,beforeMonth,beforeDay);
                    // 这里也合并掉,包括PieData和ColumnData
                    PieData perData= new PieData();
                    perData.setName(dayBeforeXday.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.CHINA));
                    pieDataList.add(perData);
                    ColumnData columnDataPerData = new ColumnData();
                    columnDataPerData.setName(dayBeforeXday.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.CHINA));
                    columnDataList.add(columnDataPerData);
                    // 必须提前,因为对应关系不固定
                    for (CO2 co2 : co2List) {
                        if(debug)System.out.println(co2.getName());
                        perData.setValue(perData.getValue() + co2.getAmount());
                        columnDataPerData.setData(columnDataPerData.getData() + co2.getAmount());
                        // 这是area的部分
                        total += co2.getAmount();
                        String areaName = co2.getArea();

                        // 如果之前查的到,就查出位置,然后先累加上
                        Integer areaNumber = areaMap.get(areaName);
                        if(areaNumber != null){
                            AreaPercent areaPercent = areaPercentList.get(areaNumber);
                            areaPercent.setPercent(areaPercent.getPercent() + co2.getAmount());
                            // 发现查不到,那就说明需要新处理
                        }else {
                            // 已有元素个数
                            int size = areaPercentList.size();
                            AreaPercent areaPercent = new AreaPercent();
                            areaPercent.setAreaName(areaName);
                            // 暂时先记录数字,最后处理成百分比
                            areaPercent.setPercent(co2.getAmount());
                            // 添加上去
                            areaPercentList.add(areaPercent);
                            areaMap.put(areaName,size);
                        }
                        // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                    }
                }

                // 接下来把三个部分统计
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / total);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);

                break;
            case 1:
                break;
            default:

                break;
        }

        return chartData;
    }

    public ChartData monthAndDay(ChartReceiveData chartReceiveData){
        ChartData chartData = new ChartData();
        int year = chartReceiveData.getThisDate().getYear();
        int month = chartReceiveData.getThisDate().getMonth();
        int day = chartReceiveData.getThisDate().getDay();
        int type = chartReceiveData.getType();
        int account = chartReceiveData.getAccount();

        boolean isThisToday = false;
        int hourToCount = 24;

        // pieData
        List<PieData> pieDataList = new ArrayList<>();
        chartData.setPieData(pieDataList);
        // areaPercent
        List<AreaPercent> areaPercentList = new ArrayList<>();
        chartData.setAreaPercent(areaPercentList);
        // ColumnData
        List<ColumnData> columnDataList = new ArrayList<>();
        chartData.setColumnData(columnDataList);

        // 这里要作一个区分,如果是今年,那数据到本月即可
        if(DateUtil.isToday(year,month,day)){
            hourToCount = DateUtil.getNowHour();
        }else {
            hourToCount = 24;
        }

        switch (type){
            case 0:
                List<CO2> co2List = new ArrayList<>();
                // 只有一行
                co2List.addAll(co2Mapper.selectByAccountAndYearAndMonthAndDay(account,year,month,day));
                for (int i = 0; i < hourToCount; i++) {
                    // 这里也合并掉,包括PieData和ColumnData
                    PieData perData= new PieData();
                    perData.setName("" + (i + 1) + "h");
                    pieDataList.add(perData);
                    ColumnData columnDataPerData = new ColumnData();
                    columnDataPerData.setName("" + (i + 1) + "h");
                    columnDataList.add(columnDataPerData);
                }
                // 统计已经出现过的area
                Map<String,Integer> areaMap = new HashMap<>();
                // 接下来把三个部分统计
                int total = 0;                           // 为最后计算百分比作准备
                for (CO2 co2 : co2List) {
                    if(debug)System.out.println(co2.getName());
                    if(debug) System.out.println(co2.getDate());
                    int co2Hour = DateUtil.getDateHour(co2.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(co2Hour);
                    pieData.setValue(pieData.getValue() + co2.getAmount());
                    ColumnData columnData = columnDataList.get(co2Hour);
                    columnData.setData(columnData.getData() + co2.getAmount());
                    // 这是area的部分
                    total += co2.getAmount();
                    String areaName = co2.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = areaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + co2.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(co2.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        areaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / total);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);

                break;
            case 1:
                List<Power> powerList = new ArrayList<>();
                // 只有一行
                powerList.addAll(powerMapper.selectByAccountAndYearAndMonthAndDay(account,year,month,day));
                for (int i = 0; i < hourToCount; i++) {
                    // 这里也合并掉,包括PieData和ColumnData
                    PieData perData= new PieData();
                    perData.setName("" + (i + 1) + "h");
                    pieDataList.add(perData);
                    ColumnData columnDataPerData = new ColumnData();
                    columnDataPerData.setName("" + (i + 1) + "h");
                    columnDataList.add(columnDataPerData);
                }
                // 统计已经出现过的area
                Map<String,Integer> powerAreaMap = new HashMap<>();
                // 接下来把三个部分统计
                int powerTotal = 0;                           // 为最后计算百分比作准备
                for (Power power : powerList) {
                    if(debug)System.out.println(power.getName());
                    if(debug) System.out.println(power.getDate());
                    int co2Hour = DateUtil.getDateHour(power.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(co2Hour);
                    pieData.setValue(pieData.getValue() + power.getAmount());
                    ColumnData columnData = columnDataList.get(co2Hour);
                    columnData.setData(columnData.getData() + power.getAmount());
                    // 这是area的部分
                    powerTotal += power.getAmount();
                    String areaName = power.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = powerAreaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + power.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(power.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        powerAreaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / powerTotal);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);
                break;
            default:
                List<Water> waterList = new ArrayList<>();
                // 只有一行
                waterList.addAll(waterMapper.selectByAccountAndYearAndMonthAndDay(account,year,month,day));
                for (int i = 0; i < hourToCount; i++) {
                    // 这里也合并掉,包括PieData和ColumnData
                    PieData perData= new PieData();
                    perData.setName("" + (i + 1) + "h");
                    pieDataList.add(perData);
                    ColumnData columnDataPerData = new ColumnData();
                    columnDataPerData.setName("" + (i + 1) + "h");
                    columnDataList.add(columnDataPerData);
                }
                // 统计已经出现过的area
                Map<String,Integer> waterAreaMap = new HashMap<>();
                // 接下来把三个部分统计
                int waterTotal = 0;                           // 为最后计算百分比作准备
                for (Water water : waterList) {
                    if(debug)System.out.println(water.getName());
                    if(debug) System.out.println(water.getDate());
                    int co2Hour = DateUtil.getDateHour(water.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(co2Hour);
                    pieData.setValue(pieData.getValue() + water.getAmount());
                    ColumnData columnData = columnDataList.get(co2Hour);
                    columnData.setData(columnData.getData() + water.getAmount());
                    // 这是area的部分
                    waterTotal += water.getAmount();
                    String areaName = water.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = waterAreaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + water.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(water.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        waterAreaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / waterTotal);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);

                break;
        }

        return chartData;
    }

    // 这里是具体查询
    public ChartData trueMonth(ChartReceiveData chartReceiveData){
        ChartData chartData = new ChartData();
        int year = chartReceiveData.getThisDate().getYear();
        int month = chartReceiveData.getThisDate().getMonth();
        int type = chartReceiveData.getType();
        int account = chartReceiveData.getAccount();

        boolean isThisMonth = false;
        int thisSelectMonth = 0;

        // pieData
        List<PieData> pieDataList = new ArrayList<>();
        chartData.setPieData(pieDataList);
        // areaPercent
        List<AreaPercent> areaPercentList = new ArrayList<>();
        chartData.setAreaPercent(areaPercentList);
        // ColumnData
        List<ColumnData> columnDataList = new ArrayList<>();
        chartData.setColumnData(columnDataList);

        // 这里要作一个区分,如果是今年,那数据到本月即可
        if(DateUtil.isThisMonth(year,month)){
            thisSelectMonth = DateUtil.getThisDay();
        }else {
            thisSelectMonth = DateUtil.getMaxMonthDay(year,month);
        }

        switch (type){
            case 0:
                List<CO2> co2List = new ArrayList<>();
                co2List.addAll(co2Mapper.selectByAccountAndYearAndMonth(account,year,month));
                for (int i = 0; i < thisSelectMonth; i++) {
                    // 这里也合并掉,包括PieData和ColumnData
                    PieData perData= new PieData();
                    perData.setName("" + (i + 1) + "号");
                    pieDataList.add(perData);
                    ColumnData columnDataPerData = new ColumnData();
                    columnDataPerData.setName("" + (i + 1) + "号");
                    columnDataList.add(columnDataPerData);
                }
                // 统计已经出现过的area
                Map<String,Integer> areaMap = new HashMap<>();
                // 接下来把三个部分统计
                int total = 0;                           // 为最后计算百分比作准备
                for (CO2 co2 : co2List) {
                    if(debug)System.out.println(co2.getName());
                    int co2Day = DateUtil.getDateDay(co2.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(co2Day - 1);
                    pieData.setValue(pieData.getValue() + co2.getAmount());
                    ColumnData columnData = columnDataList.get(co2Day - 1);
                    columnData.setData(columnData.getData() + co2.getAmount());
                    // 这是area的部分
                    total += co2.getAmount();
                    String areaName = co2.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = areaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + co2.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(co2.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        areaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / total);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);
                break;
            case 1:
                List<Power> powerList = new ArrayList<>();
                powerList.addAll(powerMapper.selectByAccountAndYearAndMonth(account,year,month));
                for (int i = 0; i < thisSelectMonth; i++) {
                    // 这里也合并掉,包括PieData和ColumnData
                    PieData perData= new PieData();
                    perData.setName("" + (i + 1) + "号");
                    pieDataList.add(perData);
                    ColumnData columnDataPerData = new ColumnData();
                    columnDataPerData.setName("" + (i + 1) + "号");
                    columnDataList.add(columnDataPerData);
                }
                // 统计已经出现过的area
                Map<String,Integer> powerAreaMap = new HashMap<>();
                // 接下来把三个部分统计
                int powerTotal = 0;                           // 为最后计算百分比作准备
                for (Power power : powerList) {
                    if(debug)System.out.println(power.getName());
                    int co2Day = DateUtil.getDateDay(power.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(co2Day - 1);
                    pieData.setValue(pieData.getValue() + power.getAmount());
                    ColumnData columnData = columnDataList.get(co2Day - 1);
                    columnData.setData(columnData.getData() + power.getAmount());
                    // 这是area的部分
                    powerTotal += power.getAmount();
                    String areaName = power.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = powerAreaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + power.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(power.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        powerAreaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / powerTotal);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);
                break;
            default:
                List<Water> waterList = new ArrayList<>();
                waterList.addAll(waterMapper.selectByAccountAndYearAndMonth(account,year,month));
                for (int i = 0; i < thisSelectMonth; i++) {
                    // 这里也合并掉,包括PieData和ColumnData
                    PieData perData= new PieData();
                    perData.setName("" + (i + 1) + "号");
                    pieDataList.add(perData);
                    ColumnData columnDataPerData = new ColumnData();
                    columnDataPerData.setName("" + (i + 1) + "号");
                    columnDataList.add(columnDataPerData);
                }
                // 统计已经出现过的area
                Map<String,Integer> waterAreaMap = new HashMap<>();
                // 接下来把三个部分统计
                int waterTotal = 0;                           // 为最后计算百分比作准备
                for (Water water : waterList) {
                    if(debug)System.out.println(water.getName());
                    int co2Day = DateUtil.getDateDay(water.getDate());
                    // 获取到对应的年份
                    PieData pieData = pieDataList.get(co2Day - 1);
                    pieData.setValue(pieData.getValue() + water.getAmount());
                    ColumnData columnData = columnDataList.get(co2Day - 1);
                    columnData.setData(columnData.getData() + water.getAmount());
                    // 这是area的部分
                    waterTotal += water.getAmount();
                    String areaName = water.getArea();

                    // 如果之前查的到,就查出位置,然后先累加上
                    Integer areaNumber = waterAreaMap.get(areaName);
                    if(areaNumber != null){
                        AreaPercent areaPercent = areaPercentList.get(areaNumber);
                        areaPercent.setPercent(areaPercent.getPercent() + water.getAmount());
                        // 发现查不到,那就说明需要新处理
                    }else {
                        // 已有元素个数
                        int size = areaPercentList.size();
                        AreaPercent areaPercent = new AreaPercent();
                        areaPercent.setAreaName(areaName);
                        // 暂时先记录数字,最后处理成百分比
                        areaPercent.setPercent(water.getAmount());
                        // 添加上去
                        areaPercentList.add(areaPercent);
                        waterAreaMap.put(areaName,size);
                    }
                    // 最后是柱状图部分,这部分其实和PieChart部分几乎一模一样。
                }
                // 将数值转化为百分比
                for (AreaPercent area : areaPercentList) {
                    area.setPercent(area.getPercent() * 100 / waterTotal);
                }
                // 柱状图的成长率部分得通过这部分计算得到
                columnDataGetGrowRate(columnDataList);
                break;
        }

        return chartData;
    }

    public ChartData illgalRead(ChartReceiveData chartReceiveData) {
        ChartData chartData = new ChartData();
        int type = chartReceiveData.getType();
        int account = chartReceiveData.getAccount();
        ThisDate thisDate = new ThisDate();
        thisDate.setYear(DateUtil.getThisYear());
        chartReceiveData.setThisDate(thisDate);

        return onlyYear(chartReceiveData);
    }
    // 这里把复用代码
}
