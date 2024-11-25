<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>

<c:if test="${!empty topRecommends}">
	<div class="row">
		<div class="col-sm-12">
			<div class="row" style="padding-bottom: 0px;">
				<c:forEach var="topDoc" items="${topRecommends}" varStatus="status">
					<div class="col-xs-3 wcp-photo-col" style="padding: 10px;">
						<div class="wcp-photo-box"
							style="background-color: #ffffff; border: 1px solid #eee; padding: 20px; border-radius: 5px;">
							<!-- up -->
							<c:if test="${topDoc.imgurl!=null}">
								<div class="wcp-photo-imgbox" style="overflow: hidden;">
									<a class="doc_node_title" style=" user-select: none;"
										target="${config_sys_link_newwindow_target}"
										href="${topDoc.exlink}"> <img alt="${topDoc.title}"
										class="effect-img" src="${topDoc.imgurl}">
									</a>
								</div>
							</c:if>
							<div class="wcp-photo-contentbox">
								<div class="wcp-photo-titlebox"
									style="text-align: center; margin: 10px; color: #8c8c8c; font-size: 13px; padding: 10px; padding-top: 0px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; padding-bottom: 0px; margin-bottom: 0px;">
									<a target="${config_sys_link_newwindow_target}"
										href="${topDoc.exlink}">${topDoc.title}</a>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</c:if>
<script src="text/javascript/wcp-photoImgs.js"></script>
<script type="text/javascript">
	$(function() {
		initPhotoImgsSize('.wcp-photo-imgbox');
		$(window).resize(function() {
			initPhotoImgsSize('.wcp-photo-imgbox');
		});
		$("img").on('load', function() {
			initPhotoImgSize($(this).parents('.wcp-photo-imgbox'));
		});
	});
</script>