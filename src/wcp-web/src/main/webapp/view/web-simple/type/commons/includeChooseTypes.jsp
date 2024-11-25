<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">选择分类</h4>
			</div>
			<div class="modal-body" id="treeChooseTypeBoxId">
				<ul class="doctypeUl">
					<c:forEach var="node" items="${types}">
						<c:if test="${node.parentid=='NONE'}">
							<li>
								<h5 class="showLableType">
									<c:if test="${node.type=='1'}">
										<font style="color: #8f8f8f">${node.name}</font>
									</c:if>
									<c:if test="${node.type=='3'}">
										<a id="${node.id}">${node.name}</a>
									</c:if>
									<c:if test="${node.num>0}">
										<span style="color: #D9534F; font-weight: bold;">${node.num}</span>
									</c:if>
								</h5>
								<ul>
									<c:forEach var="node1" items="${types}">
										<c:if test="${node1.parentid==node.id}">
											<li>
												<h5 class="showLableType">
													<c:if test="${node1.type=='1'}">
														<font style="color: #8f8f8f">${node1.name}</font>
													</c:if>
													<c:if test="${node1.type=='3'}">
														<a id="${node1.id}">${node1.name}</a>
													</c:if>
													<c:if test="${node1.num>0}">
														<span style="color: #D9534F; font-weight: bold;">${node1.num}</span>
													</c:if>
												</h5>
												<ul>
													<c:forEach var="node2" items="${types}">
														<c:if test="${node2.parentid==node1.id}">
															<li>
																<h5 class="showLableType">
																	<c:if test="${node2.type=='1'}">
																		<font style="color: #8f8f8f">${node2.name}</font>
																	</c:if>
																	<c:if test="${node2.type=='3'}">
																		<a id="${node2.id}">${node2.name}</a>
																	</c:if>
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
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					关闭</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>