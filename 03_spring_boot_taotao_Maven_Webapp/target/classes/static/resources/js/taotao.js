var TT = TAOTAO = {
	checkLogin : function(){
		var _ticket = $.cookie("TAOTAO_TOKEN");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://weige.taotao.com/rest/customer/user/query/"+ _ticket,
			dataType : "json",
			type : "GET",
			success : function(_data){
				var html =_data.username+"，欢迎来到淘淘！<a href=\"http://weige.taotao.com/rest/customer/user/logout\" class=\"link-logout\">[退出]</a>";
				$("#loginbar").html(html);
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});