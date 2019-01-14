var userRole;
$(function () {
    // layui.use(['layer', 'form'], function () {
    //     var layer = layui.layer
    //         , form = layui.form;
    // });
    layui.use(['form', 'layedit', 'laydate'], function(){
        var form = layui.form
            ,layer = layui.layer
            ,layedit = layui.layedit
            ,laydate = layui.laydate;
    });
    initHtmlCss();
    initLeftUtilOfAdmin();
    initLeftUtilOfDistributor();
    monitorWindowView();
    logout();

    /*默认首页管理*/
    $("#indexManage").click();
});

/*页面样式*/
function initHtmlCss() {
    var userName = getCookie("iframe_user");
    userRole = getCookie("iframe_userRole") == "4" ? "超级管理员" : "分销商";
    $("#name").text("您好！" + userName + " " + userRole);
    var leftMenu = "";
    if (userRole == "超级管理员") {
        leftMenu = '<div id="jquery-accordion-menu" class="jquery-accordion-menu">' +
            '                <ul id="demo-list" class="menu-container">' +
            '                    <li class="active mainmenu">' +
            '                        <a href="javascript:void(0);" id="indexManage" class="submenu-indicator-minus"><b>首页管理</b></a>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);" id="newsManage"><b>头条管理</b></a>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);" id="projectPublish"><b>项目发布</b></a>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);" id="projectList"><b>项目管理</b></a>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);" id="baobei"><b>报备管理</b></a>' +
            '                        <ul class="submenu" id="userManage">' +
            '                            <li><a href="javascript:void(0);">管理员</a></li>' +
            '                            <li><a href="javascript:void(0);">分销商</a></li>' +
            '                            <li><a href="javascript:void(0);">客户</a></li>' +
            '                        </ul>' +
            '                        </ul>' +
            '                    </li>' +
            '                </ul>' +
            '            </div>';
    } else if (userRole == "分销商") {
        leftMenu = '<div id="jquery-accordion-menu" class="jquery-accordion-menu">' +
            '                <ul id="demo-list" class="menu-container">' +
            // '                    <li class="mainmenu">' +
            // '                        <a href="javascript:void(0);"><b>首页管理</b></a>' +
            // '                    </li>' +
            '                    <li class="active mainmenu">' +
            '                        <a href="javascript:void(0);" class="submenu-indicator-minus"><b>客户管理</b></a>' +
            '                        <ul class="submenu" style="display: block;">' +
            '                            <li id="customerList" class="active"><a href="javascript:void(0);">客户列表</a></li>' +
            '                            <li><a href="javascript:void(0);">新增客户报备</a></li>' +
            '                            <li><a href="javascript:void(0);">批量报备</a></li>' +
            '                        </ul>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);"><b>业务员管理</b></a>' +
            '                        <ul class="submenu">' +
            '                            <li id="salesList"><a href="javascript:void(0);">业务员列表</a></li>' +
            '                            <li id="saleAdd"><a href="javascript:void(0);">新增业务员</a></li>' +
            '                        </ul>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);"><b>项目管理</b></a>' +
            '                        <ul class="submenu">' +
            '                            <li id="projectView"><a href="javascript:void(0);">项目查看</a></li>' +
            '                        </ul>' +
            '                    </li>' +
            '                </ul>' +
            '            </div>';
    }
    $(".main-left").html(leftMenu);
    //动态字体
    var wh = $(window).width();
    $("body").css("font-size", wh * 18 / 1920);
    //左侧菜单插件
    $("#jquery-accordion-menu").jqueryAccordionMenu();
    //左侧菜单选中
    $("#demo-list li").click(function () {
        $("#demo-list li.active").removeClass("active");
        $(this).addClass("active");
    });
    $("#baobei").click();
}

/** 窗口大小resize监听 */
function monitorWindowView() {
    $(window).resize(function () {
        $('#rightTable').bootstrapTable('resetView');
    });
}

/*退出*/
function logout() {
    $("#signOut").on("click", function () {
        $.post("/backApp/signOut", function () {
            window.location.href = window.location.href.replace("index", "login");
        })
    });
}

/*左边菜单_分销商*/
function initLeftUtilOfDistributor() {
    customerManage();
    salesManage();
    projectManageDis();
}

/*客户管理_分销商*/
function customerManage() {
    $("#customerList").on('click', function () {
        getUserByRole("客户");
    });

}

/*业务员管理_分销商*/
function salesManage() {
    $("#salesList").on('click', function () {
        getUserByRole("业务员");
    });
    $("#saleAdd").on('click', function () {

    });
}

/*项目管理_分销商*/
function projectManageDis() {
    $("#projectView").on('click', function () {
        projectList();
    });
}

/*左边菜单_管理员*/
function initLeftUtilOfAdmin() {
    indexManage();
    newsManage();
    projectPublish();
    projectManage();
    userManage();
}

/*首页管理_管理员*/
function indexManage() {
    $("#indexManage").on('click', function () {
        var indexHtml = '<div class="recommendDiv">' +
            '                <img src="">' +
            '                <span> ' +
            '                     <button class="btn btn-primary btn-sm">修改</button> ' +
            '                </span>' +
            '            </div>' +
            '            <div class="recommendDiv">' +
            '                <img src="">' +
            '                <span> ' +
            '                      <button class="btn btn-primary btn-sm">修改</button> ' +
            '                </span>' +
            '            </div>' +
            '            <div class="recommendDiv">' +
            '                <img src="">' +
            '                <span> ' +
            '                      <button class="btn btn-primary btn-sm">修改</button> ' +
            '                </span>' +
            '            </div>' +
            '            <div class="recommendDiv" title="添加">' +
            '                <img src="img/home/addPic.png" style="height: 14.6em;cursor: pointer;" id="addRecommendImg">' +
            '            </div>' +
            '            <div id="recommendProManage">' +
            '                <h3 style="margin-left: 1em;margin-bottom: 0.5em;">项目推荐管理</h3>' +
            '                <div>' +
            '                    <table id="rightTable" class="table table-hover">' +
            '                    </table>' +
            '                </div>' +
            '            </div>';
        $(".main-right").html(indexHtml);
        var fileds = {
            develop: "地产商",
            project_name: "项目名称",
            price: "宣传费用(万)",
            publish_time: "宣传时间"
        };
        $.get('/backApp/project/getRecommendPro', function (res) {
            if (res.status == 200) {
                var data = res.data;
                var imgs = $(".main-right").find("img");
                for (var i = 0; i < 3; i++) {
                    $(imgs[i]).attr("src", "/backApp" + data[i].url);
                }
                var columns = [];
                for (var attr in data[0]) {
                    if (attr == "desc_pic" || attr.indexOf("index") > -1 || attr == "url" || attr.indexOf("id") > -1) {
                        continue;
                    }
                    var column = {
                        field: attr,
                        title: fileds[attr],
                        valign: "middle",
                        align: "center",
                        visible: true,
                        formatter: paramsMatter
                    };
                    columns.push(column);
                }
                columns.reverse();
                columns.push({
                    field: 'operate',
                    title: '操作',
                    valign: "middle",
                    align: 'center',
                    events: recommOperateEvents,
                    formatter: recommOperateFormatter
                });
                initTable(columns, data);
            }
        });
        $("#addRecommendImg").on('click', function () {
            $.get("/backApp/project/queryRecommendPro", function (res) {
                if (res.status == 200) {
                    var div = $('<div style="text-align: center;margin-top: 1em;"></div>');
                    var label = $('<label>请选择项目名称：</label>');
                    var select = $('<select id="proSelect" name="proName" lay-verify="" style="width: 10em;"></select>');
                    if (res.data.length > 0) {
                        for (var i = 0; i < res.data.length; i++) {
                            var option = $('<option value=' + res.data[i].gid + '>' + res.data[i].name + '</option>');
                            select.append(option);
                        }
                    }
                    div.append(label).append(select);
                    layer.open({
                        type: 1, offset: '250px', area: '650px', title: '添加项目推荐',
                        content: '<div id="addRecommendProDiv"></div>',
                        btn: ['确认'],
                        yes: function (index, layero) {
                            var val = $("#proSelect option:selected").val();
                            $.post("/backApp/project/addProjectRecomm", {proId: val}, function (res) {
                                if (res.status == 200) {
                                    layer.msg(res.msg, {icon: 1});
                                    $("#indexManage").click();
                                    layer.close(index)
                                } else {
                                    layer.msg(res.msg, {icon: 2});
                                }
                            })
                        }
                    });
                    $("#addRecommendProDiv").append(div);
                }
            });
        });
    })
}

/*头条管理_超级管理员*/
function newsManage() {
    $("#newsManage").on('click', function () {
        var newsHtml = '<div style="background-color: #fff;height: 100%;">' +
            '       <div class="main-right-search"> ' +
            '           <input type="text" id="newsTitle" style="display: inline" class="form-control" placeholder="请输入标题"/> ' +
            // '               <button type="button" class="btn-primary btn" style="display: inline;margin-left: 20px" id="queryNews">查询</button>'+
            '               <button type="button" class="btn-primary btn" style="float: right;display: inline;margin-left: 20px" id="addNews">添加</button>'+
            '       </div> ' +
            '       <div class="table-responsive"> ' +
            '           <table id="rightTable" class="table text-nowrap"></table> ' +
            '       </div>' +
            '  </div>';
        $(".main-right").html(newsHtml);
        newsList();
        $("#newsTitle").on('keypress', function (e) {
            if (e.keyCode == "13") {
                queryNewsByTitle();
            }
        });
        $("#queryNews").on("click", function () {
            queryNewsByTitle();
        });
        $("#addNews").on('click',function () {
           addNews();
        });
    });
}

