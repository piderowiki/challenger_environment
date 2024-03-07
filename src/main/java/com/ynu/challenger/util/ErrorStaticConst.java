package com.ynu.challenger.util;

public class ErrorStaticConst {
    // 0 代表问题已经解决
    public static final int ERROR_FINISHED = 0;
    // 1 代表出现问题且需要解决
    public static final int ERROR_APPEAR = 1;
    // 2 代表问题暂且搁置
    public static final int ERROR_NERVERMIND = 2;
    // 0 代表问题完全不严重
    public static final int ERROR_URGENCY_LOW = 0;
    // 1 代表存在问题
    public static final int ERROR_URGENCY_MID = 1;
    // 2 代表非常紧急
    public static final int ERROR_URGENCY_HIGH = 2;
}
