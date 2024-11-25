<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<style>
<!--
.farm_tree_flag {
	color: #c3c3c3;
	text-decoration: none;
}

.farm_tree_h {
	color: #c3c3c3;
}

.farm_tree_s {
	color: #e2e2e2;
}
-->
</style>
<div class="row column_box">
	<div class="col-sm-12">
		<span class="glyphicon glyphicon-fire  column_title">知识分类</span>
	</div>
</div>
<div class="panel panel-default  side_column">
	<div class="panel-body">
		<ul class="doctypeUl">
			<c:forEach var="node" items="${types}">
				<c:if test="${node.parentid=='NONE'}">
					<li>
						<h5 class="showLableType">
							<a id="${node.id}">${node.name}</a>
							<c:if test="${node.num>0}">
								<span style="color: #D9534F; font-weight: bold;">${node.num}</span>
							</c:if>
						</h5>
						<ul>
							<c:forEach var="node1" items="${types}">
								<c:if test="${node1.parentid==node.id}">
									<li>
										<h5 class="showLableType">
											<a id="${node1.id}">${node1.name}</a>
											<c:if test="${node1.num>0}">
												<span style="color: #D9534F; font-weight: bold;">${node1.num}</span>
											</c:if>
											<a class="glyphicon glyphicon-chevron-down farm_tree_flag"></a>
										</h5>
										<ul>
											<c:forEach var="node2" items="${types}">
												<c:if test="${node2.parentid==node1.id}">
													<li>
														<h5 class="showLableType">
															<a id="${node2.id}"> ${node2.name}</a>
															<c:if test="${node2.num>0}">
																<span style="color: #D9534F; font-weight: bold;">${node2.num}</span>
															</c:if>
														</h5>
													</li>
												</c:if>
											</c:forEach>
										</ul>
									</li>
								</c:if>
							</c:forEach>
						</ul>
					</li>
				</c:if>
			</c:forEach>
		</ul>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		$('a', '.showLableType').bind(
				'click',
				function() {
					if ($(this).attr('id')) {
						window.location = basePath + "webtype/view"
								+ $(this).attr('id') + "/Pub1.html";
					}
				});

		$('.farm_tree_flag').each(function(i, o) {
			if($(this).parent().next().contents().length<=1){
				$(this).removeClass("farm_tree_flag");
				$(this).removeClass("glyphicon");
				$(this).removeClass("glyphicon-chevron-down");
			}else{
				$(this).parent().next().hide();
			}
		});
		$('.farm_tree_flag').bind('click', function() {
			var ul = $(this).parent().next();
			if ($(ul).is(':hidden')) {
				$(this).parent().next().show();
				$(this).removeClass("farm_tree_s");
				$(this).addClass("farm_tree_h");
				$(this).removeClass("glyphicon-chevron-down");
				$(this).addClass("glyphicon-chevron-up");
			} else {
				$(this).parent().next().hide();
				$(this).removeClass("farm_tree_h");
				$(this).addClass("farm_tree_s");
				$(this).removeClass("glyphicon-chevron-up");
				$(this).addClass("glyphicon-chevron-down");
			}
		});
	});
</script>