function newsList() {
    $.get('/backApp/news/newsList', function (res) {
        if (res.status == 200) {
            doNewsTable(res);
        }
    })
}

function queryNewsByTitle() {
    var title = $("#newsTitle").val();
    $.get('/backApp/news/queryNewsByTitle',{title:title},function (res) {
        if (res.status == 200){
            doNewsTable(res);
        }
    })
}

function doNewsTable (res) {
    var news_fileds = {
        "gid": "ID",
        "picture": "图片",
        "title": "标题",
        "content": "内容",
        "linkUrl": "链接",
        "newsTime": "时间"
    };
    var columns = [];
    for (var attr in res.data[0]) {
        if (attr == "gid") {
            continue;
        }
        var column = {
            field: attr,
            title: news_fileds[attr],
            valign: "middle",
            align: "center",
            // sortable: true,
            visible: true,
        };
        if (attr == "picture"){
            column.formatter = imgParamsMatter;
        }else if (attr == "linkUrl"){
            column.formatter = linkParamsMatter;
        }else {
            column.formatter = paramsMatter;
        }

        columns.push(column);
    }
    columns.push({
        field: 'operate',
        title: '操作',
        valign: "middle",
        align: 'center',
        width: '210',
        events: newsOperateEvents,
        formatter: newsOperateFormatter
    });
    initTable(columns, res.data);
}

function addNews(){
    var newsHtml = '<div id="newsDiv" style="background: #e9edf3;padding-top: 1em;padding-bottom: 1em;text-align: center;">' +
            '            <form id="newsForm" method="post" enctype="multipart/form-data">' +
            '                <table style="border-collapse: separate;border-spacing: 5px;margin: auto;">' +
            '                    <tr>' +
            '                        <td style="text-align: right">标题：</td>' +
            '                        <td style="text-align: left" colspan="2"><input type="text" name="title" style="width: 25em;"/></td>' +
            '                    </tr>' +
            '                    <tr>' +
            '                        <td style="text-align: right">图片：</td>' +
            '                        <td style="text-align: left"><input id="newsFile" type="file" name="pictureFile" onchange="uploadImg(this)" accept="image/gif,image/jpeg,image/jpg,image/png" style="width: 16em;"/></td>' +
            '                        <td><img id="newsImg" src="" style="display: none;width: auto;height: auto;max-width: 10em;max-height: 5em;"/></td>' +
            '                    </tr>' +
            '                    <tr>' +
            '                        <td style="text-align: right">内容：</td>' +
            '                        <td style="text-align: left" colspan="2"><textarea name="content" style="width: 25em;height: 10em;"></textarea></td>' +
            '                    </tr>' +
            '                    <tr>' +
            '                        <td style="text-align: right">链接：</td>' +
            '                        <td style="text-align: left" colspan="2"><input type="text" name="linkUrl" style="width: 25em;"/></td>' +
            '                    </tr>' +
            '                </table>' +
            '            </form>' +
            // '            <button type="button" class="btn btn-primary btn-sm" style="margin-right:15px;">添加</button>' +
            '        </div>';
    layer.open({
        type: 1, offset: '250px', area: '650px', title: '添加爱家头条',
        content: '<div id="addNewsDiv"></div>',
        btn: ['确认','取消'],
        yes: function (index, layero) {
            if ($("#newsForm input[name=title]").val().replace(/^\s*|\s*$/g,"") == ""){
                layer.msg("标题不能为空!",{icon:2});
                return;
            }
            if ($("#newsForm input[name=pictureFile]").val() == ""){
                layer.msg("图片不能为空!",{icon:2});
                return;
            }
            if ($("#newsForm textarea[name=content]").val().replace(/^\s*|\s*$/g,"") == ""){
                layer.msg("内容不能为空!",{icon:2});
                return;
            }
            if ($("#newsForm input[name=linkUrl]").val().replace(/^\s*|\s*$/g,"") == ""){
                layer.msg("链接不能为空!",{icon:2});
                return;
            }

            var form = new FormData(document.getElementById("newsForm"));
            /**
             * ajax提交表单(带文件)
             */
            $.ajax({
                //几个参数需要注意一下
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                cache: false,    //上传文件不需缓存
                contentType: false,//需设置为false，因为是FormData对象，且已经声明了属性enctype="multipart/form-data"
                processData: false,//需设置为false，因为data值是FormData对象，不需要对数据做处理
                async: true,
                url: "/backApp/news/addNews",//url
                data: form,
                success: function (res) {
                    // console.log(res);//打印服务端返回的数据(调试用)
                    if (res.status == 200) {
                        layer.msg(res.msg, {icon: 1});
                        newsList();
                    } else {
                        layer.msg(res.msg, {icon: 2});
                    }
                    layer.close(index);
                },
                error: function () {
                    layer.msg("上传接口异常!", {icon: 2});
                }
            });
        }
    });
    $("#addNewsDiv").append(newsHtml);
}

function uploadImg() {
    var pictureFile = $("#newsDiv input[name=pictureFile]");
    var picVal = pictureFile.val();
    var picFormat = picVal.substring(picVal.lastIndexOf(".") + 1).toLowerCase();
    if (picFormat !== "png" && picFormat !== "jpg" && picFormat !== "gif" && picFormat !== "jpeg") {
        layer.msg("请上传正确格式的图片!", {icon: 2});
        pictureFile.val("");
        return;
    }
    // 图片的实时显示
    $("#newsImg").css("display", "none");
    var reads = new FileReader();
    var f = document.getElementById('newsFile').files[0];
    reads.readAsDataURL(f);
    reads.onload = function (e) {
        document.getElementById('newsImg').src = this.result;
        $("#newsImg").css("display", "block");
    };
}

/*项目发布_管理员*/
function projectPublish() {
    $("#projectPublish").on('click', function () {
        var publishHtml = '<div class="publishFileDiv" style="display: none">' +
            '                <img src="img/home/upFile.png">' +
            '                <span>' +
            '                    <button class="btn btn-primary btn-sm">上传文档</button>' +
            '                </span>' +
            '            </div>' +
            '            <div class="publishFileDiv" style="display: none">' +
            '                <img src="img/home/upPic.png">' +
            '                <span>' +
            '                    <button class="btn btn-primary btn-sm">上传图片</button>' +
            '                </span>' +
            '            </div>' +
            '            <div class="publishFileDiv" style="display: none">' +
            '                <img src="img/home/upZiLiao.png">' +
            '                <span>' +
            '                    <button class="btn btn-primary btn-sm">资料上传</button>' +
            '                </span>' +
            '            </div>' +
            '            <div id="projectPublishDiv">' +
            '                <div>' +
            '                    <form id="proForm" method="post" enctype="multipart/form-data">' +
            // '                      <button class="btn btn-primary btn-sm" onclick="addProject()" style="width: 16em;height: 3em;font-size: 16px;">发布</button>' +
            '                    </form>' +
            '                    <button class="btn btn-primary btn-sm" onclick="addProject()" style="width: 16em;height: 3em;font-size: 16px;">发布</button>' +  // 放在form里面会页面跳转
            '                </div>' +
            '            </div>';
        $(".main-right").html(publishHtml);
        var tableHtml = '<table id="addProjectTable" style="border-collapse:separate;margin: auto;">' +
            '            <tr>' +
            '                <td><b>项目名称:</b></td>' +
            '                <td><input type="text" name="name" id ="pro_name"/></td>' +
            '                <td><b>开发商:</b></td>' +
            '                <td><input type="text" name="develop" id="pro_develop"/></td>' +
            '                <td><b>分销商数:</b></td>' +
            '                <td><input type="text" name="disnum" id="pro_disnum" /></td>' +
            '            </tr>' +
            '            <tr>' +
            '                <td><b>允许报备次数:</b></td>' +
            '                <td><input type="text" name="reportLimit" id="pro_reportLimit"/></td>' +
            '                <td><b>项目单价:</b></td>' +
            '                <td><input type="text" name="price" id="pro_price"/></td>' +
            '                <td><b>佣金:</b></td>' +
            '                <td><input type="text" name="commission" id="pro_commission"/></td>' +
            '            </tr>' +
            '            <tr>' +
            '                <td><b>地址:</b></td>' +
            '                <td><input type="text" name="address" id="pro_address"/></td>' +
            '                <td><b>关键字:</b></td>' +
            '                <td><input type="text" name="keyword" id="pro_keyword"/></td>' +
            '                <td><b>房型:</b></td>' +
            '                <td>' +
            '                    <select id="proType" name="type">' +
            '                        <option value="住宅">住宅</option>' +
            '                        <option value="商铺">商铺</option>' +
            '                        <option value="写字楼">写字楼</option>' +
            '                        <option value="公寓">公寓</option>' +
            '                        <option value="二手房">二手房</option>' +
            '                        <option value="其他项目">其他项目</option>' +
            '                    </select>' +
            '                </td>' +
            '            </tr>' +
            '            <tr>' +
            '                <td><b>负责人:</b></td>' +
            '                <td><input type="text" name="header" id="pro_header"/></td>' +
            '                <td><b>电话:</b></td>' +
            '                <td><input type="text" name="tel" id="pro_tel"/></td>' +
            '                <td><b>合作时间:</b></td>' +
            '                <td><input type="text" id="fromTime" readonly="true" name="biddingBegin"><b> 至 </b><input type="text" id="toTime" readonly="true"  name="biddingEnd"></td>' +
            '            </tr>' +
            '            <tr>' +
            '                <td><b>项目图标:</b></td>' +
            '                <td><input type="file" name="xmtb" onchange="fileChange(this);" id="pro_xmtb" accept="image/gif,image/jpeg,image/jpg,image/png"/></td>' +
            '                <td><b>户型图:</b></td>' +
            '                <td><input type="file" name="hxt" onchange="fileChange(this);" id="pro_hxt"/></td>' +
            '                <td><b>效果图:</b></td>' +
            '                <td><input type="file" name="xgt" onchange="fileChange(this);" id="pro_xgt"/></td>' +
            '            </tr>' +
            '            <tr>' +
            '                <td><b>沙盘解说:</b></td>' +
            '                <td><input type="file" name="spjs" onchange="fileChange(this);" id="pro_spjs"/></td>' +
            '                <td><b>销售问答:</b></td>' +
            '                <td><input type="file" name="xswd" onchange="fileChange(this);" id="pro_xswd"/></td>' +
            '                <td><b>其他:</b></td>' +
            '                <td><input type="file" name="other" onchange="fileChange(this);" id="pro_other"/></td>' +
            '            </tr>' +
            '            <tr>' +
            '                <td><b>项目状态:</b></td>' +
            '                <td><input type="radio" name="state" checked="checked" value="在售"/>在售<input type="radio" name="state" value="暂停"/>暂停<input type="radio" name="state" value="售罄"/>售罄</td>' +
            '                <td><b>项目描述:</b></td>' +
            '                <td><textarea name="description" style="width: 20em;height: 4em;"></textarea></td>' +
            '            </tr>' +
            '        </table>';
        $("#proForm").html(tableHtml);
        $("#fromTime,#toTime,#startReportTime").datetimepicker({
            format: 'yyyy-mm-dd',//显示格式
            todayHighlight: 1,//今天高亮
            minView: "month",//设置只显示到月份
            startView: 2,
            forceParse: 0,
            language: "zh-CN", //语言设置
            showMeridian: 1,
            autoclose: 1//选择后自动关闭
        });
    })
}

