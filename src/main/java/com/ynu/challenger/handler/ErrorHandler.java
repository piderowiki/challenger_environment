package com.ynu.challenger.handler;

import com.ynu.challenger.service.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorHandler {
    @Autowired
    ErrorService errorService;

    @RequestMapping("/main/error/{page}")
    public ModelAndView ErrorHandler(@PathVariable int page){
        ModelAndView mav = new ModelAndView();
        // 主要要发送的数据是
        // 1.回传的页数   2.4个或更少的错误日志  3.电表位置和状态的回传.
        // 关于第三点要做特别说明,由于区域数未知,所以我们分为 电表位置的回传/状态的回传
        // 状态永远只有两种，很简单，只用传0或者1
        // 电表位置比较麻烦,首先我们需要传全部电表的位置信息，然后再传选择的情况，默认为1
        // 哈哈
        mav.setViewName("web");
        return mav;
    }
}
