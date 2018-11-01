// 列表数据
var tbody = "" +
    "<tr>" +
    "   <td>data1</td>" +
    "   <td>data2</td>" +
    "   <td>data3</td>" +
    "   <td><img src='data4' height='50px' width='100px' /></td>" +
    "   <td>data5</td>" +
    "   <td>data6</td>" +
    "   <td>data7</td>" +
    "   <td class='td-manage'>" +
    "       <a title='编辑'  onclick='x_partner_show(\"编辑\",\"data8\")' href='javascript:;'>" +
    "           <i class='layui-icon'>&#xe63c;</i>" +
    "       </a>" +
    "       <a title='删除' onclick=\"partner_del('data9')\" href='javascript:;'>" +
    "           <i class='layui-icon'>&#xe640;</i>" +
    "       </a>" +
    "   </td>" +
    "</tr>";

var pageNum = 1;
var pageSize = 10;


// 分页查询
function queryPageData(partner_name) {
    var param = {
        pageNum: pageNum,
        pageSize: pageSize,
        partner_name: partner_name,
    };

    $.ajax({
        type : 'get',
        url : '/admin/partner/data',
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
                        tableBody += tbody;
                        tableBody = tableBody.replace('data1', i + 1);
                        tableBody = tableBody.replace('data2', dataList[i].number);
                        tableBody = tableBody.replace('data3', dataList[i].partner_name);
                        tableBody = tableBody.replace('data4', dataList[i].picture);
                        tableBody = tableBody.replace('data5', dataList[i].link);
                        tableBody = tableBody.replace('data7', dataList[i].create_time);
                        tableBody = tableBody.replace('data6', state == 1 ? '展示' : '隐藏');
                        tableBody = tableBody.replace('data8', '/admin/partner/info/' + dataList[i].id);
                        tableBody = tableBody.replace('data9', dataList[i].id);
                    }
                    $("#partnerTbody").html(tableBody);

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
                    var tableBody = '<tr><td colspan="9" style="text-align: center;">暂无数据</td></tr>';
                    $("#partnerTbody").html(tableBody);
                }
            } else {
                var tableBody = '<tr><td colspan="9" style="text-align: center;">查询数据异常</td></tr>';
                $("#partnerTbody").html(tableBody);
            }
        }
    })
}

// 搜索
function partnerSreach() {
    var partner_name = $("#partnerTitle").val();
    queryPageData(partner_name);
}

// 删除
function partner_del(id) {
    layer.confirm('确认要删除吗？',function(){
        $.ajax({
            type : 'post',
            url : '/admin/partner/del',
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

// 保存
function partnerSave(field) {

    //保存
    $("#addto").attr("disabled",true);
    $.ajax({
        type : 'post',
        url : '/admin/partner/save',
        data: new FormData($('#myFormElement')[0]),
        processData: false,
        contentType: false,
        dataType:"json",
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6},function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href="/admin/partner/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg,{icon:1,time:1000});
                $("#addto").attr("disabled",false);
            }
        }
    })

}

// 修改
function partnerUpdate(field) {
    updatePartner(field);
}

function updatePartner(field) {

    //修改
    $.ajax({
        type : 'post',
        url : '/admin/partner/update',
        data: new FormData($('#myFormElement')[0]),
        processData: false,
        contentType: false,
        dataType:"json",
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6},function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href="/admin/partner/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg,{icon:1,time:1000});
            }
        }
    })
}

function x_partner_show(title,url,w,h){
    if (title == null ||  title == '') {
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