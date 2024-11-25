<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<!-- 如果是内容类型等于4小组首页，则在此处不提供任何操作功能 -->
<c:if test="${DOCE.doc.domtype!='4'}">
	<c:if test="${DOCE.doc.state=='2'||DOCE.doc.state=='3'}">
		<div class="alert alert-danger" role="alert">
			<span class=" glyphicon glyphicon-info-sign">[未审核,该文档未经过必要的审核请谨慎参考]</span>
		</div>
	</c:if>
	<div style="padding: 2px;">
		<c:if test="${USEROBJ!=null}">
			<c:if test="${ISENJOY}">
				<button type="button" id="disEnjoyId"
					class="btn btn-default  btn-xs">
					<span class="glyphicon glyphicon-star"></span>&nbsp;取消关注
				</button>
			</c:if>
			<c:if test="${!ISENJOY}">
				<button type="button" id="enjoyId" class="btn btn-default  btn-xs">
					<span class="glyphicon glyphicon-star"></span>&nbsp;关注
				</button>
			</c:if>
			<DOC:canWriteIsShow docId="${DOCE.doc.id}">
				<button type="button" id="editTopButtonId"
					class="btn btn-default  btn-xs">
					<span class="glyphicon glyphicon-envelope"></span>&nbsp;修改
				</button>
			</DOC:canWriteIsShow>
			<DOC:canDelIsShow docId="${DOCE.doc.id}">
				<button type="button" id="delTopButtonId"
					class="btn btn-default  btn-xs">
					<span class="glyphicon glyphicon-envelope"></span>&nbsp;删除
				</button>
				<button type="button" id="publickButtonId"
					class="btn btn-default  btn-xs">
					<span class="glyphicon glyphicon-plane"></span>&nbsp;公开文档
				</button>
			</DOC:canDelIsShow>
		</c:if>
		<c:if test="${USEROBJ==null}">
			<button type="button" class="btn btn-default btn-xs"
				disabled="disabled">
				<span class="glyphicon glyphicon-star"></span>&nbsp;登录后关注
			</button>
		</c:if>
		<h4 class="pull-right">
			&nbsp; <span class="label label-default" title="访问量"><span
				class="glyphicon glyphicon-hand-up"></span>&nbsp;${DOCE.runinfo.visitnum}</span>
		</h4>
		<h4 class="pull-right">
			&nbsp; <a href="webdocmessage/Pubmsg.do?docid=${DOCE.doc.id}"><span
				class="label label-info" title="评论"><span
					class="glyphicon glyphicon-comment"></span>&nbsp;${DOCE.runinfo.answeringnum}</span></a>
		</h4>
	</div>
</c:if>
<script type="text/javascript">
	$(function() {
		$('#editTopButtonId').bind(
				'click',
				function() {
					window.location = basePath
							+ 'webfile/edit.do?docId=${DOCE.doc.id}';
				});
		$('#delTopButtonId').bind(
				'click',
				function() {
					if (confirm("删除后该知识将无法恢复，确定要删除吗？")) {
						window.location = basePath
								+ 'webdoc/FLDelKnow.do?id=${DOCE.doc.id}';
					}
				});
		$('#publickButtonId').bind(
				'click',
				function() {
					if (confirm("是否要将该文档开放阅读和编辑权限，同时如果是小组文档将删除小组所有权？")) {
						window.location = basePath
								+ 'webdoc/FLflyKnow.do?id=${DOCE.doc.id}';
					}
				});
		//关注
		$('#enjoyId')
				.live(
						'click',
						function() {
							$
									.post(
											'webdoc/enjoy.do?id=${DOCE.doc.id}',
											function(flag) {
												if (flag.commitType == '0') {
													alert('关注成功');
													var buttonStr = '<button type="button" id="disEnjoyId" class="btn btn-default  btn-xs"><span class="glyphicon glyphicon-star"></span>取消关注</button>';
													$('#enjoyId').before(
															buttonStr);
													$('#enjoyId').remove();
												} else {
													alert('关注失败');
												}
											}, 'json');
						});
		//取消关注
		$('#disEnjoyId')
				.live(
						'click',
						function() {
							$
									.post(
											'webdoc/FLunEnjoy.do?id=${DOCE.doc.id}',
											function(flag) {
												if (flag.commitType == '0') {
													alert('取消关注成功');
													var buttonStr = '<button type="button" id="enjoyId" class="btn btn-default  btn-xs"><span class="glyphicon glyphicon-star"></span>关注</button>';
													$('#disEnjoyId').before(
															buttonStr);
													$('#disEnjoyId').remove();
												} else {
													alert('取消关注失败');
												}
											}, 'json');
						});
	});
</script>