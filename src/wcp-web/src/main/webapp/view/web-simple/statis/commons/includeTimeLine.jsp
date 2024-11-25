<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="java.net.InetAddress"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<script src="text/javascript/highstock.1.1.js"></script>
<script type="text/javascript">
	$(function() {
		$.getJSON('webstat/PubStatAll.html', function(data) {
			var color = {
				linearGradient : {
					x1 : 0,
					y1 : 0,
					x2 : 0,
					y2 : 1
				},
				stops : [
						[ 0, Highcharts.getOptions().colors[0] ],
						[
								1,
								Highcharts.Color(
										Highcharts.getOptions().colors[0])
										.setOpacity(0).get('rgba') ] ]
			};
			var groupingUnits = [ [ 'week', // unit name 
			[ 1 ] // allowed multiples 
			], [ 'month', [ 1, 2, 3, 4, 6 ] ] ];
			$('#container').highcharts('StockChart', {
				rangeSelector : {
					selected : 1,
					inputEnabled : $('#container').width() > 480
				},
				yAxis : [ {
					labels : {
						align : 'right',
						x : -3
					},
					title : {
						text : '文档总量统计'
					},
					height : '60%',
					lineWidth : 2
				}, {
					labels : {
						align : 'right',
						x : -3
					},
					title : {
						text : '每日发布统计'
					},
					top : '65%',
					height : '35%',
					offset : 0,
					lineWidth : 2
				} ],
				series : [ {
					name : '累计文档数量',
					data : data.nums.TotalNum,
					tooltip : {
						valueDecimals : 2
					},
					type : 'areaspline',
					marker : {
						enabled : true,
						radius : 3
					},
					shadow : true,
					fillColor : color
				}, {
					name : '当天发布数量',
					data : data.nums.DayNum,
					type : 'column',
					yAxis : 1,
					tooltip : {
						valueDecimals : 2
					},
					dataGrouping : {
						units : groupingUnits
					}
				}, {
					name : '累计好评文档',
					data : data.nums.GoodNum,
					shadow : true
				}, {
					name : '累计差评文档',
					data : data.nums.BadNum,
					shadow : true
				} ]
			});
		});
	});
</script>
<div class="panel panel-default" style="background-color: #FCFCFA;">
	<div class="panel-body">
		<p>
			<span class="glyphicon glyphicon-stats  ">系统使用统计</span>
		</p>
		<hr />
		<div class="panel panel-default" style="background-color: white;">
			<div class="panel-body">
				<div id="container">加载中... ...</div>
			</div>
		</div>
	</div>
</div>