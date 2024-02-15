var selectedAnalysisType = null;//选择分析类型
var totalUsage = null;//总用量
// 计算年份统计信息
var highUsageYear = 'xxxx'; // 用量最多的年份
var lowUsageYear = 'xxxx'; // 用量最少的年份

// 计算月份统计信息
var highUsageMonth = 'xx'; // 用量最多的月份
var lowUsageMonth = 'xx'; // 用量最少的月份

// 计算日期统计信息
var highUsageDay = 'xx'; // 用量最多的日期
var lowUsageDay = 'xx'; // 用量最少的日期

var highExceptionYear = 'xxxx';//异常情况最多年
var lowExceptionYear = 'xxxx';

var highExceptionMonth = 'xx';//异常情况最多月
var lowExceptionMonth = 'xx';

var highExceptionDay = 'xx';//异常情况最多日
var lowExceptionDay = 'xx';

var unsolvedExceptionYear = 'xxxx';//未解决异常情况年
var solvedExceptionYear = 'xxxx';//解决异常情况年

var unsolvedExceptionMonth = 'xx';//未解决异常情况月
var solvedExceptionMonth = 'xx';//解决异常情况月

var unsolvedExceptionDay = 'xx';//未解决异常情况
var solvedExceptionDay = 'xx';//解决异常情况日

function selectAnalysis(type) {
    selectedAnalysisType = type;
    // 在按钮上添加样式以表示当前选择
    document.querySelectorAll('.container_button button').forEach(btn => {
        btn.classList.remove('selected');
    });
    event.target.classList.add('selected');
}

function showDatePicker(inputId) {
    var startDateError = document.getElementById('startDateError');
    var endDateError = document.getElementById('endDateError');
    
    // 先清空错误提示内容和样式
    startDateError.textContent = "";
    endDateError.textContent = "";
    // 检查是否选择了分析内容
    if (selectedAnalysisType === null) {

        // 如果没有选中的按钮，显示提示
        startDateError.textContent = "请先选择需要分析的内容";
        return;
    }

    var inputElement = document.getElementById(inputId);
    inputElement.type = "date";
    inputElement.addEventListener('blur', function () {
        if (!this.value) {
            this.type = "text";
            this.placeholder = "选择" + (inputId === "startDate" ? "起始" : "截至") + "日期 年/月/日";
        }
        validateDates();
    });
}

function validateDates() {
    var startDate = document.getElementById('startDate').value;
    var endDate = document.getElementById('endDate').value;
    var startDateError = document.getElementById('startDateError');
    var endDateError = document.getElementById('endDateError');
    var confirmButton = document.querySelector('.button4');

    // 先清空错误提示内容和样式
    startDateError.textContent = "";
    endDateError.textContent = "";

    // 获取当前选中的按钮
    var selectedButton = document.querySelector('.container_button button.selected');

    if (startDate && endDate) {
        if (new Date(startDate) > new Date(endDate)) {
            startDateError.textContent = "起始日期不能晚于截至日期";
        }

        var today = new Date();
        var startDateRange = new Date("2010-01-01");

        if (new Date(startDate) < startDateRange || new Date(startDate) > today) {
            startDateError.textContent = "请选择" + formatDate(startDateRange) + "到" + formatDate(today) + "之间的日期";
        }

        if (new Date(endDate) > today) {
            endDateError.textContent = "请选择" + formatDate(today) + "之前的日期";
        }
    }

    // 禁用按钮
    confirmButton.disabled = !startDate || !endDate || !!startDateError.textContent || !!endDateError.textContent;
}

function formatDate(date) {
    var year = date.getFullYear();
    var month = (date.getMonth() + 1).toString().padStart(2, '0');
    var day = date.getDate().toString().padStart(2, '0');
    return year + "年" + month + "月" + day + "日";
}

// 获取起始日期和终止日期的输入框元素
var startDateInput = document.getElementById('startDate');
var endDateInput = document.getElementById('endDate');

