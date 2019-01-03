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
        remark: "备注"
    }
};
var project_fields = {
    gid: "ID",
    name: "项目名称",
    disnum: "分销商数",
    reportNum: "总报备次数",
    desc: "描述",
    develop: "开发商",
    keyword: "关键字",
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
var userRole;
$(function () {
    layui.use(['layer', 'form'], function(){
        var layer = layui.layer
            ,form = layui.form;
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
            '                        <a href="javascript:void(0);" id="projectPublish"><b>项目发布</b></a>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);" id="projectList"><b>项目管理</b></a>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);"><b>报备管理</b></a>' +
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
}

/** 窗口大小resize监听 */
function monitorWindowView() {
    $(window).resize(function () {
        $('#rightTable').bootstrapTable('resetView');
    });
}

/*退出*/
function logout(){
    $("#signOut").on("click",function () {
        $.post("/backApp/signOut",function () {
            window.location.href =window.location.href.replace("index","login");
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
        $.get('/backApp/project/getRecommendPro',function (res) {
            if (res.status == 200){
                var data = res.data;
                var imgs = $(".main-right").find("img");
                for (var i = 0;i < 3;i++){
                    $(imgs[i]).attr("src","data:image/gif;base64," + data[i].desc_pic);
                }
                var columns=[];
                for (var attr in data[0]){
                    if (attr == "desc_pic" || attr == "index" || attr.indexOf("id") > -1){
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
        $("#addRecommendImg").on('click',function () {
            var $form = $('<form id="proForm" class="layui-form" style="padding: 20px 30px 10px 0;"></form>');
            $form.append($('<div class="layui-form-item"></div>')
                .append($('<label class="layui-form-label" style="width: 107px">项目名称:</label>'))
                .append($('<div class="layui-input-block"></div>')
                    .append($('<input type="text" id="proName" name="proName" lay-verify="required" autocomplete="off" ' +
                        'placeholder="奥山光谷世纪城" class="layui-input"/>'))
                )
            );
            $form.append($('<div class="layui-form-item"></div>')
                .append($('<label class="layui-form-label" style="width: 107px">开发商:</label>'))
                .append($('<div class="layui-input-block"></div>')
                    .append($('<input type="text" id="deveName" name="deveName" lay-verify="required" autocomplete="off" ' +
                        'placeholder="奥山集团" class="layui-input"/>'))
                )
            );
            $form.append($('<div class="layui-form-item"></div>')
                .append($('<label class="layui-form-label" style="width: 107px">宣传时间:</label>'))
                .append($('<div class="layui-input-block"></div>')
                    .append($('<input type="text" id="publishTime" name="publishTime" lay-verify="required" autocomplete="off" ' +
                        'placeholder="6月-12月" class="layui-input"/>'))
                )
            );
            $form.append($('<div class="layui-form-item"></div>')
                .append($('<label class="layui-form-label" style="width: 107px">宣传费用:</label>'))
                .append($('<div class="layui-input-block"></div>')
                    .append($('<input type="text" id="publishPrice" name="publishPrice" lay-verify="required" autocomplete="off" ' +
                        'placeholder="20" class="layui-input"/>'))
                )
            );
            // $form.append($('<div class="layui-form-item"></div>')
            //     .append($('<label class="layui-form-label" style="width: 107px">上传图片:</label>'))
            //     .append($('<div class="layui-input-block"></div>')
            //         .append($('<input type="file" id="upPic" name="upPic" lay-verify="required" autocomplete="off" ' +
            //             'placeholder="20" class="layui-input"/>'))
            //     )
            // );
            layer.open({
                type: 1, offset: '250px', area: '650px', title: '添加项目推荐',
                content:'<div id="addRecommendProDiv"></div>',
                btn:['确认'],
                yes:function (index, layero) {
                    // var data ={
                    //     gid:row.gid,
                    //     username:$("#updatename").val(),
                    //     password:$("#updatepassword").val(),
                    //     tel:$("#updatetel").val()
                    // };
                    // $.post("/backApp/user/updateUser",data,function (res) {
                    //     if(res.status==200){
                    //         layer.msg(res.msg,{icon:1});
                    //         getUserByRole("管理员");
                    //         layer.close(index)
                    //     }else{
                    //         layer.msg(res.msg,{icon:2});
                    //     }
                    // })
                }
            });
            $("#addRecommendProDiv").append($form);
        });
    })
}

/*项目发布_管理员*/
function projectPublish() {
    $("#projectPublish").on('click', function () {
        var publishHtml = '<div class="publishFileDiv">' +
            '                <img src="img/home/upFile.png">' +
            '                <span>' +
            '                    <button class="btn btn-primary btn-sm">上传文档</button>' +
            '                </span>' +
            '            </div>' +
            '            <div class="publishFileDiv">' +
            '                <img src="img/home/upPic.png">' +
            '                <span>' +
            '                    <button class="btn btn-primary btn-sm">上传图片</button>' +
            '                </span>' +
            '            </div>' +
            '            <div class="publishFileDiv">' +
            '                <img src="img/home/upZiLiao.png">' +
            '                <span>' +
            '                    <button class="btn btn-primary btn-sm">资料上传</button>' +
            '                </span>' +
            '            </div>' +
            '            <div id="projectPublishDiv">' +
            '                <div>' +
            '                    <form action="/backApp/project/addProject" method="post"  enctype="multipart/form-data">' +
            '                        <table style="border-collapse:separate; border-spacing:0.5em;margin: auto;">' +
            '                            <tr>' +
            '                                <td><b>项目名称：</b><input type="text" name="proName"></td>' +
            '                                <td><b>开发商：</b><input type="text" name="develop"></td>' +
            '                                <td><b>详细地址：</b><input type="text" name="address"></td>' +
            '                            </tr>' +
            '                            <tr>' +
            '                                <td><b>合作时间：</b><input type="text" id="fromTime" name="startTime"><b> 至 </b><input type="text" id="toTime" name="endTime"></td>' +
            '                                <td><b>允许报备次数：</b><input type="text" name="disnum"></td>' +
            '                                <td><b>佣金设置：</b><input type="text" name="commission"></td>' +
            '                            </tr>' +
            '                            <tr>' +
            '                                <td><b>上传资料：</b><input type="file" name="file" style="display: inline;line-height: normal;"></td>' +
            '                                <td><b>关键字：</b><input type="text" name="keyword"></td>' +
            '                                <td><b>项目单价：</b><input type="text" name="price"></td>' +
            '                            </tr>' +
            '                            <tr>' +
            '                                <td><b>项目描述：</b></td>' +
            '                                <td><textarea name="desc" placeholder="请输入内容" style="width: 25em;height: 5em;"></textarea></td>' +
            '                            </tr>' +
            '                        </table>' +
            '                        <span>' +
            // '                            <button class="btn btn-primary btn-sm" style="width: 16em;height: 3em;font-size: 16px;">发布</button>' +
            '                            <input id="submit_form" type="submit" class="btn btn-primary btn-sm" style="width: 16em;height: 3em;font-size: 16px;color: #fff;background-color: #337ab7;border-color: #2e6da4;" value="发布"/>' +
            '                        </span>' +
            '                    </form>' +
            '                </div>' +
            '            </div>';
        $(".main-right").html(publishHtml);
        $("#fromTime,#toTime").datetimepicker({
            format: 'yyyy-mm-dd',//显示格式
            todayHighlight: 1,//今天高亮
            minView: "month",//设置只显示到月份
            startView: 2,
            forceParse: 0,
            language:"zh-CN", //语言设置
            showMeridian: 1,
            autoclose: 1//选择后自动关闭
        });
    })
}

/*项目管理_管理员*/
function projectManage() {
    $("#projectList").on('click', function () {
        var proListHtml = '<div style="background-color: #fff;height: 100%;">' +
            '       <div class="main-right-search"> ' +
            '           <input type="text" id="projectName" style="display: inline" class="form-control" placeholder="请输入项目名称"/> ' +
            // '               <button type="button" class="btn-primary btn" style="display: inline;margin-left: 20px" id="queryPro">查询</button>'+
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
        $("#queryPro").on("click",function () {
            queryProByName();
        })
    });
}

/*根据项目名称查*/
function queryProByName(){
    var projectName =$("#projectName").val();
    $.post("/backApp/project/queryPro",{projectName:projectName},function (res) {
        if (res.status == 200) {
            var columns = [];
            for (var attr in res.data[0]) {
                if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1 || attr.indexOf("desc") > -1 || attr == "keyword" || attr == "address" || attr == "remark") {
                    continue;
                }
                var column = {
                    field: attr,
                    title: project_fields[attr],
                    valign: "middle",
                    align: "center",
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
                    width: '250',
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
    })
}

/*项目清单*/
function projectList() {
    $.get('/backApp/project/getAllProject', function (res) {
        if (res.status == 200) {
            var columns = [];
            for (var attr in res.data[0]) {
                if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1 || attr.indexOf("desc") > -1 || attr == "keyword" || attr == "address" || attr == "remark") {
                    continue;
                }
                var column = {
                    field: attr,
                    title: project_fields[attr],
                    valign: "middle",
                    align: "center",
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
                    width: '250',
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
    });
}

function paramsMatter(value, row, index) {
    return "<span title=" + value + ">" + value + "</span>";
}

/*人员管理_管理员*/
function userManage() {
    $('#userManage>li').each(function (index) {
        $(this).on('click', function () {
            // var role = $('#userManage>li').eq(index).text();
            var role =$("#userManage").find(".active").find("a").eq(0).text();
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
            if (role == "管理员"||role=="分销商") {
                $("#searchUser").append('<span><input id="userName" type="text" class="form-control" placeholder="请输入姓名"/>' +
                    '</span><span><input id="userTel" type="text" class="form-control" placeholder="请输入电话号码"/></span>' +
                    '<span><input  type="text" class="form-control" placeholder="请选择起始报备时间" id="fromReport"/></span>' +
                    '<span style="width: 2em;line-height: 30px;margin-right: 1em;">至</span>' +
                    '<span><input  type="text" class="form-control" placeholder="请选择终止报备时间" id="toReport"/></span>')
                $("#searchUser").append('<button id="addUser" class="btn btn-primary" type="button" style="float: right;margin-right: 20px">添加</button>');
                $("#searchUser").append('<button id="findUser" class="btn btn-primary" type="button" style="float: right;margin-right: 480px">查询人员</button>');
                addUser(role);
            }
            if(role=="客户"){
                $("#searchUser").append('<span><input id="proName" type="text" class="form-control" placeholder="请输入项目名称"/></span>' +
                    '<span><input id="userName" type="text" class="form-control" placeholder="请输入分销商姓名"/></span>' +
                    '<span><input id="userTel" type="text" class="form-control" placeholder="请输入电话号码"/></span>' +
                    '<span><input  type="text" class="form-control" placeholder="请选择起始报备时间" id="fromReport"/></span>' +
                    '<span style="width: 2em;line-height: 30px;margin-right: 1em;">至</span>' +
                    '<span><input  type="text" class="form-control" placeholder="请选择终止报备时间" id="toReport"/></span>')
                $("#searchUser").append('<button id="findUser" class="btn btn-primary" type="button">查询</button>');
                findCustomer();
            }

            $("#fromReport,#toReport").datetimepicker({
                format: 'yyyy-mm-dd',//显示格式
                todayHighlight: 1,//今天高亮
                minView: "month",//设置只显示到月份
                startView: 2,
                forceParse: 0,
                language:"zh-CN", //语言设置
                showMeridian: 1,
                autoclose: 1//选择后自动关闭
            });
            // var role =$("#userManage").find(".active").find("a").eq(0).text();
            getUserByRole(role);
            $("#userName,#userTel,#fromReport,#toReport").on('keypress', function (e) {
                if (e.keyCode == "13") {
                    findUserByOptions(role);
                }
            });
            $("#findUser").on("click",function () {
                findUserByOptions(role);
            })
        })
    })
}

/*条件查询用户find*/
function findUserByOptions(role){
    var data ={
        usertel:$("#userTel").val(),
        username:$("#userName").val(),
        starttime:$("#fromReport").val(),
        endtime:$("#toReport").val(),
        role:role
    };
    if(data.endtime!=""&&data.starttime!=""&&data.endtime==data.starttime){
        layer.msg("起始时间和终止时间不能相同",{icon:2});
        return false;
    }
    $.post("/backApp/user/findUser",data,function (res) {
        if (res.status == 200) {
            var columns = [];
            for (var attr in res.data[0]) {
                if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1) {
                    continue;
                }
                var titles;
                if (role == "客户") {
                    titles = user_fields.customer;
                } else {
                    titles = user_fields.user;
                }
                var column = {
                    field: attr,
                    title: titles[attr],
                    valign: "middle",
                    align: "center",
                    visible: true
                };
                columns.push(column);
            }
            if (role == "管理员" || role == "分销商" || role == "业务员") {
                columns.push({
                    field: 'operate',
                    title: '操作',
                    valign: "middle",
                    align: 'center',
                    events: userOperateEvents,
                    formatter: userOperateFormatter
                });
            } else {
                columns.push({
                    field: 'operate',
                    title: '操作',
                    valign: "middle",
                    align: 'center',
                    // events: customerOperateEvents,
                    formatter: customerOperateFormatter
                });
            }
            initTable(columns, res.data);
        }else{
            layer.msg(res.msg,{icon:1});
        }
    });
}


function findCustomer() {
    $("#findUser").on("click",function () {
        var data ={
            usertel:$("#userTel").val(),
            proname:$("#proName").val(),
            username:$("#userName").val(),
            starttime:$("#fromReport").val(),
            endtime:$("#toReport").val()
        };
        if(data.endtime!=""&&data.starttime!=""&&data.endtime==data.starttime){
            layer.msg("起始时间和终止时间不能相同",{icon:2});
            return false;
        }
        $.post("/backApp/user/findCustomer",data,function (res) {
            if (res.status == 200) {
                var columns = [];
                for (var attr in res.data[0]) {
                    if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1) {
                        continue;
                    }
                    var titles;
                    titles = user_fields.customer;
                    var column = {
                        field: attr,
                        title: titles[attr],
                        valign: "middle",
                        align: "center",
                        visible: true
                    };
                    columns.push(column);
                }
                initTable(columns, res.data);
            }else{
                layer.msg(res.msg,{icon:1});
            }
        })
    })
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
                content:'<div id="addUserDiv"></div>',
                btn:['添加']
                ,yes: function(index, layero){
                    var reg = /\S+/;//正则
                   if(!reg.test($("#username").val())){
                       layer.msg("用户名不符合规范",{icon:2});
                       return false;
                   }
                    // var telpass =/0?(13|14|15|18|17)[0-9]{9}/;
                   var telpass =/^1([38]\d|5[0-35-9]|7[3678])\d{8}$/;
                    if(!telpass.test($("#tel").val())){
                        layer.msg("请输入正确的手机号",{icon:2});
                        return false;
                    }
                    var pass =/^[0-9A-Za-z]{6,12}$/;
                    if(!pass.test($("#password").val())){
                        layer.msg("密码必须是6到12位，且不能出现空格",{icon:2});
                        return false;
                    }
                    if($("#password").val()!=$("#confirmPassword").val()){
                        layer.msg("两次密码不一致!",{icon:2});
                        return false;
                    }
                    data={
                        username:$("#username").val(),
                        password:$("#password").val(),
                        tel:$("#tel").val(),
                        role:role
                    },
                    $.post("/backApp/user/addUser",data,function (res) {
                        if(res.status==200){
                            layer.msg("添加管理员成功",{icon:1});
                            getUserByRole(role);
                            layer.close(index)
                        }else{
                            layer.msg(res.msg,{icon:2});
                        }
                    })
                }

            });

            $("#addUserDiv").append($form);
        });

    })
}


function getUserByRole(role) {
    $.get('/backApp/user/getUserByRole', {role: role}, function (res) {
        if (res.status == 200) {
            var columns = [];
            for (var attr in res.data[0]) {
                if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1) {
                    continue;
                }
                var titles;
                if (role == "客户") {
                    titles = user_fields.customer;
                } else {
                    titles = user_fields.user;
                }
                var column = {
                    field: attr,
                    title: titles[attr],
                    valign: "middle",
                    align: "center",
                    sortable:true,
                    visible: true
                };
                // if (attr == "gid"){
                //     column.visible = false;
                //     column.sortable = true;
                // }
                columns.push(column);
            }
            if (role == "管理员" || role == "分销商" || role == "业务员") {
                columns.push({
                    field: 'operate',
                    title: '操作',
                    valign: "middle",
                    align: 'center',
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
    })
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
        data: data,
        columns: columns
    });
}

function userOperateFormatter(value, row, index) {
    if (row.state == "正常"){
        return [
            '<button type="button" class="RoleOfedit btn btn-primary  btn-sm" style="margin-right:15px;">修改</button>',
            '<button type="button" class="RoleOfdisable btn btn-primary  btn-sm" style="margin-right:15px;">禁用</button>',
            '<button type="button" class="RoleOfdelete btn btn-primary  btn-sm" style="margin-right:15px;">删除</button>'
        ].join('');
    }else {
        return [
            '<button type="button" class="RoleOfedit btn btn-primary  btn-sm" style="margin-right:15px;">修改</button>',
            '<button type="button" class="RoleOfdisable btn btn-primary  btn-sm" style="margin-right:15px;">启用</button>',
            '<button type="button" class="RoleOfdelete btn btn-primary  btn-sm" style="margin-right:15px;">删除</button>'
        ].join('');
    }
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

function projectOperateFormatter_Dis(value, row, index) {
    return [
        '<button type="button" class="ProOfwatch btn btn-primary btn-sm" style="margin-right:15px;">查看资料</button>'
    ].join('');
}

window.userOperateEvents = {
    'click .RoleOfedit': function (e, value, row, index) {
        var role =$("#userManage").find(".active").find("a").eq(0).text();
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
            content:'<div id="updateUserDiv"></div>',
            btn:['修改'],
            yes:function (index, layero) {
                var data ={
                        gid:row.gid,
                        username:$("#updatename").val(),
                        password:$("#updatepassword").val(),
                        tel:$("#updatetel").val()
                };
                $.post("/backApp/user/updateUser",data,function (res) {
                      if(res.status==200){
                          layer.msg(res.msg,{icon:1});
                          getUserByRole(role);
                          layer.close(index)
                      }else{
                          layer.msg(res.msg,{icon:2});
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
        var role =$("#userManage").find(".active").find("a").eq(0).text();
        $.post("/backApp/user/changeState",{gid:row.gid,state:row.state},function (res) {
            if(res.status==200){
                layer.msg(res.msg,{icon:1});
                getUserByRole(role);
                layer.close(index);
            }else{
                layer.msg(res.msg,{icon:2});
            }
        })
    },
    'click .RoleOfdelete': function (e, value, row, index) {
        var role =$("#userManage").find(".active").find("a").eq(0).text()
        layer.confirm('是否删除此用户？', {
            title:"删除用户",
            btn: ['删除','取消'] //按钮
        }, function(){
            $.post("/backApp/user/delUser",{gid:row.gid},function (res) {
                layer.msg(res.msg,{icon:1});
                getUserByRole(role);
            })
        }, function(){

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
window.projectOperateEvents = {
    'click .ProOfwatch': function (e, value, row, index) {
        console.log(row);
        console.log(index);
    },
    'click .ProOfedit': function (e, value, row, index) {

    },
    'click .ProOfdelete': function (e, value, row, index) {
        layer.confirm('确认要删除吗？', {
            btn : [ '确定', '取消' ]//按钮
        }, function(index) {
            layer.close(index);
            //此处请求后台程序，下方是成功后的前台处理……
            $.get('/backApp/project/deleteProById',{proId:row.gid},function (res) {
               layer.msg(res.msg,{icon:1});
            });
        });
    }
};

function getCookie(name) {
    var DC = document.cookie;
    var cookies = DC.split(";");
    for (var i = 0;i < cookies.length;i++){
        var cookie = cookies[i].trim().split("=");
        if (cookie[0] == name){
            return cookie[1];
        }
    }
}


























