<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--推荐服务表单-->
<div class="easyui-layout" data-options="fit:true">
	<div class="TableTitle" data-options="region:'north',border:false">
		<span class="label label-primary">
			<c:if test="${pageset.operateType==1}">新增${JSP_Messager_Title}记录</c:if>
			<c:if test="${pageset.operateType==2}">修改${JSP_Messager_Title}记录</c:if>
			<c:if test="${pageset.operateType==0}">浏览${JSP_Messager_Title}记录</c:if>
		</span>
	</div>
	<div data-options="region:'center'">
		<form id="dom_formWeburl">
			<input type="hidden" id="entity_id" name="id" value="${entity.id}">
			<table class="editTable">
				<tr>
					<td class="title">名称:</td>
					<td>
						<input type="text" style="width: 120px;"
							class="easyui-validatebox"
							data-options="required:true,validType:[,'maxLength[32]']"
							id="entity_webname" name="webname" value="${entity.webname}">
					</td>
					<td class="title">排序</td>
					<td>
						<input class="easyui-numberspinner" style="width: 120px;" name="sort"
							value="${entity.sort}" required="required"
							data-options="min:1,max:100,editable:false">
					</td>
				</tr>
				<tr>
					<td class="title">URL:</td>
					<td colspan="3">
						<textarea rows="2" style="width: 360px;"
							class="easyui-validatebox"
							data-options="required:true,validType:[,'maxLength[256]']"
							id="entity_url" name="url">${entity.url}</textarea>
					</td>
				</tr>
				<tr>
					<td class="title">图标:</td>
					<td colspan="3">
						<c:if test="${pageset.operateType !=0}">
							<input type="button" id="uploadButton" value="上传内容图" />
							<script type="text/javascript">
								$(function() {
									var uploadbutton;
									uploadbutton = KindEditor
											.uploadbutton({
												button : KindEditor('#uploadButton')[0],
												fieldName : 'imgFile',
												url : 'actionImg/PubFPuploadImg.do',
												afterUpload : function(data) {
													if (data.error === 0) {
														$("#entity_fileid").val(data.id);
														$("#imgFileboxId").html("<a  target='_blank'  href='" + data.url+ "'>下载</a>");
														$("#imgFileboxId").show();
													} else {
														if (data.message) {
															alert(data.message);
														} else {
															alert('请检查文件格式');
														}
													}
												},
												afterError : function(str) {
													alert('自定义错误信息: ' + str);
												}
											});
									uploadbutton.fileBox.change(function(e) {
										uploadbutton.submit();
									});
								});
							</script>
						</c:if>
						<span id="imgFileboxId">
							<c:if test="${imgUrl!=null}">
								<a target='_blank' href='${imgUrl}'>下载</a>
								<a href='javascript:void(0);' onclick="delImg()">删除</a>
							</c:if>
						</span>
						<input type="hidden" id="entity_fileid" name="fileid"
							value="${entity.fileid}">
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'south',border:false">
		<div class="div_button" style="text-align: center; padding: 4px;">
			<c:if test="${pageset.operateType==1}">
				<a id="dom_add_entityWeburl" href="javascript:void(0)"
					iconCls="icon-save" class="easyui-linkbutton">增加</a>
			</c:if>
			<c:if test="${pageset.operateType==2}">
				<a id="dom_edit_entityWeburl" href="javascript:void(0)"
					iconCls="icon-save" class="easyui-linkbutton">修改</a>
			</c:if>
			<a id="dom_cancle_formWeburl" href="javascript:void(0)"
				iconCls="icon-cancel" class="easyui-linkbutton"
				style="color: #000000;">取消</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	var submitAddActionWeburl = 'weburl/add.do';
	var submitEditActionWeburl = 'weburl/edit.do';
	var currentPageTypeWeburl = '${pageset.operateType}';
	var submitFormWeburl;
	$(function() {
		//表单组件对象
		submitFormWeburl = $('#dom_formWeburl').SubmitForm({
			pageType : currentPageTypeWeburl,
			grid : gridWeburl,
			currentWindowId : 'winWeburl'
		});
		//关闭窗口
		$('#dom_cancle_formWeburl').bind('click', function() {
			$('#winWeburl').window('close');
		});
		//提交新增数据
		$('#dom_add_entityWeburl').bind('click', function() {
			submitFormWeburl.postSubmit(submitAddActionWeburl);
		});
		//提交修改数据
		$('#dom_edit_entityWeburl').bind('click', function() {
			submitFormWeburl.postSubmit(submitEditActionWeburl);
		});
	});
	
	function delImg(){
		$.messager.confirm(MESSAGE_PLAT.PROMPT, "确定删除?", function(r) {
			if (r) {
				$.post("weburl/delImg.do", {imgid: "${entity.fileid}"}, function(obj){
					if(obj.STATE == 0){
						$("#entity_fileid").val("");
						$("#imgFileboxId").hide();
					}else{
						$.messager.alert(MESSAGE_PLAT.ERROR, obj.MESSAGE, 'error');
					}
				}, "json");
			}
		});
	}
//-->
</script>