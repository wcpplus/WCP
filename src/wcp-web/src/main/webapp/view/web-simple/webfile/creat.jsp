<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>资源文件-<PF:ParameterValue key="config.sys.title" /></title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="noindex,nofllow">
<jsp:include page="../atext/include-web.jsp"></jsp:include>
<link rel="stylesheet"
	href="<PF:basePath/>text/lib/kindeditor/themes/default/default.css" />
<script charset="utf-8"
	src="<PF:basePath/>text/lib/kindeditor/kindeditor-all-min.js"></script>
<script charset="utf-8" src="<PF:basePath/>text/lib/kindeditor/zh_CN.js"></script>
<link rel="stylesheet"
	href="<PF:basePath/>text/lib/kindeditor/wcpplug/wcp-kindeditor.css" />
<script charset="utf-8"
	src="<PF:basePath/>text/lib/kindeditor/wcpplug/wcp-kindeditor.js"></script>
<script charset="utf-8"
	src="<PF:basePath/>text/lib/super-validate/validate.js"></script>
<link rel="stylesheet" type="text/css"
	href="text/lib/uploadify/uploadify.css">
<script type="text/javascript"
	src="text/lib/uploadify/jquery.uploadify-3.1.min.js"></script>
</head>
<style>
<!--
.webfile-buttonplus .ke-button-common {
	background-image: none;
	background-color: transparent;
	margin: 0px;
	width: 100px;
}

.webfile-buttonplus .ke-button {
	cursor: pointer;
	color: #fff;
	margin: 0px;
}
-->
</style>
<body>
	<jsp:include page="../commons/head.jsp"></jsp:include>
	<jsp:include page="../commons/superContent.jsp"></jsp:include>
	<div class="containerbox">
		<div class="container ">
			<div class="row  column_box">
				<div class="col-md-3  visible-lg visible-md"></div>
				<div class="col-md-9">
					<div class="row">
						<div class="col-md-12">
							<ol class="breadcrumb">
								<li class="active">WCP</li>
								<li class="active">创建文件资源</li>
							</ol>
						</div>
					</div>
					<form role="form" action="webfile/addsubmit.do"
						id="knowSubmitFormId" method="post">
						<div class="row">
							<div class="col-md-12">
								<div class="panel panel-default">
									<div class="panel-heading center-block webfile-buttonplus"
										style="height: 50px;">
										<div style="float: left;">
											<input type="button" class="btn btn-info btn-xs center-block"
												style="padding: 0px; padding-bottom: 8px;" id="uploadButton"
												value="上传资源" />
										</div>
										<div style="float: right;">
											<input type="file" name="uploadify" id="uploadify" />
										</div>
									</div>
									<div class="panel-body" id="fileListId">
										<div id="fileQueue"></div>
										<%-- 
										<input type="hidden" name="fileId" id="uploadfileId"
											value="${fileId}" /> <span id="uploadMarkId">${fileName}</span> --%>

									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label for="exampleInputEmail1"> <input type="checkbox" name="creattype"
										id="creattype">
										创建为独立知识&nbsp;&nbsp;(选中为创建为独立知识，默认将所有附件创建为一条知识)
									</label>
								</div>
							</div>
						</div>
						<div class="row" id="titleBoxId">
							<div class="col-md-12">
								<div class="form-group">
									<label for="exampleInputEmail1"> 标题 <span
										class="alertMsgClass">*</span>
									</label> <input type="text" class="form-control" name="knowtitle"
										value="${doc.doc.title}" id="knowtitleId" placeholder="输入知识标题" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label for="exampleInputEmail1"> tag </label> <input
										type="text" class="form-control" id="knowtagId"
										value="${doc.doc.tagkey}" name="knowtag"
										placeholder="输入类别标签(如果没有系统将自动创建)" />
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label for="exampleInputEmail1"> 文档分类 <span
										class="alertMsgClass">*</span>
									</label>
									<div class="row">
										<div class="col-md-9">
											<input type="text" class="form-control" id="knowtypeTitleId"
												readonly="readonly" placeholder="选择文档分类"
												value="${doc.type.name}" /> <input type="hidden"
												name="knowtype" id="knowtypeId" value="${doc.type.id}" />
										</div>
										<div class="col-md-3">
											<button class="btn btn-info btn-sm" data-toggle="modal"
												id="openChooseTypeButtonId" data-target="#myModal">
												选择</button>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label for="exampleInputEmail1"> 是否发布到小组 </label> <select
										class="form-control" name="docgroup" id="docgroupId"
										val="${(doce.doc.docgroupid!=null && !empty doce.doc.docgroupid)?(doce.doc.docgroupid):'0'}">
										<option value="0">否</option>
										<DOC:docGroupOption aroundS="[工作小组]:" />
									</select>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="exampleInputEmail1"> 编辑权限 <span
										class="alertMsgClass">*</span>
									</label> <select class="form-control" name="writetype" id="writetypeId"
										val="0">
										<option value="">~请选择~</option>
										<option value="0">创建人</option>
										<option value="1">分类</option>
										<option value="2">小组</option>
									</select>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="exampleInputEmail1"> 阅读权限 <span
										class="alertMsgClass">*</span>
									</label> <select class="form-control" name="readtype" id="readtypeId"
										val="1">
										<option value="">~请选择~</option>
										<option value="0">创建人</option>
										<option value="1">分类</option>
										<option value="2">小组</option>
									</select>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<label for="exampleInputEmail1"> 摘要 </label>
								<textarea name="text" id="contentId"
									style="height: 50px; width: 100%;">${text}</textarea>
							</div>
						</div>
						<br /> <br />
						<div class="row">
							<div class="col-md-2">
								<button type="button" id="knowSubmitButtonId"
									class="btn btn-info">提交</button>
							</div>
							<div class="col-md-10">
								<span class="alertMsgClass" id="errormessageShowboxId"></span>
							</div>
						</div>
						<div class="row column_box">
							<div class="col-md-12"></div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<jsp:include
		page="/view/web-simple/type/commons/includeChooseTypes.jsp"></jsp:include>
	<!-- /.modal -->
	<jsp:include page="../commons/foot.jsp"></jsp:include><a onclick=""></a>
