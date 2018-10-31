// 列表数据
var tbody = "" +
    "<tr>" +
    "   <td>data1</td>" +//id
    "   <td>data2</td>" +//from
    "   <td>data3</td>" +//to
    "   <td>data10</td>" +//tx_address
    "   <td>data4</td>" +//currency
    "   <td>data5</td>" +//periods
    "   <td>data6</td>" +//state
    "   <td>data7</td>" +//describe
    "   <td>data8</td>" + //admin
    "   <td>data9</td>" + //time
    "</tr>";
var pageNum = 1;
var pageSize = 10;
var dataId;

$(function () {
    // queryPageData();
});

// 分页查询
function queryPageData(id) {
    dataId = id;
    var param = {
        pageNum: pageNum,
        pageSize: pageSize,
        id: dataId,
    };

    $.ajax({
        type : 'get',
        url : '/admin/currency/record/data',
        dataType : 'json',
        data: param,
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                if (data.data.size > 0) { // 有记录
                    // 表格
                    var tableBody = '';
                    var dataList = data.data.list;
                    for (var i = 0; i < dataList.length; i++) {
                        var state = dataList[i].state == 1 ? '成功' : '失败';
                        tableBody += tbody;
                        tableBody = tableBody.replace('data1', i + 1);
                        tableBody = tableBody.replace('data2', dataList[i].from);
                        tableBody = tableBody.replace('data3', dataList[i].to);
                        tableBody = tableBody.replace('data10', dataList[i].txAddress);
                        tableBody = tableBody.replace('data4', dataList[i].currency);
                        tableBody = tableBody.replace('data5', dataList[i].periods);
                        tableBody = tableBody.replace('data6', state);
                        tableBody = tableBody.replace('data7', dataList[i].describe);
                        tableBody = tableBody.replace('data8', dataList[i].admin);
                        tableBody = tableBody.replace('data9', dataList[i].time);
                    }
                    $("#currencyRecordTbody").html(tableBody);

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
                            queryPageData(dataId);
                        }
                    })
                } else {
                    var tableBody = '<tr><td colspan="10" style="text-align: center;">暂无数据</td></tr>';
                    $("#currencyRecordTbody").html(tableBody);
                }
            } else {
                var tableBody = '<tr><td colspan="10" style="text-align: center;">查询数据异常</td></tr>';
                $("#currencyRecordTbody").html(tableBody);
            }
        }
    })
}

