<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<PF:basePath/>">
<title>简历基本信息数据管理</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<jsp:include page="/view/conf/include.jsp"></jsp:include>
</head>
<body class="easyui-layout">
	<div data-options="region:'north'"
		style="height: 200px; overflow: hidden;">
		<form id="searchResumebaseForm">
			<div style="height: 160px; overflow: auto;">
				<table class="editTable">
					<tr>
						<td class="title">姓名:</td>
						<td><input name="NAME:like" type="text"></td>
						<td class="title">出生日期:</td>
						<td><input name="BIRTHDAY:like" type="text"
							class="easyui-datebox"></input></td>
						<td class="title">性别:</td>
						<td><select name="SEX:=">
								<option value=""></option>
								<PF:OptionDictionary index="RESUME_SEX" isTextValue="0" />
						</select></td>
					</tr>
					<tr>
						<td class="title">户口所在地:</td>
						<td><input name="REGISTERED:like" type="text"></td>
						<td class="title">居住所在地:</td>
						<td><input name="LIVESTR:like" type="text"></td>
						<td class="title">参加工作年份（年）:</td>
						<td><input name="DATEYEAR:like" type="text"></input></td>
					</tr>
					<tr>
						<td class="title">qq:</td>
						<td><input name="QQCODE:like" type="text"></td>
						<td class="title">微信:</td>
						<td><input name="WXCODE:like" type="text"></td>
						<td class="title">手机:</td>
						<td><input name="MOBILECODE:like" type="text"></td>
					</tr>
					<tr>
						<td class="title">电子邮箱:</td>
						<td><input name="EMAILCODE:like" type="text"></td>
						<td class="title">婚姻状况:</td>
						<td><select name="MARRIAGESTA:=">
								<option value=""></option>
								<PF:OptionDictionary index="RESUME_MARRIAGESTA" isTextValue="0" />
						</select></td>
						<td class="title">国籍:</td>
						<td><select name="NATIONALITY:=">
								<option value=""></option>
								<PF:OptionDictionary index="RESUME_NATIONALITY" isTextValue="0" />
						</select></td>
					</tr>
					<tr>
						<td class="title">海外工作/学习经历:</td>
						<td><select name="STUDYABROAD:=">
								<option value=""></option>
								<PF:OptionDictionary index="RESUME_STUDYABROAD" isTextValue="0" />
						</select></td>
						<td class="title">其他证件编号:</td>
						<td><input name="OTHERID:like" type="text"></td>
						<td class="title">身份证号:</td>
						<td><input name="IDCODE:like" type="text"></td>
					</tr>
					<tr>
						<td class="title">政治面貌:</td>
						<td><select name="ZZMM:=">
								<option value=""></option>
								<PF:OptionDictionary index="RESUME_ZZMM" isTextValue="0" />
						</select></td>


						<td class="title">最高学历:</td>
						<td><select name="DEGREEMAX:=">
								<option value=""></option>
								<PF:OptionDictionary index="RESUME_DEGREE" isTextValue="0" />
						</select></td>


						<td class="title">期望从事行业:</td>
						<td><select name="WORKINDUSTRY:=">
								<option value=""></option>
								<PF:OptionDictionary index="RESUME_WORKINDUSTRY" isTextValue="0" />
						</select></td>

					</tr>
					<tr>
						<td class="title">期望工作性质:</td>
						<td><select name="WORKNATURE:=">
								<option value=""></option>
								<PF:OptionDictionary index="RESUME_WORKNATURE" isTextValue="0" />
						</select></td>
						<td class="title">期望工作地点:</td>
						<td><input name="WORKADDR:like" type="text"></td>
						<td class="title">期望从事职业:</td>
						<td><select name="WORKOCCUPATION:=">
								<option value=""></option>
								<PF:OptionDictionary index="RESUME_WORKOCCUPATION"
									isTextValue="0" />
						</select></td>
					</tr>
					<tr>
						<td class="title">工作状态:</td>
						<td><select name="WORKSTAT:=">
								<option value=""></option>
								<PF:OptionDictionary index="RESUME_WORKSTAT" isTextValue="0" />
						</select></td>
						<td class="title"></td>
						<td></td>
						<td class="title"></td>
						<td></td>
					</tr>
				</table>
			</div>
			<table class="editTable">
				<tr style="text-align: center;">
					<td colspan="6"><a id="a_search" href="javascript:void(0)"
						class="easyui-linkbutton" iconCls="icon-search">查询</a> <a
						id="a_reset" href="javascript:void(0)" class="easyui-linkbutton"
						iconCls="icon-reload">清除条件</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table id="dataResumebaseGrid">
			<thead data-options="frozen:true">
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th field="NAME" data-options="sortable:true" width="100">姓名</th>
					<th field="SEX" data-options="sortable:true" width="100">性别</th>
					<th field="BIRTHDAY" data-options="sortable:true" width="100">出生日期</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="DATEYEAR" data-options="sortable:true" width="100">参加工作年份</th>
					<th field="REGISTERED" data-options="sortable:true" width="200">户口所在地</th>
					<!--<th field="DATEMONTH" data-options="sortable:true" width="100">参加工作年份（月）</th> <th field="REGISTEREDAREA" data-options="sortable:true" width="100">户口所在地（省）</th>
					<th field="LIVESTRAREA" data-options="sortable:true" width="100">居住所在地（省）</th> -->
					<th field="LIVESTR" data-options="sortable:true" width="200">居住所在地</th>
					<th field="MOBILECODE" data-options="sortable:true" width="100">手机</th>
					<th field="EMAILCODE" data-options="sortable:true" width="200">电子邮箱</th>
					<th field="QQCODE" data-options="sortable:true" width="100">qq</th>
					<th field="WXCODE" data-options="sortable:true" width="100">微信</th>
					<th field="MARRIAGESTA" data-options="sortable:true" width="100">婚姻状况</th>
					<th field="NATIONALITY" data-options="sortable:true" width="100">国籍</th>
					<th field="IDCODE" data-options="sortable:true" width="200">身份证号</th>
					<!-- <th field="OTHERTYPE" data-options="sortable:true" width="100">其他证件类型</th>
					<th field="OTHERID" data-options="sortable:true" width="100">其他证件编号</th> -->
					<th field="STUDYABROAD" data-options="sortable:true" width="120">海外工作/学习经历</th>
					<th field="ZZMM" data-options="sortable:true" width="200">政治面貌</th>
					<th field="DEGREEMAX" data-options="sortable:true" width="200">最高学历</th>
					<th field="WORKNATURE" data-options="sortable:true" width="100">期望工作性质</th>
					<th field="WORKADDR" data-options="sortable:true" width="100">期望工作地点</th>
					<th field="WORKOCCUPATION" data-options="sortable:true" width="100">期望从事职业</th>
					<th field="WORKINDUSTRY" data-options="sortable:true" width="100">期望从事行业</th>
					<th field="WORKPAY" data-options="sortable:true" width="100">期望月薪(税前)</th>
					<th field="WORKSTAT" data-options="sortable:true" width="100">工作状态</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
