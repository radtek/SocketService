package com.escst.socket.service.hongmo;

import com.escst.socket.dao.localSqlServer.hongmo.UserInfoRecordsDao;
import com.escst.socket.entity.hongmo.UserInfoRecords;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

public class UserInfoRecordsService {

	private UserInfoRecordsDao userInfoRecordsDao;

	public UserInfoRecordsService(UserInfoRecordsDao userInfoRecordsDao){
		this.userInfoRecordsDao = userInfoRecordsDao;
	}
	/**
	 * 用户识别记录写入数据库
	 * @param userInfoRecords
	 * @throws Exception
	 */
	public void insertUserRecord(UserInfoRecords userInfoRecords)throws Exception{
		userInfoRecordsDao.insertUserRecords(userInfoRecords);
	}

}
