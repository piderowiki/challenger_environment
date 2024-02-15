package com.ynu.challenger.handler;

import com.ynu.challenger.VO.*;
import com.ynu.challenger.service.ChartService;
import com.ynu.challenger.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChartHandler {
    @Autowired
    ChartService chartService;

    @RequestMapping("/main/chart/co2")
    public ModelAndView CO2ChartHandler(){
        // 这里我们把东西塞进那个什么东西里面
        ModelAndView mav = new ModelAndView();
        mav.addObject("ThisDateYear",DateUtil.getThisYear());
        mav.addObject("ThisDateMonth",DateUtil.getThisMonth());
        mav.addObject("ThisDateWeek", DateUtil.getThisWeek());
        mav.addObject("ThisDateDay",DateUtil.getThisDay());
        mav.addObject("type",0);
        mav.setViewName("change");
        return mav;
    }

    @ResponseBody
    @RequestMapping("/main/chart/fresh")
    // 根据日期来完成查询
    public ChartData ChartFreshByDateHandler(@RequestBody ChartReceiveData chartReceiveData){
        System.out.println(chartReceiveData);
        ThisDate checkDate = chartReceiveData.getThisDate();
        // 根据Receive到的值确认一下
        // 有以下几种可能:1.只有年,2.有年,月,3.有年,月,周,4.有年,月,日;其余情况全部视为非法
        // 别的可能是空查询,其它情况都会带上年.
        // 还有一种特殊情况,是虽然有年有月,但是实际上是查本月
        if(checkDate.getMonth() == 0 && checkDate.getYear() != 0){
            return chartService.onlyYear(chartReceiveData);
        }else if(checkDate.getWeek() != 0){
            return chartService.yearAndMonthAndWeek(chartReceiveData);
        }else if(checkDate.getDay() != 0){
            return chartService.monthAndDay(chartReceiveData);
        }// 这个情况只可能是有年有月,-1是那啥的查询
        else if(checkDate.getYear() != 0 && checkDate.getMonth() == -1){
            return chartService.yearAndMonth(chartReceiveData);
        }else if(checkDate.getYear() != 0 && checkDate.getMonth() != -1){
            return chartService.trueMonth(chartReceiveData);
        }
        // 非法情况
        else {
            System.out.println("检测到非法输入");
            // 查询结果直接给空就行,不做多余处理了
            return chartService.illgalRead(chartReceiveData);
        }
    }
}
