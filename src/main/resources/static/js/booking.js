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
    "   <td>data8</td>" +
    "   <td>data9</td>" +
    "   <td>data10</td>" +
    "   <td>data11</td>" +
    "   <td>data12</td>" +
    "   <td>data13</td>" +
    "   <td>data14</td>" +
    "   <td>data15</td>" +
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
        url : '/admin/booking/data',
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
                        var state = dataList[i].state;//状态
                        var investment = dataList[i].investment;
                        var currency = dataList[i].currency;//投资货币(1.BTC 2.ETH 3.TUSD)
                         // if(currency==1){
                         //     currency="BTC";
                         // }else if (currency==2){
                         //     currency="ETH";
                         // }else if (currency==3){
                         //     currency="TUSD";
                         // }
                        //
                        var park_eco = dataList[i].park_eco;
                        // if(park_eco==1){
                        //      park_eco="口口相传";
                        //  }else if (park_eco==2){
                        //      park_eco="电报";
                        //  }else if (park_eco==3){
                        //      park_eco="媒体出版物";
                        //  }else if (park_eco==4){
                        //      park_eco="互联网";
                        //  }else if (park_eco==5){
                        //      park_eco="一次会议";
                        //  }else if (park_eco==6){
                        //      park_eco="我们目前投资者之一";
                        //  }else if (park_eco==7){
                        //     park_eco="其他";
                        //  }

                        tableBody = tableBody.replace('data1', i + 1);
                        tableBody = tableBody.replace('data2', dataList[i].user_name);
                        tableBody = tableBody.replace('data3', dataList[i].sur_name);
                        tableBody = tableBody.replace('data4', dataList[i].mobile);
                        tableBody = tableBody.replace('data5', dataList[i].email);
                        tableBody = tableBody.replace('data6', dataList[i].address);
                        tableBody = tableBody.replace('data7', park_eco);
                        tableBody = tableBody.replace('data8', dataList[i].country);
                        tableBody = tableBody.replace('data9', investment);
                        tableBody = tableBody.replace('data10', dataList[i].dollar);
                        tableBody = tableBody.replace('data11', currency);  //currency
                        tableBody = tableBody.replace('data12', dataList[i].account);
                        tableBody = tableBody.replace('data13',  state == 1 ? '有效' : '无效');
                        tableBody = tableBody.replace('data14', dataList[i].feedback);
                        tableBody = tableBody.replace('data15', dataList[i].create_time);

                    }
                    $("#bookingTbody").html(tableBody);

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
                    var tableBody = '<tr><td colspan="15" style="text-align: center;">暂无数据</td></tr>';
                    $("#bookingTbody").html(tableBody);
                }
            } else {
                var tableBody = '<tr><td colspan="15" style="text-align: center;">查询数据异常</td></tr>';
                $("#bookingTbody").html(tableBody);
            }
        }
    })
}

// 搜索
function bookingSreach() {
    var user_name = $("#user_name").val();
    queryPageData(user_name);
}

// 删除
function news_del(id) {
    layer.confirm('确认要删除吗？',function(){
        $.ajax({
            type : 'post',
            url : '/admin/news/del',
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
function newsSave(field) {
    var param = {
        user_name: field.user_name,
        sur_name: field.sur_name,
        mobile: field.mobile,
        email: field.email,
        address: field.address,
         create_time: field.create_time,
        investment: field.investment,
        dollar: field.dollar,
        currency: field.currency,
        account: field.account,
        country: field.country,
        park_eco: field.park_eco,
        feedback: field.feedback,
    };

    $.ajax({
        type : 'post',
        url : '/admin/news/save',
        dataType : 'json',
        contentType: "application/json",
        data: JSON.stringify(param),
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6},function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href="/admin/news/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg,{icon:1,time:1000});
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

function downloadfile(){
    window.location.href="/admin/booking/excel";
}

// 修改
function newsUpdate(field) {
    // 校验参数，地址是否合法
    if (CheckUrl(field.url)) {
        if (CheckUrl(field.icon)) {
            updateNews(field);
        } else {
            layer.msg("图片地址不正确",{icon:2,time:1000});
        }
    } else {
        layer.msg("原文地址不正确",{icon:2,time:1000});
    }
}

function updateNews(field) {
    var param = {
        id: field.id,
        title: field.title,
        content: field.content,
        timeInsert: field.timeInsert,
        icon: field.icon,
        url: field.url,
        // create_time: field.create_time,
        state: field.state,

    };

    $.ajax({
        type : 'post',
        url : '/admin/news/update',
        dataType : 'json',
        contentType: "application/json",
        data: JSON.stringify(param),
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                layer.alert(data.msg, {icon: 6},function () {
                    x_admin_close(); // 关闭当前frame
                    parent.window.location.href="/admin/news/list"; // 重新载入刷新数据
                });
            } else {
                layer.msg(data.msg,{icon:1,time:1000});
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

