package com.farm.authority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.farm.authority.domain.Action;
import com.farm.authority.domain.User;
import com.farm.authority.service.ActionServiceInter;
import com.farm.authority.service.UserServiceInter;
import com.farm.core.AuthorityService;
import com.farm.core.auth.domain.AuthKey;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.auth.domain.WebMenu;
import com.farm.core.auth.exception.LoginUserNoExistException;
import com.farm.core.auth.util.AuthenticateInter;
import com.farm.core.auth.util.AuthenticateProvider;
import com.farm.util.spring.BeanFactory;

public class FarmAuthorityService implements AuthorityService {
	private UserServiceInter userServiceImpl;
	private ActionServiceInter actionServiceImpl;
	private AuthenticateInter authUtil = AuthenticateProvider.getInstance();
	private static FarmAuthorityService service;

	public static AuthorityService getInstance() {
		if (service == null) {
			service = new FarmAuthorityService();
			service.userServiceImpl = (UserServiceInter) BeanFactory.getBean("userServiceImpl");
			service.actionServiceImpl = (ActionServiceInter) BeanFactory.getBean("actionServiceImpl");
		}
		return service;
	}

	@Override
	public void loginHandle(String userId) {
		userServiceImpl.setLoginTime(userId);
	}

	@Override
	public Set<String> getUserAuthKeys(String userId) {
		User user = userServiceImpl.getUserEntity(userId);
		List<Action> actions = null;
		if (user.getType().equals("3")) {
			actions = actionServiceImpl.getAllActions();
		} else {
			actions = userServiceImpl.getUserActions(userId);
		}
		Set<String> set = new HashSet<String>();
		for (Action action : actions) {
			set.add(action.getAuthkey());
		}
		return set;
	}

	@Override
	public LoginUser getUserById(String userId) {
		return userServiceImpl.getUserEntity(userId);
	}

	@Override
	public LoginUser getUserByLoginName(String loginName) {
		return userServiceImpl.getUserByLoginName(loginName);
	}

	@Override
	public List<WebMenu> getUserMenu(String userId) {
		User user = userServiceImpl.getUserEntity(userId);
		List<WebMenu> list = null;
		if (user.getType().equals("3")) {
			list = actionServiceImpl.getAllMenus();
		} else {
			list = userServiceImpl.getUserMenus(userId);
		}
		return list;
	}

	@Override
	public boolean isLegality(String loginName, String password) throws LoginUserNoExistException {
		User user = userServiceImpl.getUserByLoginName(loginName);
		if (user == null) {
			throw new LoginUserNoExistException("该登录名不存在！");
		}
		if (user.getType().equals("2")) {
			throw new LoginUserNoExistException("该用户无登录权限！");
		}
		if (!user.getState().equals("1")) {
			throw new LoginUserNoExistException("该用户已停用！");
		}
		if (authUtil.isMd5code(password)) {
			if (password.toUpperCase().equals(user.getPassword())) {
				return true;
			}
		} else {
			if (authUtil.encodeLoginPasswordOnMd5(password, loginName).equals(user.getPassword())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public AuthKey getAuthKey(String key) {
		return actionServiceImpl.getCacheAction(key);
	}

	@Override
	public List<String> getUserPostKeys(String userId) {
		return userServiceImpl.getUserPostIds(userId);
	}

	@Override
	public String getUserOrgKey(String userId) {
		return userServiceImpl.getUserOrganization(userId).getId();
	}

	public UserServiceInter getUserServiceImpl() {
		return userServiceImpl;
	}

	public void setUserServiceImpl(UserServiceInter userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	public ActionServiceInter getActionServiceImpl() {
		return actionServiceImpl;
	}

	public void setActionServiceImpl(ActionServiceInter actionServiceImpl) {
		this.actionServiceImpl = actionServiceImpl;
	}

}
