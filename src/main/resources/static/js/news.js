// 列表数据
var tbody = "" +
    "<tr>" +
    "   <td>data1</td>" +
    "   <td>data2</td>" +
    "   <td>data3</td>" +
    "   <td>data4</td>" +
    "   <td><img src='data5' height='50px' width='50px' /></td>" +
    "   <td><a href='data6' target='_blank'>原文</a></td>" +
    "   <td>data7</td>" +
    "   <td>data8</td>" +
    "   <td>data9</td>" +
    "   <td class='td-manage'>" +
    "       <a title='编辑'  onclick='x_news_show(\"编辑\",\"data10\")' href='javascript:;'>" +
    "           <i class='layui-icon'>&#xe63c;</i>" +
    "       </a>" +
    "       <a title='删除' onclick=\"news_del('data11')\" href='javascript:;'>" +
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
function queryPageData(title,lang) {
    var param = {
        pageNum: pageNum,
        pageSize: pageSize,
        title: title,
        lang: lang,
    };

    $.ajax({
        type : 'get',
        url : '/admin/news/data',
        dataType : 'json',
        data: param,
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                if (data.data.size > 0) { // 有记录
                    // 表格
                    var tableBody = '';
                    var dataList = data.data.list;

                    for (var i = 0; i < dataList.length; i++) {
                        var lang = dataList[i].lang;
                        var state = dataList[i].state;

                        tableBody += tbody;
                        tableBody = tableBody.replace('data1', i + 1);
                        tableBody = tableBody.replace('data2', dataList[i].title);
                        tableBody = tableBody.replace('data3', dataList[i].content);
                        tableBody = tableBody.replace('data4', dataList[i].time);
                        tableBody = tableBody.replace('data5', dataList[i].icon);
                        tableBody = tableBody.replace('data6', dataList[i].url);
                        tableBody = tableBody.replace('data7', dataList[i].create_time);
                        tableBody = tableBody.replace('data8', state == 1 ? '展示' : '隐藏');
                        tableBody = tableBody.replace('data9', lang == 1 ? '英文' : '中文');
                        tableBody = tableBody.replace('data10', '/admin/news/info/' + dataList[i].id);
                        tableBody = tableBody.replace('data11', dataList[i].id);
                    }
                    $("#newsTbody").html(tableBody);

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
                    var tableBody = '<tr><td colspan="11" style="text-align: center;">暂无数据</td></tr>';
                    $("#newsTbody").html(tableBody);
                }
            } else {
                var tableBody = '<tr><td colspan="11" style="text-align: center;">查询数据异常</td></tr>';
                $("#newsTbody").html(tableBody);
            }
        }
    })
}

// 搜索
function newsSreach() {
    var title = $("#newsTitle").val();
    var lang = $("#newsLang").val();
    queryPageData(title,lang);
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
        title: field.title,
        content: field.content,
        timeInsert: field.timeInsert,
        icon: field.icon,
        url: field.url,
        // create_time: field.create_time,
        lang: field.lang,
        state: field.state,
    };

    $("#addto").attr("disabled",true);
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
                $("#addto").attr("disabled",false);
            }
        }

    })
 //   $("#addNews").attr(disabled,true);
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
function newsUpdate(field) {
    updateNews(field);
 /*   // 校验参数，地址是否合法
    if (CheckUrl(field.url)) {

    } else {
        layer.msg("原文地址不正确",{icon:2,time:1000});
    }*/
}

function updateNews(field) {
    var param = {
        id: field.id,
        title: field.title,
        content: field.content,
        timeInsert: field.timeInsert,
        icon: field.icon,
        url: field.url,
        lang: field.lang,
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