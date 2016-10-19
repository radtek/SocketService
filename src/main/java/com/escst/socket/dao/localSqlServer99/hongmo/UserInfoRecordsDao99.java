package com.escst.socket.dao.localSqlServer99.hongmo;

import com.escst.socket.entity.hongmo.UserInfoRecords;
import org.apache.ibatis.annotations.Insert;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface UserInfoRecordsDao99 {

	@Insert("INSERT INTO bk_UserInfoRecords (recog_time,recog_type,user_id,auth_stat,sn) VALUES (#{recog_time},#{recog_type},#{user_id},#{auth_stat},#{sn})")
	public void insertUserRecords(UserInfoRecords userInfoRecords);
}
