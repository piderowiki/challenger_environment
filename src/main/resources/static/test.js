var chartDom1 = document.getElementById('f2_2_1_2_1_2');//柱状图
var myChart1 = echarts.init(chartDom1);

var chartDom2 = document.getElementById('f2_2_1_2_1_3');//饼图
var myChart2 = echarts.init(chartDom2);

var chartDom3 = document.getElementById('canvas_f2_2_1_2_2');
var div_f2_2_1_2_2 = document.getElementById('f2_2_1_2_2');

var f2_2_1_2_2_buttom_left = document.querySelector('.f2_2_1_2_2_buttom_left');
var f2_2_1_2_2_buttom_right = document.querySelector('.f2_2_1_2_2_buttom_right');





var date = [//区域条形图绘制所需数据
  { color: "#9055A2", percentage: 50, text: 'A区域' },
  { color: "#FFF9f3", percentage: 30, text: 'B区域' },
  { color: "#76B6EA", percentage: 20, text: 'C区域' },
  { color: "#F7CAC9", percentage: 50, text: 'A区域' },
  { color: "#FFA500", percentage: 30, text: 'B区域' },
  { color: "#7ED321", percentage: 20, text: 'C区域' },
  { color: "#7ED321", percentage: 20, text: 'C区域' }
];
var offset=0;//记录翻过的页数
var pow_date=[
  {x_pow:0,y_pow:0},
  {x_pow:0,y_pow:1},
  {x_pow:0,y_pow:2},
  {x_pow:1,y_pow:0},
  {x_pow:1,y_pow:1},
  {x_pow:1,y_pow:2}
];
/*
document.getElementById("年").innerText = ;
document.getElementById("月").innerText = ;
document.getElementById("周").innerText = ;
document.getElementById("日").innerText = ;

myChart1.setOption({//柱状图修改数据
  
});
myChart2.setOption({
  series: [{
      data: data_day
  }]
});

*/


    // 配置图表选项
    var option = {
      title: {
        text: '月份数据'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['Sales', 'Growth Rate']
      },
      xAxis: {
        type: 'category',
        data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun']
        
      },
      yAxis: [
        {
          type: 'value',
          name: 'Sales',
          min: 0,
          max: 250,
          interval: 50,
          axisLabel: {
            formatter: '{value}K'
          }
        },
        {
          type: 'value',
          name: 'Growth Rate',
          min: 0,
          max: 25,
          interval: 5,
          axisLabel: {
            formatter: '{value}%'
          }
        }
      ],
      series: [
        {
          name: 'Sales',
          type: 'bar',
          data: [150, 200, 180, 210, 220, 190]
        },
        {
          name: 'Growth Rate',
          type: 'line',
          yAxisIndex: 1,
          data: [10, 15, 12, 18, 20, 16]
        }
      ]
    };
    myChart1.setOption(option);


var data_month = [
    {value: 1, name: '1月'},
    {value: 2, name: '2月'},
    {value: 3, name: '3月'},
    {value: 4, name: '4月'},
    {value: 5, name: '5月'},
    {value: 6, name: '6月'},
    {value: 7, name: '7月'},
    {value: 8, name: '8月'},
    {value: 9, name: '9月'},
    {value: 10, name: '10月'},
    {value: 11, name: '11月'},
    {value: 12, name: '12月'}
];
var data_day = [
    {value: 1, name: '1号'},
    {value: 2, name: '2号'},
    {value: 3, name: '3号'},
    {value: 4, name: '4号'},
    {value: 5, name: '5号'},
    {value: 6, name: '6号'},
    {value: 7, name: '7号'},
    {value: 8, name: '8号'},
    {value: 9, name: '9号'},
    {value: 10, name: '10号'},
    {value: 11, name: '11号'},
    {value: 12, name: '12号'},
    {value: 13, name: '13号'},
    {value: 14, name: '14号'},
    {value: 15, name: '15号'},
    {value: 16, name: '16号'},
    {value: 17, name: '17号'},
    {value: 18, name: '18号'},
    {value: 19, name: '19号'},
    {value: 20, name: '20号'},
    {value: 21, name: '21号'},
    {value: 22, name: '22号'},
    {value: 23, name: '23号'},
    {value: 24, name: '24号'}
    
];

draw_picture_f2_2_1_2_2();

