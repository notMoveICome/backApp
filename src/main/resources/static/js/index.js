$(function () {
    //动态字体
    var wh = $(window).width();
    $("body").css("font-size",wh*18/1920);
    
    //左侧菜单插件
    $("#jquery-accordion-menu").jqueryAccordionMenu();

    //左侧菜单选中
    $("#demo-list li").click(function () {
        $("#demo-list li.active").removeClass("active");
        $(this).addClass("active");
    });
});