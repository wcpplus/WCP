<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<div class="widget-box shadow-box">
	<div class="stream-list p-stream">
		<div class="stream-item" id="loop-30">
			<p>
				<c:forEach items="${docgroup.tags}" var="tag">
					<span class="label label-info tagsearch" title="${tag}"
						style="font-weight: lighter; font-size: 12px; cursor: pointer;">${tag}</span>
				</c:forEach>
			</p>
			<a class="thumbnail"> <c:if test="${docgroup.groupimg==null}">
					<img src="UI-WEB/docgroup/commons/imgDemo.png" alt="小组">
				</c:if> <c:if test="${docgroup.groupimg!=null}">
					<img src="${docgroup.imgurl}" alt="小组">
				</c:if>
			</a>
			<p>${docgroup.groupnote}</p>
		</div>
	</div>
</div>
<div class="widget-box shadow-box">
	<div class="title">
		<h3>
			<i class="glyphicon glyphicon-fire"></i> 管理员
		</h3>
	</div>
	<div class="stream-list p-stream">
		<div class="stream-item" id="loop-30">
			<c:forEach items="${docgroup.admins}" var="note">
				<div style="float: left;">
					<a href="webuser/PubHome.do?userid=${note.id}" title="${note.name}"
						style="font-size: 12px; margin: auto; margin: 4px;"> <img
						class="img-circle" style="max-width: 45px; max-height: 45px;"
						src="<DOC:ImgUrl fileid="${note.imgid}"/>" alt="${note.name}"></a>
				</div>
			</c:forEach>
			<div style="clear: both;">&nbsp;</div>
		</div>
	</div>
</div>
<div class="widget-box shadow-box">
	<div class="title">
		<h3>
			<i class="glyphicon glyphicon-fire"></i> 小组成员
		</h3>
	</div>
	<div class="stream-list p-stream">
		<div class="stream-item" id="loop-30">
			<c:forEach items="${docgroup.users}" var="note">
				<div style="float: left;">
					<a href="webuser/PubHome.do?userid=${note.id}" title="${note.name}"
						style="font-size: 12px; margin: auto; margin: 4px;"> <img
						class="img-circle" style="max-width: 45px; max-height: 45px;"
						src="<DOC:ImgUrl fileid="${note.imgid}"/>" alt="${note.name}"></a>
				</div>
			</c:forEach>
			<div style="clear: both;">&nbsp;</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		//标签
		$('.tagsearch').bind('click', function() {
			luceneSearch('TYPE:' + $(this).attr('title'));
		});
	});
	function leaveGroupFunc(id) {
		//判断是否有别的管理员，没有的话提示用户
		if (confirm('是否确定退出小组？')) {
			if ('${usergroup.leadis}' != '1') {
				//如果用户非管理员则直接退出
				window.location = basePath + 'webgroup/leaveGroup.do?groupId='
						+ id;
				return false;
			}

			$.post('webgroup/haveAdministratorIs.html', {
				"groupId" : id
			}, function(flag) {
				if (flag.adminNum > 1) {
					window.location = basePath
							+ 'webgroup/leaveGroup.do?groupId=' + id;
				} else {
					if (confirm('如果退出该小组，该小组将没有任何管理员。是否确定退出？')) {
						window.location = basePath
								+ 'webgroup/leaveGroup.do?groupId=' + id;
					}
				}
			});
		}
	}
//-->
</script>