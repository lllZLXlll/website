// 列表数据
var tbody = "" +
    "<tr>" +
    "   <td>data1</td>" +
    "   <td>data2</td>" +
    "   <td>data3</td>" +
    "   <td>data4</td>" +
    "   <td>data5</td>" +
    "   <td class='td-manage'>" +
    "       <a title='编辑'  onclick='x_news_show(\"编辑\",\"data6\")' href='javascript:;'>" +
    "           <i class='layui-icon'>&#xe63c;</i>" +
    "       </a>" +
    "   </td>" +
    "</tr>";

var pageNum = 1;
var pageSize = 10;

$(function () {
    queryPageData();

});

// 分页查询
function queryPageData(username, mobile) {
    var param = {
        pageNum: pageNum,
        pageSize: pageSize,
        username: username,
        mobile: mobile,
    };

    $.ajax({
        type: 'get',
        url: '/admin/authority/user/data',
        dataType: 'json',
        data: param,
        success: function (data) {
            if (data && data.code == '00000' && data.status == 'success') {
                if (data.data.size > 0) { // 有记录
                    // 表格
                    var tableBody = '';
                    var dataList = data.data.list;

                    for (var i = 0; i < dataList.length; i++) {
                        var state = dataList[i].state;

                        tableBody += tbody;
                        tableBody = tableBody.replace('data1', i + 1);
                        tableBody = tableBody.replace('data2', dataList[i].username);
                        tableBody = tableBody.replace('data3', dataList[i].mobile);
                        tableBody = tableBody.replace('data4', state == 1 ? '启用' : '禁用');
                        tableBody = tableBody.replace('data5', dataList[i].create_time);
                        tableBody = tableBody.replace('data6', '/admin/authority/user/info/' + dataList[i].id);
                    }
                    $("#userTbody").html(tableBody);

                    // 分页组件
                    new myPagination({
                        id: 'pagination',
                        curPage: data.data.pageNum, //初始页码
                        pageTotal: data.data.lastPage, //总页数
                        pageAmount: data.data.pageSize, //每页多少条
                        dataTotal: data.data.total, //总共多少条数据
                        pageSize: 5, //可选,分页个数
                        showPageTotalFlag: true, //是否显示数据统计
                        showSkipInputFlag: true, //是否支持跳转
                        getPage: function (page) {
                            //获取当前页数
                            // console.log(page);
                            pageNum = page;
                            queryPageData();
                        }
                    })
                } else {
                    var tableBody = '<tr><td colspan="6" style="text-align: center;">暂无数据</td></tr>';
                    $("#userTbody").html(tableBody);
                }
            } else {
                var tableBody = '<tr><td colspan="6" style="text-align: center;">查询数据异常</td></tr>';
                $("#userTbody").html(tableBody);
            }
        }
    })
}

// 搜索
function userSreach() {
    var username = $("#user-username").val();
    var mobile = $("#user-mobile").val();
    queryPageData(username, mobile);
}


// 保存
function userSave(field) {

    if (field.username.length < 4) {
        layer.msg("用户名不能少于4位", {icon: 2, time: 2000});
        return;
    }
    if (isNaN(field.mobile)) {
        layer.msg("手机号码不正确", {icon: 2, time: 2000});
        return;
    }
    if (field.mobile.length != 11) {
        layer.msg("手机号码不正确", {icon: 2, time: 2000});
        return;
    }
    if (field.password.length < 6) {
        layer.msg("密码不能少于6位", {icon: 2, time: 2000});
        return;
    }

    var param = {
        username: field.username,
        mobile: field.mobile,
        password: hex_md5(field.password),
        roleId: field.roleId,
        state: field.state,
    };

    $("#addUserBtn").attr("disabled", true);
    $.ajax({
        type: 'post',
        url: '/admin/authority/user/save',
        dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify(param),
        success: function (data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6}, function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href = "/admin/authority/user/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg, {icon: 2, time: 1000});
                $("#addUserBtn").attr("disabled", false);
            }
        },
        error: function () {
            $("#addUserBtn").attr("disabled", false);
        }

    })
}


function userEdit(field) {
    if (field.username.length < 4) {
        layer.msg("用户名不能少于4位", {icon: 2, time: 2000});
        return;
    }
    if (isNaN(field.mobile)) {
        layer.msg("手机号码不正确", {icon: 2, time: 2000});
        return;
    }
    if (field.mobile.length != 11) {
        layer.msg("手机号码不正确", {icon: 2, time: 2000});
        return;
    }

    var param = {
        id: field.id,
        username: field.username,
        mobile: field.mobile,
        roleId: field.roleId,
        state: field.state,
    };

    $("#editUserBtn").attr("disabled", true);
    $.ajax({
        type: 'post',
        url: '/admin/authority/user/update',
        dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify(param),
        success: function (data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6}, function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href = "/admin/authority/user/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg, {icon: 2, time: 1000});
                $("#editUserBtn").attr("disabled", false);
            }
        },
        error: function () {
            $("#editUserBtn").attr("disabled", false);
        }
    })
}

function x_news_show(title, url, w, h) {
    if (title == null || title == '') {
        title = false;
    }
    ;
    if (url == null || url == '') {
        url = "404.html";
    }
    ;
    if (w == null || w == '') {
        w = ($(window).width() * 0.5);
    }
    ;
    if (h == null || h == '') {
        h = ($(window).height() - 150);
    }
    ;
    layer.open({
        type: 2,
        area: [w + 'px', h + 'px'],
        fix: false, //不固定
        maxmin: true,
        shadeClose: true,
        shade: 0.4,
        title: title,
        content: url
    });
}