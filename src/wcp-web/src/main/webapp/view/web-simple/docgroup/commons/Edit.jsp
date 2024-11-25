<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!-- 修改 -->
<c:if test="${group!=null&&group.id!=null}">
	<div class="row">
		<div class="col-md-12">
			<ol class="breadcrumb">
				<li><a href="webgroup/PubHome.html">小组</a></li>
				<li><a href="webgroup/Pubshow.do?groupid=${group.id}">${group.groupname}</a></li>
				<li class="active">修改小组</li>
			</ol>
		</div>
	</div>
	<c:set var="action" value="webgroup/editCommit.do"></c:set>
</c:if>
<!-- 新增 -->
<c:if test="${group==null||group.id==null}">
	<div class="row">
		<div class="col-md-12">
			<ol class="breadcrumb">
				<li><a href="webgroup/PubHome.html">小组</a></li>
				<li class="active">创建小组</li>
			</ol>
		</div>
	</div>
	<c:set var="action" value="webgroup/addCommit.do"></c:set>
</c:if>
<form role="form" action="${action}" id="knowSubmitFormId" method="post">
	<input type="hidden" name="id" id="group_id" value="${group.id}" />
	<div class="row">
		<div class="col-md-6">
			<div class="form-group">
				<label for="exampleInputEmail1"> 小组名称 <span
					class="alertMsgClass">*</span>
				</label> <input type="text" class="form-control" name="groupname"
					value="${group.groupname}" id="group_groupname"
					placeholder="输入小组名称" />
			</div>
			<div class="form-group">
				<label for="exampleInputEmail1"> 当注册用户加入小组时是否需要管理员审核 </label> <select
					class="form-control" id="group_joincheck" name="joincheck"
					val="${group.joincheck}">
					<option value="0">自由加入</option>
					<option value="1">需要审核</option>
				</select>
			</div>
			<div class="form-group">
				<label for="exampleInputEmail1"> 小组头像(200px*200px) <span
					class="alertMsgClass">*</span>
				</label>
				<div>
					<input type="hidden" name="groupimg" id="group_groupimg"
						value="${group.groupimg}" /> <input type="button"
						id="uploadButton" value="上传头像" /> <span id="uploadMarkId"></span>
				</div>
			</div>
		</div>
		<div class="col-md-6" style="text-align: center;">
			<c:if test="${group.groupimg==null}">
				<img id="groupPhotoId" src="text/img/none.png"
					class="img-responsive" alt="小组头像">
			</c:if>
			<c:if test="${group.groupimg!=null}">
				<img id="groupPhotoId" src="${group.imgurl}" class="img-responsive"
					alt="小组头像">
			</c:if>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<div class="form-group">
				<label for="exampleInputEmail1"> 小组标签 </label> <input type="text"
					class="form-control" name="grouptag" value="${group.grouptag}"
					id="group_grouptag" placeholder="输入小组标签,如：科技、技术...标签间用逗号分隔" />
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<div class="form-group">
				<label for="exampleInputEmail1"> 小组介绍 <span
					class="alertMsgClass">*</span>
				</label>
				<textarea name="groupnote" id="group_groupnote" class="form-control"
					rows="3">${group.groupnote}</textarea>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<button type="button" id="knowSubmitButtonId" class="btn btn-primary">
				提交</button>
			&nbsp;&nbsp; <span class="alertMsgClass" id="errormessageShowboxId"></span>
		</div>
	</div>
</form>
<script>
	KindEditor
			.ready(function(K) {
				var uploadbutton = K
						.uploadbutton({
							button : K('#uploadButton')[0],
							fieldName : 'imgFile',
							url : basePath + 'actionImg/PubFPuploadImg.do',
							afterUpload : function(data) {
								if (data.error === 0) {
									$('#group_groupimg').val(data.id);
									$('#uploadMarkId')
											.html(
													'<span style="color:green" class="glyphicon glyphicon-ok">上传成功</span>');
									$('#groupPhotoId').attr('src', data.url);
								} else {
									if (data.message == '') {
										alert("请检查上传文件类型(png、gif、jpg、bmp)以及文件大小不能超过2M");
									} else {
										alert(data.message);
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
	$(function() {
		$('select', '#knowSubmitFormId').each(function(i, obj) {
			var val = $(obj).attr('val');
			var text = $(obj).attr('vat');
			$(obj).val(val);
			var checkText = $(obj).find("option:selected").text();
		});
		$('#knowSubmitButtonId')
				.bind(
						'click',
						function() {
							if (!validate('knowSubmitFormId')) {
								//$('#group_groupimg').val(data.id);
								$('#errormessageShowboxId').text('信息录入有误，请检查！');
							} else {
								if ($('#group_groupimg').val() == null
										|| $('#group_groupimg').val() == '') {
									$('#errormessageShowboxId')
											.text('请上传小组头像！');
									return false;
								}
								if (confirm("是否提交?")) {
									$('#errormessageShowboxId').text('');
									$('#knowSubmitFormId').submit();
									$('#knowSubmitButtonId').hide();
									$('#knowSubmitButtonId')
											.before(
													"<h2><span class='label label-info glyphicon glyphicon-link'>提交中...</span></h2>");
								}
							}
						});
		// 小组名称
		validateInput('group_groupname', function(id, val, obj) {
			if (valid_isNull(val)) {
				return {
					valid : false,
					msg : '不能为空'
				};
			}
			if (valid_maxLength(val, 32)) {
				return {
					valid : false,
					msg : '长度不能大于' + 32
				};
			}
			return {
				valid : true,
				msg : '正确'
			};
		});
		// tag
		validateInput('group_grouptag', function(id, val, obj) {
			if (valid_maxLength(val, 60)) {
				return {
					valid : false,
					msg : '长度不能大于' + 60
				};
			}
			return {
				valid : true,
				msg : '正确'
			};
		});
		// 小组介绍
		validateInput('group_groupnote', function(id, val, obj) {
			if (valid_isNull(val)) {
				return {
					valid : false,
					msg : '不能为空'
				};
			}
			if (valid_maxLength(val, 256)) {
				return {
					valid : false,
					msg : '长度不能大于' + 256
				};
			}
			return {
				valid : true,
				msg : '正确'
			};
		});
	});
</script>