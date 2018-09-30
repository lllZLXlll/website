$(function () {
    $.ajax({
        type : 'post',
        url : '/admin/index/data',
        dataType : 'json',
        success : function(data) {
            if (data && data.code == '00000' && data.status == 'success') {
                $("#newsCount").text(data.data.newsCount);
                $("#noticeCount").text(data.data.noticeCount);
            } else {
                $("#loginMsg").text(data.msg)
            }
        },
        error: function (data) {
            layer.msg('刷新页面重试',{icon:2,time:2000});
        }
    })


});