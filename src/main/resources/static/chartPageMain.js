$(function () {
    var unit = "t"
    var chooseType = 0
    $("#year_btn").click(function () {
        chooseType = 0;
        $.ajax({
            url: "/main/chart/fresh",
            type:"POST",
            // type 0代表是CO2
            contentType:"application/json;charset=UTF-8",
            data:JSON.stringify({account:1,type:thisType,thisDate:{
                    year:thisYear
                }}),
            success:function (response) {
                handleData(response,unit)
            },
            dataType:"json",
        })
    })

    $("#month_btn").click(function () {
        chooseType = 1;
        $.ajax({
            url: "/main/chart/fresh",
            type:"POST",
            // type 0代表是CO2
            contentType:"application/json;charset=UTF-8",
            data:JSON.stringify({account:1,type:thisType,thisDate:{
                    year:thisYear,
                    month:-1
                }}),
            success:function (response) {
                handleData(response,unit)
            },
            dataType:"json",
        })
    })


    $("#week_btn").click(function () {
        chooseType = 2;
        $.ajax({
            url: "/main/chart/fresh",
            type:"POST",
            // type 0代表是CO2
            contentType:"application/json;charset=UTF-8",
            data:JSON.stringify({account:1,type:thisType,thisDate:{
                    year:thisYear,
                    month:thisMonth,
                    week:-1,
                    day:thisDay
                }}),
            success:function (response) {
                handleData(response,unit)
            },
            dataType:"json",
        })
    })

    $("#day_btn").click(function () {
        chooseType = 3;
        $.ajax({
            url: "/main/chart/fresh",
            type:"POST",
            // type 0代表是CO2
            contentType:"application/json;charset=UTF-8",
            data:JSON.stringify({account:1,type:thisType,thisDate:{
                    year:thisYear,
                    month:thisMonth,
                    day:thisDay
                }}),
            success:function (response) {
                handleData(response,unit)
            },
            dataType:"json",
        })
    })

    // 这里的改变type逻辑比较复杂,就往下走
    $("#submit_btn").click(function () {
        // 这三个用来作为查找的设置
        var selectYear = parseInt($("#yearInput").val())
        var selectMonth= parseInt($("#monthInput").val())
        var selectDay  = parseInt($("#dayInput").val())

        // 非法输入统一视为输入为空
        if(!isNaN(selectYear)){
            thisYear = selectYear
            chooseType = 0
        }
        if(!isNaN(selectMonth)){
            thisMonth = selectMonth
            chooseType = 1
        }
        if(!isNaN(selectDay)){
            thisDay = selectDay
            chooseType = 3
        }

        $.ajax({
            url: "/main/chart/fresh",
            type:"POST",
            // type 0代表是CO2
            contentType:"application/json;charset=UTF-8",
            data:JSON.stringify({account:1,type:thisType,thisDate:{
                    year:selectYear,
                    month:selectMonth,
                    day:selectDay
                }}),
            success:function (response) {
                handleData(response,unit)
            },
            dataType:"json",
        })
    })


    $("#leftBtn").click(function () {
        // 这俩仅针对
        var handleDate = new Date(thisYear,thisMonth,thisDay)
        var oneDay = 24 * 3600 * 1000
        var nowTime = handleDate.getTime()
        alert("现在的type是" + chooseType)
        // 是年
        if(chooseType == 0){
            thisYear = thisYear - 1
            $("#year_btn").click()
        // 是月
        }else if(chooseType == 1){
            thisMonth = thisMonth - 1
            if(thisMonth == 0){
                thisMonth = 12
                thisYear = thisYear - 1
                $("#month_btn").click()
            }
        }// 是周
        else if(chooseType == 2){
            var handleTime =nowTime - 7 * oneDay
            var newTime = new Date(handleTime)
            thisYear  = newTime.getFullYear()
            thisMonth = newTime.getMonth()+1
            thisDay   = newTime.getDay()+1
            alert(thisYear + "," +thisMonth+","+thisDay);
            $("#week_btn").click()
        }// 是天
        else if(chooseType == 3){
            var handleTime =nowTime - oneDay
            var newTime = new Date(handleTime)
            thisYear  = newTime.getFullYear()
            thisMonth = newTime.getMonth()+1
            thisDay   = newTime.getDay()+1
            $("#day_btn").click()
        }
    })

    $("#rightBtn").click(function () {
        var handleDate = new Date(thisYear,thisMonth,thisDay)
        var oneDay = 24 * 3600 * 1000
        var nowTime = handleDate.getTime()
        alert("现在的type是" + chooseType)
        // 是年
        if(chooseType == 0){
            thisYear = thisYear + 1
            $("#year_btn").click()
            // 是月
        }else if(chooseType == 1){
            thisMonth = thisMonth + 1
            if(thisMonth == 13){
                thisMonth = 1
                thisYear = thisYear + 1
                $("#month_btn").click()
            }
        }// 是周
        else if(chooseType == 2){
            var handleTime =nowTime + 7 * oneDay
            var newTime = new Date(handleTime)
            thisYear  = newTime.getFullYear()
            thisMonth = newTime.getMonth()+1
            thisDay   = newTime.getDay()+1
            alert(thisYear + "," +thisMonth+","+thisDay);
            $("#week_btn").click()
        }// 是天
        else if(chooseType == 3){
            var handleTime =nowTime + oneDay
            var newTime = new Date(handleTime)
            thisYear  = newTime.getFullYear()
            thisMonth = newTime.getMonth()+1
            thisDay   = newTime.getDay()+1
            alert(thisYear + "," +thisMonth+","+thisDay);
            $("#day_btn").click()
        }
    })

    // 开始就点击
    $("#year_btn").click()
})