function addProject() {
    var pro_name = $("#pro_name").val();
    var reg = /^$| /;
    if (reg.test(pro_name)) {
        layer.msg("请输入正确项目名,不能有空格", {icon: 2});
        return false;
    }

    if (reg.test($("#pro_develop").val())) {
        layer.msg("请输入开发商信息", {icon: 2});
        return false;
    }
    //输入的数字只能为数字，且不能以0开头

    var regex = /^\d+(\d+)?$/;
    if (!regex.test($("#pro_disnum").val())) {
        layer.msg("分销商数必须是正整数", {icon: 2});
        return false;
    }

    if (!regex.test($("#pro_reportLimit").val())) {
        layer.msg("允许报备次数必须是正整数", {icon: 2});
        return false;
    }

    if (!regex.test($("#pro_price").val())) {
        layer.msg("输入的项目单价必须是正整数", {icon: 2});
        return false;
    }

    if (!regex.test($("#pro_commission").val())) {
        layer.msg("输入的佣金必须是正整数", {icon: 2});
        return false;
    }

    if (reg.test($("#pro_address").val())) {
        layer.msg("请输入地址,不能有空格", {icon: 2});
        return false;
    }

    if (reg.test($("#pro_keyword").val())) {
        layer.msg("请输入关键字,不能有空格", {icon: 2});
        return false;
    }

    if (reg.test($("#pro_header").val())) {
        layer.msg("请输入负责人,不能有空格", {icon: 2});
        return false;
    }
    //用户手机号正则表达式
    var telpass = /^1([38]\d|5[0-35-9]|7[3678])\d{8}$/;
    if (!telpass.test($("#pro_tel").val())) {
        layer.msg("请输入正确的手机号", {icon: 2});
        return false;
    }

    if (reg.test($("#fromTime").val())) {
        layer.msg("请选择合作的开始时间", {icon: 2});
        return false;
    }

    if (reg.test($("#toTime").val())) {
        layer.msg("请选择合作的结束时间", {icon: 2});
        return false;
    }

    if (reg.test($("#pro_xmtb").val())) {
        layer.msg("请上传户型图文件", {icon: 2});
        return false;
    }
    if (reg.test($("#pro_hxt").val())) {
        layer.msg("请上传户型图文件", {icon: 2});
        return false;
    }

    if (reg.test($("#pro_xgt").val())) {
        layer.msg("请上传效果图文件", {icon: 2});
        return false;
    }
    if (reg.test($("#pro_spjs").val())) {
        layer.msg("请上传沙盘解说文件", {icon: 2});
        return false;
    }

    if (reg.test($("#pro_xswd").val())) {
        layer.msg("请上传销售问答文件", {icon: 2});
        return false;
    }
    // 非必须
    // if(reg.test($("#pro_other").val())){
    //     layer.msg("请上传其他文件",{icon:2});
    //     return false;
    // }
    var state = $('input:radio[name="state"]:checked').val();
    if (state == null) {
        layer.msg("请选择项目状态", {icon: 2});
        return false;
    }

    $.post("/backApp/project/existPro", {pro_name: pro_name}, function (result) {
        if (result.status == 200) {
            // ajax提交表单(带文件)
            var form = new FormData(document.getElementById("proForm"));
            $.ajax({
                //几个参数需要注意一下
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                cache: false,    //上传文件不需缓存
                contentType: false,//需设置为false，因为是FormData对象，且已经声明了属性enctype="multipart/form-data"
                processData: false,//需设置为false，因为data值是FormData对象，不需要对数据做处理
                async: true,
                url: "/backApp/project/addProject",//url
                data: form,
                success: function (res) {
                    // console.log(res);//打印服务端返回的数据(调试用)
                    if (res.status == 200) {
                        layer.msg(res.msg, {icon: 1});
                    } else {
                        layer.msg(res.msg, {icon: 2});
                    }
                },
                error: function () {
                    layer.msg("上传接口异常!", {icon: 2});
                }
            });
        } else {
            layer.msg(result.msg, {icon: 2});
            return false;
        }
    });
}

/**
 * 限制上传文件类型及大小
 * @param target
 * @param id
 * @returns {boolean}
 */
function fileChange(target, id) {
    var inputName = target.name;
    // var filetypes =[".jpg",".png",".rar",".txt",".zip",".doc",".ppt",".xls",".pdf",".docx",".xlsx"];
    var filetypes;
    var msg;
    if (inputName == "xmtb"){
        filetypes = [".jpg",".png","jpeg"];
        msg = "图片";
    }else if (inputName == "spjs" || inputName == "xswd") {
        filetypes = [".doc", ".docx", ".pdf"];
        msg = "Word或Pdf文件";
    } else if (inputName == "cusExcel") {
        filetypes = [".xls", ".xlsx"];
        msg = "Excel文件";
    } else {
        filetypes = [".zip", ".rar"];
        msg = "zip或rar压缩文件";
    }
    var isIE = /msie/i.test(navigator.userAgent) && !window.opera;
    var fileSize = 0;
    var filepath = target.value;
    var filemaxsize = 1024 * 24;//24M
    if (filepath) {
        var isnext = false;
        var fileend = filepath.substring(filepath.lastIndexOf("."));
        if (filetypes && filetypes.length > 0) {
            for (var i = 0; i < filetypes.length; i++) {
                if (filetypes[i] == fileend) {
                    isnext = true;
                    break;
                }
            }
        }
        if (!isnext) {
            layer.msg("不接受此文件类型,请选择" + msg + "!", {icon: 2});
            target.value = "";
            return false;
        }
    } else {
        return false;
    }
    if (isIE && !target.files) {
        var filePath = target.value;
        var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
        if (!fileSystem.FileExists(filePath)) {
            layer.msg("附件不存在，请重新输入!", {icon: 2});
            return false;
        }
        var file = fileSystem.GetFile(filePath);
        fileSize = file.Size;
    } else {
        fileSize = target.files[0].size;
    }

    var size = fileSize / 1024;
    if (size > filemaxsize) {
        layer.msg("附件大小不能大于" + filemaxsize / 1024 + "M!", {icon: 2});
        target.value = "";
        return false;
    }
    if (size <= 0) {
        layer.msg("附件大小不能为0M!", {icon: 2});
        target.value = "";
        return false;
    }
}


/*项目管理_管理员*/
function projectManage() {
    $("#projectList").on('click', function () {
        var proListHtml = '<div style="background-color: #fff;height: 100%;">' +
            '       <div class="main-right-search"> ' +
            '           <input type="text" id="projectName" style="display: inline" class="form-control" placeholder="请输入项目名称"/> ' +
            '               <button type="button" class="btn-primary btn" style="display: inline;margin-left: 20px" id="queryPro">查询</button>'+
            '       </div> ' +
            '       <div class="table-responsive"> ' +
            '           <table id="rightTable" class="table text-nowrap"></table> ' +
            '       </div>' +
            '  </div>';
        $(".main-right").html(proListHtml);
        projectList();
        $("#projectName").on('keypress', function (e) {
            if (e.keyCode == "13") {
                queryProByName();
            }
        });
        $("#queryPro").on("click", function () {
            queryProByName();
        })
    });
}

