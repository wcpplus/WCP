<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--文档小组表单-->
<div class="easyui-layout" data-options="fit:true">
	<div class="TableTitle" data-options="region:'north',border:false">
		<c:if test="${pageset.operateType==1}">新增${JSP_Messager_Title}记录</c:if>
		<c:if test="${pageset.operateType==2}">修改${JSP_Messager_Title}记录</c:if>
		<c:if test="${pageset.operateType==0}">浏览${JSP_Messager_Title}记录</c:if>
	</div>
	<div data-options="region:'center'">
		<form id="dom_formDocgroup">
			<input type="hidden" id="entity_id" name="id"
				value="${entity.id}">
			<table class="editTable">
				<tr>
					<td class="title">
						小组名称:
					</td>
					<td colspan="3">
						<input type="text" style="width: 360px;"
							class="easyui-validatebox"
							data-options="required:true,validType:[,'maxLength[64]']"
							id="entity_groupname" name="groupname"
							value="${entity.groupname}">
					</td>
					<td id="imgTdId" rowspan="5"
						style="text-align: center; width: 200px;">
						<c:if test="${imgurl!=null}">
							<img style='max-width: 100%;max-height: 300px;' src='${imgurl}' />
						</c:if>
						<c:if test="${pageset.operateType==1}">
							<img style='max-width: 100%;' src='WEB-FACE/img/style/NoImg.png' />
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="title">
						小组介绍:
					</td>
					<td colspan="3">
						<textarea rows="2" style="width: 360px;"
							class="easyui-validatebox"
							data-options="required:true,validType:[,'maxLength[1024]']"
							id="entity_groupnote"  name="groupnote">${entity.groupnote}</textarea>
					</td>
				</tr>
				<tr>
					<td class="title">
						小组标签:
					</td>
					<td colspan="3">
						<input type="text" style="width: 360px;"
							class="easyui-validatebox"
							data-options="required:true,validType:[,'maxLength[128]']"
							id="entity_grouptag"  name="grouptag"
							value="${entity.grouptag}">
					</td>
				</tr>
				<tr>
					<td class="title">
						是否验证用户:
					</td>
					<td colspan="3">
						<select  name="joincheck" id="entity_joincheck"
							style="width: 120px;" val="${entity.joincheck}">
							<option value="1">
								是
							</option>
							<option value="0">
								否
							</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="title">
						小组头像:
					</td>
					<td colspan="3">
						<c:if test="${pageset.operateType!=0}">
							<input type="button" id="uploadButton" value="上传小组头像" />
							<script type="text/javascript">
	$(function() {
		var uploadbutton;
		uploadbutton = KindEditor.uploadbutton( {
			button : KindEditor('#uploadButton')[0],
			fieldName : 'imgFile',
			url : 'actionImg/PubFPuploadImg.do',
			afterUpload : function(data) {
				if (data.error === 0) {
					$("#entity_fileid").val(data.id);
					$("#imgTdId").html(
							"<img  style='max-width:100%;max-height: 200px;' src='" + data.url
									+ "'/>");
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
						<input type="hidden" id="entity_fileid" name="groupimg"
							value="${entity.groupimg}">
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'south',border:false">
		<div class="div_button" style="text-align: center; padding: 4px;">
			<c:if test="${pageset.operateType==1}">
				<a id="dom_add_entityDocgroup" href="javascript:void(0)"
					iconCls="icon-save" class="easyui-linkbutton">增加</a>
			</c:if>
			<c:if test="${pageset.operateType==2}">
				<a id="dom_edit_entityDocgroup" href="javascript:void(0)"
					iconCls="icon-save" class="easyui-linkbutton">修改</a>
			</c:if>
			<a id="dom_cancle_formDocgroup" href="javascript:void(0)"
				iconCls="icon-cancel" class="easyui-linkbutton"
				style="color: #000000;">取消</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	var submitAddActionDocgroup = 'docgroup/add.do';
	var submitEditActionDocgroup = 'docgroup/edit.do';
	var currentPageTypeDocgroup = '${pageset.operateType}';
	var submitFormDocgroup;
	$(function() {
		//表单组件对象
		submitFormDocgroup = $('#dom_formDocgroup').SubmitForm( {
			pageType : currentPageTypeDocgroup,
			grid : gridDocgroup,
			currentWindowId : 'winDocgroup'
		});
		//关闭窗口
		$('#dom_cancle_formDocgroup').bind('click', function() {
			$('#winDocgroup').window('close');
		});
		//提交新增数据
		$('#dom_add_entityDocgroup').bind('click', function() {
			if (!$('#entity_fileid').val()) {
				$.messager.alert(MESSAGE_PLAT.PROMPT, "请上传头像", 'info');
				return false;
			}
			submitFormDocgroup.postSubmit(submitAddActionDocgroup);
		});
		//提交修改数据
		$('#dom_edit_entityDocgroup').bind('click', function() {
			if (!$('#entity_fileid').val()) {
				$.messager.alert(MESSAGE_PLAT.PROMPT, "请上传头像", 'info');
				return false;
			}
			submitFormDocgroup.postSubmit(submitEditActionDocgroup);
		});
	});
	//-->
</script>