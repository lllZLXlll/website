// 列表数据
var tbody = "" +
    "<tr>" +
    "   <td>data1</td>" +//id
    "   <td>data2</td>" +//user_name
    "   <td>data3</td>" +//mobile
    "   <td>data4</td>" +//address
    "   <td>data5</td>" +//currency
    "   <td>data6</td>" +//surplus
    "   <td>data7</td>" +//lock_describe
    "   <td>data10</td>" + //remarks
    "   <td>data12</td>" + //create_time
    // "   <td class='td-manage'>" +
    // "       <a title='明细' onclick='x_currency_show(\"明细\",\"data13\")' href='javascript:;'>" +
    // "           <span style='color: #007DDB;'>明细</span>" +
    // "       </a> | " +
    // "       <a title='转账' onclick=\"x_currency_show2('转账','data14')\" href='javascript:;'>" +
    // "           <span style='color: red;'>转账</span>" +
    // "       </a>" +
    // "   </td>" +
    "</tr>";
var pageNum = 1;
var pageSize = 10;

$(function () {
    queryPageData();
});

// 分页查询
function queryPageData(user_name) {
    var param = {
        pageNum: pageNum,
        pageSize: pageSize,
        user_name: user_name,
    };

    $.ajax({
        type : 'get',
        url : '/admin/currency/data',
        dataType : 'json',
        data: param,
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                if (data.data.size > 0) { // 有记录
                    // 表格
                    var tableBody = '';
                    var dataList = data.data.list;
                    for (var i = 0; i < dataList.length; i++) {
                        tableBody += tbody;
                        tableBody = tableBody.replace('data1', i + 1);
                        tableBody = tableBody.replace('data2', dataList[i].user_name);
                        tableBody = tableBody.replace('data3', dataList[i].mobile);
                        tableBody = tableBody.replace('data4', dataList[i].address);
                        tableBody = tableBody.replace('data5', dataList[i].currency);
                        tableBody = tableBody.replace('data6', dataList[i].surplus);
                        tableBody = tableBody.replace('data7', dataList[i].lock_describe);
                        tableBody = tableBody.replace('data10', dataList[i].remarks);
                        tableBody = tableBody.replace('data12', dataList[i].create_time);
                        tableBody = tableBody.replace('data13', '/admin/currency/record/' + dataList[i].id);
                        tableBody = tableBody.replace('data14', '/admin/currency/transfer/' + dataList[i].id);
                    }
                    $("#currencyTbody").html(tableBody);

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
                        getPage: function(page) {
                            //获取当前页数
                            // console.log(page);
                            pageNum = page;
                            queryPageData();
                        }
                    })
                } else {
                    var tableBody = '<tr><td colspan="14" style="text-align: center;">暂无数据</td></tr>';
                    $("#currencyTbody").html(tableBody);
                }
            } else {
                var tableBody = '<tr><td colspan="14" style="text-align: center;">查询数据异常</td></tr>';
                $("#currencyTbody").html(tableBody);
            }
        }
    })
}

// 搜索
function currencySreach() {
    var user_name = $("#user_name").val();
    queryPageData(user_name);
}

// 删除
function currency_del(id) {
    layer.confirm('确认要删除吗？',function(){
        $.ajax({
            type : 'post',
            url : '/admin/currency/del',
            dataType : 'json',
            data: {id: id},
            success : function(data) {
                if (data && data.code == '00000' && data.status == 'success') {
                    layer.msg(data.msg,{icon:1,time:2000});
                    queryPageData();
                } else {
                    layer.msg(data.msg,{icon:1,time:2000});
                }
            }
        })
    });
}

// 保存
function currencySave(field) {
    var param = {
        user_name: field.user_name,
        mobile: field.mobile,
        address: field.address,
        currency: field.currency,
        lock_describe: field.lock_describe,
        remarks: field.remarks,

    };
    $("#addto").attr("disabled",true);
    $.ajax({
        type : 'post',
        url : '/admin/currency/save',
        dataType : 'json',
        contentType: "application/json",
        data: JSON.stringify(param),
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6},function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href="/admin/currency/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg,{icon:2,time:2000});
                $("#addto").attr("disabled",false);
            }
        }
    })
}

// 转账
function recordTransfer(field) {
    // 校验参数
    var surplus = field.surplus;
    var money = field.money;

    if (!isNaN(money)) {
        if (parseFloat(money) > parseFloat(surplus)) {
            layer.msg("转账代币不能大于剩余可转账代币",{icon:2,time:3000});
            return;
        }
    } else {
        layer.msg("请输入合法的转账代币",{icon:2,time:3000});
        return;
    }

    transfer(field);
}

function transfer(field) {

    $("#editxx").attr("disabled", true); // 失效
    $.ajax({
        type : 'get',
        url : '/admin/currency/transfers/' + field.id + '/' + field.money,
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6},function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href="/admin/currency/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg,{icon:2,time:5000});
                $("#editxx").attr("disabled", false); // 生效
            }
        }
    })
}
//导入Excel文件
function uploadExcel() {

    $("#addto").attr("disabled",true);
    $.ajax({
        type : 'post',
        url : '/admin/currency/excel',
        data: new FormData($('#uploadExcel')[0]),
        processData: false,
        contentType: false,
        dataType:"json",
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6},function () {
                    queryPageData();
                    layer.closeAll();
                });
            } else {
                layer.msg(data.msg,{icon:2,time:2000});
                $("#addto").attr("disabled",false);
            }
        }
    })
}


function x_currency_show(title,url,w,h){
    if (title == null || title == '') {
        title=false;
    };
    if (url == null || url == '') {
        url="404.html";
    };
    if (w == null || w == '') {
        w=($(window).width()*0.9);
    };
    if (h == null || h == '') {
        h=($(window).height() - 50);
    };
    layer.open({
        type: 2,
        area: [w+'px', h +'px'],
        fix: false, //不固定
        maxmin: true,
        shadeClose: true,
        shade:0.4,
        user_name: user_name,
        content: url
    });
}

function x_currency_show2(title,url,w,h){
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
        user_name: user_name,
        content: url
    });
}
