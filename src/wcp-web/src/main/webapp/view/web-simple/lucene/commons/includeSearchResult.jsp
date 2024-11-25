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
		<span class="glyphicon glyphicon-fire  column_title">检索到${result.totalSize}条结果,用时${result.runtime}毫秒
		</span>
	</div>
</div>
<div class="row">
	<div class="col-sm-12">
		<c:forEach items="${result.resultList}" var="node">
			<div class="col-sm-12">
				<hr class="hr_split" />
			</div>
			<div class="col-sm-12 doc_node">
				<div class="media">
					<div class="media-body">
						<div style="margin-left: 4px;" class="pull-left">
							<div class="doc_node_title_box">
								<c:if test="${node.DOMTYPE=='file'}">
									<a class="doc_node_title_min"
										href="webdoc/view/PubFile${node.ID}.html">${node.TITLE}<span
										style="color: #d9534f;"
										class="glyphicon glyphicon-download-alt"></span></a>
								</c:if>
								<c:if test="${node.DOMTYPE!='file'}">
									<a class="doc_node_title_min"
										href="webdoc/view/Pub${node.ID}.html">${node.TITLE}<c:if
											test="${node.DOMTYPE=='3'}">
											<span class="glyphicon glyphicon-home"></span>
										</c:if> <c:if test="${node.DOMTYPE=='1'}">
											<span class="glyphicon glyphicon-file"></span>
										</c:if> <c:if test="${node.DOMTYPE=='4'}">
											<span class="glyphicon glyphicon-bookmark"></span>
										</c:if> <c:if test="${node.DOMTYPE=='5'}">
											<span style="color: #d9534f;"
												class="glyphicon glyphicon-folder-open"></span>
										</c:if>
									</a>
								</c:if>
							</div>
							<div class="doc_node_content_box">
								<p class="doc_node_content">${node.DOCDESCRIBE}</p>
							</div>
							<div class="side_unit_info side_unit_info_left_box">
								<a href="webuser/PubHome.do?userid=${node.USERID}"
									title="${node.AUTHOR}"> <span title=""
									class="side_unit_info">${node.AUTHOR}</span>
								</a>
								<c:if test="${node.DOMTYPE=='file'}">
									<a href="webdoc/view/Pub${node.DOCID}.html">知识来源</a>
								</c:if>
								<c:if test="${node.DOMTYPE!='file'}">
									<a href="webtype/view${node.TYPEID}/Pub1.html">${node.TYPENAME}</a>
								</c:if>
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
						<li><a onclick="luceneSearchGo(${page})">${page}</a></li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>