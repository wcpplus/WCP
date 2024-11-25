<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div id="groupUserLoadDivId">
	<!-- 普通用户管理 -->
	<div class="row">
		<div class="col-md-12">
			<div class="table table-condensed table-bordered">
				<table class="table">
					<tr>
						<th>用户</th>
						<th>可编辑</th>
						<th>操作</th>
					</tr>
					<c:forEach items="${users.resultList}" var="node">
						<tr>
							<td><c:if test="${node.LEADIS=='1'}">
									<b><span class="glyphicon glyphicon-user"></span> ${node.NAME}</b>
								</c:if> <c:if test="${node.LEADIS=='0'}">
									${node.NAME}
								</c:if></td>
							<td><c:if test="${node.EDITIS=='1'}">
								是 
								</c:if> <c:if test="${node.EDITIS=='0'}">
								否
								</c:if></td>
							<td><c:if test="${node.LEADIS=='0'}">
									<a href="javascript:setGroupAdminUser('${node.GROUPUSERID}')"
										class="btn btn-primary btn-xs"> 设置为管理员 </a>
								</c:if> <c:if test="${node.LEADIS=='1'}">
									<a href="javascript:wipeManagePop('${node.GROUPUSERID}')"
										class="btn btn-warning btn-xs"> 取消管理权限 </a>
								</c:if> <c:if test="${node.EDITIS=='0'}">
									<a href="javascript:setGroupEditorUser('${node.GROUPUSERID}')"
										class="btn btn-primary btn-xs"> 授予编辑权限 </a>
								</c:if> <c:if test="${node.EDITIS=='1'}">
									<a href="javascript:wipeGroupEditorUser('${node.GROUPUSERID}')"
										class="btn btn-warning btn-xs"> 取消编辑权限 </a>
								</c:if> <c:if test="${node.LEADIS=='0'}">
									<a href="javascript:quitGourp('${node.GROUPUSERID}')"
										class="btn btn-danger btn-xs"> 退出小组 </a>
								</c:if></td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<ul class="pagination pagination-sm">
				<c:forEach var="page" begin="${users.startPage}"
					end="${users.endPage}" step="1">
					<c:if test="${page==users.currentPage}">
						<li class="active"><a>${page} </a></li>
					</c:if>
					<c:if test="${page!=users.currentPage}">
						<li><a href="webgroup/userMag.html?groupid=${groupid}&page=${page}">${page} </a>
						</li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>
	<script type="text/javascript">
		function setGroupAdminUser(groupUserId) {
			if (confirm('是否将该用户设置为小组管理员？')) {
				$.post('webgroup/groupSetAdmin.do', {
					groupUserId : groupUserId
				}, function(flag) {
					if (flag.STATE == '0') {
						window.location.href = basePath +'webgroup/userMag.html?groupid=${groupid}';
					} else {
						alert(flag.pageset.message);
					}
				}, 'json');
			}
		}
		function setGroupEditorUser(groupUserId) {
			if (confirm('是否允许该用户编辑小组文档？')) {
				$.post('webgroup/groupSetEditor.do', {
					groupUserId : groupUserId
				}, function(flag) {
					if (flag.STATE == '0') {
						window.location.href = basePath +'webgroup/userMag.html?groupid=${groupid}';
					} else {
						alert(flag.MESSAGE);
					}
				},'json');
			}
		}
		function wipeGroupEditorUser(groupUserId) {
			if (confirm('是否取消该用户小组文档编辑权限？')) {
				$.post('webgroup/groupWipeEditor.do', {
					groupUserId : groupUserId
				}, function(flag) {
					if (flag.STATE == '0') {
						window.location.href = basePath +'webgroup/userMag.html?groupid=${groupid}';
					} else {
						alert(flag.MESSAGE);
					}
				}, 'json');
			}
		}
		function wipeManagePop(groupUserId) {
			if (confirm('是否取消该用户小组管理员权限？')) {
				$.post('webgroup/wipeAdmin.do', {
					groupUserId : groupUserId
				}, function(flag) {
					if (flag.STATE == '0') {
						window.location.href = basePath +'webgroup/userMag.html?groupid=${groupid}';
					} else {
						alert(flag.MESSAGE);
					}
				} ,'json');
			}
		}
		function quitGourp(groupUserId) {
			if (confirm('是否将该用户退出小组？')) {
				$.post('webgroup/groupQuit.do', {
					groupUserId : groupUserId
				}, function(flag) {
					if (flag.STATE == '0') {
						window.location.href = basePath +'webgroup/userMag.html?groupid=${groupid}';
					} else {
						alert(flag.MESSAGE);
					}
				}, 'json');
			}
		}
	//-->
	</script>
</div>