var option = {
    series: [
        {
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: data_month,
            label: {
                formatter: '{b} : {c} ({d}%)'
            }
        }
    ]
};
myChart2.setOption(option);
    



window.addEventListener('resize', function () {// 在窗口大小变化时调整图表大小
    myChart1.resize();//重画柱状图
    myChart2.resize();//重画饼状图
    draw_picture_f2_2_1_2_2();//重画条形框

    myChart2.setOption({
        series: [{
            data: data_day
        }]
    });

   
    draw_picture_f2_2_1_2_2();
});




function draw_picture_f2_2_1_2_2() {//需要重画条形图时调用
  var rect_f2_2_1_2_2 = div_f2_2_1_2_2.getBoundingClientRect();
  var width = rect_f2_2_1_2_2.width; // 获取宽度
  var height = rect_f2_2_1_2_2.height; // 获取高度
  // 修改Canvas的大小
  chartDom3.width = width;
  chartDom3.height = height;
  var y=height*70/250.0;
  var x=width*500/1000;
  var ctx = chartDom3.getContext('2d');
  ctx.clearRect(0, 0, width, height);//先清除画布
  
  var show_number=0;
  if(date.length-offset*6<=6){
    show_number=date.length-offset*6;
  }else{
    show_number=6;
  }
  
  for(var i=0+offset*6;i<show_number+offset*6;i++){
    var item=date[i];
    draw_one(width,height,x*pow_date[i%6].x_pow,y*pow_date[i%6].y_pow,item.color,item.percentage,item.text);
  }
}


function draw_one(width,height,left,right,color,percent,text){
  var ctx = chartDom3.getContext('2d');


  var borderX = width*80/1000.0+left; // 长方形左上角 x 坐标
  var borderY = height*50/250.0+right; // 长方形左上角 y 坐标
  var borderWidth = width*320/1000.0; // 长方形宽度
  var borderHeight = height*20/250; // 长方形高度

    ctx.fillStyle = 'black'; // 设置文本的颜色
    ctx.font = width/900*10+"px Bold"; // 设置文本的字体样式和大小
    ctx.textAlign = 'left'; // 设置文本的对齐方式
    ctx.fillText(text, borderX, borderY - Math.min(width,height) / 18); // 绘制文本，将文本内容替换为你想要显示的实际文本


    // 绘制长方形边框
    ctx.strokeStyle = '#3c4f6d'; // 设置边框的颜色
    ctx.lineWidth = 2; // 设置边框线条的粗细
    ctx.strokeRect(borderX, borderY, borderWidth, borderHeight); // 绘制长方形边框


    const circleX = borderX - width*30/1000; // 圆心 x 坐标
    const circleY = borderY + borderHeight / 2; // 圆心 y 坐标
    const radius = 8; // 圆的半径

    ctx.beginPath();
    ctx.arc(circleX, circleY, radius, 0, Math.PI * 2); // 绘制圆形路径
    ctx.fillStyle = color; // 设置圆形填充颜色
    ctx.fill(); // 填充圆形
    ctx.closePath();
    // 绘制条形
    ctx.fillStyle = color; // 设置条形的填充颜色
    ctx.fillRect(borderX, borderY, borderWidth*percent/100, borderHeight); // 绘制填充的矩形


    ctx.fillStyle = '#3c4f6d'; // 设置文本的颜色
    ctx.font = Math.min(width, height) / 15 + "px Arial"; // 设置文本的字体样式和大小
    ctx.textAlign = 'left'; // 设置文本的对齐方式
    ctx.fillText(percent+'%', borderX + borderWidth+5  , borderY + borderHeight / 2 ); // 绘制文本

}






// 为 .f2_2_1_2_2_buttom_left 元素添加点击事件监听器
f2_2_1_2_2_buttom_left.addEventListener('click', function() {
  // 在这里编写你的处理函数代码
  
  if(offset>0){
    offset-=1;
  }
  draw_picture_f2_2_1_2_2();
});
// 为 .f2_2_1_2_2_buttom_right 元素添加点击事件监听器
f2_2_1_2_2_buttom_right.addEventListener('click', function() {
  // 在这里编写你的处理函数代码
  if(offset+6<date.length){
    offset+=1;
  }
  draw_picture_f2_2_1_2_2();
});













