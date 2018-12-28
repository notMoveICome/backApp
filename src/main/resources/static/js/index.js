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
var userRole = "超级管理员";
// var userRole = "分销商";
$(function () {
    initHtmlCss();
    initLeftUtilOfAdmin();
    initLeftUtilOfDistributor();
    monitorWindowView();
    //退出
    $("#signOut").on("click",function () {
        $.post("/backApp/signOut",function () {
            window.location.href =window.location.href.replace("index","login");
        })
    })
    /*默认首页管理*/
    $("#indexManage").click();
});

/*页面样式*/
function initHtmlCss() {
    var leftMenu = "";
    if (userRole == "超级管理员") {
        leftMenu = '<div id="jquery-accordion-menu" class="jquery-accordion-menu">' +
            '                <ul id="demo-list" class="menu-container">' +
            '                    <li class="active mainmenu">' +
            '                        <a href="javascript:void(0);" id="indexManage"><b>首页管理</b></a>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);" id="projectPublish"><b>项目发布</b></a>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);" class="submenu-indicator-minus" id="projectList"><b>项目管理</b></a>' +
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
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);"><b>首页管理</b></a>' +
            '                    </li>' +
            '                    <li class="mainmenu">' +
            '                        <a href="javascript:void(0);"><b>客户管理</b></a>' +
            '                        <ul class="submenu">' +
            '                            <li id="customerList"><a href="javascript:void(0);">客户列表</a></li>' +
            '                            <li><a href="javascript:void(0);">新增客户报备</a></li>' +
            '                            <li><a href="javascript:void(0);">批量报备</a></li>' +
            '                        </ul>' +
            '                    </li>' +
            '                    <li class="active mainmenu">' +
            '                        <a href="javascript:void(0);" class="submenu-indicator-minus"><b>业务员管理</b></a>' +
            '                        <ul class="submenu">' +
            '                            <li class="active" id="salesList"><a href="javascript:void(0);">业务员列表</a></li>' +
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
            '                <img src="img/home/home1.png">' +
            '                <span> ' +
            '                     <button class="btn btn-primary btn-sm">修改</button> ' +
            '                </span>' +
            '            </div>' +
            '            <div class="recommendDiv">' +
            '                <img src="img/home/home2.png">' +
            '                <span> ' +
            '                      <button class="btn btn-primary btn-sm">修改</button> ' +
            '                </span>' +
            '            </div>' +
            '            <div class="recommendDiv">' +
            '                <img src="img/home/home3.png">' +
            '                <span> ' +
            '                      <button class="btn btn-primary btn-sm">修改</button> ' +
            '                </span>' +
            '            </div>' +
            '            <div id="recommendProManage">' +
            '                <h3 style="margin-left: 1em;">项目推荐管理</h3>' +
            '                <div>' +
            '                    <table class="table table-hover">' +
            '                        <thead>' +
            '                        <tr>' +
            '                            <th>项目名称</th>' +
            '                            <th>开放商</th>' +
            '                            <th>开包时间</th>' +
            '                            <th>项目名称</th>' +
            '                            <th>允许设备次数</th>' +
            '                            <th>佣金额度</th>' +
            '                            <th>操作</th>' +
            '                        </tr>' +
            '                        </thead>' +
            '                        <tbody>' +
            '                        <tr>' +
            '                            <td>1</td>' +
            '                            <td>2</td>' +
            '                            <td>3</td>' +
            '                            <td>4</td>' +
            '                            <td>5</td>' +
            '                            <td>6</td>' +
            '                            <td><a href="javascript:void(0);" class="btn btn-primary">修改</a><a href="javascript:void(0);" class="btn btn-danger">删除</a></td>' +
            '                        </tr>' +
            '                        <tr>' +
            '                            <td>1</td>' +
            '                            <td>2</td>' +
            '                            <td>3</td>' +
            '                            <td>4</td>' +
            '                            <td>5</td>' +
            '                            <td>6</td>' +
            '                            <td><a href="javascript:void(0);" class="btn btn-primary">修改</a><a href="javascript:void(0);" class="btn btn-danger">删除</a></td>' +
            '                        </tr>' +
            '                        <tr>' +
            '                            <td>1</td>' +
            '                            <td>2</td>' +
            '                            <td>3</td>' +
            '                            <td>4</td>' +
            '                            <td>5</td>' +
            '                            <td>6</td>' +
            '                            <td><a href="javascript:void(0);" class="btn btn-primary">修改</a><a href="javascript:void(0);" class="btn btn-danger">删除</a></td>' +
            '                        </tr>' +
            '                        <tr>' +
            '                            <td>1</td>' +
            '                            <td>2</td>' +
            '                            <td>3</td>' +
            '                            <td>4</td>' +
            '                            <td>5</td>' +
            '                            <td>6</td>' +
            '                            <td><a href="javascript:void(0);" class="btn btn-primary">修改</a><a href="javascript:void(0);" class="btn btn-danger">删除</a></td>' +
            '                        </tr>' +
            '                        <tr>' +
            '                            <td>1</td>' +
            '                            <td>2</td>' +
            '                            <td>3</td>' +
            '                            <td>4</td>' +
            '                            <td>5</td>' +
            '                            <td>6</td>' +
            '                            <td><a href="javascript:void(0);" class="btn btn-primary">修改</a><a href="javascript:void(0);" class="btn btn-danger">删除</a></td>' +
            '                        </tr>' +
            '                        <tr>' +
            '                            <td>1</td>' +
            '                            <td>2</td>' +
            '                            <td>3</td>' +
            '                            <td>4</td>' +
            '                            <td>5</td>' +
            '                            <td>6</td>' +
            '                            <td><a href="javascript:void(0);" class="btn btn-primary">修改</a><a href="javascript:void(0);" class="btn btn-danger">删除</a></td>' +
            '                        </tr>' +
            '                        </tbody>' +
            '                    </table>' +
            '                </div>' +
            '            </div>';
        $(".main-right").html(indexHtml);
    })
}

