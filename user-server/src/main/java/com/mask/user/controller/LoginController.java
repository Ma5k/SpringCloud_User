package com.mask.user.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mask.user.VO.ResultVO;
import com.mask.user.constant.CookieConstant;
import com.mask.user.constant.RedisConstant;
import com.mask.user.dataobject.UserInfo;
import com.mask.user.enums.ResultEnum;
import com.mask.user.enums.RoleEnum;
import com.mask.user.service.UserService;
import com.mask.user.utils.CookieUtil;
import com.mask.user.utils.ResultVOUtil;


@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@GetMapping("/buyer")
	public ResultVO buyer(@RequestParam("openid") String openid, HttpServletResponse response) {
		// 1.openid和数据库里的数据进行匹配
		UserInfo userInfo = userService.findByOpenid(openid);
		if (userInfo == null) {
			return ResultVOUtil.error(ResultEnum.LOGIN_FAIL);
		}
		// 2.判断角色
		if (RoleEnum.BUYER.getCode() != userInfo.getRole()) {
			return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
		}

		// 3.cookie里设置openid=abc
		CookieUtil.set(response, CookieConstant.OPENID, openid, CookieConstant.expire);
		return ResultVOUtil.success();
	}

	@GetMapping("/seller")
	public ResultVO seller(@RequestParam("openid") String openid, HttpServletRequest request, HttpServletResponse response) {
		
		// 判断是否已登录
		Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
		if (cookie != null && !StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE, cookie.getValue())))) {
			return ResultVOUtil.success();
		}
		
		// 1.openid和数据库里的数据进行匹配
		UserInfo userInfo = userService.findByOpenid(openid);
		if (userInfo == null) {
			return ResultVOUtil.error(ResultEnum.LOGIN_FAIL);
		}
		// 2.判断角色
		if (RoleEnum.SELLER.getCode() != userInfo.getRole()) {
			return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
		}

		//3.redis设置key=UUID, value=xyz
		String token = UUID.randomUUID().toString();
		Integer expire = CookieConstant.expire;
		stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_TEMPLATE, token), openid, expire, TimeUnit.SECONDS);
		
		
		// 4.cookie里设置openid=abc
		CookieUtil.set(response, CookieConstant.TOKEN, token, CookieConstant.expire);
		
		return ResultVOUtil.success();
	}
}
