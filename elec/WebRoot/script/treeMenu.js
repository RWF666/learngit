var menu = {
	setting: {
        isSimpleData: true,
        treeNodeKey: "mid",
        treeNodeParentKey: "pid",
        showLine: true,
        root: {
            isRoot: true,
            nodes: []
        }
    },
	loadMenuTree:function(){
		//使用jquery的ajax完成对数据的加载
		$.post("elecMenuAction_showMenu.do",{},function(privilegeDate){
			$("#menuTree").zTree(menu.setting, privilegeDate);//privilegeDate数据一定是一个json的数组
		});
		
	}
};

$(document).ready(function(){
	menu.loadMenuTree();
});