/*项目发布_管理员*/
function projectPublish() {
    $("#projectPublish").on('click', function () {
        var publishHtml = '<div class="publishFileDiv">' +
            '                <img src="img/home/home1.png">' +
            '                <span>' +
            '                    <button class="btn btn-primary btn-sm">上传文档</button>' +
            '                </span>' +
            '            </div>' +
            '            <div class="publishFileDiv">' +
            '                <img src="img/home/home2.png">' +
            '                <span>' +
            '                    <button class="btn btn-primary btn-sm">上传资料</button>' +
            '                </span>' +
            '            </div>' +
            '            <div class="publishFileDiv">' +
            '                <img src="img/home/home3.png">' +
            '                <span>' +
            '                    <button class="btn btn-primary btn-sm">上传图片</button>' +
            '                </span>' +
            '            </div>' +
            '            <div id="projectPublishDiv">' +
            '                <div>' +
            '                    <form>' +
            '                        <table style="border-collapse:separate; border-spacing:1em;margin: auto;">' +
            '                            <tr>' +
            '                                <td><b>项目名称：</b><input type="text" ></td>' +
            '                                <td><b>开发商：</b><input type="text"></td>' +
            '                                <td><b>详细地址：</b><input type="text"></td>' +
            '                            </tr>' +
            '                            <tr>' +
            '                                <td><b>发包时间：</b><input type="text"><b> 至 </b><input type="text"></td>' +
            '                                <td><b>允许报备次数：</b><input type="text"></td>' +
            '                                <td><b>佣金设置：</b><input type="text"></td>' +
            '                            </tr>' +
            '                        </table>' +
            '                        <span>' +
            '                            <button class="btn btn-primary btn-sm" style="width: 16em;height: 3em;font-size: 16px;">发布</button>' +
            '                        </span>' +
            '                    </form>' +
            '                </div>' +
            '            </div>';
        $(".main-right").html(publishHtml);
    })
}

/*项目管理_管理员*/
function projectManage() {
    $("#projectList").on('click', function () {
        var proListHtml = '<div style="background-color: #fff;height: 100%;">' +
                        '       <div class="main-right-search"> ' +
                        '           <input type="text" class="form-control" placeholder="请输入项目名称"/> ' +
                        '       </div> ' +
                        '       <div class="table-responsive"> ' +
                        '           <table id="rightTable" class="table text-nowrap"></table> ' +
                        '       </div>' +
                        '  </div>';
        $(".main-right").html(proListHtml);
        projectList();
    });
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
                    halign: "center",
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
            var userHtml = '<div style="background-color: #fff;height: 100%;padding-top: 1em;">' +
                '                <div id="searchUser" style="margin-left: 1em;height: 2em;margin-bottom: 1em;">' +
                '                    <span><input type="text" class="form-control" placeholder="请输入电话号码"/></span>' +
                '                    <span><input type="text" class="form-control" placeholder="请输入姓名"/></span>' +
                '                    <span><input type="text" class="form-control" placeholder="请输入报备时间"/></span>' +
                '                    <span style="width: 2em;line-height: 30px;margin-right: 1em;">至</span>' +
                '                    <span><input type="text" class="form-control" placeholder="请输入报备时间"/></span>' +
                '                </div>' +
                '                <div class="table-responsive">' +
                '                    <table id="rightTable" class="table text-nowrap">' +
                '                    </table>' +
                '                </div>' +
                '            </div>';
            $(".main-right").html(userHtml);
            var role = $('#userManage>li').eq(index).text();
            getUserByRole(role);
        })
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
                    halign: "center",
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
            }
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
        sortable: false,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        // pageList: [5, 10, 25, 50, 100],        //可供选择的每页的行数（*）
        smartDisplay: false,
        // search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: true,
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
    return [
        '<button type="button" class="RoleOfedit btn btn-primary  btn-sm" style="margin-right:15px;">修改</button>',
        '<button type="button" class="RoleOfdisable btn btn-primary  btn-sm" style="margin-right:15px;">禁用</button>',
        '<button type="button" class="RoleOfdelete btn btn-primary  btn-sm" style="margin-right:15px;">删除</button>'
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
        console.log(row);
        console.log(index);
        // $("#editModal").modal('show');
    },
    'click .RoleOfdisable': function (e, value, row, index) {
        console.log(row);
        console.log(index);
    },
    'click .RoleOfdelete': function (e, value, row, index) {
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
        console.log(row);
        console.log(index);
    },
    'click .ProOfdelete': function (e, value, row, index) {
        console.log(row);
        console.log(index);
    }

};



























