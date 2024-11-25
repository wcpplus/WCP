package com.farm.core;

import java.util.List;
import java.util.Set;

import com.farm.core.auth.domain.AuthKey;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.auth.domain.WebMenu;
import com.farm.core.auth.exception.LoginUserNoExistException;

/**
 * 权限服务接口
 * 
 * @author wangdong
 * @version 2014-12
 * 
 */
public interface AuthorityService {

	/**
	 * 验证用户是否合法
	 * 
	 * @param loginName
	 *            用户登录名
	 * @param password
	 *            用户密码
	 * @return
	 */
	public boolean isLegality(String loginName, String password)
			throws LoginUserNoExistException;

	/**
	 * 获得用户对象
	 * 
	 * @param loginName
	 * @return
	 */
	public LoginUser getUserByLoginName(String loginName);
	
	
	/**获得用户岗位（用于工作流等应用中的对应KEY）
	 * @param userId
	 * @return
	 */
	public List<String> getUserPostKeys(String userId);
	
	/**获得用户组织机构KEY
	 * @param userId
	 * @return
	 */
	public String getUserOrgKey(String userId);

	/**
	 * 获得用户对象
	 * 
	 * @param userId
	 * @return
	 */
	public LoginUser getUserById(String userId);

	/**
	 * 获得用户权限关键字
	 * 
	 * @param userId
	 * @return
	 */
	public Set<String> getUserAuthKeys(String userId);

	/**
	 * 获得key对象(用于检查key权限)
	 * 
	 * @param key
	 * @return
	 */
	public AuthKey getAuthKey(String key);

	/**
	 * 获得用户的菜单
	 * 
	 * @param userId
	 * @return
	 */
	public List<WebMenu> getUserMenu(String userId);

	/**
	 * 登录成功时会调用此方法
	 * 
	 * @param userId
	 */
	public void loginHandle(String userId);

}