<script type="text/javascript">
	var url_delActionResumebase = "resumebase/del.do";//删除URL
	var url_formActionResumebase = "resumebase/form.do";//增加、修改、查看URL
	var url_searchActionResumebase = "resumebase/query.do";//查询URL
	var title_windowResumebase = "简历基本信息管理";//功能名称
	var gridResumebase;//数据表格对象
	var searchResumebase;//条件查询组件对象
	var toolBarResumebase = [ {
		id : 'view',
		text : '查看',
		iconCls : 'icon-tip',
		handler : viewDataResumebase
	} /**, {
			id : 'add',
			text : '新增',
			iconCls : 'icon-add',
			handler : addDataResumebase
		}, {
			id : 'edit',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editDataResumebase
		}, {
			id : 'del',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delDataResumebase
		} **/
	];
	$(function() {
		//初始化数据表格
		gridResumebase = $('#dataResumebaseGrid').datagrid({
			url : url_searchActionResumebase,
			fit : true,
			fitColumns : false,
			'toolbar' : toolBarResumebase,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			border : false,
			striped : true,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchResumebase = $('#searchResumebaseForm').searchForm({
			gridObj : gridResumebase
		});
	});
	//查看
	function viewDataResumebase() {
		var selectedArray = $(gridResumebase).datagrid('getSelections');
		if (selectedArray.length == 1) {
			window.open('resume/view.do?userid=' + selectedArray[0].USERID);
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE_ONLY,
					'info');
		}
	}
	//新增
	function addDataResumebase() {
		var url = url_formActionResumebase + '?operateType=' + PAGETYPE.ADD;
		$.farm.openWindow({
			id : 'winResumebase',
			width : 600,
			height : 300,
			modal : true,
			url : url,
			title : '新增'
		});
	}
	//修改
	function editDataResumebase() {
		var selectedArray = $(gridResumebase).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionResumebase + '?operateType='
					+ PAGETYPE.EDIT + '&ids=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winResumebase',
				width : 600,
				height : 300,
				modal : true,
				url : url,
				title : '修改'
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE_ONLY,
					'info');
		}
	}
	//删除
	function delDataResumebase() {
		var selectedArray = $(gridResumebase).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridResumebase).datagrid('loading');
					$.post(url_delActionResumebase + '?ids='
							+ $.farm.getCheckedIds(gridResumebase, 'ID'), {},
							function(flag) {
								var jsonObject = JSON.parse(flag, null);
								$(gridResumebase).datagrid('loaded');
								if (jsonObject.STATE == 0) {
									$(gridResumebase).datagrid('reload');
								} else {
									var str = MESSAGE_PLAT.ERROR_SUBMIT
											+ jsonObject.MESSAGE;
									$.messager.alert(MESSAGE_PLAT.ERROR, str,
											'error');
								}
							});
				}
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE,
					'info');
		}
	}
</script>
</html>