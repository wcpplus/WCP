<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--文档附件表单-->
<div class="easyui-layout" data-options="fit:true">
	<div class="TableTitle" data-options="region:'north',border:false">
		<c:if test="${pageset.operateType==1}">新增${JSP_Messager_Title}记录</c:if>
		<c:if test="${pageset.operateType==2}">修改${JSP_Messager_Title}记录</c:if>
		<c:if test="${pageset.operateType==0}">浏览${JSP_Messager_Title}记录</c:if>
	</div>
	<div data-options="region:'center'">
		<form id="dom_formDocindex">
			<table class="editTable">
				<tr>
					<td class="title">文档id或附件id:</td>
					<td><input type="text" style="width: 360px;"
						class="easyui-validatebox"
						data-options="required:true,validType:[,'length[32,32]']"
						id="indexid" name="indexid"></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'south',border:false">
		<div class="div_button" style="text-align: center; padding: 4px;">
			<a id="dom_edit_entityDocindex" href="javascript:void(0)"
				iconCls="icon-save" class="easyui-linkbutton">删除索引</a> <a
				id="dom_cancle_formDocindex" href="javascript:void(0)"
				iconCls="icon-cancel" class="easyui-linkbutton"
				style="color: #000000;">取消</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	var currentPageTypeDocfile = '${pageset.operateType}';
	var submitformDocindex;
	$(function() {
		//表单组件对象
		submitformDocindex = $('#dom_formDocindex').SubmitForm({
			pageType : currentPageTypeDocfile,
			grid : gridfarmdoc,
			currentWindowId : 'winDocindexdel'
		});
		//关闭窗口
		$('#dom_cancle_formDocindex').bind('click', function() {
			$('#winDocindexdel').window('close');
		});
		//提交删除请求
		$('#dom_edit_entityDocindex').bind('click', function() {
			submitformDocindex.postSubmit("lucene/delCommit.do");
		});
	});
//-->
</script>