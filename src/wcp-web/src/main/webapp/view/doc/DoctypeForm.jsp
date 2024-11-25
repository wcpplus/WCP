<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--文档分类表单-->
<div class="easyui-layout" data-options="fit:true">
	<div class="TableTitle" data-options="region:'north',border:false">
		<c:if test="${pageset.operateType==1}">新增${JSP_Messager_Title}记录</c:if>
		<c:if test="${pageset.operateType==2}">修改${JSP_Messager_Title}记录</c:if>
		<c:if test="${pageset.operateType==0}">浏览${JSP_Messager_Title}记录</c:if>
	</div>
	<div data-options="region:'center'">
		<form id="dom_formDoctype">
			<input type="hidden" id="entity_id" name="id" value="${entity.id}">
			<table class="editTable">
				<tr>
					<td class="title">上级分类:</td>
					<td colspan="3"><c:if test="${parent!=null&&parent.id!=null}">
        ${parent.name}
        <input type="hidden" name='parentid' value="${parent.id}" />
						</c:if> <c:if test="${parent==null||parent.id==null}">
        无
      </c:if></td>
				</tr>
				<tr>
					<td class="title">栏目名称:</td>
					<td colspan="3"><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="required:true,validType:[,'maxLength[64]']"
						id="entity_name" name="name" value="${entity.name}"></td>
				</tr>
				<tr>
					<td class="title">排列顺序:</td>
					<td colspan="3"><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="required:true,validType:[,'maxLength[5]']"
						id="entity_sort" name="sort" value="${entity.sort}"></td>
				</tr>
				<tr>
					<td class="title">栏目类型:</td>
					<td><select type" id="entity_type" name="type"
						val="${entity.type}">
							<option value="3">知识</option>
							<option value="1">结构</option>
							<!-- 
							<option value="1">
								内容
							</option>
							<option value="2">
								建设
							</option>
							<option value="4">
								链接
							</option>
							<option value="5">
								单页
							</option>
							<option value="6">
								模板
							</option> -->
					</select></td>
					<td class="title">状态:</td>
					<td><select pstate" id="entity_pstate" name="pstate"
						val="${entity.pstate}">
							<option value="1">正常</option>
							<option value="0">禁用</option>
					</select></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'south',border:false">
		<div class="div_button" style="text-align: center; padding: 4px;">
			<c:if test="${pageset.operateType==1}">
				<a id="dom_add_entityDoctype" href="javascript:void(0)"
					iconCls="icon-save" class="easyui-linkbutton">增加</a>
			</c:if>
			<c:if test="${pageset.operateType==2}">
				<a id="dom_edit_entityDoctype" href="javascript:void(0)"
					iconCls="icon-save" class="easyui-linkbutton">修改</a>
			</c:if>
			<a id="dom_cancle_formDoctype" href="javascript:void(0)"
				iconCls="icon-cancel" class="easyui-linkbutton"
				style="color: #000000;">取消</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	var submitAddActionDoctype = 'doctype/add.do';
	var submitEditActionDoctype = 'doctype/edit.do';
	var currentPageTypeDoctype = '${pageset.operateType}';
	var submitFormDoctype;
	$(function() {
		//表单组件对象
		submitFormDoctype = $('#dom_formDoctype').SubmitForm({
			pageType : currentPageTypeDoctype,
			grid : gridDoctype,
			currentWindowId : 'winDoctype'
		});
		//关闭窗口
		$('#dom_cancle_formDoctype').bind('click', function() {
			$('#winDoctype').window('close');
		});
		//提交新增数据
		$('#dom_add_entityDoctype').bind('click', function() {
			submitFormDoctype.postSubmit(submitAddActionDoctype);
		});
		//提交修改数据
		$('#dom_edit_entityDoctype').bind('click', function() {
			submitFormDoctype.postSubmit(submitEditActionDoctype);
		});
	});
//-->
</script>