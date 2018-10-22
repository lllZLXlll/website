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
    // queryPageData();

});

// 分页查询
function queryPageData(title) {
    var param = {
        pageNum: pageNum,
        pageSize: pageSize,
        title: title,
    };

    $.ajax({
        type : 'get',
        url : '/admin/community/data',
        dataType : 'json',
        data: param,
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                if (data.data.length > 0) { // 有记录
                    // 表格
                    var tableBody = '';
                    var dataList = data.data;
                    for (var i = 0; i < dataList.length; i++) {
                        tableBody += tbody;
                        tableBody = tableBody.replace('data1', i + 1);
                        tableBody = tableBody.replace('data2', dataList[i].count1);
                        tableBody = tableBody.replace('data3', dataList[i].count2);
                        tableBody = tableBody.replace('data4', dataList[i].count3);
                        tableBody = tableBody.replace('data5', dataList[i].count4);
                        tableBody = tableBody.replace('data6', '/admin/community/info/' + dataList[i].id);
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


// 修改
function communityUpdate(field) {
    // 校验参数，地址是否合法
    if (isNaN(field.count1) || isNaN(field.count2) || isNaN(field.count3) || isNaN(field.count4)) {
        layer.msg("请输入数字",{icon:2,time:1000});
        return;
    }
    updateCommunity(field);
}

function updateCommunity(field) {
    var param = {
        id: field.id,
        count1: field.count1,
        count2: field.count2,
        count3: field.count3,
        count4: field.count4,
    };
    $("#edit").attr("disabled",true);
    $.ajax({
        type : 'post',
        url : '/admin/community/update',
        dataType : 'json',
        contentType: "application/json",
        data: JSON.stringify(param),
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6},function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href="/admin/community/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg,{icon:1,time:1000});
                $("#edit").attr("disabled",false);
            }
        }
    })
}

function x_news_show(title,url,w,h){
    if (title == null || title == '') {
        title=false;
    };
    if (url == null || url == '') {
        url="404.html";
    };
    if (w == null || w == '') {
        w=($(window).width()*0.5);
    };
    if (h == null || h == '') {
        h=($(window).height() - 150);
    };
    layer.open({
        type: 2,
        area: [w+'px', h +'px'],
        fix: false, //不固定
        maxmin: true,
        shadeClose: true,
        shade:0.4,
        title: title,
        content: url
    });
}