$(function () {

    initHtmlCss();

    initLeftUtil();

    monitorWindowView();
});

/*页面样式*/
function initHtmlCss(){
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
}

/** 窗口大小resize监听 */
function monitorWindowView(){
    $(window).resize(function () {
        $('#rightTable').bootstrapTable('resetView');
    });
}

/*左边菜单*/
function initLeftUtil(){
    projectManage();
    userManage();
}

/*项目管理*/
function projectManage(){
    var fields = {
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
    $("#projectList").on('click',function () {
        $.get('/backApp/project/getAllProject',function (res) {
            if (res.status == 200){
                var columns = [];
                for (var attr in res.data[0]){
                    if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1 || attr.indexOf("desc") > -1 || attr == "keyword" || attr == "address" || attr == "remark"){
                        continue;
                    }
                    var column = {field: attr,title: fields[attr],valign: "middle",halign: "center",visible: true,formatter:paramsMatter};
                    columns.push(column);
                }
                columns.push({
                    field: 'operate',
                    title: '操作',
                    valign: "middle",
                    align: 'center',
                    width: '250',
                    events: projectOperateEvents,
                    formatter: projectOperateFormatter
                });
                initTable(columns,res.data);
            }
        });
    });
    $("#projectAdd").on('click',function () {
        $.get('/backApp/image/getImage',function (res) {
            if (res.status == 200){
                // var img = $('<img src="data:image/jpeg;base64,'+ res.data[0].content +'">');
                $("#pic").attr("src","data:image/jpeg;base64,"+ res.data[0].content );
            }
        })
    });
}

function paramsMatter(value, row, index) {
    return "<span title="+value+">"+value+"</span>";
}

/*人员管理*/
function userManage(){
    var fields = {
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
    $('#userManage>li').each(function(index){
        $(this).on('click',function(){
            var role = $('#userManage>li').eq(index).text();
            $.get('/backApp/user/getUserByRole',{role:role},function (res) {
                if (res.status == 200){
                    var columns = [];
                    for (var attr in res.data[0]){
                        if (attr.indexOf("gid") > -1 || attr.indexOf("Id") > -1){
                            continue;
                        }
                        var titles;
                        if (role == "客户"){
                            titles = fields.customer;
                        }else {
                            titles = fields.user;
                        }
                        var column = {field: attr,title: titles[attr],valign: "middle",halign: "center",visible: true};
                        columns.push(column);
                    }
                    if (role != "客户"){
                        columns.push({
                            field: 'operate',
                            title: '操作',
                            valign: "middle",
                            align: 'center',
                            visible: false,
                            events: userOperateEvents,
                            formatter: userOperateFormatter
                        });
                    }
                    initTable(columns,res.data);
                }
            })
        })
    })
}

/*建表*/
function initTable(columns,data) {
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
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 5,                       //每页的记录行数（*）
        pageList: [5,10, 25, 50, 100],        //可供选择的每页的行数（*）
        smartDisplay:false,
        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: true,
        showColumns: true,                  //是否显示所有的列
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        height: 'auto',                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
        showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
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
        '<button type="button" class="ProOfwatch btn btn-primary  btn-sm" style="margin-right:15px;">查看资料</button>',
        '<button type="button" class="ProOfedit btn btn-primary  btn-sm" style="margin-right:15px;">修改</button>',
        '<button type="button" class="ProOfdelete btn btn-primary  btn-sm" style="margin-right:15px;">删除</button>'
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



























