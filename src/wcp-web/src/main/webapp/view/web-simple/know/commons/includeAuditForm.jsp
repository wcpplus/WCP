<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="row doc_column_box">
	<div class="col-sm-12">
		<span class="glyphicon glyphicon-user column_title">审核</span>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<a name="markAuditform"></a>
		<hr class="hr_split" />
		<form role="form" action="audit/auditSubmit.do" id="knowSubmitFormId"
			method="post">
			<div class="row">
				<div class="col-md-12">
					<textarea class="form-control" id="mesTextId" name="content"
						placeholder="请输入审核意见"></textarea>
					<input type="hidden" name="auditid" value="${DOCE.audit.id}" />
				</div>
			</div>
			<div class="row column_box">
				<div class="col-md-12">
					<div class="btn-group" role="group" aria-label="...">
						<button type="button" id="passSubmitButtonId"
							class="btn btn-success">通过</button>
						<button type="button" id="cantSubmitButtonId"
							class="btn btn-danger">拒绝</button>
					</div>
				</div>
				<div class="col-md-12">
					<span class="alertMsgClass" id="errormessageShowboxId"></span>
				</div>
			</div>
			<div class="row column_box">
				<div class="col-md-12"></div>
			</div>
		</form>
	</div>
</div>
<script>
	$(function() {
		$('#passSubmitButtonId')
				.bind(
						'click',
						function() {
							if (!validate('knowSubmitFormId')) {
								$('#errormessageShowboxId').text('信息录入有误，请检查！');
							} else {
								if (confirm("是否提交?")) {
									$('#knowSubmitFormId').attr('action',"audit/auditYes.do");
									$('#cantSubmitButtonId').hide();
									$('#passSubmitButtonId').hide();
									$('#knowSubmitFormId').submit();
									$('#passSubmitButtonId')
											.before(
													"<h2><span class='label label-info glyphicon glyphicon-link'>提交中...</span></h2>");
								}
							}
						});
		$('#cantSubmitButtonId')
				.bind(
						'click',
						function() {
							if (!validate('knowSubmitFormId')) {
								$('#errormessageShowboxId').text('信息录入有误，请检查！');
							} else {
								if (confirm("是否提交?")) {
									$('#knowSubmitFormId').attr('action',"audit/auditNo.do");
									$('#cantSubmitButtonId').hide();
									$('#passSubmitButtonId').hide();
									$('#knowSubmitFormId').submit();
									$('#cantSubmitButtonId')
											.before(
													"<h2><span class='label label-info glyphicon glyphicon-link'>提交中...</span></h2>");
								}
							}
						});
		// 小组介绍
		validateInput('mesTextId', function(id, val, obj) {
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