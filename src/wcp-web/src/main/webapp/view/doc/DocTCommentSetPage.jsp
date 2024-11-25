<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--小组用户-->
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false">
		<form id="searchDocCommentForm">
			<table class="editTable">
				<tr>
					<td class="title">
						内容:
					</td>
					<td>
						<input name="CONTENT:like" type="text">
					</td>
					<td>
						<a id="a_search" href="javascript:void(0)"
							class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<a id="a_reset" href="javascript:void(0)"
							class="easyui-linkbutton" iconCls="icon-reload">清除条件</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table id="dataDocCommentGrid">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th field="CUSERNAME" data-options="sortable:true" width="20">
						创建人
					</th>
					<th field="CONTENT" data-options="sortable:true" width="70">
						内容
					</th>
					<th field="TITLE" data-options="sortable:true" width="50">
						主题
					</th>
					<th field="CTIME" data-options="sortable:true" width="50">
						创建时间
					</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<script type="text/javascript">
	var url_delActionDocComment = "docmessage/del.do";//删除URL
	var url_searchActionDocComment = "docmessage/query.do?ids=${ids}";//查询URL
	var title_windowDocComment = "小组成员管理";//功能名称
	var gridDocComment;//数据表格对象
	var searchDocComment;//条件查询组件对象
	var toolBarDocComment = [ {
		id : 'del',
		text : '删除',
		iconCls : 'icon-remove',
		handler : delDataDocComment
	} ];
	$(function() {
		//初始化数据表格
		gridDocComment = $('#dataDocCommentGrid').datagrid( {
			url : url_searchActionDocComment,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarDocComment,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			striped : true,
			border : false,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchDocComment = $('#searchDocCommentForm').searchForm( {
			gridObj : gridDocComment
		});
	});

	//删除
	function delDataDocComment() {
		var selectedArray = $(gridDocComment).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridDocComment).datagrid('loading');
					$.post(url_delActionDocComment + '?ids='
							+ $.farm.getCheckedIds(gridDocComment, 'ID'), {},
							function(flag) {
								$(gridDocComment).datagrid('loaded');
								if (flag.STATE == 0) {
									$(gridDocComment).datagrid('reload');
								} else {
									var str = MESSAGE_PLAT.ERROR_SUBMIT
											+ flag.MESSAGE;
									$.messager.alert(MESSAGE_PLAT.ERROR, str,
											'error');
								}
							}, 'json');
				}
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE,
					'info');
		}
	}
</script>