// unit是单位
function handleData(response,unit) {
    // 处理饼状图
    data_day = response.pieData
    myChart2.setOption({
        series: [{
            data: data_day
        }]
    });
    // 处理区域
    var AreaPercentData = [];
    for (let i = 0; i < response.areaPercent.length; i++){
        var areaPercentOneData = {
            color: "#9055A2",
            percentage: response.areaPercent[i].percent,
            text: response.areaPercent[i].areaName
        }
        // 设置一下颜色
        if(i == 1){
            areaPercentOneData.color = "#FFF9f3"
        }else if( i == 2){
            areaPercentOneData.color =  "#76B6EA"
        }else if(i == 3){
            areaPercentOneData.color = "#F7CAC9"
        }else if(i == 4){
            areaPercentOneData.color = "#FFA500"
        }else if(i == 5){
            areaPercentOneData.color = "#7ED321"
        }
        AreaPercentData.push(areaPercentOneData)
    }
    date = AreaPercentData
    // 重绘这片区域
    draw_picture_f2_2_1_2_2()
    if(AreaPercentData.length <= 6){
        $("#areaLeftBtn").hide()
        $("#areaRightBtn").hide()
    }else {
        $("#areaLeftBtn").show()
        $("#areaRightBtn").show()
    }

    // 最后是柱状图,首先把基础数据排列好,然后我们再好好处理
    var columnDataArr = []
    var growDataArr = []
    var nameDataAr= []
    var maxData = 0
    for (let i = 0; i < response.columnData.length;i++){
        var columnData = response.columnData[i].data
        var columnGrowRate = response.columnData[i].growRate
        // 获取最大最小值来确定范围
        if(columnData > maxData)maxData = columnData
        columnDataArr.push(columnData)
        growDataArr.push(columnGrowRate)
        nameDataAr.push(response.columnData[i].name)
    }

    var option = {
        title: {
            text: '年度数据'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['使用量', '增长率']
        },
        xAxis: {
            type: 'category',
            data: nameDataAr

        },
        yAxis: [
            {
                type: 'value',
                name: '使用量',
                min: 0,
                max: maxData * 1.2,
                interval: (maxData * 1.2)/10,
                axisLabel: {
                    formatter: '{value}' + unit
                }
            },
            {
                type: 'value',
                name: '增长率',
                min: -100,
                max: 100,
                interval: 20,
                axisLabel: {
                    formatter: '{value}%'
                }
            }
        ],
        series: [
            {
                name: '使用量',
                type: 'bar',
                data: columnDataArr
            },
            {
                name: '增长率',
                type: 'line',
                yAxisIndex: 1,
                data: growDataArr
            }
        ]
    }
    myChart1.setOption(option)
}