// 添加事件监听器，以确保用户输入的日期范围是有效的
startDateInput.addEventListener('change', function () {
    // 设置终止日期的最小值为起始日期
    endDateInput.min = startDateInput.value;
    validateDates();
});

endDateInput.addEventListener('change', function () {
    // 设置起始日期的最大值为终止日期
    startDateInput.max = endDateInput.value;
    validateDates();
});

function submitForm() {
    var startDate = document.getElementById('startDate').value;
    var endDate = document.getElementById('endDate').value;

    // 显示结果盒子
    var resultBox = document.getElementById('resultBox');
    resultBox.style.display = 'block';

    // 显示选择的日期和分析类型
    var selectedDates = document.getElementById('selectedDates');
    var startDateInput = new Date(startDate);
    var endDateInput = new Date(endDate);
    // 显示选择的日期和分析类型
    selectedDates.textContent ="在"+ formatDate(startDateInput) + " 到 " + formatDate(endDateInput)+"您的企业" + selectedAnalysisType + "总量为"+totalUsage;
    // 计算统计信息
    calculateStatistics(startDateInput, endDateInput);
    //显示高异常情况日期
    var highExceptionDates = document.getElementById('highExceptionDates');
    highExceptionDates.textContent = highExceptionYear+"年"+highExceptionMonth+"月"+highExceptionDay+"日异常情况最多" ;
    //显示低异常情况日期
    var lowExceptionDates = document.getElementById('lowExceptionDates');
    lowExceptionDates.textContent = lowExceptionYear+"年"+lowExceptionMonth+"月"+lowExceptionDay+"日异常情况最低" ;
    //显示未解决异常情况
    var unsolvedExceptionDates = document.getElementById('unsolvedExceptionDates');
    unsolvedExceptionDates.textContent = unsolvedExceptionYear+"年"+unsolvedExceptionMonth+"月"+unsolvedExceptionDay+"日异常情况已解决"; ;
    //显示已解决异常情况日期
    var solvedExceptionDates = document.getElementById('solvedExceptionDates');
    solvedExceptionDates.textContent = solvedExceptionYear+"年"+solvedExceptionMonth+"月"+solvedExceptionDay+"日异常情况已解决"; ;
    
}

function goBack() {
    // 隐藏结果盒子
    var resultBox = document.getElementById('resultBox');
    resultBox.style.display = 'none';
}

function calculateStatistics(startDate, endDate) {
    var dateDiff = Math.abs(endDate - startDate);
    var daysDiff = Math.ceil(dateDiff / (1000 * 60 * 60 * 24));

    // 如果间隔大于两年，则显示年月日的统计信息
    if (daysDiff > 2 * 365) {
        // 显示统计信息
        var usageStatistics = document.getElementById('usageStatistics');
        usageStatistics.innerHTML = `
            <p>其中，${highUsageYear}年${highUsageMonth}月${highUsageDay}日${selectedAnalysisType}量最多</p>
            <p>${lowUsageYear}年${lowUsageMonth}月${lowUsageDay}日${selectedAnalysisType}量最少</p>
        `;
    } else if (daysDiff > 2 * 30) { // 如果间隔大于两个月，则显示月日的统计信息
        // 显示统计信息
        var usageStatistics = document.getElementById('usageStatistics');
        usageStatistics.innerHTML = `
        <p>其中，${highUsageMonth}月${highUsageDay}日${selectedAnalysisType}量最多</p>
        <p>${lowUsageMonth}月${lowUsageDay}日${selectedAnalysisType}量最少</p>
        `;
    } else { // 否则，只显示日的统计信息
        // 显示统计信息
        var usageStatistics = document.getElementById('usageStatistics');
        usageStatistics.innerHTML = `
        <p>其中，${highUsageDay}日${selectedAnalysisType}量最多</p>
        <p>${lowUsageDay}日${selectedAnalysisType}量最少</p>
        `;
    }
}