/*根据项目名称查*/
function queryProByName() {
    var projectName = $("#projectName").val();
    $.post("/backApp/project/queryPro", {projectName: projectName}, function (res) {
        if (res.status == 200) {
            doProjectTable(res);
        }
    })
}

/*项目清单*/
function projectList() {
    $.get('/backApp/project/getAllProject', function (res) {
        if (res.status == 200) {
            // var columns = [];
            // for (var attr in res.data[0]) {
            //     if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1 || attr.indexOf("desc") > -1 || attr == "picUrl" || attr.indexOf("bidding") > -1 || attr == "reportLimit" || attr == "keyword" || attr == "address" || attr == "remark") {
            //         continue;
            //     }
            //     var column = {
            //         field: attr,
            //         title: project_fields[attr],
            //         valign: "middle",
            //         align: "center",
            //         // sortable: true,
            //         visible: true,
            //         formatter: paramsMatter
            //     };
            //     columns.push(column);
            // }
            // if (userRole == "超级管理员") {
            //     columns.push({
            //         field: 'operate',
            //         title: '操作',
            //         valign: "middle",
            //         align: 'center',
            //         width: '210',
            //         events: projectOperateEvents,
            //         formatter: projectOperateFormatter
            //     });
            // } else if (userRole == "分销商") {
            //     columns.push({
            //         field: 'operate',
            //         title: '操作',
            //         valign: "middle",
            //         align: 'center',
            //         width: '90',
            //         events: projectOperateEvents,
            //         formatter: projectOperateFormatter_Dis
            //     });
            // }
            // initTable(columns, res.data);
            doProjectTable(res);
        }
    });
}

function doProjectTable(res){
    var project_fields = {
        gid: "ID",
        name: "项目名称",
        disnum: "分销商数",
        reportNum: "总报备数",
        description: "描述",
        develop: "开发商",
        keyword: "关键字",
        reportLimit: "最大报备次数",
        type: "房型",
        price: "项目单价",
        address: "项目地址",
        commission: "佣金",
        header: "负责人",
        tel: "电话",
        backTime: "报备时间",
        state: "项目状态",
        descPic: "项目图片",
        remark: "备注"
    };
    var columns = [];
    for (var attr in res.data[0]) {
        if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1 || attr.indexOf("desc") > -1 || attr == "picUrl" || attr.indexOf("bidding") > -1 || attr == "keyword" || attr == "address" || attr == "remark") {
            continue;
        }
        var column = {
            field: attr,
            title: project_fields[attr],
            valign: "middle",
            align: "center",
            // sortable: true,
            visible: true,
            formatter: paramsMatter
        };
        columns.push(column);
    }
    if (userRole == "超级管理员") {
        columns.push({
            field: 'operate',
            title: '操作',
            valign: "middle",
            align: 'center',
            width: '210',
            events: projectOperateEvents,
            formatter: projectOperateFormatter
        });
    } else if (userRole == "分销商") {
        columns.push({
            field: 'operate',
            title: '操作',
            valign: "middle",
            align: 'center',
            width: '90',
            events: projectOperateEvents,
            formatter: projectOperateFormatter_Dis
        });
    }
    initTable(columns, res.data);
}

function paramsMatter(value, row, index) {
    if (value == null || value == "null") {
        value = "-";
    }
    return "<span title=" + value + ">" + value + "</span>";
}

function  imgParamsMatter(value, row, index) {
    if (value == null || value == "null") {
        value = "-";
    }
    return "<img src='/backApp"+value+"'>";
}

function  linkParamsMatter(value, row, index) {
    if (value == null || value == "null") {
        value = "-";
    }
    return "<a href='"+value+"' target='_blank'>"+value+"</a>";
}

/*人员管理_管理员*/
function userManage() {
    $('#userManage>li').each(function (index) {
        $(this).on('click', function () {
            // var role = $('#userManage>li').eq(index).text();
            var role = $("#userManage").find(".active").find("a").eq(0).text();
            var userHtml = '<div style="background-color: #fff;height: 100%;padding-top: 1em;">' +
                '                <div id="searchUser" style="margin-left: 1em;height: 2em;margin-bottom: 1em;">' +
                // '                    <span><input id="userName" type="text" class="form-control" placeholder="请输入姓名"/></span>' +
                // '                    <span><input id="userTel" type="text" class="form-control" placeholder="请输入电话号码"/></span>' +
                // '                    <span><input  type="text" class="form-control" placeholder="请选择起始报备时间" id="fromReport"/></span>' +
                // '                    <span style="width: 2em;line-height: 30px;margin-right: 1em;">至</span>' +
                // '                    <span><input  type="text" class="form-control" placeholder="请选择终止报备时间" id="toReport"/></span>' +
                '                </div>' +
                '                <div class="table-responsive">' +
                '                    <table id="rightTable" class="table text-nowrap">' +
                '                    </table>' +
                '                </div>' +
                '            </div>';
            $(".main-right").html(userHtml);
            if (role == "管理员" || role == "分销商") {
                $("#searchUser").append('<span><input id="userName" type="text" class="form-control" placeholder="请输入姓名"/>' +
                    '</span><span><input id="userTel" type="text" class="form-control" placeholder="请输入电话号码"/></span>' +
                    '<span><input  type="text" class="form-control" placeholder="请选择起始报备时间" readonly="readonly" id="fromReport"/></span>' +
                    '<span style="width: 2em;line-height: 30px;margin-right: 1em;">至</span>' +
                    '<span><input  type="text" class="form-control" placeholder="请选择终止报备时间" readonly="readonly" id="toReport"/></span>');
                $("#searchUser").append('<button id="downExcel" class="btn btn-primary" type="button" style="float: right;margin-right: 20px">导出</button>');
                $("#searchUser").append('<button id="addUser" class="btn btn-primary" type="button" style="float: right;margin-right: 20px">添加</button>');
                $("#searchUser").append('<button id="findUser" class="btn btn-primary" type="button" style="float: right;margin-right: 270px">查询人员</button>');
                addUser(role);
                $("#userName,#userTel,#fromReport,#toReport").on('keypress', function (e) {
                    if (e.keyCode == "13") {
                        findUserByOptions(role);
                    }
                });
                $("#findUser").on("click", function () {
                    findUserByOptions(role);
                });

            }
            if (role == "客户") {
                layui.use(['form'], function () {
                    var form = layui.form
                        , layer = layui.layer
                });
                    $("#searchUser").append('<span><select id="proName" name="proName"><option value="" selected>请选择项目名</option></select></span>' +
                        '<span><input id="userName" type="text" class="form-control" placeholder="请输入分销商姓名"/></span>' +
                        '<span><input id="userTel" type="text" class="form-control" placeholder="请输入电话号码"/></span>' +
                        '<span><input  type="text" class="form-control" placeholder="请选择起始报备时间" readonly="readonly" id="fromReport"/></span>' +
                        '<span style="width: 2em;line-height: 30px;margin-right: 1em;">至</span>' +
                        '<span><input  type="text" class="form-control" placeholder="请选择终止报备时间" readonly="readonly" id="toReport"/></span>');
                    $("#searchUser").append('<button id="findUser" class="btn btn-primary" type="button" style="margin-right: 2em;">查询</button>');
                    $("#searchUser").append('<button id="downExcel" class="btn btn-primary" type="button" style="float: right;margin-right: 2em;">导出</button>');
                    $("#searchUser").append('<button id="exportCustomer" class="btn btn-primary" type="button" style="float: right;margin-right: 2em;">批量报备</button>');
                findAllProject();
                $("#proName,#userName,#userTel,#fromReport,#toReport").on('keypress', function (e) {
                    if (e.keyCode == "13") {
                        findCustomer(role);
                    }
                });
                $("#findUser").on("click", function () {
                    findCustomer(role);
                });
                $("#exportCustomer").on('click', function () {
                    batchExportCustomer();
                });
            }

            $("#fromReport,#toReport").datetimepicker({
                format: 'yyyy-mm-dd',//显示格式
                todayHighlight: 1,//今天高亮
                minView: "month",//设置只显示到月份
                startView: 2,
                forceParse: 0,
                language: "zh-CN", //语言设置
                showMeridian: 1,
                autoclose: 1//选择后自动关闭
            });
            getUserByRole(role);
            downExcel(role);
        })
    })
}

function findAllProject() {
    $.get('/backApp/project/getAllProject', function (res) {
        for(var i=0;i<res.data.length;i++){
            var projectName =res.data[i].name;
            var option =$("<option value="+projectName+">"+projectName+"</option>");
            $("#proName").append(option);
        }
        $('select').searchableSelect();
    })
}
/**
 * 导出Excel表格
 * @param role
 */
