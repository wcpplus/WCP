<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<div class="row column_box">
	<div class="col-sm-12">
		<span class="glyphicon glyphicon-star  column_title">最新评论 </span>
	</div>
</div>
<div class="row">
	<div class="col-sm-12">
		<c:forEach items="${result.resultList}" var="node">
			<div class="col-sm-12">
				<hr class="hr_split" />
			</div>
			<div class="col-sm-12 doc_node">
				<div class="media message_boxunit">
					<a class="pull-left"
						href="webuser/PubHome.do?userid=${node.CUSER }"
						style="max-width: 200px; text-align: center;"
						title="${node.CUSERNAME }"> <img
						class="img-circle side_unit_author_img_min"
						alt="${node.CUSERNAME}" src="<PF:basePath/>${node.IMGURL}">
						<br />
					</a>
					<div class="media-body">
						<div class="side_unit_info">
							<div class="side_unit_info">
								<span class="side_unit_info"> <a
									href="webuser/PubHome.do?userid=${node.CUSER }">${node.CUSERNAME }</a>
									${node.CTIME }
								</span>
							</div>
							<div class="doc_node_content" style="margin: 4px;">${node.CONTENT}</div>
							<div class="side_unit_info">
								<button id="approveOf${node.ID }" type="button"
									class="btn btn-default btn-xs"
									onclick="approveOf('${node.ID }')">赞同(${node.PRAISNUM })</button>
								<button id="oppose${node.ID }" type="button"
									class="btn btn-default btn-xs" onclick="oppose('${node.ID }')">反对(${node.CRITCISMNUM })</button>
								<button type="button" onclick="replyClick('${node.ID}')"
									class="btn btn-info btn-xs">回复</button>
							</div>
							<div class="side_unit_info message_reply_box"
								id="reply${node.ID}box" style="margin-top: 4px;">
								<form class="form-inline" action="webdocmessage/reply.do"
									method="post">
									<input type="hidden" name="id" value="${node.ID }" /> <input
										type="hidden" name="appid" value="${node.APPID }" />
									<div class="form-group">
										<input type="text" id="inputPassword2" name="content"
											class="form-control" style="width: 400px;">
									</div>
									<button type="submit" class="btn btn-default">回复</button>
								</form>
							</div>
						</div>
						<div class="side_unit_info">
							<ul class="list-group" style="margin-top: 8px;">
								<c:forEach var="replay" items="${node.replys }">
									<li class="list-group-item"><span class="side_unit_info">
											<a href="webuser/PubHome.do?userid=${replay.CUSER }"
											class="color_a"> ${replay.CUSERNAME }</a> <PF:FormatTime
												date="${replay.CTIME}" yyyyMMddHHmmss="yyyy-MM-dd HH:mm" />
									</span>：<br/><br/>${replay.CONTENT }</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
<c:if test="${fun:length(result.resultList) > 0}">
	<div class="row">
		<div class="col-xs-12">
			<ul class="pagination pagination-sm">
				<c:forEach var="page" begin="${result.startPage}"
					end="${result.endPage}" step="1">
					<c:if test="${page==result.currentPage}">
						<li class="active"><a>${page} </a></li>
					</c:if>
					<c:if test="${page!=result.currentPage}">
						<li><a href="webtype/view${typeid}/Pub${page}.html?pagenum=">${page}</a></li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>
<script type="text/javascript">
	$(function() {
		$('.message_reply_box').hide();
		$('.message_operate_box').hide();
		$(".message_boxunit").mouseenter(function() {
			$('.message_operate_box', this).show();
		});
		$(".message_boxunit").mouseleave(function() {
			$('.message_operate_box').hide();
			$('.message_reply_box').hide();
		});
	});
	function replyClick(messageId) {
		$('.message_reply_box').hide();
		$('#reply' + messageId + 'box').show();
	}
	//赞同
	function approveOf(id) {
		$.ajax({
			type : "post",
			url : "webdocmessage/approveOf.do?id=" + id,
			dataType : "json",
			async : false,
			success : function(obj) {
				$("#approveOf" + id).html(
						"赞同(" + obj.farmDocmessage.praisnum + ")");
			}
		});

	}

	//反对
	function oppose(id) {
		$.ajax({
			type : "post",
			url : "webdocmessage/oppose.do?id=" + id,
			dataType : "json",
			async : false,
			success : function(obj) {
				$("#oppose" + id).html(
						"反对(" + obj.farmDocmessage.critcismnum + ")");
			}
		});
	}
</script>