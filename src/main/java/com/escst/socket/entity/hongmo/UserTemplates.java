package com.escst.socket.entity.hongmo;

import java.util.Date;

/**
 * ��Ա��������ʵ����
 * @author liyong
 *
 */
public class UserTemplates {
	private long id;
	private String numbr;
	private String sn;
	private byte[] template;
	private String id_card;//IC、ID、身份证
	private int type; //1为虹膜 2为人脸 3为指纹 4为IC卡 5为身份证 6为ID卡
	private Date add_time;
	private String architecturalid; //工地ID
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
	public String getNumbr() {
		return numbr;
	}
	/**
	 * @param numbr the numbr to set
	 */
	public void setNumbr(String numbr) {
		this.numbr = numbr;
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
	 * @return the template
	 */
	public byte[] getTemplate() {
		return template;
	}
	/**
	 * @param template the template to set
	 */
	public void setTemplate(byte[] template) {
		this.template = template;
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
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
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
	public String getArchitecturalid() {
		return architecturalid;
	}
	public void setArchitecturalid(String architecturalid) {
		this.architecturalid = architecturalid;
	}
	
	
}
