<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--知识段落文本表单-->
<div class="easyui-layout" data-options="fit:true">
	<div class="TableTitle" data-options="region:'north',border:false">
		<span class="label label-primary"> <c:if
				test="${pageset.operateType==1}">新增${JSP_Messager_Title}记录</c:if> <c:if
				test="${pageset.operateType==2}">修改${JSP_Messager_Title}记录</c:if> <c:if
				test="${pageset.operateType==0}">浏览${JSP_Messager_Title}记录</c:if>
		</span>
	</div>
	<div data-options="region:'center'">
		<form id="dom_formShorttext">
			<input type="hidden" id="entity_id" name="id" value="${entity.id}">
			<table class="editTable">
				<tr>
					<td class="title">ID:</td>
					<td colspan="3">${entity.id}</td>
				</tr>
				<tr>
					<td class="title">标题:</td>
					<td colspan="3"><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="validType:[,'maxLength[16]']" id="entity_title"
						name="title" value="${entity.title}"></td>
				</tr>
				<tr>
					<td class="title">顺序号:</td>
					<td><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="required:true,validType:[,'maxLength[5]']"
						id="entity_pno" name="pno" value="${entity.pno}"></td>
					<td class="title">顺序长度:</td>
					<td><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="required:true,validType:[,'maxLength[5]']"
						id="entity_pall" name="pall" value="${entity.pall}"></td>
				</tr>
				<tr>
					<td class="title">知识访问地址:</td>
					<td colspan="3"> <a target="_blank" href="${url}">${url}</a>  </td>
				</tr>
				<tr>
					<td class="title">正文:</td>
					<td colspan="3"><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="validType:[,'maxLength[512]']" id="entity_text"
						name="text" value="${text}"></td>
				</tr>
				<tr>
					<td class="title">向量:</td>
					<td colspan="3">${floats}</td>
				</tr>
				<tr>
					<td class="title">知识ID:</td>
					<td colspan="3"><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="validType:[,'maxLength[16]']" id="entity_sid"
						name="sid" value="${entity.sid}"></td>
				</tr>
				<tr>
					<td class="title">备注:</td>
					<td colspan="3"><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="validType:[,'maxLength[64]']" id="entity_pcontent"
						name="pcontent" value="${entity.pcontent}"></td>
				</tr>
				<tr>
					<td class="title">创建用户:</td>
					<td colspan="3"><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="required:true,validType:[,'maxLength[16]']"
						id="entity_cuser" name="cuser" value="${entity.cuser}"></td>
				</tr>
				<tr>
					<td class="title">创建时间:</td>
					<td colspan="3"><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="required:true,validType:[,'maxLength[8]']"
						id="entity_ctime" name="ctime" value="${entity.ctime}"></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'south',border:false">
		<div class="div_button" style="text-align: center; padding: 4px;">
			<c:if test="${pageset.operateType==1}">
				<a id="dom_add_entityShorttext" href="javascript:void(0)"
					iconCls="icon-save" class="easyui-linkbutton">增加</a>
			</c:if>
			<c:if test="${pageset.operateType==2}">
				<a id="dom_edit_entityShorttext" href="javascript:void(0)"
					iconCls="icon-save" class="easyui-linkbutton">修改</a>
			</c:if>
			<a id="dom_cancle_formShorttext" href="javascript:void(0)"
				iconCls="icon-cancel" class="easyui-linkbutton"
				style="color: #000000;">取消</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	var submitAddActionShorttext = 'shorttext/add.do';
	var submitEditActionShorttext = 'shorttext/edit.do';
	var currentPageTypeShorttext = '${pageset.operateType}';
	var submitFormShorttext;
	$(function() {
		//表单组件对象
		submitFormShorttext = $('#dom_formShorttext').SubmitForm({
			pageType : currentPageTypeShorttext,
			grid : gridShorttext,
			currentWindowId : 'winShorttext'
		});
		//关闭窗口
		$('#dom_cancle_formShorttext').bind('click', function() {
			$('#winShorttext').window('close');
		});
		//提交新增数据
		$('#dom_add_entityShorttext').bind('click', function() {
			submitFormShorttext.postSubmit(submitAddActionShorttext);
		});
		//提交修改数据
		$('#dom_edit_entityShorttext').bind('click', function() {
			submitFormShorttext.postSubmit(submitEditActionShorttext);
		});
	});
//-->
</script>