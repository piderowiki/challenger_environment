package com.ynu.challenger.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartReceiveData {
    int account;
    int type;
    ThisDate thisDate;
}
