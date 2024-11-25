package com.farm.core.auth.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.farm.core.AuthorityService;
import com.farm.core.auth.domain.AuthKey;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.auth.domain.WebMenu;
import com.farm.core.auth.exception.LoginUserNoExistException;

@Service
public class AuthTestImpl implements AuthorityService {

	@Override
	public void loginHandle(String userId) {
		System.out.println("登录成功");

	}

	@Override
	public Set<String> getUserAuthKeys(String userId) {
		Set<String> map = new HashSet<String>();
		map.add("AUTHKEY");
		return map;
	}

	@Override
	public LoginUser getUserById(String userId) {
		LoginUser user = new LoginUser() {
			@Override
			public String getName() {
				return "userName";
			}

			@Override
			public String getLoginname() {
				return "loginName";
			}

			@Override
			public String getId() {
				return "userId";
			}
		};
		return user;
	}

	@Override
	public LoginUser getUserByLoginName(String loginName) {
		LoginUser user = new LoginUser() {
			@Override
			public String getName() {
				return "userName";
			}

			@Override
			public String getLoginname() {
				return "loginName";
			}

			@Override
			public String getId() {
				return "userId";
			}
		};
		return user;
	}

	@Override
	public List<WebMenu> getUserMenu(String userId) {
		List<WebMenu> list = new ArrayList<WebMenu>();
		return list;
	}

	@Override
	public boolean isLegality(String loginName, String password)
			throws LoginUserNoExistException {
		return true;
	}

	@Override
	public AuthKey getAuthKey(String key) {
		return new AuthKey() {

			@Override
			public boolean isLogin() {
				return false;
			}

			@Override
			public boolean isCheck() {
				return false;
			}

			@Override
			public boolean isUseAble() {
				return true;
			}

			@Override
			public String getTitle() {
				return "权限名称(测试)";
			}
		};
	}

	@Override
	public List<String> getUserPostKeys(String userId) {
		List<String> list = new ArrayList<String>();
		list.add("POSTID");
		return list;
	}

	@Override
	public String getUserOrgKey(String userId) {
		return "ORGID";
	}

}