function downExcel(role) {
    $('#downExcel').on("click", function () {
        var data = $("#rightTable").bootstrapTable("getData");
        var dataStr = JSON.stringify(data);
        // var url = "/backApp/downloadExcel?role="+role+"&data="+encodeURIComponent(dataStr);
        var url = "/backApp/downloadExcel";
        $('<form id="excelForm" method="post" action="' + url + '" style="display: none">' +
            '<input name="role" value="' + role + '">' +
            '<input name="data" value="' + encodeURIComponent(dataStr) + '">' +
            '</form>').appendTo('body').submit().remove();

        // window.open(url);

        // $.ajax({
        //     url: url,
        //     type: "post",
        //     dataType: "json",
        //     contentType: "application/json",
        //     data:JSON.stringify({'role':role,'data':data}),
        //     success: function (res) {
        //         // if (res.status=201) {
        //         //     $.ajax({
        //         //         url:"/backApp/download",
        //         //         type: "post"
        //         //     })
        //         // } else {
        //         //     alert("下载失败!");
        //         // }
        //     },
        //     error: function (err) {
        //         alert("下载失败");
        //     }
        // });
    })
}

/**
 * 批量导入客户
 */
function batchExportCustomer() {
    var div = $("<div style='margin-top: 1em;'></div>");
    var formHtml = '<form id="exportCusForm" method="post",enctype="multipart/form-data">' +
        '            <table style="border-collapse:separate;border-spacing: 10px;margin: auto;">' +
        '                <tr>' +
        '                    <td><b>分销商:</b></td>' +
        '                    <td><select id="disSelect" name="disId"></select></td>' +
        '                </tr>' +
        '                <tr>' +
        '                    <td><b>Excel文件:</b></td>' +
        '                    <td><input type="file" name="cusExcel" onchange="fileChange(this)"></td>' +
        '                </tr>' +
        '            </table>' +
        '        </form>';
    div.append(formHtml);
    $.get('/backApp/user/getUserByRole', {role: "分销商"}, function (res) {
        if (res.status == 200) {
            var data = res.data;
            var disSelect = $("#disSelect");
            for (var i = 0; i < data.length; i++) {
                if (data[i].checkState == "已过审") {
                    disSelect.append('<option value="' + data[i].gid + '">' + data[i].name + '</option>');
                }
            }
        }
    });
    layer.open({
        type: 1, offset: '250px', area: '650px', title: '批量导入',
        content: '<div id="batchExportCustomer"></div>',
        btn: ['确认', '取消'],
        yes: function (index, layero) {
            var form = new FormData(document.getElementById("exportCusForm"));
            /**
             * ajax提交表单(带文件)
             */
            $.ajax({
                //几个参数需要注意一下
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                cache: false,    //上传文件不需缓存
                contentType: false,//需设置为false，因为是FormData对象，且已经声明了属性enctype="multipart/form-data"
                processData: false,//需设置为false，因为data值是FormData对象，不需要对数据做处理
                async: true,
                url: "/backApp/user/batchExportCus",//url
                data: form,
                success: function (res) {
                    // console.log(res);//打印服务端返回的数据(调试用)
                    if (res.status == 200) {
                        var success = res.data.success;
                        var fail = res.data.fail;
                        var text = "成功报备" + "<b style='color: blue'>" + success + "</b>" + "个客户,失败" + "<b style='color: red'>" + fail.length + "</b>" +
                            "个客户。<a href='javascript:void(0);' id='failList' style='color: blue;'>查看失败列表</a>";
                        layer.open({
                            title: '报备详情',
                            type: 1,
                            offset: 'auto',//具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                            content: '<div style="padding-top: 20px;padding-left: 20px;">' + text + '</div>',
                            btn: '关闭',
                            btnAlign: 'c', //按钮居中
                            shade: 0,//不显示遮罩
                            yes: function () {
                                layer.closeAll();
                                getUserByRole("客户");
                            }
                        });
                        $("#failList").on('click', function () {
                            var arr = [];
                            for (var i = 0; i < fail.length; i++) {
                                var json = {
                                    "姓名": fail[i].name,
                                    "电话": fail[i].tel,
                                    "项目": fail[i].projectName,
                                    "客户区域": fail[i].cusArea,
                                    "面积": fail[i].acreage,
                                    "投资额": fail[i].money,
                                    "备注": fail[i].remark
                                };
                                arr.push(json);
                            }
                            JSONToExcelConvertor(arr, "未报备列表");
                        })
                    } else {
                        layer.msg(res.msg, {icon: 2});
                    }
                    layer.close(index);
                },
                error: function () {
                    layer.msg("上传接口异常!", {icon: 2});
                }
            });
        }
    });
    $("#batchExportCustomer").append(div);
}


