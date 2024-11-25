<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<!-- DOCID,title,DOCDESCRIBE,AUTHOR,PUBTIME,TAGKEY
	 *         ,IMGID,VISITNUM,PRAISEYES,PRAISENO,HOTNUM,EVALUATE,ANSWERINGNUM,
	 *         TYPENAME -->
<div class="row column_box">
	<div class="col-sm-12">
		<span class="glyphicon glyphicon-fire  column_title">小组知识</span>
	</div>
</div>
<div class="row">
	<div class="col-sm-12">
		<c:forEach items="${docs.resultList}" var="node">
			<div class="col-sm-12">
				<hr class="hr_split" />
			</div>
			<div class="col-sm-12 doc_node">
				<div class="media">
					<a class="pull-right"
						href="webgroup/Pubshow.do?groupid=${node.GROUPID}"
						style="max-width: 200px; text-align: center;"
						title="${node.GROUPNAME}"> <img
						class="img-rounded side_unit_author_img" alt="${node.GROUPNAME}"
						src="  <PF:basePath/>${node.IMGURL}"> <br /> <span
						class="side_unit_info">${node.GROUPNAME}</span>
					</a>
					<div class="media-body">
						<div style="margin-left: 4px;" class="pull-left">
							<div class="doc_node_title_box">
								<a class="doc_node_title_min"
									href="webdoc/view/Pub${node.DOCID}.html">${node.TITLE}<c:if
										test="${node.DOMTYPE=='3'}">
										<span class="glyphicon glyphicon-home"></span>
									</c:if> 
									<c:if test="${node.DOMTYPE=='1'}">
										<span class="glyphicon glyphicon-file"></span>
									</c:if>
									<c:if test="${node.DOMTYPE=='4'}">
										<span class="glyphicon glyphicon-bookmark"></span>
									</c:if> 
									<c:if test="${node.DOMTYPE=='5'}">
										<span style="color: #d9534f;"
											class="glyphicon glyphicon-download-alt"></span>
									</c:if>
								</a>
							</div>
							<div class="doc_node_content_box">
								<p class="doc_node_content">${node.DOCDESCRIBE}</p>
							</div>
							<div class="side_unit_info side_unit_info_left_box">

								<a href="webtype/view${node.TYPEID}/Pub1.html">${node.TYPENAME}</a>
								阅读(${node.VISITNUM}) 评论(${node.ANSWERINGNUM})
								<PF:FormatTime date="${node.PUBTIME}"
									yyyyMMddHHmmss="yyyy-MM-dd HH:mm" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
<c:if test="${fun:length(docs.resultList) > 0}">
	<div class="row">
		<div class="col-xs-12">
			<ul class="pagination pagination-sm">
				<c:forEach var="page" begin="${docs.startPage}"
					end="${docs.endPage}" step="1">
					<c:if test="${page==docs.currentPage}">
						<li class="active"><a>${page} </a></li>
					</c:if>
					<c:if test="${page!=docs.currentPage}">
						<li><a href="webgroup/Pubshow.do?groupid=${groupid }&pagenum=${page}">${page}</a></li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>