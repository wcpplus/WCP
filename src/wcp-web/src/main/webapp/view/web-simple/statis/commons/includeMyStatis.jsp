<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="java.net.InetAddress"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="panel panel-default " style="background-color: #FCFCFA;">
	<div class="panel-body">
		<p>
			<span class="glyphicon glyphicon-stats  ">个人贡献</span>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<span style="color: #959486;"> <c:forEach
					items="${users.resultList}" varStatus="status" var="node">
					<small>当前用户于</small>
					<strong> <PF:FormatTime date="${node.CTIME}"
							yyyyMMddHHmmss="yyyy年MM月dd日" /> </strong>
					<em> 建立账户</em>
					<small>，于</small>
					<strong><PF:FormatTime date="${node.STIME}"
							yyyyMMddHHmmss="yyyy年MM月dd日" /> </strong>
					<small>发表</small>
					<em>第一篇</em>
					<small>文档，于</small>
					<strong><PF:FormatTime date="${node.ETIME}"
							yyyyMMddHHmmss="yyyy年MM月dd日" /> </strong>
					<small>发表</small>
					<em>最后一篇</em>
					<small>文档 </small>
				</c:forEach> </span>
		</p>
		<div class="table-responsive">
			<table class="table" style="font-size: 12px;">
				<tr>
					<th rowspan="2">
						<c:if test="${photourl!=null}">
							<img id="imgShowBoxId" src="${photourl}" alt="wcp"
								style="max-height: 80px; max-width: 80px;" class="img-thumbnail" />
						</c:if>
						<c:if test="${photourl==null}">
							<img id="imgShowBoxId"
								src="<PF:basePath/>text/img/none.png" alt="wcp"
								style="max-height: 80px; max-width: 80px;" class="img-thumbnail" />
						</c:if>
					</th>
					<th>
						用户
					</th>
					<th>
						文档数量
					</th>
					<th>
						好评文章
					</th>
					<th>
						差评文章
					</th>
					<th>
						文章好评率
					</th>
					<th>
						被访问总量
					</th>
					<th>
						累计好评
					</th>
					<th>
						累计差评
					</th>
					<th>
						综合评价:
						<span style="color: #BA1D1D;" class="glyphicon glyphicon-star"></span>
						<span style="color: #ED8A16;" class="glyphicon glyphicon-star"></span>
						<span class="glyphicon glyphicon-star"></span>
						<span class="glyphicon glyphicon-star-empty"></span>
					</th>
				</tr>
				<c:forEach items="${users.resultList}" varStatus="status"
					var="node">
					<tr>
						<td>
							${node.NAME}
						</td>
						<td>
							${node.NUM}
						</td>
						<td>
							${node.GOODNUM}
						</td>
						<td>
							${node.BADNUM}
						</td>
						<td>
							<c:if test="${node.NUM==0}">
								0
							</c:if>
							<c:if test="${node.NUM!=0}">
								${node.GOODNUM*100/node.NUM}%
							</c:if>
						</td>
						<td>
							${node.VINUM}
						</td>
						<td>
							${node.OKNUM}
						</td>
						<td>
							${node.NONUM}
						</td>
						<td title="${node.EVANUM}" style="font-size: 14px;">
							<c:forEach begin="1" end="${node.L4}">
								<span style="color: #BA1D1D;" class="glyphicon glyphicon-star"></span>
							</c:forEach>
							<c:forEach begin="1" end="${node.L3}">
								<span style="color: #ED8A16;" class="glyphicon glyphicon-star"></span>
							</c:forEach>
							<c:forEach begin="1" end="${node.L2}">
								<span class="glyphicon glyphicon-star"></span>
							</c:forEach>
							<c:forEach begin="1" end="${node.L1}">
								<span class="glyphicon glyphicon-star-empty"></span>
							</c:forEach>
							&nbsp;(&nbsp;${node.EVANUM}&nbsp;)&nbsp;
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</div>