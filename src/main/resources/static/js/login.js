$(function () {
    //动态字体
    var wh = $(window).width();
    $("body").css("font-size",wh*18/1920);
    layui.use(['layer', 'form'], function () {
        var form = layui.form;
        $(document).keydown(function (event) {
            if (event.keyCode === 13) { //绑定回车
                // $('#login-btn').click();
                $('#login').click();
            }
        });
        form.render();
        $("#login").on("click",function () {
            var username =$("#username").val();
            var password =$("#password").val();
            if(username==null||username==""){
                layer.msg("账号不能为空",{icon:2})
                return false;
            }
            if(password==null||password==""){
                layer.msg("密码不能为空",{icon:2})
                return false;
            }
            $.post("/backApp/userLogin",{username:username,password:password},function (res) {
               if(res.status==200){
                 var path = window.location.href.replace("login","index");
                   window.location.href =path;
               }else{
                   layer.msg("用户名或密码错误",{icon:2});
               }
            })
        })
    })
});