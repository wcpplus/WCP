function initPhotoImgsSize(domindex) {
	$(domindex).each(function(i, obj) {
		initPhotoImgSize(obj);
	});
}
function initPhotoImgSize(obj) {
	// 通過寬度設置高度210mm×297mm
	var width = $(obj).width();
	var height = 210 * width / 297;
	$(obj).height(height);
	// 判斷圖片高寬
	// 图片地址 后面加时间戳是为了避免缓存
	var img_url = $(obj).find("img").attr("src");
	if(!img_url){
		return;
	}
	var img = new Image();
	img.src = img_url;
	var imgWidth = img.width;
	var imgHeight = img.height;
	if (imgWidth / imgHeight > width / height) {
		$(obj).find("img").height(height);
	} else {
		$(obj).find("img").width(width);
	}
}