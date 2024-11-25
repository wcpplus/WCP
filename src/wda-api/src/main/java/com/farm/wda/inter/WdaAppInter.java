package com.farm.wda.inter;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.farm.wda.exception.ErrorTypeException;

/**
 * wda远程调用接口
 * 
 * @author Administrator
 *
 */
public interface WdaAppInter extends Remote {

	/**
	 * 开始生产WEB文档
	 * 
	 * @param key
	 *            文档关键字，后续通过它调用相关资源
	 * @param file
	 *            原文件
	 * @param htmlinfo
	 *            被显示的html信息（如文件名称等）
	 * @throws ErrorTypeException
	 * @throws RemoteException
	 */
	public void generateDoc(String key, File file, String htmlinfo, String authid)
			throws ErrorTypeException, RemoteException;

	/**
	 * 开始生产WEB文档
	 * 
	 * @param key
	 *            文档关键字，后续通过它调用相关资源
	 * @param file
	 *            原文件
	 * @param fileTypeName
	 *            扩展名
	 * @param htmlinfo
	 *            被显示的html信息（如文件名称等）
	 * @throws ErrorTypeException
	 * @throws RemoteException
	 */
	public void generateDoc(String key, File file, String fileTypeName, String htmlinfo, String authid)
			throws ErrorTypeException, RemoteException;

	/**
	 * 获得可以被转换的文件类型
	 * 
	 * @return
	 */
	public Set<String> getSupportTypes() throws RemoteException;

	/**
	 * 文档是否已经生成完毕
	 * 
	 * @param key
	 * @return
	 * @throws ErrorTypeException
	 */
	public boolean isGenerated(String key, String doctype) throws ErrorTypeException, RemoteException;

	/**
	 * 文档是否有日志记录
	 * 
	 * @param key
	 * @return
	 * @throws ErrorTypeException
	 */
	public boolean isLoged(String key) throws RemoteException;

	/**
	 * 删除日志（通过日志判断是否生成过文档时，可以通过此方法重新生成文档）
	 * 
	 * @param key
	 * @return
	 * @throws ErrorTypeException
	 */
	public void delLog(String key) throws RemoteException;

	/**
	 * 获得日志地址
	 * 
	 * @param key
	 * @return
	 */
	public String getlogURL(String key) throws RemoteException;

	/**
	 * 获得文档文本字符串
	 * 
	 * @param key
	 * @return
	 * @throws ErrorTypeException
	 */
	public String getText(String key) throws ErrorTypeException, RemoteException;

	/**
	 * 获得日志信息字符串
	 * 
	 * @param key
	 * @return
	 * @throws RemoteException
	 */
	public String getLogText(String key) throws RemoteException;

	/**
	 * 获得文档信息字符串
	 * 
	 * @param key
	 * @return
	 * @throws ErrorTypeException
	 */
	public String getInfo(String key) throws ErrorTypeException, RemoteException;

	/**
	 * 清空文档的预览目录
	 * 
	 * @param key
	 * @throws RemoteException
	 */
	public void clearDir(String key) throws RemoteException;

	/**
	 * 获得在线文档浏览的URL
	 * 
	 * @param key
	 * @param exname
	 * @return
	 * @throws ErrorTypeException
	 */
	public String getUrl(String key, String docType) throws ErrorTypeException, RemoteException;

	/**
	 * 获得转换任务信息队列
	 * 
	 * @return
	 * @throws RemoteException 
	 */
	public List<Map<String, String>> getTasksinfo() throws RemoteException;

}
