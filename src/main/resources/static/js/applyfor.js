// 列表数据
var tbody = "" +
    "<tr>" +
    "   <td>data1</td>" +
    "   <td>data2</td>" +
    "   <td>data3</td>" +
    "   <td>data4</td>" +
    "   <td>data5</td>" +
    "   <td>data6</td>" +
    "   <td>data9</td>" +
    "   <td>data7</td>" +
    "   [data8]" +
    "</tr>";
var pageNum = 1;
var pageSize = 10;

$(function () {
    queryPageData();
});

// 分页查询
function queryPageData() {
    var param = {
        pageNum: pageNum,
        pageSize: pageSize,
    };

    $.ajax({
        type: 'get',
        url: '/admin/applyfor/data',
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
                        tableBody = tableBody.replace('data3', dataList[i].address);
                        tableBody = tableBody.replace('data4', dataList[i].money);
                        tableBody = tableBody.replace('data5', dataList[i].currency);
                        tableBody = tableBody.replace('data6', dataList[i].time);
                        tableBody = tableBody.replace('data9', dataList[i].confirm_time);
                        tableBody = tableBody.replace('data7', state == 0 ? "申请提现中" : "提现成功");

                        // 如果没有提现过才需要提现的按钮
                        if (state == 0) {
                            var data8 = "   <td class='td-manage'>" +
                                "       <a title='提现' onclick=\"applyfor('data8')\" href='javascript:;'>" +
                                "           <i class='layui-icon'><span style='color: red'>提现</span></i>" +
                                "       </a>" +
                                "   </td>";

                            tableBody = tableBody.replace('[data8]', data8);
                            tableBody = tableBody.replace('data8', dataList[i].id);
                        } else {
                            tableBody = tableBody.replace('[data8]', "<td></td>");
                        }
                    }
                    $("#applyforTbody").html(tableBody);

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
                    var tableBody = '<tr><td colspan="9" style="text-align: center;">暂无数据</td></tr>';
                    $("#applyforTbody").html(tableBody);
                }
            } else {
                var tableBody = '<tr><td colspan="9" style="text-align: center;">查询数据异常</td></tr>';
                $("#applyforTbody").html(tableBody);
            }
        }
    })
}

// 财务确认以提现
function applyfor(id) {
    layer.confirm('确认已经给用户提现了吗？', function () {
        $.ajax({
            type: 'post',
            url: '/admin/applyfor/submit',
            dataType: 'json',
            data: {id: id},
            success: function (data) {
                if (data && data.code == '00000' && data.status == 'success') {
                    layer.msg(data.msg, {icon: 1, time: 2000});
                    queryPageData();
                } else {
                    layer.msg(data.msg, {icon: 2, time: 2000});
                }
            }
        })
    });
}