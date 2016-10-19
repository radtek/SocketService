package com.escst.socket.service.hongmo;

import com.escst.socket.dao.localSqlServer99.hongmo.UserInfoRecordsDao99;
import com.escst.socket.entity.hongmo.UserInfoRecords;

public class UserInfoRecordsService99 {

	private UserInfoRecordsDao99 userInfoRecordsDao;

	public UserInfoRecordsService99(UserInfoRecordsDao99 userInfoRecordsDao){
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
