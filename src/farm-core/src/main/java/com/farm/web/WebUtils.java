package com.farm.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.farm.core.auth.domain.LoginUser;
import com.farm.core.auth.domain.WebMenu;
import com.farm.core.time.TimeTool;
import com.farm.util.spring.BeanFactory;
import com.farm.web.constant.FarmConstant;

public class WebUtils {
	/**
	 * 通过Spring获得对象
	 * 
	 * @param beanIndex
	 * @return
	 */
	protected Object BEAN(String beanIndex) {
		return BeanFactory.getBean(beanIndex);
	}

	@SuppressWarnings("unchecked")
	public List<WebMenu> getCurrentUserMenus(HttpSession session) {
		List<WebMenu> menuList = (List<WebMenu>) session
				.getAttribute(FarmConstant.SESSION_USERMENU);
		return menuList;
	}

	/**
	 * 获得当前登录用户对象
	 * 
	 * @return
	 */
	public static LoginUser getCurrentUser(HttpSession session) {
		LoginUser user = (LoginUser) session
				.getAttribute(FarmConstant.SESSION_USEROBJ);
		return user;
	}

	public LoginUser getCurrentUserByDebug(HttpSession session) {
		LoginUser user = new LoginUser() {
			@Override
			public String getName() {
				return "测试";
			}

			@Override
			public String getLoginname() {
				return "debug";
			}

			@Override
			public String getId() {
				return "debug";
			}
		};
		return user;
	}

	/**
	 * 使用httpSession设置当前登录用户
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unused")
	public LoginUser setCurrentUser(LoginUser user, HttpSession session) {
		session.setAttribute(FarmConstant.SESSION_USEROBJ, user);
		String photoid = null;
		if (photoid != null && photoid.trim().length() > 0) {
			// if (session == null) {
			// getSession().put(AloneConstant.SESSION_USERPHOTO,
			// EkpFileFaceImpl.getInstance().getFileUrl(photoid));
			// } else {
			// session.setAttribute(AloneConstant.SESSION_USERPHOTO,
			// EkpFileFaceImpl.getInstance().getFileUrl(photoid));
			// }
		}
		return user;
	}

	/**
	 * 清除当前登录用户
	 * 
	 * @param user
	 * @return
	 */
	public void clearCurrentUser(HttpSession session) {
		session.setAttribute(FarmConstant.SESSION_USEROBJ, null);
	}

	/**
	 * 使用httpSession设置当前登录用户权限
	 * 
	 * @param user
	 * @return
	 */
	public void setCurrentUserAction(Set<String> userAction, HttpSession session) {
		session.setAttribute(FarmConstant.SESSION_USERACTION, userAction);
	}

	/**
	 * 使用httpSession设置当前登录时间
	 * 
	 * @param user
	 * @return
	 */
	public void setLoginTime(HttpSession session) {
		session.setAttribute(FarmConstant.SESSION_LOGINTIME,
				TimeTool.getTimeDate14());
	}

	/**
	 * 获得当前登录时间
	 */
	public String getLoginTime(HttpSession session) {
		return (String) session.getAttribute(FarmConstant.SESSION_LOGINTIME);
	}

	/**
	 * 使用httpSession设置当前登录用户菜单
	 * 
	 * @param user
	 * @return
	 */
	public void setCurrentUserMenu(List<WebMenu> userMenu, HttpSession session) {
		session.setAttribute(FarmConstant.SESSION_USERMENU, userMenu);
	}

	/**
	 * 如果httpsession有就返回httpsession没有就返回strutsSession
	 * 
	 * @param httpSession
	 * @return
	 */
	public HttpSession getSession(HttpSession httpSession) {
		return httpSession;
	}

	/**
	 * 获得用户ip地址
	 * 
	 * @return
	 */
	public static String getCurrentIp(HttpServletRequest httpRequest) {
		return httpRequest.getRemoteAddr();
	}

	/**
	 * 设置一个保存30天的cookie
	 * 
	 * @param cookieName
	 * @param value
	 */
	public void setCookie(String cookieName, String value,
			HttpServletResponse httpResponse) {
		Cookie cookie = new Cookie(cookieName, value);
		int expireday = 60 * 60 * 24 * 30; // 不设置的话，则cookies不写入硬盘,而是写在内存,只在当前页面有用,以秒为单位
		cookie.setMaxAge(expireday);
		httpResponse.addCookie(cookie);
	}

	/**
	 * 删除一个cookie
	 * 
	 * @param cookieName
	 * @param value
	 */
	public void delCookie(String cookieName, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		if (cookieName == null || cookieName.equals("")) {
			return;
		}
		Cookie[] cookies = httpRequest.getCookies();
		int length = 0;

		if (cookies != null && cookies.length > 0) {
			length = cookies.length;
			for (int i = 0; i < length; i++) {
				String cname = cookies[i].getName();
				if (cname != null && cname.equals(cookieName)) {
					String cValue = cookies[i].getValue();
					setCookie(cname, cValue, httpResponse);
				} else {
					continue;
				}
			}
		}
	}

	public String getCookieValue(String cookieName,
			HttpServletRequest httpRequest) {
		if (cookieName == null || cookieName.equals("")) {
			return null;
		}
		Cookie[] cookies = httpRequest.getCookies();
		int length = 0;

		if (cookies != null && cookies.length > 0) {
			length = cookies.length;
			for (int i = 0; i < length; i++) {
				String cname = cookies[i].getName();
				if (cname != null && cname.equals(cookieName)) {
					String cValue = cookies[i].getValue();
					return cValue;
				} else {
					continue;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * 将id序列字符串转化为id的list序列
	 * 
	 * @param ids
	 * @return
	 */
	public List<String> parseIds(String ids) {
		if (ids == null) {
			return new ArrayList<String>();
		}
		ids = ids.replace("，", ",");
		String[] markdot = ids.split(",");
		List<String> list_ = new ArrayList<String>();
		for (int i = 0; i < markdot.length; i++) {
			String temp = markdot[i];
			if (temp != null && !temp.equals("") && !temp.equals(" "))
				list_.add(temp);
		}
		return list_;
	}
}
