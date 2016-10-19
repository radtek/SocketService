package com.escst.socket.entity.hongmo;

import java.util.Date;

/**
 * 上传脱机注册信息实体类
 * @author zhangchaogfeng
 *
 */
public class RegisterUpload {
	//	{"user_id":"1003","name":"路人甲","tts_text":"路","duty":"职务1","depart":"部门1","ic":"卡号1",
//	"numbr":"工号1","schedule_id":"班别编号1","weigen":"123456"," permission":"3","id_number":"123"，
//	" data_len ":"12345","md5":"24f929aa2b25ee864fe4c3f44bf220d0",
//	”enroll_type”:”0000000000000000”,”fp_count”:”3” ,”fv_count”:”2”,”sn”:”1234567890”}
	private long id;
	private String user_id;//用户ID
	private String name;  //用户名
	private String gender;//性别 1：男；0：女
	private String tts_text;//TTS 文本
	private String duty;//职务
	private String workType;//工种
	private String depart;//部门
	private String ic; //IC卡号码
	private String _id;//ID卡号
	private String phone_card;//手机卡号码
	private String work_sn; //工号
	private String user_password;//用户密码
	private String class_id;//班别编号
	private String weigen;//PC端下发的韦根 HEX
	private String deny_recog;//黑名单 不可识别     1：是；0：否；
	private String deny_open_door;//不可开门      1：是；0：否；
	private String permission;//用户权限 1：超级管理员，2：普通管理员，3：普通用户
	private String id_card;//身份证号码
	private String data_len;//zip数据长度
	private String md5; //压缩前数据的MD5值
	//  注册类型,共16位,每一位代表一种类型，分别是，"虹膜", "人脸", "指纹", "IC卡", "指静脉",
//  "密码", "身份证", "一代手机卡", "二代手机卡", "ID卡", 后面剩余的暂时保留
//  如果为0：未注册，1：已经注册 (后续包数据需要跟据此值进行判断)
	private String enroll_type;
	private String fp_count;//注册指纹数量
	private String fv_count;//注册指静脉数量
	private String sn;
	private Date add_time;
	private String architecturalid; //工地ID
	private String architecturalname;//工地名称
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the tts_text
	 */
	public String getTts_text() {
		return tts_text;
	}
	/**
	 * @param tts_text the tts_text to set
	 */
	public void setTts_text(String tts_text) {
		this.tts_text = tts_text;
	}
	/**
	 * @return the duty
	 */
	public String getDuty() {
		return duty;
	}
	/**
	 * @param duty the duty to set
	 */
	public void setDuty(String duty) {
		this.duty = duty;
	}
	
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	/**
	 * @return the depart
	 */
	public String getDepart() {
		return depart;
	}
	/**
	 * @param depart the depart to set
	 */
	public void setDepart(String depart) {
		this.depart = depart;
	}
	
	/**
	 * @return the weigen
	 */
	public String getWeigen() {
		return weigen;
	}
	/**
	 * @param weigen the weigen to set
	 */
	public void setWeigen(String weigen) {
		this.weigen = weigen;
	}
	/**
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}
	/**
	 * @param permission the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	/**
	 * @return the data_len
	 */
	public String getData_len() {
		return data_len;
	}
	/**
	 * @param data_len the data_len to set
	 */
	public void setData_len(String data_len) {
		this.data_len = data_len;
	}
	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}
	/**
	 * @param md5 the md5 to set
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	/**
	 * @return the enroll_type
	 */
	public String getEnroll_type() {
		return enroll_type;
	}
	/**
	 * @param enroll_type the enroll_type to set
	 */
	public void setEnroll_type(String enroll_type) {
		this.enroll_type = enroll_type;
	}
	/**
	 * @return the fp_count
	 */
	public String getFp_count() {
		return fp_count;
	}
	/**
	 * @param fp_count the fp_count to set
	 */
	public void setFp_count(String fp_count) {
		this.fp_count = fp_count;
	}
	/**
	 * @return the fv_count
	 */
	public String getFv_count() {
		return fv_count;
	}
	/**
	 * @param fv_count the fv_count to set
	 */
	public void setFv_count(String fv_count) {
		this.fv_count = fv_count;
	}
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	/**
	 * @return the add_time
	 */
	public Date getAdd_time() {
		return add_time;
	}
	/**
	 * @param add_time the add_time to set
	 */
	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the ic
	 */
	public String getIc() {
		return ic;
	}
	/**
	 * @param ic the ic to set
	 */
	public void setIc(String ic) {
		this.ic = ic;
	}
	/**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
	}
	/**
	 * @return the phone_card
	 */
	public String getPhone_card() {
		return phone_card;
	}
	/**
	 * @param phone_card the phone_card to set
	 */
	public void setPhone_card(String phone_card) {
		this.phone_card = phone_card;
	}
	/**
	 * @return the work_sn
	 */
	public String getWork_sn() {
		return work_sn;
	}
	/**
	 * @param work_sn the work_sn to set
	 */
	public void setWork_sn(String work_sn) {
		this.work_sn = work_sn;
	}
	/**
	 * @return the user_password
	 */
	public String getUser_password() {
		return user_password;
	}
	/**
	 * @param user_password the user_password to set
	 */
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	/**
	 * @return the class_id
	 */
	public String getClass_id() {
		return class_id;
	}
	/**
	 * @param class_id the class_id to set
	 */
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	/**
	 * @return the deny_recog
	 */
	public String getDeny_recog() {
		return deny_recog;
	}
	/**
	 * @param deny_recog the deny_recog to set
	 */
	public void setDeny_recog(String deny_recog) {
		this.deny_recog = deny_recog;
	}
	/**
	 * @return the deny_open_door
	 */
	public String getDeny_open_door() {
		return deny_open_door;
	}
	/**
	 * @param deny_open_door the deny_open_door to set
	 */
	public void setDeny_open_door(String deny_open_door) {
		this.deny_open_door = deny_open_door;
	}
	/**
	 * @return the id_card
	 */
	public String getId_card() {
		return id_card;
	}
	/**
	 * @param id_card the id_card to set
	 */
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getArchitecturalid() {
		return architecturalid;
	}
	public void setArchitecturalid(String architecturalid) {
		this.architecturalid = architecturalid;
	}
	public String getArchitecturalname() {
		return architecturalname;
	}
	public void setArchitecturalname(String architecturalname) {
		this.architecturalname = architecturalname;
	}
	
	

}
