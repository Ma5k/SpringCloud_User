package com.mask.user.service;

import com.mask.user.dataobject.UserInfo;

public interface UserService {
		
	/**
	 * 	通过openid来查询用户信息
	 * @param openid
	 * @return
	 */
	UserInfo findByOpenid(String openid);
}
