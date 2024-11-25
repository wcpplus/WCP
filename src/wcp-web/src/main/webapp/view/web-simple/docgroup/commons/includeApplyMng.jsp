<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div id="groupApplyLoadDivId">
	<!-- 申请人管理 -->
	<div class="row">
		<div class="col-md-12">
			<div class="table table-condensed table-bordered">
				<table class="table">
					<tr>
						<th>用户</th>
						<th>申请留言</th>
						<th>操作</th>
					</tr>
					<c:forEach items="${applys.resultList}" var="node">
						<tr>
							<td>${node.NAME}</td>
							<td>${node.APPLYNOTE}</td>
							<td><a href="javascript:agreeApply('${node.GROUPUSERID}')"
								type="button" class="btn btn-primary btn-xs"> 同意 </a> <a
								href="javascript:refuseApply('${node.GROUPUSERID}')" type="button"
								class="btn btn-warning btn-xs"> 拒绝 </a></td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		function agreeApply(groupUserId) {
			if (confirm('是否同意该用户加入小组？')) {
				$.post('webgroup/agreeApply.do', {
					"groupUserId" : groupUserId
				}, function(flag) {
					if (flag.STATE == '0') {
						window.location.href = basePath +'webgroup/userMag.html?groupid=${groupid}';
					} else {
						alert(flag.MESSAGE);
					}
				},'json');
			}
		}
		function refuseApply(groupUserId) {
			if (confirm('是否拒绝该用户加入小组？')) {
				$.post('webgroup/refuseApply.do', {
					"groupUserId" : groupUserId
				}, function(flag) {
					if (flag.STATE == '0') {
						window.location.href = basePath +'webgroup/userMag.html?groupid=${groupid}';
					} else {
						alert(flag.MESSAGE);
					}
				},'json');
			}
		}
	//-->
	</script>
</div>
