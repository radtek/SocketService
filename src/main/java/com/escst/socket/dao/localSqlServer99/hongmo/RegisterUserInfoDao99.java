package com.escst.socket.dao.localSqlServer99.hongmo;

import com.escst.socket.entity.hongmo.RegisterUpload;
import com.escst.socket.entity.hongmo.UserTemplates;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

import java.sql.SQLException;

@MapperScan
public interface RegisterUserInfoDao99 {

	/**
	 * 用户注册信息写入数据库
	 * @param registerUpload
	 */
	@Insert("INSERT INTO tbl_PersonInfoList (NAME,tts_text,zhicheng,ic,numbr," +
			"id_card,data_len,md5,enroll_type,fp_count,fv_count,create_date,_id,projectid,projectname,gongzhong) " +
			" VALUES (#{name},#{tts_text},#{duty},#{ic},#{work_sn},#{id_card},#{data_len},#{md5},#{enroll_type}," +
			" #{fp_count},#{fv_count},getdate(),#{_id},#{architecturalid},#{architecturalname},#{workType})")
	void inserRegisterUserInfo(RegisterUpload registerUpload);

	/**
	 * 用户生物特征写入数据库
	 * @param userTemplates
	 */
	@Insert("INSERT INTO bk_userTemplates (numbr,sn,template,id_card,type,add_time,architecturalid)" +
			" VALUES (#{numbr},#{sn},#{template},#{id_card},#{type},getdate(),#{architecturalid})")
	void insertUserTemplate(UserTemplates userTemplates);

	/**
	 * 根据人员ID查询此人员是否已注册
	 * @param numbr 用户ID
	 * @return
	 * @throws SQLException
	 */
	@Select("SELECT COUNT(*) AS total FROM tbl_PersonInfoList WHERE numbr = #{numbr} AND projectid = #{architecturalid}")
	int findUserById(@Param("numbr") String numbr, @Param("architecturalid") String architecturalid);

	/**
	 * 更新注册人员信息
	 * @param registerUpload
	 * @throws SQLException
	 */
	@Update("UPDATE tbl_PersonInfoList SET name=#{name},tts_text=#{tts_text},ic=#{ic}," +
			" id_card=#{id_card},data_len=#{data_len},md5=#{md5},enroll_type=#{enroll_type},fp_count=#{fp_count},fv_count=#{fv_count},_id=#{_id}" +
			" WHERE numbr=#{work_sn} AND projectid=#{architecturalid}")
	void updateUserInfo(RegisterUpload registerUpload);

	/**
	 * 查询用户生物特征信息是否存在
	 * @param userTemplates
	 * @return
	 * @throws SQLException
	 */
	@Select("SELECT COUNT(*) AS total FROM bk_userTemplates WHERE numbr = #{numbr} AND type= #{type}")
	int findUserTemplates(UserTemplates userTemplates);

	/**
	 * 更新用户生物特征
	 * @param userTemplates
	 * @throws SQLException
	 */
	@Update("UPDATE bk_userTemplates SET template = #{template},id_card = #{id_card},architecturalid = #{architecturalid}" +
			" WHERE numbr = #{numbr} AND type= #{type}")
	void updateUserTemplates(UserTemplates userTemplates);

}
