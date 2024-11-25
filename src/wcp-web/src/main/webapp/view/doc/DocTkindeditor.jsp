<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<a id="completebtn" class="easyui-linkbutton" style="margin: 8px;"
	data-options="iconCls:'icon-search'">完成</a>
<div style="margin: 8px; width: 95%;">
	<textarea name="editcontent"
		style="height: 400px; width: 100%; border: 0px;"></textarea>
</div>
<script type="text/javascript">
	var editor;
	$(function() {
		$('#winEditDoc').window('maximize');
		editor = KindEditor.create('textarea[name="editcontent"]', {
			resizeType : 1,
			uploadJson : basePath + 'actionImg/PubFPuploadImg.do',
			allowPreviewEmoticons : false,
			allowImageUpload : true,
			formatUploadUrl : false,
			items : [ 'source', '|', 'fontname', 'fontsize', '|', 'forecolor',
					'hilitecolor', 'bold', 'italic', 'underline',
					'removeformat', '|', 'justifyleft', 'justifycenter',
					'justifyright', 'insertorderedlist', 'insertunorderedlist',
					'|', 'emoticons', 'image', 'link' ]
		});
		editor.html($('#contentId').val());
		$('#completebtn').bind('click', function() {
			$('#contentId').val(editor.html());
			$('#winEditDoc').window('close');
		});
	});
	//-->
</script>