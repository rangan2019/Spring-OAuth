package com.unimoni.oauth.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unimoni.oauth.dao.UserDao;
import com.unimoni.oauth.dao.UserDetailDao;
import com.unimoni.oauth.entity.Converter.Mapper;
import com.unimoni.oauth.model.User;
import com.unimoni.oauth.model.UserDetail;
import com.unimoni.oauth.request.dto.UserDetailReq;
import com.unimoni.oauth.request.dto.UserReq;
import com.unimoni.oauth.response.dto.UserDetailDto;
import com.unimoni.oauth.response.dto.UserDto;
import com.unimoni.oauth.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserDetailDao userDetailDao;

	@Autowired
	private Mapper mapper;


	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDao.findByLoginId(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				getAuthority());
	}
//
	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	private boolean userDuplicateValidate(String loginId, String email, String mobileNo) {
		long countByLoginIdOrEmailOrMobileNumber = userDao.countByLoginIdOrEmailOrMobileNumber(loginId, email,
				mobileNo);
		return countByLoginIdOrEmailOrMobileNumber > 0;

	}

	@Override
	public UserDto registerUser(UserDetailReq userReq) {
		boolean isExists = userDuplicateValidate(userReq.getUserName(), userReq.getEmail(), userReq.getMobileNumber());
		if (!isExists) {
			User user = new User();
//			user.setUserId(userReq.getUserId())
			user.setUserName(userReq.getUserName());
			user.setPassword(userReq.getPassword());
			user.setFirstName(userReq.getFirstName());
			user.setMiddleName(userReq.getMiddleName());
			user.setLastName(userReq.getLastName());
			user.setEmail(userReq.getEmail());
			user.setMobileNumber(userReq.getMobileNumber());
			user.setIsActive(userReq.getIsActive());
			user.setIsEmailVerified(userReq.getIsEmailVerified());
			user.setIsGuest(userReq.getIsGuest());
			user.setCreatedOn(new Date());
			user.setModifiedOn(new Date());
			user.setUserIdentity(user.hashCode());
			user = userDao.save(user);
			user.setUserIdentity(user.hashCode());
			user = userDao.save(user);
			user.setUserIdentity(user.hashCode());
			user = userDao.save(user);
			UserDetail userDetail = new UserDetail();
			userDetail.setUserDetailId(userReq.getUserDetailId());
			userDetail.setName(userReq.getName());
			userDetail.setGender(userReq.getGender());
			userDetail.setLocation(userReq.getLocation());
			userDetail.setHouseNo(userReq.getHouseNo());
			userDetail.setAddress1(userReq.getAddress1());
			userDetail.setAddress2(userReq.getAddress2());
			userDetail.setMobileNo(userReq.getMobileNo());
			userDetail.setCity(userReq.getCity());
			userDetail.setState(userReq.getState());
			userDetail.setPincode(userReq.getPincode());
			userDetail.setLandMark(userReq.getLandMark());
			userDetail.setAddressType(userReq.getAddressType());
			userDetail.setIsPrimaryAddress(userReq.getIsPrimaryAddress());
			userDetail.setCreatedOn(new Date());
			userDetail.setModifiedOn(new Date());
			userDetail.setUserId(user.getUserId());
			userDetail = userDetailDao.save(userDetail);
			userDetail.setUserIdentity(userDetail.hashCode());
			userDetailDao.save(userDetail);

		} else {
			throw new UsernameNotFoundException("User Already exists!!!.");
		}
		return null;
	}

	@Override
	public UserDto getUserDetail(Long userId) {
		return mapper.map(userDao.findOne(userId), UserDto.class);
	}

	@Override
	public List<UserDetailDto> getUserAdditionalInfoDetail(Long userId) {
		List<UserDetail> userDetails = userDetailDao.findByUserId(userId);
		return userDetails.stream().map(u -> mapper.map(u, UserDetailDto.class)).collect(Collectors.toList());
	}

	@Override
	public UserDetailDto updateUserDetail(UserDetailReq userDetailReq) {
		if(userDetailReq==null || userDetailReq.getUserDetailId() == null) {
			throw new UsernameNotFoundException("Could not update the user detail. loginName is missing");
		}
		UserDetail userDetail = userDetailDao.findByUserDetailId(userDetailReq.getUserDetailId());
		if (userDetail.getName().equals(userDetailReq.getName())) {
			userDetail.setName(userDetailReq.getName());
		}
		if (userDetail.getGender().equals(userDetailReq.getGender())) {
			userDetail.setGender(userDetailReq.getGender());
		}
		if (userDetail.getLocation().equals(userDetailReq.getLocation())) {
			userDetail.setLocation(userDetailReq.getLocation());
		}

		if (userDetail.getHouseNo().equals(userDetailReq.getHouseNo())) {
			userDetail.setHouseNo(userDetailReq.getHouseNo());
		}

		if (userDetail.getAddress1().equals(userDetailReq.getAddress1())) {
			userDetail.setAddress1(userDetailReq.getAddress1());
		}

		if (userDetail.getAddress2().equals(userDetailReq.getAddress2())) {
			userDetail.setAddress2(userDetailReq.getAddress2());
		}
		if (userDetail.getMobileNo().equals(userDetailReq.getMobileNo())) {
			userDetail.setMobileNo(userDetailReq.getMobileNo());
		}
		if (userDetail.getCity().equals(userDetailReq.getCity())) {
			userDetail.setCity(userDetailReq.getCity());
		}
		if (userDetail.getState().equals(userDetailReq.getState())) {
			userDetail.setState(userDetailReq.getState());
		}
		if (userDetail.getPincode() != userDetailReq.getPincode()) {
			userDetail.setPincode(userDetailReq.getPincode());
		}
		if (userDetail.getLandMark().equals(userDetailReq.getLandMark())) {
			userDetail.setLandMark(userDetailReq.getLandMark());
		}
		if (userDetail.getAddressType().equals(userDetailReq.getAddressType())) {
			userDetail.setAddressType(userDetailReq.getAddressType());
		}
		if (userDetail.getIsPrimaryAddress().equals(userDetailReq.getIsPrimaryAddress())) {
			userDetail.setIsPrimaryAddress(userDetailReq.getIsPrimaryAddress());
		}
		userDetail.setModifiedOn(new Date());
		userDetail.setUserIdentity(userDetail.hashCode());
		userDetailDao.save(userDetail);

		return mapper.map(userDetail, UserDetailDto.class);
	}

	@Override
	public UserDto updateUserStatus(UserReq userReq) {
		if(userReq==null || userReq.getUserId() == null) {
			throw new UsernameNotFoundException("Could not update the user detail. loginName is missing");
		}
		User user = userDao.findByUserId(userReq.getUserId());
		if (user.getIsActive() != userReq.getIsActive()) {
			user.setIsActive(userReq.getIsActive());
		}
		if (user.getIsEmailVerified() != userReq.getIsEmailVerified()) {
			user.setIsEmailVerified(userReq.getIsEmailVerified());
		}
		if (user.getIsGuest() != userReq.getIsGuest()) {
			user.setIsGuest(userReq.getIsGuest());
		}
		user.setUserIdentity(user.hashCode());
		user.setModifiedOn(new Date());
		return mapper.map(userDao.save(user), UserDto.class);
	}

}
