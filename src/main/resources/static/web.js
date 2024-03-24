const selectElement = document.getElementById("select1");

selectElement.addEventListener("change", function () {
    // 当选择了其他选项时，移除样式
    selectElement.classList.remove("translucent");
});

// 初始时添加半透明样式
selectElement.classList.add("translucent");

const selectElement2 = document.getElementById("select2");

selectElement2.addEventListener("change", function() {
  selectElement2.classList.remove("translucent");
});

selectElement2.classList.add("translucent");