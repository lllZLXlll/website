/* 获取指定cookie */
function getCookie(name) {
    var strCookie = document.cookie;
    var arrCookie = strCookie.split("; ");
    for (var i = 0; i < arrCookie.length; i++) {
        var arr = arrCookie[i].split("=");
        if (arr[0] == name)
            return arr[1];
    }
    return "";
}

// 设置首页管理员名称
function setAdminName() {
    $("#admin_name").text("欢迎您，管理员 " + getCookie("admin_name"));
}

$(function () {

    $("#loginSub").click(function () {
        var username = $("#username").val();
        var password = $("#password").val();

        if (username == '') {
            $("#loginMsg").text('账号不能为空')
            return;
        }
        if (password == '') {
            $("#loginMsg").text('密码不能为空')
            return;
        }

        password = hex_md5(password);

        // console.log(password)

        var data = {
            username: username,
            password: password
        };

        login(data);

    });

    function login(data) {
        $.ajax({
            type: 'post',
            url: '/admin/login/to',
            dataType: 'json',
            data: data,
            success: function (data) {
                if (data && data.code == '00000' && data.status == 'success') {
                    // 登录成功，保存token到浏览器中
                    // document.cookie = "";
                    // saveCookie("token", data.data.token);
                    // saveCookie("admin_name", data.data.admin_name);
                    // 访问管理后台首页
                    goToIndex();
                } else {
                    $("#loginMsg").text(data.msg)
                }
            },
            fail: function () {
                $("#loginMsg").text("登录失败")
            }
        })
    }

    function saveCookie(key, value) {
        document.cookie = key + "=" + value;
    }

    function goToIndex() {
        $.ajax({
            type: 'post',
            url: '/admin/index/init',
            dataType: 'json',
            success: function (data) {
                if (data && data.code == '00000' && data.status == 'success') {
                    // 跳转页面
                    window.location.href = "/admin/index";
                } else {
                    layer.msg(data.msg,{icon:2,time:1000});
                }
            },
            error: function (data) {
                layer.msg('刷新页面重试',{icon:2,time:2000});
            }
        })
    }


});

function loginOut() {
    $.ajax({
        type: 'get',
        url: '/admin/login/out',
        dataType: 'json',
        success: function (data) {
            if (data && data.code == '00000' && data.status == 'success') {
                document.cookie = '';
                // 跳转页面
                window.location.href = "/admin/login";
            } else {
                layer.msg(data.msg,{icon:2,time:1000});
            }
        },
        error: function (data) {
            layer.msg('刷新页面重试',{icon:2,time:2000});
        }
    })
}
