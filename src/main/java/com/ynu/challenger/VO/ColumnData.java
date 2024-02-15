package com.ynu.challenger.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
// 实际上只需要传data数据,但是也确实可以传下标
public class ColumnData {
    int data;
    int growRate;
    String name;
}
