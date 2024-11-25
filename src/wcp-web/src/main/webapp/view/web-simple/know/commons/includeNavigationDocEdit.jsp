<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!-- 文档中的目录导航 -->
<div class="panel panel-default " id="docContentMenuId">
	<div class="panel-heading" style="font-size: 14px;">
		<span class="glyphicon glyphicon-list-alt"></span> 目录
	</div>
	<div class="panel-body"
		style="line-height: 20px; list-style: none; padding: 4px;">
		<ul id="ContentMenuId" style="list-style-type: none"></ul>
	</div>
	<div id="docContentsId" style="height: 0px; overflow: hidden;"></div>
</div>
<script type="text/javascript">
	var n1 = 1;
	var n2 = 1;
	var n3 = 1;
	function creatMenus(i, obj) {
		if ($(obj).get(0).tagName == 'H1') {
			$('#ContentMenuId').append(
					'<li class="doc_vavigat_h1">' + n1 + $(obj).text()
							+ '</li>');
			n1++;
			n2 = 1;
			n3 = 1;
		}
		if ($(obj).get(0).tagName == 'H2') {
			$('#ContentMenuId').append(
					'<li class="doc_vavigat_h2">' + (n1 - 1) + '.' + n2
							+ $(obj).text() + '</li>');
			n2++;
			n3 = 1;
		}
		if ($(obj).get(0).tagName == 'H3') {
			$('#ContentMenuId').append(
					'<li class="doc_vavigat_h3">' + (n1 - 1) + '.' + (n2 - 1)
							+ '.' + n3 + $(obj).text() + '</li>');
			n3++;
		}
	}
	function initLeftMenuFromHtml(html) {
		$('#ContentMenuId').html('');
		try {
			$('#docContentsId').html(html);
		} catch (e) {
		}
		n1 = 1;
		n2 = 1;
		n3 = 1;
		$('h1,h2,h3', '#docContentsId').each(function(i, obj) {
			creatMenus(i, obj);
		});
		$('#ContentMenuId').css('height', $(window).height() - 300);
		$('#ContentMenuId').css('overflow-y', 'auto');
	}
</script>