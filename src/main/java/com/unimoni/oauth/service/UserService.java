package com.unimoni.oauth.service;

import java.util.List;

import com.unimoni.oauth.request.dto.UserDetailReq;
import com.unimoni.oauth.request.dto.UserReq;
import com.unimoni.oauth.response.dto.UserDetailDto;
import com.unimoni.oauth.response.dto.UserDto;

public interface UserService {

//	public List<UserDto> findAll();

	public UserDto registerUser(UserDetailReq userdetail); 

	public UserDto getUserDetail(Long userId);

	public List<UserDetailDto> getUserAdditionalInfoDetail(Long userId);

	public UserDto updateUserDetail(UserDetailReq userdetail);
	
	public UserDto updateUserStatus(UserReq user);
	
	
}
