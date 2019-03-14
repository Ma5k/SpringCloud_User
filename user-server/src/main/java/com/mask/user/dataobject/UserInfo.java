package com.mask.user.dataobject;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class UserInfo {
	@Id
	private String id;
	
	private String  username;
	
	private String password;
	
	private String openid;
	
	private Integer role;
}