//json数据转excel
function JSONToExcelConvertor(JSONData, FileName) {
    //先转化json
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
    var excel = '<table>';
    var row = "<tr>";
    //设置表头
    var keys = Object.keys(JSONData[0]);
    keys.forEach(function (item) {
        row += "<td>" + item + '</td>';
    });
    //换行
    excel += row + "</tr>";
    //设置数据
    for (var i = 0; i < arrData.length; i++) {
        var row = "<tr>";
        for (var index in arrData[i]) {
            console.log(arrData[i][index]);
            //var value = arrData[i][index] === "." ? "" : arrData[i][index];
            row += '<td>' + arrData[i][index] + '</td>';
        }
        excel += row + "</tr>";
    }

    excel += "</table>";

    var excelFile = "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>";
    excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8">';
    excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-excel';
    excelFile += '; charset=UTF-8">';
    excelFile += "<head>";
    excelFile += "<!--[if gte mso 9]>";
    excelFile += "<xml>";
    excelFile += "<x:ExcelWorkbook>";
    excelFile += "<x:ExcelWorksheets>";
    excelFile += "<x:ExcelWorksheet>";
    excelFile += "<x:Name>";
    excelFile += "{worksheet}";
    excelFile += "</x:Name>";
    excelFile += "<x:WorksheetOptions>";
    excelFile += "<x:DisplayGridlines/>";
    excelFile += "</x:WorksheetOptions>";
    excelFile += "</x:ExcelWorksheet>";
    excelFile += "</x:ExcelWorksheets>";
    excelFile += "</x:ExcelWorkbook>";
    excelFile += "</xml>";
    excelFile += "<![endif]-->";
    excelFile += "</head>";
    excelFile += "<body>";
    excelFile += excel;
    excelFile += "</body>";
    excelFile += "</html>";

    var uri = 'data:application/vnd.ms-excel;charset=utf-8,' + encodeURIComponent(excelFile);

    var link = document.createElement("a");
    link.href = uri;

    link.style = "visibility:hidden";
    link.download = FileName + ".xls";

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

//判断是否为JSON格式数据
function isJSON(str) {
    try {
        if (typeof JSON.parse(str) == "object") {
            return true;
        }
    } catch (e) {
    }
    return false;
}

//添加管理员用户
function addUser(role) {
    $("#addUser").on("click", function () {
        var $form = $('<form id="userForm" class="layui-form" style="padding: 20px 30px 10px 0;"></form>');
        $form.append($('<div class="layui-form-item"></div>')
            .append($('<label class="layui-form-label" style="width: 107px">用户姓名:</label>'))
            .append($('<div class="layui-input-block"></div>')
                .append($('<input type="text" id="username" name="username" lay-verify="required" autocomplete="off" ' +
                    'placeholder="请输入用户姓名" class="layui-input"/>'))
            )
        );
        $form.append($('<div class="layui-form-item"></div>')
            .append($('<label class="layui-form-label" style="width: 107px">用户号码:</label>'))
            .append($('<div class="layui-input-block"></div>')
                .append($('<input type="text" id="tel" name="tel" lay-verify="required" autocomplete="off" ' +
                    'placeholder="请输入用户号码" class="layui-input"/>'))
            )
        );
        $form.append($('<div class="layui-form-item"></div>')
            .append($('<label class="layui-form-label" style="width: 107px">用户密码:</label>'))
            .append($('<div class="layui-input-block"></div>')
                .append($('<input type="password" id="password" name="password" lay-verify="required|pass" ' +
                    'placeholder="请输入密码" autocomplete="off" class="layui-input" />'))
            )
        ).append($('<div class="layui-form-item"></div>')
            .append($('<label class="layui-form-label" style="width: 107px">确认密码:</label>'))
            .append($('<div class="layui-input-block"></div>')
                .append($('<input type="password" id="confirmPassword" name="confirmPassword" lay-verify="required|comfirmPass" ' +
                    'placeholder="确认密码" autocomplete="off" class="layui-input"/>'))
            )
        );
        // $("body").append($form);
        layui.use(['layer', 'form'], function () {
            var form = layui.form;
            layer.open({
                type: 1, offset: '250px', area: '650px', title: '添加用户',
                content: '<div id="addUserDiv"></div>',
                btn: ['添加']
                , yes: function (index, layero) {
                    var reg = /\S+/;//正则
                    if (!reg.test($("#username").val())) {
                        layer.msg("用户名不符合规范", {icon: 2});
                        return false;
                    }
                    // var telpass =/0?(13|14|15|18|17)[0-9]{9}/;
                    var telpass = /^1([38]\d|5[0-35-9]|7[3678])\d{8}$/;
                    if (!telpass.test($("#tel").val())) {
                        layer.msg("请输入正确的手机号", {icon: 2});
                        return false;
                    }
                    var pass = /^[0-9A-Za-z]{6,12}$/;
                    if (!pass.test($("#password").val())) {
                        layer.msg("密码必须是6到12位，且不能出现空格", {icon: 2});
                        return false;
                    }
                    if ($("#password").val() != $("#confirmPassword").val()) {
                        layer.msg("两次密码不一致!", {icon: 2});
                        return false;
                    }
                    var data = {
                        username: $("#username").val(),
                        password: $("#password").val(),
                        tel: $("#tel").val(),
                        role: role
                    };
                    $.post("/backApp/user/addUser", data, function (res) {
                        if (res.status == 200) {
                            layer.msg("添加管理员成功", {icon: 1});
                            getUserByRole(role);
                            layer.close(index);
                        } else {
                            layer.msg(res.msg, {icon: 2});
                        }
                    })
                }
            });
            $("#addUserDiv").append($form);
        });

    })
}

/*条件查询用户find*/
function findUserByOptions(role) {
    var data = {
        usertel: $("#userTel").val(),
        username: $("#userName").val(),
        starttime: $("#fromReport").val(),
        endtime: $("#toReport").val(),
        role: role
    };
    if (data.endtime != "" && data.starttime != "" && data.endtime == data.starttime) {
        layer.msg("起始时间和终止时间不能相同", {icon: 2});
        return false;
    }
    $.post("/backApp/user/findUser", data, function (res) {
        if (res.status == 200) {
            doUserTable(role,res);
        } else {
            layer.msg(res.msg, {icon: 1});
        }
    });
}

function findCustomer() {
    var data = {
        usertel: $("#userTel").val(),
        proname: $("#proName").val(),
        username: $("#userName").val(),
        starttime: $("#fromReport").val(),
        endtime: $("#toReport").val()
    };
    if (data.endtime != "" && data.starttime != "" && data.endtime == data.starttime) {
        layer.msg("起始时间和终止时间不能相同", {icon: 2});
        return false;
    }
    $.post("/backApp/user/findCustomer", data, function (res) {
        if (res.status == 200) {
            // var columns = [];
            // for (var attr in res.data[0]) {
            //     if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1 || attr == "remark") {
            //         continue;
            //     }
            //     var titles = user_fields.customer;
            //     var column = {
            //         field: attr,
            //         title: titles[attr],
            //         valign: "middle",
            //         align: "center",
            //         sortable: true,
            //         visible: true,
            //         formatter: paramsMatter
            //     };
            //     columns.push(column);
            // }
            // initTable(columns, res.data);
            doUserTable("客户",res);
        } else {
            layer.msg(res.msg, {icon: 1});
        }
    })
}

function getUserByRole(role) {
    $.get('/backApp/user/getUserByRole', {role: role}, function (res) {
        if (res.status == 200) {
            doUserTable(role,res);
        }
    })
}

function doUserTable(role,res){
    var user_fields = {
        user: {
            gid: "ID",
            name: "姓名",
            password: "密码",
            tel: "电话",
            state: "状态",
            createTime: "创建时间",
            remark: "备注"
        },
        distributor: {
            gid: "ID",
            name: "姓名",
            password: "密码",
            tel: "电话",
            state: "状态",
            count: "报备数量",
            createTime: "创建时间",
            remark: "备注",
            channelComm: '渠道专员',
            checkState: '审核'
        },
        customer: {
            gid: "ID",
            name: "姓名",
            tel: "电话",
            projectName: "项目",
            distritionName: "分销商",
            saleName: "业务员",
            state: "状态",
            backTime: "报备时间",
            expireTime: "过期时间",
            visitTime: "到访时间",
            dealTime: "成交时间",
            remark: "备注",
            cusArea: "客户区域",
            acreage: "意向面积",
            money: "投资额"
        }
    };
    var columns = [];
    for (var attr in res.data[0]) {
        if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1 || attr == "disCompany" || attr == "disLinkman" || attr == "disLinktel" || attr == "size" || attr == "license" || attr == "remark") {
            continue;
        }
        if (role == "管理员" && attr.indexOf("count") > -1) {
            continue;
        }
        if (role == "管理员" && attr.indexOf("ch") > -1) {
            continue;
        }
        var titles;
        if (role == "客户") {
            titles = user_fields.customer;
        } else if (role == "管理员") {
            titles = user_fields.user;
        } else {
            titles = user_fields.distributor;
        }
        if (role == "分销商") {
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].count == null) {
                    res.data[i].count = 0;
                }
            }
        }
        var column = {
            field: attr,
            title: titles[attr],
            valign: "middle",
            align: "center",
            sortable: true,
            visible: true,
            formatter: paramsMatter
        };
        columns.push(column);
    }
    if (role == "管理员") {
        columns.push({
            field: 'operate',
            title: '操作',
            valign: "middle",
            align: 'center',
            events: userOperateEvents,
            formatter: userOperateFormatter
        });
    }
    if (role == "分销商" || role == "业务员") {
        columns.push({
            field: 'operate',
            title: '操作',
            valign: "middle",
            align: 'center',
            width: "250",
            events: userOperateEvents,
            formatter: userOperateFormatter
        });
    }
    // else {
    //     columns.push({
    //         field: 'operate',
    //         title: '操作',
    //         valign: "middle",
    //         align: 'center',
    //         events: customerOperateEvents,
    //         formatter: customerOperateFormatter
    //     });
    // }
    initTable(columns, res.data);
}

/*建表*/
function initTable(columns, data) {
    $('#rightTable').bootstrapTable('destroy').bootstrapTable({
        // url: '/Home/GetDepartment',         //请求后台的URL（*）
        // method: 'get',                      //请求方式（*）
        // toolbar: '#toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        // pageList: [5, 10, 25, 50, 100],        //可供选择的每页的行数（*）
        smartDisplay: false,
        // search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        // strictSearch: true,
        // showColumns: true,                  //是否显示所有的列
        // showRefresh: true,                  //是否显示刷新按钮
        // minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        height: 'auto',                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
        // showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                   //是否显示父子表
        // showExport: true,
        // exportDataType: "all",
        data: data,
        columns: columns
    });
}

function userOperateFormatter(value, row, index) {
    var btns = [];
    if (row.roleId != 3) {
        btns.push('<button type="button" class="RoleOfwatch btn btn-primary  btn-sm" style="margin-right:15px;">查看</button>');
        // row.checkState 四种状态:未提交,审核中,已过审,未过审
        if (row.checkState != "审核中") {
            btns.push('<button type="button" class="RoleOfaudit btn btn-primary  btn-sm" style="margin-right:15px;" disabled="true">审核</button>');
        } else {
            btns.push('<button type="button" class="RoleOfaudit btn btn-primary  btn-sm" style="margin-right:15px;">审核</button>');
        }
    }
    btns.push('<button type="button" class="RoleOfedit btn btn-primary  btn-sm" style="margin-right:15px;">修改</button>');
    if (row.roleId == 3){
        if (row.state == "正常") {
            btns.push('<button type="button" class="RoleOfdisable btn btn-primary  btn-sm" style="margin-right:15px;">禁用</button>');
        } else {
            btns.push('<button type="button" class="RoleOfdisable btn btn-primary  btn-sm" style="margin-right:15px;">启用</button>');
        }
    }
    btns.push('<button type="button" class="RoleOfdelete btn btn-primary  btn-sm" style="margin-right:15px;">删除</button>');
    return btns.join('');
}

// function customerOperateFormatter(value, row, index) {
//     return [
//         '<button type="button" class="customerOfedit btn btn-primary  btn-sm" style="margin-right:15px;">到访</button>'
//     ].join('');
// }

function recommOperateFormatter(value, row, index) {
    return [
        '<button type="button" class="recommOfedit btn btn-primary  btn-sm" style="margin-right:15px;">修改</button>',
        '<button type="button" class="recommOfdisable btn btn-primary  btn-sm" style="margin-right:15px;">停止</button>',
        '<button type="button" class="recommOfdelete btn btn-primary  btn-sm" style="margin-right:15px;">删除</button>'
    ].join('');
}

function projectOperateFormatter(value, row, index) {
    return [
        '<button type="button" class="ProOfwatch btn btn-primary btn-sm" style="margin-right:15px;">查看资料</button>',
        '<button type="button" class="ProOfedit btn btn-primary btn-sm" style="margin-right:15px;">修改</button>',
        '<button type="button" class="ProOfdelete btn btn-primary btn-sm" style="margin-right:15px;">删除</button>'
    ].join('');
}

function newsOperateFormatter(value, row, index) {
    return [
        // '<button type="button" class="newsOfwatch btn btn-primary btn-sm" style="margin-right:15px;">查看</button>',
        '<button type="button" class="newsOfedit btn btn-primary btn-sm" style="margin-right:15px;">修改</button>',
        '<button type="button" class="newsOfdelete btn btn-primary btn-sm" style="margin-right:15px;">删除</button>'
    ].join('');
}

function projectOperateFormatter_Dis(value, row, index) {
    return [
        '<button type="button" class="ProOfwatch btn btn-primary btn-sm" style="margin-right:15px;">查看资料</button>'
    ].join('');
}

