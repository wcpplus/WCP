package com.farm.doc.server;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.farm.core.auth.domain.LoginUser;
import com.farm.doc.domain.FarmDocfile;

public interface FarmFileManagerInter {
	/**
	 * 附件类型
	 * 
	 * @author 王东
	 * 
	 */
	public enum FILE_TYPE {
		HTML_INNER_IMG("1"), RESOURCE_FILE("2"), RESOURCE_ZIP("3"), OHTER("0"), WEB_FILE("4");
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		FILE_TYPE(String value) {
			this.value = value;
		}
	}

	/**
	 * 保存一个附件到系统中
	 * 
	 * @param file
	 * @param type
	 * @param title
	 * @return 附件ID
	 */
	public String saveFile(File file, FILE_TYPE type, String title, LoginUser user);

	/**
	 * 保存一个附件到系统中
	 * 
	 * @param inStream
	 *            文件流
	 * @param filename
	 *            文件名称
	 * @param title
	 *            文件title
	 * @param type
	 *            文件类型
	 * @param user
	 *            当前用户
	 * @return
	 */
	public String saveFile(InputStream inStream, String filename, String title, FILE_TYPE type, LoginUser user);

	/**
	 * 由文件id获得下载链接
	 * 
	 * @param fileid
	 *            文件id
	 * @return
	 */
	public String getFileURL(String fileid);

	/**
	 * 由文件id获得文件对象
	 * 
	 * @param fileid
	 * @return
	 */
	public FarmDocfile getFile(String fileid);

	/**
	 * 获得一个默认图片
	 * 
	 * @param file
	 * @return
	 */
	public File getNoneImg( );

	/**
	 * 将文件状态改为提交状态，否则为临时状态
	 * 
	 * @param fileId
	 */
	public void submitFile(String fileId);

	/**
	 * 将文件设置为临时状态
	 * 
	 * @param fileId
	 */
	public void cancelFile(String fileId);

	/**
	 * 删除一个文件
	 * 
	 * @param fileId
	 */
	public void delFile(String fileId, LoginUser user);

	/**
	 * 创建并使用一个新的文件（并附带一个已经存在的File对象）
	 * 
	 * @param exname
	 *            扩展名
	 * @param content
	 *            备注
	 * @param user
	 * @return
	 */
	public FarmDocfile openFile(String exname, String content, LoginUser user);

	/**
	 * 为文档添加一个附件
	 * 
	 * @param docid
	 * @param fileId
	 * @param user
	 */
	public void addFileForDoc(String docid, String fileId, LoginUser user);

	/**
	 * 为文档删除一个附件
	 * 
	 * @param docid
	 * @param fileId
	 * @param user
	 */
	public void delFileForDoc(String docid, String fileId, LoginUser user);
	/**
	 * 删除一个文档的所有附件
	 * 
	 * @param docid
	 * @param fileId
	 * @param user
	 */
	public void delFileForDoc(String docid, LoginUser user);
	/**
	 * 获得文档的所有附件
	 * 
	 * @param docid
	 */
	public List<FarmDocfile> getAllFileForDoc(String docid);

	/**
	 * 获得文档的某类型的所有附件
	 * 
	 * @param docid
	 * @param exname
	 *            扩展名如.doc
	 * @return
	 */
	public List<FarmDocfile> getAllTypeFileForDoc(String docid, String exname);

	/**
	 * 文档是否包含一个附件
	 * 
	 * @param id
	 * @param zipfileId
	 * @return
	 */
	public boolean containFileByDoc(String docid, String fileId);

	/**
	 * 删除一个文档的所有某类型附件
	 * 
	 * @param docid
	 *            文档id
	 * @param exname
	 *            扩展名如.doc
	 * @param LoginUser
	 */
	public void delAllFileForDoc(String docid, String exname, LoginUser LoginUser);
}
