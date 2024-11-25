<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div id="RelationdocsBoxId">
	<!-- 查询和文档内容相关的知识列表，传入文档id -->
	<div class="row doc_column_box">
		<div class="col-sm-12">
			<span class="glyphicon glyphicon-resize-small column_title">相关知识</span>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<hr class="hr_split" />
			<div id="luceneResultMinId">
				<ol id="luceneResultMinOlId">
				</ol>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		$('#luceneResultMinOlId').append('<li>加载中~</li>');
		$.post('websearch/PubRelationDocs.do', {
			docid : '${DOCE.doc.id}'
		}, function(flag) {
			$('#luceneResultMinOlId').empty();
			$($.parseJSON(flag).RELATIONDOCS).each(function(i,obj){
				if(obj.domtype!='file'){
					$('#luceneResultMinOlId').append('<li><a href="webdoc/view/Pub'+obj.docid+'.html">'+obj.title+'</a></li>');
				}
			});
		});
	});
</script>