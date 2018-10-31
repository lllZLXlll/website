// 列表数据
var tbody = "" +
    "<tr>" +
    "   <td>data1</td>" +
    "   <td>data2</td>" +
    "   <td>data3</td>" +
    "   <td>data4</td>" +
    "   <td>data5</td>" +
    "   <td>data6</td>" +
    "   <td>data7</td>" +
    "</tr>";

var pageNum = 1;
var pageSize = 10;

$(function () {
    // queryPageData();

});

// 分页查询
function queryPageData(admin_name) {
    var param = {
        pageNum: pageNum,
        pageSize: pageSize,
        admin_name: admin_name,
    };

    $.ajax({
        type : 'get',
        url : '/admin/operation/data',
        dataType : 'json',
        data: param,
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                if (data.data.size > 0) { // 有记录
                    // 表格
                    var tableBody = '';
                    var dataList = data.data.list;
                    for (var i = 0; i < dataList.length; i++) {
                        var state = dataList[i].state;
                        var operation_type = dataList[i].operation_type;
                        var money = dataList[i].money;
                        if (operation_type==1) {
                            operation_type = "登录";
                        }else if(operation_type==2){
                            operation_type = "退出";
                        }else if(operation_type==3){
                            operation_type = "导入数据";
                        }else if(operation_type==4){
                            operation_type = "转账";
                        }else if(operation_type==5){
                            operation_type = "确认转账";
                        }
                        tableBody += tbody;
                        tableBody = tableBody.replace('data1', i + 1);
                        tableBody = tableBody.replace('data2', dataList[i].admin_name);
                        tableBody = tableBody.replace('data3', operation_type);
                        tableBody = tableBody.replace('data4', dataList[i].create_time);
                        tableBody = tableBody.replace('data5', state == 1 ? '成功' : '失败');
                        tableBody = tableBody.replace('data6', money == 0 ? "" : money);
                        tableBody = tableBody.replace('data7', dataList[i].address);

                    }
                    $("#operationTbody").html(tableBody);

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
                    var tableBody = '<tr><td colspan="7" style="text-align: center;">暂无数据</td></tr>';
                    $("#operationTbody").html(tableBody);
                }
            } else {
                var tableBody = '<tr><td colspan="7" style="text-align: center;">查询数据异常</td></tr>';
                $("#operationTbody").html(tableBody);
            }
        }
    })
}

// 搜索
function operationSreach() {
    var admin_name = $("#admin_name").val();
    queryPageData(admin_name);
}

// 删除
function operation_del(id) {
    layer.confirm('确认要删除吗？',function(){
        $.ajax({
            type : 'post',
            url : '/admin/operation/del',
            dataType : 'json',
            data: {id: id},
            success : function(data) {
                if (data && data.code == '00000' && data.status == 'success') {
                    layer.msg(data.msg,{icon:1,time:1000});
                    queryPageData();
                } else {
                    layer.msg(data.msg,{icon:1,time:1000});
                }
            }
        })
    });
}

function operationSave(field) {
// 保存
    var param = {
        admin_name: field.admin_name,
        operation_type: field.operation_type,
        money: field.money,
        address: field.address,
        create_time: field.create_time,
        state: field.state,
    };
    $("#addto").attr("disabled",true);
    $.ajax({
        type : 'post',
        url : '/admin/operation/save',
        dataType : 'json',
        contentType: "application/json",
        data: JSON.stringify(param),
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6},function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href="/admin/operation/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg,{icon:1,time:1000});
                $("#addto").attr("disabled",false);
            }
        }
    })
}

function CheckUrl(str) {
    var strRegex = '^((https|http|ftp|rtsp|mms)?://)'
        + '?(([0-9a-z_!~*\'().&=+$%-]+: )?[0-9a-z_!~*\'().&=+$%-]+@)?' //ftp的user@
        + '(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184
        + '|' // 允许IP和DOMAIN（域名）
        + '([0-9a-z_!~*\'()-]+.)*' // 域名- www.
        + '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名
        + '[a-z]{2,6})' // first level domain- .com or .museum
        + '(:[0-9]{1,4})?' // 端口- :80
        + '((/?)|' // a slash isn't required if there is no file name
        + '(/[0-9a-z_!~*\'().;?:@&=+$,%#-]+)+/?)$';
    var re=new RegExp(strRegex);
    if (re.test(str)) {
        return (true);
    } else {
        return (false);
    }
}

// 修改
function operationUpdate(field) {
    updateOperation(field);

}

function updateOperation(field) {
    var param = {
        id: field.id,
        admin_name: field.admin_name,
        operation_type: field.operation_type,
        money: field.money,
        address: field.address,
        create_time: field.create_time,
        state: field.state,
    };

    $("#edit").attr("disabled",true);
    $.ajax({
        type : 'post',
        url : '/admin/operation/update',
        dataType : 'json',
        contentType: "application/json",
        data: JSON.stringify(param),
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6},function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href="/admin/operation/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg,{icon:1,time:1000});
                $("#edit").attr("disabled",false);
            }
        }
    })
}

function x_operation_show(title,url,w,h){
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