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
		//ʹ��jquery��ajax��ɶ����ݵļ���
		$.post("elecMenuAction_showMenu.do",{},function(privilegeDate){
			$("#menuTree").zTree(menu.setting, privilegeDate);//privilegeDate����һ����һ��json������
		});
		
	}
};

$(document).ready(function(){
	menu.loadMenuTree();
});
