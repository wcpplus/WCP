<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--知识段落文本表单-->
<div class="easyui-layout" data-options="fit:true">
	<div class="TableTitle" data-options="region:'north',border:false">
		<span class="label label-primary">语义索引类型 </span>
	</div>
	<div data-options="region:'center'">
		<form id="dom_formShorttext">
			<input type="hidden" id="entity_id" name="id" value="${entity.id}">
			<table class="editTable">
				<tr>
					<td class="title">索引模型:</td>
					<td colspan="3">config.ai.index.model.type&nbsp;=&nbsp;<b><PF:ParameterValue
								key="config.ai.index.model.type" /></b></td>
				</tr>
				<tr>
					<td class="title">索引库:</td>
					<td colspan="3">config.ai.index.db.type&nbsp;=&nbsp;<b><PF:ParameterValue
								key="config.ai.index.db.type" /></b></td>
				</tr>
			</table>
			<PF:IfParameterEquals key="config.ai.index.model.type" val="embedding">
				<table class="editTable">
					<tr>
						<td class="title">模型类型:</td>
						<td colspan="3">config.imbar.qa.model.type&nbsp;=&nbsp;<b><PF:ParameterValue
									key="config.imbar.qa.model.type" /></b></td>
					</tr>
				</table> 
			</PF:IfParameterEquals>
			<c:if test="${!empty MESSAGE}">
				<table class="editTable">
					<tr>
						<td class="title">索引库状态</td>
						<td colspan="3"><span style="color: red;">${MESSAGE}</span></td>
					</tr>
				</table>
			</c:if>
			<c:if test="${empty MESSAGE}">
				<table class="editTable">
					<tr>
						<td class="title">索引库状态</td>
						<td colspan="3"><span style="color: green;">可用</span></td>
					</tr>
				</table>
			</c:if>
			<PF:IfParameterEquals key="config.ai.index.db.type" val="lucene">
				<table class="editTable">
					<tr>
						<td class="title">索引目录:</td>
						<td colspan="3"><b><PF:ParameterValue
									key="config.file.luncene_index_dir" />\shortindex</b></td>
					</tr>
				</table>
			</PF:IfParameterEquals>
			<PF:IfParameterEquals key="config.ai.index.db.type" val="milvus">
				<table class="editTable">
					<tr>
						<td class="title">服务器IP:</td>
						<td colspan="3">config.ai.milves.server.ip&nbsp;=&nbsp;<b><PF:ParameterValue
									key="config.ai.milves.server.ip" /></b></td>
					</tr>
					<tr>
						<td class="title">端口:</td>
						<td colspan="3">config.ai.milves.server.port&nbsp;=&nbsp;<b><PF:ParameterValue
									key="config.ai.milves.server.port" /></b></td>
					</tr>
					<tr>
						<td class="title">登录名:</td>
						<td colspan="3">config.ai.milves.server.loginname&nbsp;=&nbsp;<b><PF:ParameterValue
									key="config.ai.milves.server.loginname" /></b></td>
					</tr>
					<tr>
						<td class="title">登录密码:</td>
						<td colspan="3">config.ai.milves.server.password&nbsp;=&nbsp;<b><PF:ParameterValue
									key="config.ai.milves.server.password" /></b></td>
					</tr>
					<tr>
						<td class="title">数据集名称:</td>
						<td colspan="3">config.ai.milves.collection.name&nbsp;=&nbsp;<b><PF:ParameterValue
									key="config.ai.milves.collection.name" /></b></td>
					</tr>
					<tr>
						<td class="title">数据库名称:</td>
						<td colspan="3">config.ai.milves.server.database&nbsp;=&nbsp;<b><PF:ParameterValue
									key="config.ai.milves.server.database" /></b></td>
					</tr>
				</table>
			</PF:IfParameterEquals>
		</form>
	</div>
	<div data-options="region:'south',border:false">
		<div class="div_button" style="text-align: center; padding: 4px;">
			<a id="dom_cancle_formShorttextInfo" href="javascript:void(0)"
				iconCls="icon-cancel" class="easyui-linkbutton"
				style="color: #000000;">取消</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		//关闭窗口
		$('#dom_cancle_formShorttextInfo').bind('click', function() {
			$('#winShorttext').window('close');
		});
	});
//-->
</script>