const selectElement = document.getElementById("select1");

selectElement.addEventListener("change", function () {
    // ��ѡ��������ѡ��ʱ���Ƴ���ʽ
    selectElement.classList.remove("translucent");
});

// ��ʼʱ��Ӱ�͸����ʽ
selectElement.classList.add("translucent");

const selectElement2 = document.getElementById("select2");

selectElement2.addEventListener("change", function() {
  selectElement2.classList.remove("translucent");
});

selectElement2.classList.add("translucent");