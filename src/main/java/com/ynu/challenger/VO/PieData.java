package com.ynu.challenger.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 这个PieData用来给饼状图发数据
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieData {
    private int value;
    private String name;
}
