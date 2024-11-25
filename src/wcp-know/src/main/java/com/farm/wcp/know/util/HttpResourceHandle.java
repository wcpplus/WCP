package com.farm.wcp.know.util;

import java.net.URL;


/**处理网络资源时替换和处理URL的接口
 * @author Administrator
 *
 */
public interface HttpResourceHandle {
	public String handle(String URL,URL baseUrl);
}