</body>
<script>
	KindEditor
			.ready(function(K) {
				var uploadbutton = K
						.uploadbutton({
							button : K('#uploadButton')[0],
							fieldName : 'imgFile',
							url : basePath + 'actionImg/PubFPupload.do',
							afterUpload : function(data) {
								if (data.error === 0) {
									$('#fileListId').append(
											'<div id="file_'+data.id+'">');
									$('#file_' + data.id)
											.append(
													'<input type="hidden" name="fileId" value="'+data.id+'" />');
									$('#file_' + data.id)
											.append(
													'<span>'
															+ decodeURIComponent(data.fileName)
															+ '</span>');
									$('#file_' + data.id).append('&nbsp;');
									$('#file_' + data.id).append(
											'<a href="javascript:void(0)" style="color: green;" onclick="removeFile(\''
													+ data.id + '\');">删除</a>');
									$('#file_' + data.id)
											.append('&nbsp;&nbsp;');
									$('#file_' + data.id).append('</div>');
									$('#knowtitleId').val(
											decodeURIComponent(data.fileName));
								} else {
									alert(data.message);
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
<script type="text/javascript">
	var editor = null;
	$(function() {
		$('#creattype').bind('click', function() {
			if ($("#creattype").attr("checked")) {
				$('#titleBoxId').hide();
			} else {
				$('#titleBoxId').show();
			}
		});
		$('#btn_upload').bind('click', function() {
			$('.ke-button-common').click();
		});
		editor = KindEditor.create('textarea[id="contentId"]', {
			resizeType : 1,
			afterChange : function() {

			},
			cssPath : '<PF:basePath/>text/lib/kindeditor/editInner.css',
			uploadJson : basePath + 'actionImg/PubFPuploadImg.do',
			formatUploadUrl : false,
			allowPreviewEmoticons : false,
			allowImageUpload : true,
			items : [ 'source', 'fullscreen', '|', 'fontsize', 'forecolor',
					'bold', 'italic', 'underline', 'removeformat', '|',
					'justifyleft', 'justifycenter', 'justifyright' ]
		});
		$('#openChooseTypeButtonId').bind('click', function() {
			$('#myModal').modal({
				keyboard : false
			})
		});
		$('select', '#knowSubmitFormId').each(function(i, obj) {
			var val = $(obj).attr('val');
			$(obj).val(val);
		});
		$('#knowSubmitButtonId')
				.bind(
						'click',
						function() {
							if (!$("#creattype").attr("checked")) {
								//绑定一个表单的验证事件
								validateInput('knowtitleId', function(id, val, obj) {
									// 标题
									if (valid_isNull(val)) {
										return {
											valid : false,
											msg : '不能为空'
										};
									}
									if (valid_maxLength(val, 128)) {
										return {
											valid : false,
											msg : '长度不能大于' + 128
										};
									}
									return {
										valid : true,
										msg : '正确'
									};
								});
							}
							editor.sync();
							if (!validate('knowSubmitFormId')) {
								$('#errormessageShowboxId').text('信息录入有误，请检查！');
							} else {
								if ($('#contentId').val().length > 2000) {
									$('#errormessageShowboxId')
											.text(
													'文档内容超长（'
															+ $('#contentId')
																	.val().length
															+ '>2000)');
									return false;
								}
								//判断附件是否上传
								if ($("input[name='fileId']").length == 0) {
									$('#errormessageShowboxId').text('请上传文件');
									return false;
								}
								$('#errormessageShowboxId').text('');
								if (confirm("是否提交数据?")) {
									$('#knowSubmitFormId').submit();
									$('#knowSubmitButtonId').hide();
									$('#knowSubmitButtonId')
											.before(
													"<h2><span class='label label-info glyphicon glyphicon-link'>提交中...</span></h2>");
								}
							}
						});
		validateInput('knowtypeTitleId', function(id, val, obj) {
			// 分类
			if (valid_isNull(val)) {
				return {
					valid : false,
					msg : '不能为空'
				};
			}
			return {
				valid : true,
				msg : '正确'
			};
		});
		//绑定一个表单的验证事件
		validateInput('knowtagId', function(id, val, obj) {
			// 学生姓名
			if (valid_maxLength(val, 128)) {
				return {
					valid : false,
					msg : '长度不能大于' + 128
				};
			}
			return {
				valid : true,
				msg : '正确'
			};
		});
		//编辑权限
		validateInput('writetypeId', function(id, val, obj) {
			if (valid_isNull(val)) {
				return {
					valid : false,
					msg : '不能为空'
				};
			}
			if (val == '2' && $('#docgroupId').val() == '0') {
				return {
					valid : false,
					msg : '请选择工作小组'
				};
			}
			return {
				valid : true,
				msg : '正确'
			};
		});
		//阅读权限
		validateInput('readtypeId', function(id, val, obj) {
			if (valid_isNull(val)) {
				return {
					valid : false,
					msg : '不能为空'
				};
			}
			if (val == '2' && $('#docgroupId').val() == '0') {
				return {
					valid : false,
					msg : '请选择工作小组'
				};
			}
			if ($('#docgroupId').val() != '0') {
				if (val == '0') {
					return {
						valid : false,
						msg : '阅读权限至少是小组'
					};
				}
			}
			return {
				valid : true,
				msg : '正确'
			};
		});
		//工作小组
		validateInput('docgroupId', function(id, val, obj) {
			return {
				valid : true,
				msg : '正确'
			};
		});
		$('a', '.showLableType').bind('click', function() {
			$('#knowtypeId').val($(this).attr('id'));
			$('#knowtypeTitleId').val($(this).text());
			$('#myModal').modal('hide');
		});
	});

	function removeFile(fileId) {
		$("#file_" + fileId).remove();
	}
	$(function() {
		$("#uploadify")
				.uploadify(
						{
							'auto' : true, // 选择文件后自动上传
							'swf' : 'text/lib/uploadify/uploadify.swf', // 上传flash文件路径
							'uploader' : '<PF:basePath/>actionImg/PubFPupload.do',//后台处理的请求 
							'queueID' : 'fileQueue',//与下面的id对应  文件队列
							'queueSizeLimit' : 10,
							'width' : 113,
							"fileObjName" : "imgFile", // 后台接受参数名称
							"preventCaching" : true, // 设置随机参数，防止缓存
							"progressData" : "percentage", // 显示上传进度百分比
							"removeCompleted" : true, // 上传后是否自动删除记录
							"button_image_url" : "<PF:basePath/>text/lib/uploadify/none.png",
							'fileTypeDesc' : '${filetypestr}', // 支持的文件格式，写filetypeExts该参数必须写
							'fileTypeExts' : '${filetypestrplus}', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
							'multi' : true, // 是否支持多文件上传
							"uploadLimit" : 50, // 上传限制
							"onCancel" : function() {
								// 取消上传事件
								alert("删除上传");
							},
							"onQueueComplete" : function(queueData) {
								// 所有文件上传成功后触发
								//alert("全部文件上传成功");
							},
							"onUploadSuccess" : function(file, data, response) {//队列中的每个文件上传完成时触发一次
								var obj = jQuery.parseJSON(data);
								//alert(obj.id + file.name);
								$('#fileListId').append(
										'<div id="file_'+obj.id+'">');
								$('#file_' + obj.id)
										.append(
												'<input type="hidden" name="fileId" value="'+obj.id+'" />');
								$('#file_' + obj.id).append(
										'<span>' + file.name + '</span>');
								$('#file_' + obj.id).append('&nbsp;');
								$('#file_' + obj.id).append(
										'<a href="javascript:void(0)" style="color: green;" onclick="removeFile(\''
												+ obj.id + '\');">删除</a>');
								$('#file_' + obj.id).append('&nbsp;&nbsp;');
								$('#file_' + obj.id).append('</div>');
								$('#knowtitleId').val(file.name);
							},
							'buttonText' : '批量上传'
						});
	});
</script>
</html>