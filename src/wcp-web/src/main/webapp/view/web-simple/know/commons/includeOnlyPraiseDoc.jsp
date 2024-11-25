<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!-- 文档中的目录导航 -->
<style>
<!--
.affix {
	top: 54;
	width: 230px;
}
-->
</style>
<div class="panel panel-default " id="docContentMenuId"
	style="z-index: 1000;">
	<div class="panel-heading">
		<div style="text-align: center;">
			<jsp:include page="includeDocEvaluate.jsp"></jsp:include>
		</div>
	</div>
</div>
<script type="text/javascript">
	function initLeftMenu() {
		$('#docContentMenuId').affix( {
			offset : {
				top : 50,
				bottom : 5
			}
		})
		$('h1,h2,h3', '#docContentsId').each(function(i, obj) {
			creatMenus(i, obj);
			$(obj).before("<a name='mark" + i + "'></a>");
			$('#linkmark' + i).bind('click', function() {
				//location.hash = "mark" + i;
					$('html,body').animate( {
						scrollTop : $("a[name='mark" + i + "']").offset().top
					}, 500);
				});
		});
		if ($.trim($('#ContentMenuId').text())!= null&&$.trim($('#ContentMenuId').text())!= "") {
			$('#ContentMenuId').css('height', $(window).height() - 300);
		} else {
			$('#docContentPanelbody').remove();
		}
		$('#ContentMenuId').css('overflow-y', 'auto');
	}
	$(function() {
		initLeftMenu();
	});
</script>