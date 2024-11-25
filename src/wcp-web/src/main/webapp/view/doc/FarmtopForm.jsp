<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--置顶文档表单-->
<div class="easyui-layout" data-options="fit:true">
	<div class="TableTitle" data-options="region:'north',border:false">
		<span class="label label-primary"> 
		<c:if test="${pageset.operateType==1}">新增${JSP_Messager_Title}记录</c:if> 
		<c:if test="${pageset.operateType==2}">修改${JSP_Messager_Title}记录</c:if> 
		<c:if test="${pageset.operateType==0}">浏览${JSP_Messager_Title}记录</c:if>
		</span>
	</div>
	<div data-options="region:'center'">
		<form id="dom_formFarmtop">
			<input type="hidden" id="entity_id" name="id" value="${entity.id}">
			<table class="editTable">
				<tr>
					<td class="title">文档标题</td>
					<td colspan="3">
						<input type="hidden" style="width: 360px;"
							class="easyui-validatebox"
							data-options="required:true,validType:[,'maxLength[32]']"
							id="entity_docid" name="docid" value="${entity.docid}">
						<input id="entity_doctitle" type="text" name="doctitle" value="${doctitle}"
							class="easyui-validatebox"
							data-options="required:true" readonly="readonly">
						<c:if test="${pageset.operateType!=0}">
						<input id="buttonChoosedocChoose" type="button" value="选择">
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="title">排序:</td>
					<td colspan="3">
						<input id="entity_sort" name="sort"
							class="easyui-numberspinner"  
       						required="required" data-options="min:1,max:100,editable:false" 
       						value="${entity.sort}">
					</td>
				</tr>
				<tr>
					<td class="title">PCONTENT:</td>
					<td colspan="3">
						<textarea rows="2" style="width: 410px;"
							class="easyui-validatebox"
							data-options="validType:[,'maxLength[128]']"
							id="entity_pcontent" name="pcontent">${entity.pcontent}</textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'south',border:false">
		<div class="div_button" style="text-align: center; padding: 4px;">
			<c:if test="${pageset.operateType==1}">
				<a id="dom_add_entityFarmtop" href="javascript:void(0)"
					iconCls="icon-save" class="easyui-linkbutton">增加</a>
			</c:if>
			<c:if test="${pageset.operateType==2}">
				<a id="dom_edit_entityFarmtop" href="javascript:void(0)"
					iconCls="icon-save" class="easyui-linkbutton">修改</a>
			</c:if>
			<a id="dom_cancle_formFarmtop" href="javascript:void(0)"
				iconCls="icon-cancel" class="easyui-linkbutton"
				style="color: #000000;">取消</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	var submitAddActionFarmtop = 'farmtop/add.do';
	var submitEditActionFarmtop = 'farmtop/edit.do';
	var currentPageTypeFarmtop = '${pageset.operateType}';
	var submitFormFarmtop;
	$(function() {
		//表单组件对象
		submitFormFarmtop = $('#dom_formFarmtop').SubmitForm({
			pageType : currentPageTypeFarmtop,
			grid : gridFarmtop,
			currentWindowId : 'winFarmtop'
		});
		//关闭窗口
		$('#dom_cancle_formFarmtop').bind('click', function() {
			$('#winFarmtop').window('close');
		});
		//提交新增数据
		$('#dom_add_entityFarmtop').bind('click', function() {
			submitFormFarmtop.postSubmit(submitAddActionFarmtop);
		});
		//提交修改数据
		$('#dom_edit_entityFarmtop').bind('click', function() {
			submitFormFarmtop.postSubmit(submitEditActionFarmtop);
		});
		
		//打开文档页面
		$('#buttonChoosedocChoose').bindChooseWindow('chooseChoosedocWin', {
				width : 900,
				height : 400,
				modal : true,
				url : 'farmtop/toDocTopChooseDoc.do',
				title : '选择文档',
				callback : function(rows) {
					$("#entity_docid").val(rows[0].ID);
					$("#entity_doctitle").val(rows[0].TITLE);
					//$('#NAME_like').val(rows[0].NAME);
				}
			});
		});
//-->
</script>