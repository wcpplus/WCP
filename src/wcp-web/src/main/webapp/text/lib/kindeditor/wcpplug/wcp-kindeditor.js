var wcpKnowdialog;
var wcpKnowEditor;
KindEditor.plugin('wcpknow',function(K) {
	wcpKnowEditor = this, name = 'wcpknow';
	wcpKnowEditor.clickToolbar(name,function() {
		wcpKnowdialog = K
			.dialog( {
				width : 250,
				title : '选择知识',
				body : '<div style="margin:10px;text-align: center;"><input id="wcpknow_input_id" type="text"/><span class="ke-button-common ke-button-outer ke-dialog-yes" title="查找"><input class="ke-button-common ke-button" id="wcpknow_button_id" value="查找" type="button"></span><br/><div class="wcpknow_search_rbox_c" id="wcpknow_search_rbox"></div></div>',
				closeBtn : {
					name : '关闭',
					click : function(e) {
						wcpKnowdialog.remove();
					}
				}
			});
		loadKnow_wcpKnow();
		$('#wcpknow_button_id').bind('click',function() {
			loadKnow_wcpKnow($('#wcpknow_input_id').val());
		});
		$('#wcpknow_input_id').keydown(function(e){
			if(e.keyCode==13){
				loadKnow_wcpKnow($(
				'#wcpknow_input_id')
				.val());
			}
		});
	});
});
function loadKnow_wcpKnow(knowtitle) {
	$('#wcpknow_search_rbox').html('loading...');
	$.post('home/FPsearchKnow.do', {'knowtitle' : knowtitle}, function(flag) {
		if (flag.size > 0) {
			$('#wcpknow_search_rbox').html('');
			$(flag.list).each(
					function(i, obj) {
						$('#wcpknow_search_rbox').append(
								'<a onClick="clickLink_wcpKnow(this)" id="'
										+ obj.ID + '" wcptype="'
										+ obj.DOMTYPE + '">' + obj.TITLE
										+ '</a></br>');
					});
		} else {
			alert('未匹配到相关知识!');
		}
	}, 'json');
}
function clickLink_wcpKnow(flag) {
	var id = $(flag).attr('id');
	var title = $(flag).text();
	var type = $(flag).attr('wcptype');
	wcpKnowEditor.insertHtml('<a href="webdoc/view/Pub'+id+'.html">' + title + '</a>');
	wcpKnowdialog.remove();
}


KindEditor.lang({ wcpknow : '插入知识' });