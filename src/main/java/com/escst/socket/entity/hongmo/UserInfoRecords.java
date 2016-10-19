package com.escst.socket.entity.hongmo;

public class UserInfoRecords {
	private int id;
	private String recog_time; //打卡时间
	private String recog_type; //打卡类型
	private String user_id;  //用户ID
	private int auth_stat;//打卡类别  0上班 1下班 2加班签到 3加班签退 4外出签到 5外出签退
	private String sn;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the recog_time
	 */
	public String getRecog_time() {
		return recog_time;
	}
	/**
	 * @param recog_time the recog_time to set
	 */
	public void setRecog_time(String recog_time) {
		this.recog_time = recog_time;
	}
	/**
	 * @return the recog_type
	 */
	public String getRecog_type() {
		return recog_type;
	}
	/**
	 * @param recog_type the recog_type to set
	 */
	public void setRecog_type(String recog_type) {
		this.recog_type = recog_type;
	}
	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public int getAuth_stat() {
		return auth_stat;
	}
	public void setAuth_stat(int auth_stat) {
		this.auth_stat = auth_stat;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	
	
	
}
