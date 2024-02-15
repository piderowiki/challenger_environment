package com.ynu.challenger.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartData {
    List<AreaPercent> areaPercent;
    // 这个很复杂,慢慢分析
    List<ColumnData> columnData;
    List<PieData> pieData;
}
