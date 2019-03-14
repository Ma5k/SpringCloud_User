package com.mask.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mask.user.dataobject.UserInfo;
import com.mask.user.repository.UserInfoRepository;
import com.mask.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserInfoRepository repository;
	
	/**
	 * 	通过openid来查询用户信息
	 */
	@Override
	public UserInfo findByOpenid(String openid) {
		return repository.findByOpenid(openid);
	}
}
