package com.escst.socket.service.hongmo;

import java.sql.SQLException;

import com.escst.socket.dao.localSqlServer.hongmo.RegisterUserInfoDao;
import com.escst.socket.entity.hongmo.RegisterUpload;
import com.escst.socket.entity.hongmo.UserTemplates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * 人员注册信息服务类
 * @author zhangchaofeng
 *
 */
public class RegisterUserService {
	Logger logger = LoggerFactory.getLogger(RegisterUserService.class);

	private RegisterUserInfoDao registerUserInfoDao;

	public RegisterUserService(RegisterUserInfoDao registerUserInfoDao){
		this.registerUserInfoDao = registerUserInfoDao;
	}
	/**
	 * 用户注册信息写入数据库
	 * @param registerUpload
	 */
	public void insertRegisterUserInfo(RegisterUpload registerUpload){
		logger.info("用户注册信息写入数据库......");
		registerUserInfoDao.inserRegisterUserInfo(registerUpload);
	}

	/**
	 *  用户生物特征写入数据库
	 * @param userTemplates
	 */
	public void insertUserTemplate(UserTemplates userTemplates){
		logger.info("用户注册生物信息模板写入数据库......");
		registerUserInfoDao.insertUserTemplate(userTemplates);
	}

	/**
	 * 根据人员ID查询此人员是否已注册
	 * @param numbr 用户ID
	 * @return
	 * @throws SQLException
	 */
	public int findUserById(String numbr,String architecturalid) throws SQLException{
		return registerUserInfoDao.findUserById(numbr,architecturalid);
	}

	/**
	 * 更新注册人员信息
	 * @param registerUpload
	 * @throws SQLException
	 */
	public void updateUserInfo(RegisterUpload registerUpload) throws SQLException{
		registerUserInfoDao.updateUserInfo(registerUpload);
	}

	/**
	 * 查询用户生物特征信息是否存在
	 * @param userTemplates
	 * @return
	 * @throws SQLException
	 */
	public int findUserTemplates(UserTemplates userTemplates) throws SQLException {
		return registerUserInfoDao.findUserTemplates(userTemplates);
	}

	/**
	 * 更新用户生物特征
	 * @param userTemplates
	 * @throws SQLException
	 */
	public void updateUserTemplates(UserTemplates userTemplates) throws SQLException {
		registerUserInfoDao.updateUserTemplates(userTemplates);
	}

}