window.userOperateEvents = {
    'click .RoleOfwatch': function (e, value, row, index) {

    },
    'click .RoleOfaudit': function (e, value, row, index) {
        $.get('/backApp/user/validateDisState', {disId: row.gid}, function (res) {
            if (res.status == 200) {
                var html = '<div style="text-align: center"> ' +
                    '   <img src="/backApp' + res.data.license + '" style="width: 35em;height: 20em;margin-bottom: 1em;"><br> ' +
                    // '   <div>' +
                    '        <table style="margin:auto;">' +
                    '            <tr>' +
                    '                <td style="text-align: right;"><label>公司名称：</label></td>' +
                    '                <td style="text-align: left;"><label>' + res.data.disCompany + '</label></td>' +
                    '            </tr>' +
                    '            <tr>' +
                    '                <td style="text-align: right;"><label>联系人：</label></td>' +
                    '                <td style="text-align: left;"><label>' + res.data.disLinkman + '</label></td>' +
                    '            </tr>' +
                    '            <tr>' +
                    '                <td style="text-align: right;"><label>联系电话：</label></td>' +
                    '                <td style="text-align: left;"><label>' + res.data.disLinktel + '</label></td>' +
                    '            </tr>' +
                    '            <tr>' +
                    '                <td style="text-align: right;"><label>渠道专员：</label></td>' +
                    '                <td style="text-align: left;"><label>' + res.data.channelComm + '</label></td>' +
                    '            </tr>' +
                    '            <tr>' +
                    '                <td style="text-align: right;"><label>公司规模：</label></td>' +
                    '                <td style="text-align: left;"><label>' + res.data.size + '</label></td>' +
                    '            </tr>' +
                    '            <tr>' +
                    '                <td style="text-align: right;"><label>是否通过审核：</label></td>' +
                    '                <td style="text-align: left;">' +
                    '                    <button id="passAudit" type="button" class="btn btn-primary btn-sm" style="margin-right:15px;" value="pass">通过</button>' +
                    '                    <button id="unpassAudit" type="button" class="btn btn-primary btn-sm" style="margin-right:15px;" value="unpass">不通过</button>' +
                    '                </td>' +
                    '            </tr>' +
                    '        </table>' +
                    // '   </div>' +
                    '</div>';
                var index = layer.open({
                    type: 1,
                    title: "营业执照审核", //不显示标题栏
                    closeBtn: true,
                    area: ['80%', '80%'],
                    shade: 0.8,
                    id: 'LAY_layuipro', //设定一个id，防止重复弹出
                    btn: ['取消'],
                    btnAlign: 'r',
                    moveType: 1, //拖拽模式，0或者1
                    content: '<div id="licenseDiv" style="padding: 30px; line-height: 22px; background-color: #e9edf3; color: #000; font-weight: 300;height: 100%;"></div>',
                    success: function (layero) {
                    }
                });
                $("#licenseDiv").html(html);
                $("#passAudit").on('click', function () {
                    changeDisCheckState(row.gid, $(this).val(), index);
                });
                $("#unpassAudit").on('click', function () {
                    changeDisCheckState(row.gid, $(this).val(), index);
                });
            } else {
                layer.msg(res.msg, {icon: 2});
            }
        });
    },
    'click .RoleOfedit': function (e, value, row, index) {
        var role = $("#userManage").find(".active").find("a").eq(0).text();
        var $form = $('<form id="userForm" class="layui-form" style="padding: 20px 30px 10px 0;"></form>');
        $form.append($('<div class="layui-form-item"></div>')
            .append($('<label class="layui-form-label" style="width: 107px">用户姓名:</label>'))
            .append($('<div class="layui-input-block"></div>')
                .append($('<input type="text" id="updatename" name="updatename" lay-verify="required" autocomplete="off" ' +
                    'placeholder="请输入用户姓名" class="layui-input"/>'))
            )
        );
        $form.append($('<div class="layui-form-item"></div>')
            .append($('<label class="layui-form-label" style="width: 107px">用户号码:</label>'))
            .append($('<div class="layui-input-block"></div>')
                .append($('<input type="text" id="updatetel" name="updatetel" lay-verify="required" autocomplete="off" ' +
                    'placeholder="请输入用户号码" class="layui-input"/>'))
            )
        );
        $form.append($('<div class="layui-form-item"></div>')
            .append($('<label class="layui-form-label" style="width: 107px">用户密码:</label>'))
            .append($('<div class="layui-input-block"></div>')
                .append($('<input type="password" id="updatepassword" name="updatepassword" lay-verify="required|pass" ' +
                    'placeholder="请输入密码" autocomplete="off" class="layui-input" />'))
            )
        ).append($('<div class="layui-form-item"></div>')
            .append($('<label class="layui-form-label" style="width: 107px">确认密码:</label>'))
            .append($('<div class="layui-input-block"></div>')
                .append($('<input type="password" id="updateconfirmPassword" name="updateconfirmPassword" lay-verify="required|comfirmPass" ' +
                    'placeholder="确认密码" autocomplete="off" class="layui-input"/>'))
            )
        );
        layer.open({
            type: 1, offset: '250px', area: '650px', title: '修改用户',
            content: '<div id="updateUserDiv"></div>',
            btn: ['修改'],
            yes: function (index, layero) {
                var data = {
                    gid: row.gid,
                    username: $("#updatename").val(),
                    password: $("#updatepassword").val(),
                    tel: $("#updatetel").val()
                };
                $.post("/backApp/user/updateUser", data, function (res) {
                    if (res.status == 200) {
                        layer.msg(res.msg, {icon: 1});
                        getUserByRole(role);
                        layer.close(index)
                    } else {
                        layer.msg(res.msg, {icon: 2});
                    }
                })
            }
        });
        $("#updateUserDiv").append($form);
        $("#updatename").val(row.name);
        $("#updatetel").val(row.tel);
        $("#updatepassword").val(row.password);
        $("#updateconfirmPassword").val(row.password);
    },
    'click .RoleOfdisable': function (e, value, row, index) {
        var role = $("#userManage").find(".active").find("a").eq(0).text();
        $.post("/backApp/user/changeState", {gid: row.gid, state: row.state}, function (res) {
            if (res.status == 200) {
                layer.msg(res.msg, {icon: 1});
                getUserByRole(role);
                layer.close(index);
            } else {
                layer.msg(res.msg, {icon: 2});
            }
        })
    },
    'click .RoleOfdelete': function (e, value, row, index) {
        var role = $("#userManage").find(".active").find("a").eq(0).text();
        layer.confirm('是否删除此用户？', {
            title: "删除用户",
            btn: ['删除', '取消'] //按钮
        }, function () {
            $.post("/backApp/user/delUser", {gid: row.gid}, function (res) {
                if (res.status == 200) {
                    layer.msg(res.msg, {icon: 1});
                    getUserByRole(role);
                } else {
                    layer.msg(res.msg, {icon: 2});
                }
                layer.close(index);
            })
        });
    }
};
// window.customerOperateEvents = {
//     'click .customerOfedit': function (e, value, row, index) {
//         console.log(row);
//         console.log(index);
//         // $("#editModal").modal('show');
//     }
// };
window.recommOperateEvents = {
    'click .recommOfedit': function (e, value, row, index) {
        console.log(row);
        console.log(index);
        // $("#editModal").modal('show');
    },
    'click .recommOfdisable': function (e, value, row, index) {
        console.log(row);
        console.log(index);
    },
    'click .recommOfdelete': function (e, value, row, index) {
        console.log(row);
        console.log(index);
    }
};
window.newsOperateEvents = {
    'click .newsOfwatch': function (e, value, row, index) {
        console.log(row);
        console.log(index);
        // $("#editModal").modal('show');
    },
    'click .newsOfedit': function (e, value, row, index) {
        var newsHtml = '<div id="newsDiv" style="background: #e9edf3;padding-top: 1em;padding-bottom: 1em;text-align: center;">' +
            '            <form id="newsForm" method="post" enctype="multipart/form-data">' +
            '                <input type="hidden" value="'+row.gid+'" name="gid"/>' +
            '                <table style="border-collapse: separate;border-spacing: 5px;margin: auto;">' +
            '                    <tr>' +
            '                        <td style="text-align: right">标题：</td>' +
            '                        <td style="text-align: left" colspan="2"><input type="text" name="title" style="width: 25em;" value="'+row.title+'"/></td>' +
            '                    </tr>' +
            '                    <tr>' +
            '                        <td style="text-align: right">图片：</td>' +
            '                        <td style="text-align: left;"><input id="newsFile" type="file" name="pictureFile" onchange="uploadImg(this)" accept="image/gif,image/jpeg,image/jpg,image/png" style="width: 16em;"/></td>' +
            '                        <td><img id="newsImg" src="/backApp'+row.picture+'" style="width: auto;height: auto;max-width: 10em;max-height: 5em;"/></td>' +
            '                    </tr>' +
            '                    <tr>' +
            '                        <td style="text-align: right">内容：</td>' +
            '                        <td style="text-align: left" colspan="2"><textarea name="content" style="width: 25em;height: 10em;" value="'+row.content+'">'+row.content+'</textarea></td>' +
            '                    </tr>' +
            '                    <tr>' +
            '                        <td style="text-align: right">链接：</td>' +
            '                        <td style="text-align: left" colspan="2"><input type="text" name="linkUrl" style="width: 25em;" value="'+row.linkUrl+'"/></td>' +
            '                    </tr>' +
            '                </table>' +
            '            </form>' +
            // '            <button type="button" class="btn btn-primary btn-sm" style="margin-right:15px;">添加</button>' +
            '        </div>';
        layer.open({
            type: 1, offset: 'auto', area: ['40%','55%'], title: '修改爱家头条',
            content: '<div id="editNewsDiv"></div>',
            btn: ['确认','取消'],
            yes: function (index, layero) {
                if ($("#newsForm input[name=title]").val().replace(/^\s*|\s*$/g,"") == ""){
                    layer.msg("标题不能为空!",{icon:2});
                    return;
                }
                // if ($("#newsForm input[name=pictureFile]").val() == ""){
                //     layer.msg("图片不能为空!",{icon:2});
                //     return;
                // }
                if ($("#newsForm textarea[name=content]").val().replace(/^\s*|\s*$/g,"") == ""){
                    layer.msg("内容不能为空!",{icon:2});
                    return;
                }
                if ($("#newsForm input[name=linkUrl]").val().replace(/^\s*|\s*$/g,"") == ""){
                    layer.msg("链接不能为空!",{icon:2});
                    return;
                }

                var form = new FormData(document.getElementById("newsForm"));
                /**
                 * ajax提交表单(带文件)
                 */
                $.ajax({
                    //几个参数需要注意一下
                    type: "POST",//方法类型
                    dataType: "json",//预期服务器返回的数据类型
                    cache: false,    //上传文件不需缓存
                    contentType: false,//需设置为false，因为是FormData对象，且已经声明了属性enctype="multipart/form-data"
                    processData: false,//需设置为false，因为data值是FormData对象，不需要对数据做处理
                    async: true,
                    url: "/backApp/news/editNews",//url
                    data: form,
                    success: function (res) {
                        // console.log(res);//打印服务端返回的数据(调试用)
                        if (res.status == 200) {
                            layer.msg(res.msg, {icon: 1});
                            newsList();
                        } else {
                            layer.msg(res.msg, {icon: 2});
                        }
                        layer.close(index);
                    },
                    error: function () {
                        layer.msg("上传接口异常!", {icon: 2});
                    }
                });
            }
        });
        $("#editNewsDiv").append(newsHtml);
    },
    'click .newsOfdelete': function (e, value, row, index) {
        layer.confirm('确认要删除吗？', {
            btn: ['确定', '取消']//按钮
        }, function (index) {
            layer.close(index);
            //此处请求后台程序，下方是成功后的前台处理……
            $.get('/backApp/news/deleteNewsById', {newsId: row.gid}, function (res) {
                if (res.status == 200) {
                    layer.msg(res.msg, {icon: 1});
                    newsList();
                } else {
                    layer.msg(res.msg, {icon: 2});
                }
                layer.close(index);
            });
        });
    }
};
window.projectOperateEvents = {
    'click .ProOfwatch': function (e, value, row, index) {
        var url = "/backApp/project/downloadProData";
        $('<form id="excelForm" method="post" action="' + url + '" style="display: none">' +
            '<input name="proId" value="' + row.gid + '">' +
            '</form>').appendTo('body').submit().remove();

        // 由于ajax函数的返回类型只有xml、text、json、html等类型，没有“流”类型，
        // 所以通过ajax去请求该接口是无法下载文件的，所以我们创建一个新的form元素来请求接口。
        // $.get('/backApp/project/downloadProData',{proId:row.gid},function (res) {
        //     if (res != 200){
        //         layer.msg(res.msg,{icon:2});
        //     }
        // })
    },
    'click .ProOfedit': function (e, value, row, index) {
        var div = $('<div id="editpro"></div>');
        if(row.commission==null){
            row.commission="";
        }
        if(row.keyword==null){
            row.keyword="";
        }
        if(row.header==null){
            row.header="";
        }
        if(row.backTime==null){
            row.backTime="";
        }
        if(row.tel==null){
            row.tel="";
        }
        if(row.description==null){
            row.description="";
        }
        var html = '<form id="editForm" action="#" method="post" name="editProForm">' +
                '        <table style="border-collapse:separate; border-spacing:0.5em;margin: auto;">' +
                '            <tr>' +
                '                <td>项目名称:</td>' +
                '                <td><input type="text" name="name" value="'+row.name+'"/></td>' +
                '                <td>分销商数:</td>' +
                '                <td><input type="text" name="disnum" value="'+row.disnum+'"/></td>' +
                '            </tr>' +
                '            <tr>' +
                '                <td>项目单价:</td>' +
                '                <td><input type="text" name="price" value="'+row.price+'"/></td>' +
                '                <td>佣金:</td>' +
                '                <td><input type="text" name="commission" value="'+row.commission+'"/></td>' +
                '            </tr>' +
                '            <tr>' +
                '                <td>地址:</td>' +
                '                <td><input type="text" name="address" value="'+row.address+'"/></td>' +
                '                <td>关键字:</td>' +
                '                <td></span><input type="text" name="keyword" value="'+row.keyword+'"/></td>' +
                '            </tr>' +
                '            <tr>' +
                '                <td>负责人:</td>' +
                '                <td><input type="text" name="header" value="'+row.header+'"/></td>' +
                '                <td>电话:</td>' +
                '                <td><input type="text" name="tel" value="'+row.tel+'"/></td>' +
                '            </tr>' +
                '            <tr>' +
                '                <td>报备时间:</td>' +
                '                <td><input type="text" name="backTime" id="startReportTime" value="'+row.backTime+'"/></td>' +
                '                <td>项目状态:</td>' +
                '                <td><input type="radio"  name="state" value="在售"/>在售<input type="radio" name="state" value="暂停"/>暂停<input type="radio" name="state" value="售罄"/>售罄</td>' +
                '            </tr>' +
                '            <tr>' +
                '                <td>房型:</td>' +
                '                <td>' +
                '                    <select id="proType" name="type">' +
                '                       <option value="住宅">住宅</option>' +
                '                       <option value="商铺">商铺</option>' +
                '                       <option value="写字楼">写字楼</option>' +
                '                       <option value="公寓">公寓</option>' +
                '                       <option value="二手房">二手房</option>' +
                '                       <option value="其他项目">其他项目</option>' +
                '                    </select>' +
                '                </td>' +
                '                <td>报备次数限制:</td>' +
                '                <td><input name="reportLimit" value="'+row.reportLimit+'"/></td>' +
                '            </tr>' +
                '            <tr>' +
                '                <td>开发商:</td>' +
                '                <td><input type="text" name="develop" value="'+row.develop+'"/></td>' +
                '                <td>项目描述:</td>' +
                '                <td><textarea name="description">'+row.description+'</textarea></td>' +
                '            </tr>' +
                '        </table>' +
                '        <input type="hidden" name="gid" value="'+row.gid+'"/>' +
                '    </form>';
        div.html(html);
        layer.open({
            type: 1, offset: '250px', area: '800px', title: '项目修改',
            content: '<div id="editContent"></div>',
            btn: ['确认', '取消'],
            yes: function (index, layero) {
                $.ajax({
                    type: "POST",                  //提交方式
                    dataType: "json",              //预期服务器返回的数据类型
                    url: "/backApp/project/editProjectInfo",          //目标url
                    data: $('#editForm').serialize(), //提交的数据
                    success: function (res) {
                        if (res.status == 200) {
                            layer.msg(res.msg, {icon: 1});
                            projectList();
                        } else {
                            layer.msg(res.msg, {icon: 2});
                        }
                        layer.close(index);
                    }
                });
            }
        });
        $("#editContent").append(div);
        $("#startReportTime").datetimepicker({
            format: 'yyyy-mm-dd',//显示格式
            todayHighlight: 1,//今天高亮
            minView: "month",//设置只显示到月份
            startView: 2,
            forceParse: 0,
            language: "zh-CN", //语言设置
            showMeridian: 1,
            autoclose: 1//选择后自动关闭
        });
        var radios = $("#editpro").find("input:radio");
        for (var i = 0; i < radios.length; i++) {
            var radio = $(radios[i]);
            if (radio.val() == row.state) {
                radio.attr("checked", "true");
                break;
            }
        }
        var options = $("#proType").find("option");
        for (var i = 0; i < options.length; i++) {
            var option = $(options[i]);
            if (option.val() == row.type) {
                option.attr("selected", "selected");
                break;
            }
        }
    },
    'click .ProOfdelete': function (e, value, row, index) {
        layer.confirm('确认要删除吗？', {
            btn: ['确定', '取消']//按钮
        }, function (index) {
            layer.close(index);
            //此处请求后台程序，下方是成功后的前台处理……
            $.get('/backApp/project/deleteProById', {proId: row.gid}, function (res) {
                if (res.status == 200) {
                    layer.msg(res.msg, {icon: 1});
                } else {
                    layer.msg(res.msg, {icon: 2});
                }
                layer.close(index);
            });
        });
    }
};

function getCookie(name) {
    var DC = document.cookie;
    var cookies = DC.split(";");
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].trim().split("=");
        if (cookie[0] == name) {
            return cookie[1];
        }
    }
}

/*分销商审核*/
function changeDisCheckState(disId, val, index) {
    var text = val == "pass" ? "通过" : "不通过";
    layer.confirm('是否确认' + text + '?', {icon: 3, title: '提示'}, function (index2) {
        //do something
        $.get('/backApp/user/changeDisCkState', {disId: disId, value: val}, function (res) {
            if (res.status == 200) {
                layer.msg(res.msg, {icon: 1});
            } else {
                layer.msg(res.msg, {icon: 2});
            }
            layer.close(index);
            getUserByRole("分销商");
        });
        layer.close(index2);
    });

}
























