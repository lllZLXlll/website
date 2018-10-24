// 列表数据
var tbody = "" +
    "<tr>" +
    "   <td>data1</td>" +
    "   <td>data2</td>" +
    "   <td>data3</td>" +
    "   <td>data4</td>" +
    "   <td>data5</td>" +
    "   <td>data6</td>" +
    "   <td class='td-manage'>" +
    "   <a title='编辑'  onclick='x_community_show(\"编辑\",\"data7\")' href='javascript:;'>" +
    "           <i class='layui-icon'>&#xe63c;</i>" +
    "       </a>" +
    "       <a title='删除' onclick=\"community_del('data8')\" href='javascript:;'>" +
    "           <i class='layui-icon'>&#xe640;</i>" +
    "       </a>" +
    "   </td>" +
    "</tr>";
var pageNum = 1;
var pageSize = 10;

$(function () {
    // queryPageData();

});

// 分页查询
function queryPageData() {
    var param = {
        pageNum: pageNum,
        pageSize: pageSize,
    };


    $.ajax({
        type: 'get',
        url: '/admin/community/data',
        dataType: 'json',
        data: param,
        success: function (data) {
            if (data && data.code == '00000' && data.status == 'success') {
                if (data.data.list.length > 0) { // 有记录
                    // 表格
                    var tableBody = '';
                    var dataList = data.data.list;
                    for (var i = 0; i < dataList.length; i++) {
                        tableBody += tbody;
                        tableBody = tableBody.replace('data1', i + 1);
                        tableBody = tableBody.replace('data2', dataList[i].english_name);
                        tableBody = tableBody.replace('data3', dataList[i].description);
                        tableBody = tableBody.replace('data4', dataList[i].link);
                        tableBody = tableBody.replace('data5', dataList[i].follow_number);
                        tableBody = tableBody.replace('data6', dataList[i].state);
                        tableBody = tableBody.replace('data7', '/admin/community/info/' + dataList[i].id);
                        tableBody = tableBody.replace('data8', dataList[i].id);
                    }
                    $("#communityTbody").html(tableBody);

                } else {
                    var tableBody = '<tr><td colspan="6" style="text-align: center;">暂无数据</td></tr>';
                    $("#communityTbody").html(tableBody);
                }
            } else {
                var tableBody = '<tr><td colspan="6" style="text-align: center;">查询数据异常</td></tr>';
                $("#communityTbody").html(tableBody);
            }
        }
    })
}

// 删除
function community_del(id) {
    layer.confirm('确认要删除吗？', function () {
        $.ajax({
            type: 'post',
            url: '/admin/community/del',
            dataType: 'json',
            data: {id: id},
            success: function (data) {
                if (data && data.code == '00000' && data.status == 'success') {
                    layer.msg(data.msg, {icon: 1, time: 1000});
                    queryPageData();
                } else {
                    layer.msg(data.msg, {icon: 1, time: 1000});
                }
            }
        })
    });
}

// 保存
function communitySave(field) {
    var param = {
        english_name: field.english_name,
        description: field.description,
        link: field.link,
        follow_number: field.follow_number,
        state: field.state,
    };


    $.ajax({
        type: 'post',
        url: '/admin/community/save',
        dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify(param),
        success: function (data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6}, function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href = "/admin/community/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg, {icon: 1, time: 1000});
            }
        }

    })
}

// 修改
function communityUpdate(field) {

    updateCommunity(field);
}

function updateCommunity(field) {
    var param = {
        id: field.id,
        english_name: field.english_name,
        description: field.description,
        link: field.link,
        follow_number: field.follow_number,
        state: field.state,
    };

    $.ajax({
        type: 'post',
        url: '/admin/community/update',
        dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify(param),
        success: function (data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6}, function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href = "/admin/community/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg, {icon: 1, time: 1000});

            }
        }
    })
}

function x_community_show(english_name, url, w, h) {
    if (english_name == null || english_name == '') {
        english_name = false;
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
        content: url,
        english_name: english_name,
    });
}