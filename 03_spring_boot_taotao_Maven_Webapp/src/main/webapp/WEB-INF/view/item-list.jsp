<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemList" title="商品列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/rest/item',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">商品ID</th>
            <th data-options="field:'title',width:200">商品标题</th>
            <th data-options="field:'cid',width:100">叶子类目</th>
            <th data-options="field:'sellPoint',width:100">卖点</th>
            <th data-options="field:'price',width:70,align:'right',formatter:TAOTAO.formatPrice">价格</th>
            <th data-options="field:'num',width:70,align:'right'">库存数量</th>
            <th data-options="field:'barcode',width:100">条形码</th>
            <th data-options="field:'status',width:60,align:'center',formatter:TAOTAO.formatItemStatus">状态</th>
            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>
<div id="itemEditWindow" class="easyui-window" title="编辑商品" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/rest/page/item-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<script>
    function getSelectionsIds(){
    	var itemList = $("#itemList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolbar = [{
        text:'新增',
        iconCls:'icon-add',
        handler:function(){
        	$(".tree-title:contains('新增商品')").parent().click();
        }
    },{
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个商品才能编辑!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个商品!');
        		return ;
        	}
        	
        	$("#itemEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#itemList").datagrid("getSelections")[0];
        			
        			//将表单中的内容回显
        			data.priceView = TAOTAO.formatPrice(data.price);
        			
        			// 加载类目名称
        			$.getJSON('/rest/item/cat/'+data.id,function(_data){
        				data.catName =_data.name;
        				TAOTAO.init({
            				"pics" : data.image,
            				"cid" : data.cid,
            				"catName": data.catName,
            				fun:function(node){
            					TAOTAO.changeItemParam(node, "itemeEditForm");
            				}
            			});
        			});
        			
        			$("#itemeEditForm").form("load",data);
        			
        			// 加载商品描述
        			$.getJSON('/rest/item/desc/'+data.id,function(_data){
        				itemEditEditor.html(_data.itemDesc);
        			});
        			
        			//加载商品规格
        			 $.ajax({
				   		type: "GET",
				   		url: '/rest/item/param/item/query/'+data.id,
				   		statusCode : {
					   		404:function(){
					  		 },
					  		200:function(data){
					  			$("#itemeEditForm .params").show();
	        					$("#itemeEditForm [name=itemParams]").val(data.paramData);
	        					$("#itemeEditForm [name=itemParamId]").val(data.id);
	        					
	        					 var paramData = JSON.parse(data.paramData);
	        					
	        					 var html = "<ul>";
	        					 for(var i in paramData){
	        						 var pd = paramData[i];
	        						 html+="<li><table>";
	        						 html+="<tr><td colspan=\"2\" class=\"group\">"+pd.group+"</td></tr>";
	        						 
	        						 for(var j in pd.params){
	        							 var ps = pd.params[j];
	        							 html+="<tr><td class=\"param\"><span>"+ps.k+"</span>: </td><td><input autocomplete=\"off\" type=\"text\" value='"+ps.v+"'/></td></tr>";
	        						 }
	        						 
	        						 html+="</li></table>";
	        					 }
	        					 html+= "</ul>";
	        					 $("#itemeEditForm .params td").eq(1).html(html);
					  		 },
					   		500:function(){
					   		}
				  		 }
				 
				});
        			
        			/* $.getJSON('/rest/item/param/item/query/'+data.id,function(_data){
        				if(_data && _data.status == 200 && _data.data && _data.data.paramData){
        					
        				}
        			}); */
        			
        		}
        	}).window("open");
        }
    },{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为 '+ids+' 的商品吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids/* "_method":"DELETE" */};
        	    	$.ajax({
        	 		   type: "POST",
        	 		   url: "/rest/item/delete",
        	 		   data: params,
        	 		   statusCode:{
        	 			   204: function(){
        	 				   $.messager.alert('提示','删除商品成功!');
        	 				   $("#itemList").datagrid("reload");
        	 			   },
        	 			   500: function(){
        	 				   $.messager.alert('提示','删除商品失败!');
        	 			   }
        	 		   }
        	 		});
                	/* $.post("/rest/item/delete",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','删除商品成功!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				});
            			}
            		}); */
        	    }
        	});
        }
    },'-',{
        text:'下架',
        iconCls:'icon-remove',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品!');
        		return ;
        	}
        	$.messager.confirm('确认','确定下架ID为 '+ids+' 的商品吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/instock",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','下架商品成功!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    },{
        text:'上架',
        iconCls:'icon-remove',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品!');
        		return ;
        	}
        	$.messager.confirm('确认','确定上架ID为 '+ids+' 的商品吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/reshelf",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','上架商品成功!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    }];
</script>