package com.farm.web.online;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;


import com.farm.core.sql.result.DataResult;
import com.farm.web.constant.FarmConstant;


/**
 * 在线用户管理 非集群实现 实现该功能需要将方法： userLoginHandle()
 * userVisitHandle()加入到用户登录，和用户访问系统资源的代码中
 * 
 * @author wangdong
 * 
 */
public class OnlineUserOpImpl implements OnlineUserOpInter {
	/**
	 * 当前用户ip
	 */
	private String ip;
	private String loginName;
	private HttpSession httpSession;
	private Map<String, Object> strutsSession;

	@Override
	public boolean doUserDownLine(String loginName) {
		OnlineUserOpInter.onlineUserTable.remove(loginName);
		return true;
	}

	@Override
	public DataResult findOnlineUser() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (String key : OnlineUserOpInter.onlineUserTable.keySet()) {
			// 处理时间----开始
			// 上次访问时间
			Date date = (Date) OnlineUserOpInter.onlineUserTable.get(key).get(
					OnlineUserOpInter.key_TIME);
			// 登录时间
			Date visitdate = (Date) OnlineUserOpInter.onlineUserTable.get(key)
					.get(OnlineUserOpInter.key_LOGINTIME);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat curentDayf = new SimpleDateFormat("yyyy-MM-dd");
			// 当前时间
			Date curentTime = new Date();
			// 当前时间
			Calendar curentC = Calendar.getInstance();
			curentC.setTime(curentTime);
			// 上次访问时间
			Calendar curentV = Calendar.getInstance();
			curentV.setTime(date);
			// 登录时间
			Calendar curentl = Calendar.getInstance();
			curentl.setTime(visitdate);
			// 相差分钟数
			long timeMillis = (curentC.getTimeInMillis() - curentV
					.getTimeInMillis())
					/ (1000 * 60);
			// 登录时长
			long visitMillis = (curentC.getTimeInMillis() - curentl
					.getTimeInMillis())
					/ (1000 * 60);
			if (timeMillis > OnlineUserOpInter.onlineVilaMinute) {
				// 超时用户判为不在线
				OnlineUserOpInter.onlineUserTable.remove(key);
				continue;
			}
			// 处理时间----结束
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(OnlineUserOpInter.key_LNAME, key);
			map.put(OnlineUserOpInter.key_VISITTIME, visitMillis);
			map.put(OnlineUserOpInter.key_TIME, sdf.format(date).replace(
					curentDayf.format(new Date()), "今天"));
			map.put(OnlineUserOpInter.key_USEROBJ,
					OnlineUserOpInter.onlineUserTable.get(key).get(
							OnlineUserOpInter.key_USEROBJ));
			map.put(OnlineUserOpInter.key_LOGINTIME, sdf
					.format(OnlineUserOpInter.onlineUserTable.get(key).get(
							OnlineUserOpInter.key_LOGINTIME)).replace(
									curentDayf.format(new Date()), "今天"));
			map.put(OnlineUserOpInter.key_IP, OnlineUserOpInter.onlineUserTable
					.get(key).get(OnlineUserOpInter.key_IP));
			list.add(map);

		}
		DataResult result = DataResult.getInstance(list, list.size(), 1, list
				.size());
		return result;
	}

	@Override
	public void userLoginHandle(Object user) {
		if (!((httpSession == null && strutsSession == null)
				|| loginName == null || ip == null)) {
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put(OnlineUserOpInter.key_IP, ip);
			userMap.put(OnlineUserOpInter.key_TIME, new Date());
			userMap.put(OnlineUserOpInter.key_LNAME, loginName);
			userMap.put(OnlineUserOpInter.key_LOGINTIME, new Date());
			userMap.put(OnlineUserOpInter.key_USEROBJ, user);
			// 将用户注册在在线表中
			OnlineUserOpInter.onlineUserTable.put(loginName, userMap);
		} else {
			throw new RuntimeException("参数错误");
		}
	}

	@Override
	public void userVisitHandle() {
		// 用户没有在在线表中就将用户注销
		if (!((httpSession == null && strutsSession == null)
				|| loginName == null || ip == null)) {
			Map<String, Object> userMap = OnlineUserOpInter.onlineUserTable
					.get(loginName);
			if (userMap == null) {
				// 没有在线
				clearSessionUser();
				return;
			} else {
				userMap.put(OnlineUserOpInter.key_TIME, new Date());
				if (!userMap.get(OnlineUserOpInter.key_IP).equals(ip)) {
					// 用户ip不匹配
					clearSessionUser();
					return;
				} else {
					// 没有问题，用户是在线不处理
				}
			}
		} else {
			throw new RuntimeException("参数错误");
		}
	}

	// ----------private------------------------------------------------
	private void clearSessionUser() {
		if (this.httpSession != null) {
			httpSession.removeAttribute(FarmConstant.SESSION_USEROBJ);
		}
		if (this.strutsSession != null) {
			strutsSession.put(FarmConstant.SESSION_USEROBJ, null);
			strutsSession.clear();
		}
	}

	// ----------get/set------------------------------------------------
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public Map<String, Object> getStrutsSession() {
		return strutsSession;
	}

	public void setStrutsSession(Map<String, Object> strutsSession) {
		this.strutsSession = strutsSession;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	// --------------------------------------构造方法
	public static OnlineUserOpInter getInstance(String ip, String loginName,
			HttpSession httpSession) {
		OnlineUserOpImpl obj = new OnlineUserOpImpl();
		obj.setHttpSession(httpSession);
		obj.setLoginName(loginName);
		obj.setIp(ip);
		return obj;
	}

	public static OnlineUserOpInter getInstance(String ip, String loginName,
			Map<String, Object> strutsSession) {
		OnlineUserOpImpl obj = new OnlineUserOpImpl();
		obj.setIp(ip);
		obj.setLoginName(loginName);
		obj.setStrutsSession(strutsSession);
		return obj;
	}

	public static OnlineUserOpInter getInstance() {
		OnlineUserOpImpl obj = new OnlineUserOpImpl();
		return obj;
	}

}
