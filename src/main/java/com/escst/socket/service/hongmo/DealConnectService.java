package com.escst.socket.service.hongmo;

import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import com.alibaba.fastjson.JSONArray;
import com.escst.socket.dao.localSqlServer.hongmo.DeviceTaskDao;
import com.escst.socket.dao.localSqlServer.hongmo.RegisterUserInfoDao;
import com.escst.socket.dao.localSqlServer.hongmo.UserInfoRecordsDao;
import com.escst.socket.dao.localSqlServer99.hongmo.DeviceTaskDao99;
import com.escst.socket.dao.localSqlServer99.hongmo.RegisterUserInfoDao99;
import com.escst.socket.dao.localSqlServer99.hongmo.UserInfoRecordsDao99;
import com.escst.socket.entity.hongmo.*;
import com.escst.socket.utils.*;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DealConnectService extends Thread {
	Logger logger = LoggerFactory.getLogger(DealConnectService.class);

	private Socket socket;
	private InputStream is;
	private OutputStream os;
	String sn = null;
	String desKey = "12345678";// desKey  需要通过RSA加解密处理传输，当前已经固定为12345678
	public static JSONObject MSG_CONFIG;
	String deviceTaskId;
	private DeviceTaskDao deviceTaskDao;
	private UserInfoRecordsDao userInfoRecordsDao;
	private RegisterUserInfoDao registerUserInfoDao;
	private DeviceTaskDao99 deviceTaskDao99;
	private UserInfoRecordsDao99 userInfoRecordsDao99;
	private RegisterUserInfoDao99 registerUserInfoDao99;
    boolean dB99Flg = true;
	public DealConnectService(Socket socket,DeviceTaskDao deviceTaskDao,UserInfoRecordsDao userInfoRecordsDao, RegisterUserInfoDao registerUserInfoDao,
							  DeviceTaskDao99 deviceTaskDao99,UserInfoRecordsDao99 userInfoRecordsDao99, RegisterUserInfoDao99 registerUserInfoDao99) {
		this.socket = socket;
		this.deviceTaskDao = deviceTaskDao;
		this.userInfoRecordsDao = userInfoRecordsDao;
		this.registerUserInfoDao = registerUserInfoDao;
		this.deviceTaskDao99 = deviceTaskDao99;
		this.userInfoRecordsDao99 = userInfoRecordsDao99;
		this.registerUserInfoDao99 = registerUserInfoDao99;
	}

	@Override
	public void run() {

		Integer cmd = null;
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();

			// 读取包头
			byte[] requestHead = read(26);
			logger.info("读取包头: " + requestHead );

			// 解析包头
			Map<String, Object> headMap = PackUtil.HeaderAnalysis(requestHead);

			Integer tag = (Integer)headMap.get(Value.Head_Tag);
			sn = (String.valueOf(headMap.get(Value.Head_SN))).trim();
			
			Device device = deviceTaskDao.findDeviceBySn(sn);// 通过设备号获取数据库注册过的设备
			AllDevicePool.ALL_DEVICE_POOL.put(sn, device);// 通过设备序列号获取到服务器上登记过的设备
			
			if(device == null)
            {
                throw  new Exception("设备未注册");
            }

			Device device99 = deviceTaskDao99.findDeviceBySn(sn);// 通过设备号获取数据库注册过的设备
			AllDevicePool.ALL_DEVICE_POOL_99.put(sn, device99);// 通过设备序列号获取到服务器上登记过的设备
			if(device99 == null)
			{
				dB99Flg = false;
				System.out.println("设备未注册"); // 99数据库查看设备注册信息
			}

			if (tag == Value.RequestHeaderTag)// 包头标记为请求包
			{
				cmd = (Integer)headMap.get(Value.Head_CMD);
				Integer len = (Integer)headMap.get(Value.Head_PkgLen);// 包总长

				int bodyLen = len - 27;// 包体长度
				byte[] body = read(bodyLen);// 包体字节
				// 读取最后一位 校验
				byte[] xors = read(1);
				// 本地校验的结果
				boolean xorRS = PackUtil.checkXor(requestHead, body, xors);

				if (!xorRS) {
					// 校验失败
					throw new XorException("数据包校验失败");
				}
				byte state = ((Integer)headMap.get(Value.Head_State)).byteValue();

				if (cmd == Value.CMD_Heartbeat) {

					// 心跳包
//                    String bodyStr = new String(body, Value.CharSet);
					System.out.println("-->>接入心跳包，sn=" + sn);
					logger.info("-->>接入心跳包，sn=" + sn );

					// desKey="";这里需要拿到设备的DESKey
					dealHeardBeatTask("DAO78");
					updateLastTime(state);
					if(dB99Flg) {
						// 99数据库
						dealHeardBeatTask99("DAO99");
						updateLastTime99(state);
					}

				} else if (cmd == Value.CMD_IdentifyLog) {
					// 识别记录
					// desKey=""; 这里需要拿到设备的DESKey
					// 解密数据
					body = DES.decryptDES(body, desKey);
					String bodyStr = new String(body, Value.CharSet);
					System.out.println("-->>接入识别记录::" + bodyStr);
					logger.info("-->>接入识别记录::" + bodyStr );
					JSONObject bodyJOSN = JSONObject.parseObject(bodyStr);
					dealIdentifyLog(bodyJOSN, sn, dB99Flg); // 78、99数据库
					updateLastTime(state);
					if(dB99Flg) {
						// 99数据库
						updateLastTime99(state);
					}
				} else if (cmd == Value.CMD_RemoteStaffInfo) {
					//人员注册信息上传
					remoteStaffInfo(requestHead, body, device, dB99Flg);  //78、99数据库
					//处理后续模版信息
					updateLastTime(state);
					if(dB99Flg) {
						// 99数据库
						updateLastTime99(state);
					}
				}
			}
		} catch (IOException e) {
			AllDevicePool.DEVICETASK_POOL.remove(deviceTaskId);
			AllDevicePool.DEVICETASK_POOL_99.remove(deviceTaskId);
			System.out.println("DealConnect.run  " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.out.println("DealConnect.run  " + e.getMessage());
		} catch (InvalidKeyException e) {
			System.out.println("DealConnect.run  " + e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("DealConnect.run  " + e.getMessage());
		} catch (NoSuchPaddingException e) {
			System.out.println("DealConnect.run  " + e.getMessage());
		} catch (BadPaddingException e) {
			System.out.println("DealConnect.run  " + e.getMessage());
		} catch (IllegalBlockSizeException e) {
			System.out.println("DealConnect.run  " + e.getMessage());
		} catch (JSONException e) {
			// json解析错误
			JSOnError(cmd);
			System.out.println("DealConnect.run  " + e.getMessage());
		} catch (XorException e) {
			// 数据包校验失败
			XorError(cmd);
			System.out.println("DealConnect.run  " + e.getMessage());
		} catch (Exception e) {
			System.out.println("DealConnect.run 封装数据包错误" + e);
			logger.info("DealConnect.run 封装数据包错误" + e );
		} finally {
			FreeIO.free(is);
			FreeIO.free(os);
			FreeIO.free(socket);
		}
	}

	/**
	 * 处理接收到的心跳包(78数据库)
	 */
	void dealHeardBeatTask(String dbFlg) throws Exception {

		// DeviceTaskDao deviceTaskDao = new DeviceTaskDao();
		DeviceTask deviceTask = deviceTaskDao.findTaskBySn(sn);

		// 任务被执行的次数
		byte exeTimes = 0x00;
		try {
			String data = null;
			int state = 0x00;// 空闲
			List<byte[]> responseBytesList = null;
			if(null != deviceTask) {
				if (deviceTask.getCmd() != null && !deviceTask.getCmd().equals("")) {
					// 任务被执行的次数
					exeTimes = deviceTask.getExeTimes() == null ? 0 : deviceTask.getExeTimes();
					deviceTaskId = deviceTask.getId();
					// 有任务
					int cmd = deviceTask.getCmd();

					if (AllDevicePool.DEVICETASK_POOL.get(String.valueOf(deviceTask.getId())) == null) {

						if (exeTimes < 10)// 如果任务被执行10次以下
						{
							AllDevicePool.DEVICETASK_POOL.put(String.valueOf(deviceTask.getId()),
									String.valueOf(deviceTask.getId()));// 放在正在执行的任务中
							System.out.println("-->>有任务，ID = " + deviceTask.getId() + "::cmd=" + cmd + "::JSON="
									+ deviceTask.getData());

							deviceTask.setExeTimes(++exeTimes);
							// taskDao.saveTask(deviceTask);
							switch (cmd) {
								case (Value.CMD_SetTime): {
									// 设置时间
									setTime(deviceTask, dbFlg);
									break;
								}
								case (Value.CMD_SetState): {
									//设置设备状态
									deviceSetState(deviceTask, dbFlg);
									break;
								}
								case (Value.CMD_SendPhoto): {
									//下发用户照片
									break;
								}
								case (Value.CMD_SendFile): {
									// 发送用户图片
									sendFile(deviceTask, dbFlg);
									break;
								}
								case (Value.CMD_RuntimeLog): {
									// 设备运行日志
									// getFile(deviceTask);
									break;
								}
								case (Value.CMD_SendStaffInfo): {
									// 发送人员信息
									sendStaff(deviceTask, dbFlg);
									break;
								}
								default: {
									doDeault(deviceTask, dbFlg);
									break;
								}
							}
						} else {
							update(deviceTask, cmd, dbFlg);
						}
						AllDevicePool.DEVICETASK_POOL.remove(deviceTaskId);// 执行完了，删除掉
					} else {
						System.out.println(
								"-->>有任务,ID = " + deviceTaskId + "任务正在执行中::cmd=" + cmd + "::JSON=" + deviceTask.getData());
						// 服务端没有任务需要执行
						byte[] desedData = null;
						if (!StringUtils.isBlank(data)) {
							desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
						}
						responseBytesList = PackUtil.responsePackMak(Value.CMD_Heartbeat, desedData, state, sn, 0, true);
						writeByteList(responseBytesList);
					}

				} else {
					// 服务端没有任务需要执行
					byte[] desedData = null;
					if (!StringUtils.isBlank(data)) {
						desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
					}
					responseBytesList = PackUtil.responsePackMak(Value.CMD_Heartbeat, desedData, state, sn, 0, true);
					writeByteList(responseBytesList);
				}
			}
		} catch (Exception e) {
			deviceTask.setTaskState((byte) 0x02);
			deviceTask.setResult((byte) 0x00);
			deviceTask.setResultDetail("{\"result_desc\":\"" + e.getMessage() + "\"}");
			deviceTask.setReturnTime(new Date());
			deviceTask.setExeTimes(exeTimes);
			deviceTaskDao.updateTask(deviceTask);
			System.out.println("DealConnect.dealHertBeat:" + e.getMessage());
		}
	}

	/**
	 * 处理接收到的心跳包(99数据库)
	 */
	void dealHeardBeatTask99(String dbFlg) throws Exception {

		DeviceTask deviceTask = deviceTaskDao99.findTaskBySn(sn);

		// 任务被执行的次数
		byte exeTimes = 0x00;
		try {
			String data = null;
			int state = 0x00;// 空闲
			List<byte[]> responseBytesList = null;
			if(null != deviceTask) {
				if (deviceTask.getCmd() != null && !deviceTask.getCmd().equals("")) {
					// 任务被执行的次数
					exeTimes = deviceTask.getExeTimes() == null ? 0 : deviceTask.getExeTimes();
					deviceTaskId = deviceTask.getId();
					// 有任务
					int cmd = deviceTask.getCmd();
					if (AllDevicePool.DEVICETASK_POOL_99.get(String.valueOf(deviceTask.getId())) == null) {

						if (exeTimes < 10)// 如果任务被执行10次以下
						{
							AllDevicePool.DEVICETASK_POOL_99.put(String.valueOf(deviceTask.getId()),
									String.valueOf(deviceTask.getId()));// 放在正在执行的任务中
							System.out.println("-->>有任务，ID = " + deviceTask.getId() + "::cmd=" + cmd + "::JSON="
									+ deviceTask.getData());

							deviceTask.setExeTimes(++exeTimes);
							switch (cmd) {
								case (Value.CMD_SetTime): {
									// 设置时间
									setTime(deviceTask,dbFlg);
									break;
								}
								case (Value.CMD_SetState): {
									//设置设备状态
									deviceSetState(deviceTask,dbFlg);
									break;
								}
								case (Value.CMD_SendPhoto): {
									//下发用户照片
									break;
								}
								case (Value.CMD_SendFile): {
									// 发送用户图片
									sendFile(deviceTask,dbFlg);
									break;
								}
								case (Value.CMD_RuntimeLog): {
									// 设备运行日志
									// getFile(deviceTask);
									break;
								}
								case (Value.CMD_SendStaffInfo): {
									// 发送人员信息
									sendStaff(deviceTask, dbFlg);
									break;
								}
								default: {
									doDeault(deviceTask, dbFlg);
									break;
								}
							}
						} else {
							update(deviceTask, cmd, dbFlg);
						}
						AllDevicePool.DEVICETASK_POOL_99.remove(deviceTaskId);// 执行完了，删除掉
					} else {
						System.out.println(
								"-->>有任务,ID = " + deviceTaskId + "任务正在执行中::cmd=" + cmd + "::JSON=" + deviceTask.getData());
						// 服务端没有任务需要执行
						byte[] desedData = null;
						if (!StringUtils.isBlank(data)) {
							desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
						}
						responseBytesList = PackUtil.responsePackMak(Value.CMD_Heartbeat, desedData, state, sn, 0, true);
						writeByteList(responseBytesList);
					}
				} else {
					// 服务端没有任务需要执行
					byte[] desedData = null;
					if (!StringUtils.isBlank(data)) {
						desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
					}
					responseBytesList = PackUtil.responsePackMak(Value.CMD_Heartbeat, desedData, state, sn, 0, true);
					writeByteList(responseBytesList);
				}
			}
		} catch (Exception e) {
			deviceTask.setTaskState((byte) 0x02);
			deviceTask.setResult((byte) 0x00);
			deviceTask.setResultDetail("{\"result_desc\":\"" + e.getMessage() + "\"}");
			deviceTask.setReturnTime(new Date());
			deviceTask.setExeTimes(exeTimes);
			deviceTaskDao99.updateTask(deviceTask);
			System.out.println("DealConnect.dealHertBeat:" + e.getMessage());
		}
	}

	/**
	 * 一个任务执行10次之后，更新任务和设备状态，插入一条系统消息
	 * @author ygx
	 * @date 2016-3-5
	 * @param deviceTask
	 * @param dbFlg
	 * @param cmd
	 */
	public void update(DeviceTask deviceTask, int cmd, String dbFlg)
	{
		String msg = Value.CMD_MAP.get(cmd)+"失败";
		if("DAO78".equals(dbFlg)) {
			//修改设备信息
			Device device = AllDevicePool.ALL_DEVICE_POOL.get(sn);// 通过设备序列号获取到服务器上登记过的设备
			if (device != null)
			{
				// device.setState(headMap.get(Value.Head_State).byteValue());// 设备状态
				device.setLastConnTime(new Date());// 最后通讯时间
				deviceTaskDao.updateDevice(device);
				AllDevicePool.ALL_DEVICE_POOL.put(sn, device);
			}
			//更新任务信息
			deviceTask.setResult((byte) 0x02);
			deviceTask.setResultDetail("{\"result_desc\":\""+msg+"\"}");
			deviceTask.setReturnTime(new Date());
			deviceTask.setTaskState((byte) 0x01);
			deviceTaskDao.updateTask(deviceTask);
			//系统消息
			// sysMsg = new SysMsg(msg, Value.CMD_MAP.get(cmd), deviceTask.getOprator(),
			// (byte)0, deviceTask.getFilePath(), (byte)0, AllDevicePool.ALL_DEVICE_POOL.get(sn).getName());
			// sysMsgDao.saveSysMsg(sysMsg);
		} else if ("DAO99".equals(dbFlg)) {
			//修改设备信息
			Device device = AllDevicePool.ALL_DEVICE_POOL_99.get(sn);// 通过设备序列号获取到服务器上登记过的设备
			if (device != null)
			{
				device.setLastConnTime(new Date());// 最后通讯时间
				deviceTaskDao99.updateDevice(device);
				AllDevicePool.ALL_DEVICE_POOL_99.put(sn, device);
			}
			//更新任务信息
			deviceTask.setResult((byte) 0x02);
			deviceTask.setResultDetail("{\"result_desc\":\""+msg+"\"}");
			deviceTask.setReturnTime(new Date());
			deviceTask.setTaskState((byte) 0x01);
			deviceTaskDao99.updateTask(deviceTask);
		}
	}

	/**
	 * 接收设备上传的注册信息，设备端新增的人员，设备端生物特征信息有更新的时候，都会上传数据
	 * @param head
	 * @param body
	 * @throws Exception
	 */
	void remoteStaffInfo(byte[] head, byte[] body,Device device, boolean dB99Flg) throws Exception
	{
		Map<String, Object> map = PackUtil.HeaderAnalysis(head);
		int packLen = 0;
		int packCount = (Integer) map.get(Value.Head_PkgCount);
		//解密
		body = DES.decryptDES(body, desKey);

		String bodyStr = new String(body, Value.CharSet);
		JSONObject staffInfo = JSONObject.parseObject(bodyStr);//人员信息
		String enroll_type = staffInfo.getString("enroll_type");
		int  data_len = 0;
		if(enroll_type.charAt(0)=='1' || enroll_type.charAt(1)=='1' || enroll_type.charAt(2)=='1'){
			data_len = staffInfo.getInteger("data_len");
		}

		staffInfo.put("sn",sn);
		//记录注册人员信息
		RegisterUpload registerUpload = new RegisterUpload();
		registerUpload.setName(staffInfo.getString("name"));
		registerUpload.setDuty("zhicheng05");
		registerUpload.setWorkType("gongzhong07");
		registerUpload.setDepart(staffInfo.getString("depart"));
		registerUpload.setWork_sn(staffInfo.getString("numbr"));
		registerUpload.setId_card(staffInfo.getString("id_card"));
		registerUpload.setData_len(staffInfo.getString("data_len"));
		registerUpload.setMd5(staffInfo.getString("md5"));
		registerUpload.setFp_count(staffInfo.getString("fp_count"));
		registerUpload.setFv_count(staffInfo.getString("fv_count"));
		registerUpload.setEnroll_type(staffInfo.getString("enroll_type"));
		registerUpload.setIc(staffInfo.getString("ic"));
		registerUpload.set_id(staffInfo.getString("id"));
		registerUpload.setArchitecturalid(device.getArchitecturalid());
		registerUpload.setArchitecturalname(device.getArchitecturalname());
		registerUpload.setAdd_time(new Date());

		JSONObject json ;

		// 总文件
		int pkgindex;// 包索引
		byte[] images = new byte[data_len];
		int readd = 0;
		do
		{
			head = read(26);
			map = PackUtil.HeaderAnalysis(head);
			pkgindex = (Integer) map.get(Value.Head_PkgIndex);

			packLen = (Integer) map.get(Value.Head_PkgLen);
			body = read(packLen - 27);

			byte[] xors = read(1);
			boolean xor = PackUtil.checkXor(head, body, xors);
			if(!xor){
				throw new XorException("包校验失败");
			}
			System.arraycopy(body,0,images,readd,body.length);
			readd+=body.length;

		} while (pkgindex < packCount);

		createFile("D:/test.gz",images);
		//解压
		images = ZipHelper.unGZip(images);

		String remoteMd5 = staffInfo.getString("md5").toLowerCase();
		String localMD5 = FileMD5.MD5(images).toLowerCase();
		if(!remoteMd5.equals(localMD5))
		{
			//二进制数据校验失败，后续不执行了
			System.out.println("-->>收到脱机注册信息：MD5校验失败,localMD5="+localMD5+",remoteMD5="+remoteMd5);
			json = new JSONObject();
			json.put("result_code", "9005");
			json.put("result_desc", "MD5校验失败");
			writeResult(json, Value.CMD_RemoteStaffInfo, 0x02);
			return;
		}

		//注册人员信息存入78数据库
		RegisterUserService registerUserService = new RegisterUserService(registerUserInfoDao);
		//验证人员信息是否存在
		int total = registerUserService.findUserById(registerUpload.getWork_sn(),registerUpload.getArchitecturalid());
		if(total == 0){
			//人员信息不存在
			registerUserService.insertRegisterUserInfo(registerUpload);
		}else{
			//人员已存在
			registerUserService.updateUserInfo(registerUpload);
		}

		//注册人员信息存入99数据库
		RegisterUserService99 registerUserService99 = new RegisterUserService99(registerUserInfoDao99);
		if(dB99Flg) {
			//验证人员信息是否存在
			int total99 = registerUserService99.findUserById(registerUpload.getWork_sn(),registerUpload.getArchitecturalid());
			if(total99 == 0){
				//人员信息不存在
				registerUserService99.insertRegisterUserInfo(registerUpload);
			}else{
				//人员已存在
				registerUserService99.updateUserInfo(registerUpload);
			}
		}

		UserTemplates userTemplates = new UserTemplates();
		userTemplates.setArchitecturalid(device.getArchitecturalid());
		userTemplates.setSn(sn);
		userTemplates.setNumbr(staffInfo.getString("numbr"));
		//人员注册模板解析
		Map<String,byte[][]> templates = new HashMap<String, byte[][]>();
		if(images.length>0)
		{
			//注册类型
			String enrollType = staffInfo.getString("enroll_type");
			int read_start = 0;

			if(enrollType.charAt(0)=='1')
			{
				//虹膜
				byte[] iristemplate = new byte[1024];

				int bmplen = 66614;
				if(staffInfo.get("irisbmplen")!=null)
				{
					bmplen = Integer.parseInt(staffInfo.get("irisbmplen")+"");
				}

				System.arraycopy(images,read_start,iristemplate,0,1024);

				read_start += 1024;
				byte[] left_eye_img = new byte[bmplen];
				System.arraycopy(images,read_start,left_eye_img,0,bmplen);
				read_start += bmplen;
				byte[] right_eye_img = new byte[bmplen];
				System.arraycopy(images,read_start,right_eye_img,0,bmplen);
				read_start += bmplen;

				byte[][] irises = new byte[3][];
				irises[0] = iristemplate;
				irises[1] = left_eye_img;
				irises[2] = right_eye_img;
				templates.put("iris",irises);
				userTemplates.setTemplate(iristemplate);
				userTemplates.setType(1);
				userTemplates.setId_card("");
				//人员生物特征写入78数据库
				//查询此用户生物特征是否存在
				int templatesTotal = registerUserService.findUserTemplates(userTemplates);
				if(templatesTotal == 0){
					//不存在用户生物特征信息
					registerUserService.insertUserTemplate(userTemplates);
				}else{
					//生物信息已存在
					registerUserService.updateUserTemplates(userTemplates);
				}
				if(dB99Flg) {
					//人员生物特征写入99数据库
					//查询此用户生物特征是否存在
					int templatesTotal99 = registerUserService99.findUserTemplates(userTemplates);
					if(templatesTotal99 == 0){
						//不存在用户生物特征信息
						registerUserService99.insertUserTemplate(userTemplates);
					}else{
						//生物信息已存在
						registerUserService99.updateUserTemplates(userTemplates);
					}
				}
                /*
                FileUtils.writeByteArrayToFile(new File("d://right.bmp"),right_eye_img);
                FileUtils.writeByteArrayToFile(new File("d://left.bmp"),left_eye_img);
                */
			}
			if(enrollType.charAt(1)=='1')
			{
				//人脸
				byte[] facetemplate = new byte[27668];
				System.arraycopy(images,read_start,facetemplate,0,27668);
				read_start += 27668;
				byte[][] faces = new byte[1][];
				faces[0] = facetemplate;
				templates.put("face",faces);

				userTemplates.setTemplate(facetemplate);
				userTemplates.setType(2);
				userTemplates.setId_card("");
				//人员生物特征写入78数据库
				//查询此用户生物特征是否存在
				int templatesTotal = registerUserService.findUserTemplates(userTemplates);
				if(templatesTotal == 0){
					//不存在用户生物特征信息
					registerUserService.insertUserTemplate(userTemplates);
				}else{
					//生物信息已存在
					registerUserService.updateUserTemplates(userTemplates);
				}

				if(dB99Flg) {
					//人员生物特征写入99数据库
					//查询此用户生物特征是否存在
					int templatesTotal99 = registerUserService99.findUserTemplates(userTemplates);
					if(templatesTotal99 == 0){
						//不存在用户生物特征信息
						registerUserService99.insertUserTemplate(userTemplates);
					}else{
						//生物信息已存在
						registerUserService99.updateUserTemplates(userTemplates);
					}
				}
			}
			if(enrollType.charAt(2)=='1')
			{
				//指纹
				int fpcount = staffInfo.getInteger("fp_count");
				byte[][] fps = new byte[1][];
				byte[] fp = new byte[498 * fpcount];
				System.arraycopy(images,read_start,fp,0,498 * fpcount);
				fps[0] = fp;
				userTemplates.setTemplate(fp);
				userTemplates.setType(3);
				userTemplates.setId_card("");
				//用户生物特征写入78数据库
				//查询此用户生物特征是否存在
				int templatesTotal = registerUserService.findUserTemplates(userTemplates);
				if(templatesTotal == 0){
					//不存在用户生物特征信息
					registerUserService.insertUserTemplate(userTemplates);
				}else{
					//生物信息已存在
					registerUserService.updateUserTemplates(userTemplates);
				}

				if(dB99Flg) {
					//用户生物特征写入99数据库
					//查询此用户生物特征是否存在
					int templatesTotal99 = registerUserService99.findUserTemplates(userTemplates);
					if(templatesTotal99 == 0){
						//不存在用户生物特征信息
						registerUserService99.insertUserTemplate(userTemplates);
					}else{
						//生物信息已存在
						registerUserService99.updateUserTemplates(userTemplates);
					}
				}
				templates.put("fp",fps);
			}
		}
		System.out.println("-->>收到脱机注册信息："+staffInfo.toJSONString());
		json = new JSONObject();
		json.put("result_code", "0000");
		json.put("result_desc", "收到啦");
		writeResult(json, Value.CMD_RemoteStaffInfo, 0x01);
	}

	//将byte数组写入文件
	public void createFile(String path, byte[] content) throws IOException {

		FileOutputStream fos = new FileOutputStream(path);

		fos.write(content);
		fos.close();
	}

	/**
	 * 处理识别记录
	 *
	 * @param json
	 */
	void dealIdentifyLog(JSONObject json,String sn, boolean dB99Flg) throws Exception {
		JSONArray logArray = json.getJSONArray("logs");

		List<byte[]> byteList = null;
		try {
			// 78数据库
			UserInfoRecordsService userInfoRecordsService = new UserInfoRecordsService(userInfoRecordsDao);
			// 99数据库
			UserInfoRecordsService99 userInfoRecordsService99 = new UserInfoRecordsService99(userInfoRecordsDao99);

			int num = logArray.size();
			for(int i=0;i<num;i++){
				UserInfoRecords userInfoRecords = new UserInfoRecords();
				JSONObject userLog = logArray.getJSONObject(i);
				//识别类型
				userInfoRecords.setRecog_type(userLog.getString("recog_type"));
				//用户ID
				userInfoRecords.setUser_id(userLog.getString("user_id"));
				//识别时间
				userInfoRecords.setRecog_time(userLog.getString("recog_time"));
				//打卡类别
				userInfoRecords.setAuth_stat(Integer.parseInt(userLog.getString("auth_stat")));
				//设备sn号
				userInfoRecords.setSn(sn);
				//存入78数据库
				userInfoRecordsService.insertUserRecord(userInfoRecords);
				if(dB99Flg) {
					//存入99数据库
					userInfoRecordsService99.insertUserRecord(userInfoRecords);
				}
			}
			json.clear();
			// 保存成功
			json.put("result_code", "0000");
			json.put("result_desc", "服务端接收识别记录成功");
			String data = json.toJSONString();
			byte[] desedData = null;
			if (!StringUtils.isBlank(data)) {
				desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
			}
			System.out.println("-->>上传识别记录：" + logArray.toString() + "<<-- 上传成功");
			logger.info("-->>上传识别记录：" + logArray.toString() + "<<-- 上传成功");
			byteList = PackUtil.responsePackMak(Value.CMD_IdentifyLog, desedData, 0x01, sn, 0, true);
		} catch (Exception e) {
			System.out.println("-->>保存上传的识别记录错误" + e);
			logger.info("-->>保存上传的识别记录错误" + e);
			// 保存失败
			json.put("result_code", "9998");
			json.put("result_desc", "服务端接收识别记录失败");
			String data = json.toJSONString();
			byte[] desedData = null;
			if (!StringUtils.isBlank(data)) {
				desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
			}

			System.out.println("-->>上传识别记录：" + logArray.toString() + "<<-- 上传失败");
			logger.info("-->>上传识别记录：" + logArray.toString() + "<<-- 上传失败");
			byteList = PackUtil.responsePackMak(Value.CMD_IdentifyLog, desedData, 0x02, sn, 0, true);
		}
		writeByteList(byteList);
	}

	/**
	 * JSON解析失败
	 *
	 * @param cmd
	 */
	void JSOnError(Integer cmd) {
		JSONObject jo = new JSONObject();
		jo.put("result_code", "9001");
		jo.put("result_desc", "数据格式错误[json 解析失败]");

		writeResult(jo, cmd, 0x02);
	}

	/**
	 * 数据包校验失败
	 *
	 * @param cmd
	 */
	void XorError(int cmd) {
		JSONObject jo = new JSONObject();
		jo.put("result_code", "9029");
		jo.put("result_desc", "数据包校验失败");

		writeResult(jo, cmd, 0x02);
	}

	/**
	 * 写出错误信息
	 *
	 * @param json
	 * @param cmd
	 */
	void writeResult(JSONObject json, int cmd, int rs) {
		List<byte[]> packList = null;
		try {
			String data = json.toJSONString();
			byte[] desedData = null;
			if (!StringUtils.isBlank(data)) {
				desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
			}
			packList = PackUtil.responsePackMak(cmd, desedData, rs, sn, 0, true);
			writeByteList(packList);
		} catch (Exception e) {
			System.out.println("DealConnect.JSOnError" + e);
		} finally {
			FreeIO.free(is);
			FreeIO.free(os);
			FreeIO.free(socket);
		}
	}

	/**
	 * 设置时间
	 *
	 * @param deviceTask
	 * @param dbFlg
	 * @throws Exception
	 */
	void setTime(DeviceTask deviceTask, String dbFlg) throws Exception {
		String data = deviceTask.getData();
		if (StringUtils.isBlank(data)) {
			// 时间要取当前时间，数据包要单独处理
			JSONObject json = new JSONObject();
			json.put("date_time", FormatDateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss"));
			data = json.toJSONString();
			deviceTask.setData(json.toJSONString());
		}
		int state = Value.CMD_SetTime;

		byte[] desedData = null;
		if (!StringUtils.isBlank(data)) {
			desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
		}
		List<byte[]> responseBytesList = PackUtil.responsePackMak(Value.CMD_Heartbeat, desedData, state, sn, 0, true);
		writeByteList(responseBytesList);
		dealDeviceResult(deviceTask, dbFlg);

	}

	/**
	 * 设置设备状态
	 * @param deviceTask
	 * @param dbFlg
	 */
	public void deviceSetState(DeviceTask deviceTask, String dbFlg)throws Exception{
		String data = deviceTask.getData();
//		if (StringUtils.isBlank(data)) {
//			// 时间要取当前时间，数据包要单独处理
//			JSONObject json = new JSONObject();
//			json.put("date_time", FormatDateUtil.getFormatDate("yyyyMMddHHmmss"));
//			data = json.toJSONString();
//			deviceTask.setData(json.toJSONString());
//		}
		int state = Value.CMD_SetState;

		byte[] desedData = null;
		if (!StringUtils.isBlank(data)) {
			desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
		}
		List<byte[]> responseBytesList = PackUtil.responsePackMak(Value.CMD_Heartbeat, desedData, state, sn, 0, true);
		writeByteList(responseBytesList);
		dealDeviceResult(deviceTask,dbFlg);
	}

	/**
	 * 直接操作设备的，设备只需要返回成功失败的一些指令
	 *
	 * @param deviceTask
	 * @param dbFlg   数据库判别
	 * @throws Exception
	 */
	void doDeault(DeviceTask deviceTask, String dbFlg) throws Exception {
		String data = deviceTask.getData();
		int state = deviceTask.getCmd();

		byte[] desedData = null;
		if (!StringUtils.isBlank(data)) {
			desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
		}

		List<byte[]> responseBytesList = PackUtil.responsePackMak(Value.CMD_Heartbeat, desedData, state, sn, 0, true);
		writeByteList(responseBytesList);

		dealDeviceResult(deviceTask, dbFlg);
	}

	/**
	 * 写出数据
	 *
	 * @param byteList
	 * @throws IOException
	 */
	void writeByteList(List<byte[]> byteList) throws IOException {
		for (byte[] bytes : byteList) {
			if (bytes != null) {
				os.write(bytes);
				os.flush();
			}
		}
	}

	/**
	 * 读取指定位数的字节流
	 *
	 * @param len
	 * @return
	 * @throws IOException
	 */
	byte[] read(int len) throws IOException {
		byte[] rsb = new byte[len];
		for (int i = 0; i < len; i++) {
			rsb[i] = (byte) is.read();
		}
		return rsb;
	}

	/**
	 * 处理设备返回结果
	 *
	 * @param deviceTask   操作任务
	 * @param dbFlg   数据库判别
	 * @throws IOException
	 * @throws XorException
	 */
	void dealDeviceResult(DeviceTask deviceTask,String dbFlg)
			throws IOException, XorException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		// 读取返回结果
		byte[] head = read(26);
		Map<String, Object> headMap = PackUtil.HeaderAnalysis(head);
		Integer tag = (Integer)headMap.get(Value.Head_Tag);
		int cmd = deviceTask.getCmd();
		Byte msgResult = null;
		try {
			if (tag == Value.ResponseHeaderTag) {

				Integer pkgLen = (Integer)headMap.get(Value.Head_PkgLen);
				byte[] body = read(pkgLen - 27);// 包体
				byte[] remoteXorbts = read(1);// 教研位

				boolean xorRS = PackUtil.checkXor(head, body, remoteXorbts);
				if (xorRS)// 校验成功
				{
					String msg = null;
					int result = (Integer)headMap.get(Value.Head_State);
					body = DES.decryptDES(body, desKey);
					String resultDetail = new String(body, Value.CharSet);
					System.out.println("-->>设备返回：：" + resultDetail);
					deviceTask.setResult((byte) result);
					deviceTask.setResultDetail(resultDetail);
					deviceTask.setReturnTime(new Date());
					deviceTask.setTaskState((byte) 0x01);
					if("DAO78".equals(dbFlg)) {
						deviceTaskDao.updateTask(deviceTask);
					} else if("DAO99".equals(dbFlg)) {
						deviceTaskDao99.updateTask(deviceTask);
					}

					if (result == 1)// 操作成功
					{
						msgResult = (byte) result;
						msg = Value.CMD_MAP.get(cmd) + "成功";
					} else
					// 操作失败
					{
						msgResult = (byte) 0;
						msg = Value.CMD_MAP.get(cmd) + "失败";
					}
				} else {
					deviceTask.setResult((byte) 0x02);
					deviceTask.setResultDetail("数据包校验失败");
					deviceTask.setReturnTime(new Date());
					deviceTask.setTaskState((byte) 0x01);
					if("DAO78".equals(dbFlg)) {
						deviceTaskDao.updateTask(deviceTask);
					} else if("DAO99".equals(dbFlg)) {
						deviceTaskDao99.updateTask(deviceTask);
					}
					throw new XorException("数据包校验失败");
				}
			}
		} catch (RuntimeException e) {
			// 修改任务信息
			deviceTask.setResult((byte) 0x02);
			deviceTask.setResultDetail(e.getMessage());
			deviceTask.setReturnTime(new Date());
			deviceTask.setTaskState((byte) 0x01);
			if("DAO78".equals(dbFlg)) {
				deviceTaskDao.updateTask(deviceTask);
			} else if("DAO99".equals(dbFlg)) {
				deviceTaskDao99.updateTask(deviceTask);
			}
			e.printStackTrace();
		}
	}

	/**
	 * 发送文件（人员照片，文件）
	 *
	 * @param deviceTask  操作任务
	 * @param dbFlg   数据库判别
	 * @throws Exception
	 */
	void sendFile(DeviceTask deviceTask, String dbFlg) throws Exception {
		int cmd = deviceTask.getCmd();

		try {
			String filePath = deviceTask.getFilePath();
			if (StringUtils.isBlank(filePath))
			{
				throw new IOException();
			}
			File file = new File(filePath);
			String data = deviceTask.getData();

			byte[] fileByte = FileUtils.readFileToByteArray(file);
			byte[] zipbyte = ZipHelper.gZip(fileByte);

			JSONObject json = JSONObject.parseObject(data);
			json.put("file_len", zipbyte.length);
			json.put("md5", FileMD5.MD5(fileByte));

			data = json.toJSONString();
			byte[] desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);

			// 最大包体长度
			int maxBodyLen = 10240 - 27;
			// 数据长度
			int bodyLen = zipbyte.length;
			// 最大包数
			int pkgCount = bodyLen / maxBodyLen;
			// 是否是整包
			boolean modRS = bodyLen % maxBodyLen > 0;
			if (modRS)
			{
				// 不是整包，则要再加一个包，放剩下的数据
				pkgCount += 1;
			}
			List<byte[]> byteList = PackUtil.responsePackMak(Value.CMD_Heartbeat, desedData, cmd, sn, pkgCount, true);
			writeByteList(byteList);
			byteList = PackUtil.responsePackMak(Value.CMD_Heartbeat, zipbyte, cmd, sn, pkgCount, false);
			writeByteList(byteList);

			dealDeviceResult(deviceTask,dbFlg);
		} catch (IOException e) {
			System.out.println("发送文件错误" + e);
			deviceTask.setResult((byte) 0x02);
			deviceTask.setResultDetail(e.getMessage());
			deviceTask.setTaskState((byte) 0x01);
			if("DAO78".equals(dbFlg)) {
				deviceTaskDao.updateTask(deviceTask);
			} else if("DAO99".equals(dbFlg)) {
				deviceTaskDao99.updateTask(deviceTask);
			}
			// 系统消息
			// sysMsg = new SysMsg("文件发送错误", Value.CMD_MAP.get(cmd),
			// deviceTask.getOprator(),
			// (byte)0, deviceTask.getFilePath(), (byte)0,
			// AllDevicePool.ALL_DEVICE_POOL.get(sn).getName());
			// sysMsgDao.saveSysMsg(sysMsg);
		}
	}

	/**
	 * 发送文件（人员照片，文件）
	 *
	 * @param deviceTask  操作任务
	 * @param dbFlg 数据库判别
	 * @throws Exception
	 */
	void sendStaff(DeviceTask deviceTask, String dbFlg) throws Exception
	{
		int cmd = deviceTask.getCmd();

		try
		{
			String data = deviceTask.getData();

//            byte[] fileByte = FileUtils.readFileToByteArray(file);
//            byte[] zipbyte = ZipHelper.gZip(fileByte);
			JSONObject json = JSONObject.parseObject(data);
			String enrollType = json.getString("enroll_type");

			byte[] fileByte = new byte[500 *1024];
			byte[] newByte = null;
			int length = 0;//实际长度
			//虹膜
			if(enrollType.charAt(0) == '1' || enrollType.charAt(1) == '1' || enrollType.charAt(2) == '1'){

				List<UserTemplates> userTemplatesList = new ArrayList<UserTemplates>();
				if("DAO78".equals(dbFlg)) {
					userTemplatesList = deviceTaskDao.findTemplatesBySn(json.getString("numbr"), deviceTask.getProjectId());
				} else if("DAO99".equals(dbFlg)) {
					userTemplatesList = deviceTaskDao99.findTemplatesBySn(json.getString("numbr"), deviceTask.getProjectId());
				}
				for(UserTemplates userTemplates : userTemplatesList){
					System.arraycopy(userTemplates.getTemplate(),0,fileByte,length,userTemplates.getTemplate().length);//fileByte userTemplates.getTemplate();
					length = length + userTemplates.getTemplate().length;
				}

				newByte = new byte[length];
				System.arraycopy(fileByte,0,newByte,0,length);
			}

			byte[] zipbyte = new byte[0];
			if(null != newByte){
				zipbyte = ZipHelper.gZip(newByte);
				//json.put("data_len", length);
				System.out.println(FileMD5.MD5(newByte));
				json.put("md5", FileMD5.MD5(newByte).toLowerCase());
			}

			data = json.toJSONString();
			System.out.println(data);
			byte[] desedData = DES.encryptDES(data.getBytes(Value.CharSet), desKey);
			// 最大包体长度
			int maxBodyLen = 10240 - 27;
			// 数据长度
			int bodyLen = zipbyte.length;
			// 最大包数
			int pkgCount = bodyLen / maxBodyLen;
			// 是否是整包
			boolean modRS = bodyLen % maxBodyLen > 0;
			if (modRS)
			{
				// 不是整包，则要再加一个包，放剩下的数据
				pkgCount += 1;
			}
			List<byte[]> byteList = PackUtil.responsePackMak(Value.CMD_Heartbeat, desedData, cmd, sn, pkgCount, true);
			writeByteList(byteList);

			if(zipbyte.length > 0){
				byteList = PackUtil.responsePackMak(Value.CMD_Heartbeat, zipbyte, cmd, sn, pkgCount, false);
				writeByteList(byteList);
			}
			dealDeviceResult(deviceTask, dbFlg);

		} catch (IOException e)
		{
			System.out.println("人员信息错误" + e);
			deviceTask.setResult((byte) 0x02);
			deviceTask.setResultDetail(e.getMessage());
			deviceTask.setTaskState((byte) 0x01);
			if("DAO78".equals(dbFlg)) {
				deviceTaskDao.updateTask(deviceTask);
			} else if ("DAO99".equals(dbFlg)) {
				deviceTaskDao99.updateTask(deviceTask);
			}
		}
	}

	void updateLastTime(byte state) {
		// 修改设备信息
		Device device = AllDevicePool.ALL_DEVICE_POOL.get(sn);// 通过设备序列号获取到服务器上登记过的设备
		if (device != null) {
			device.setState(state);// 设备状态
			device.setLastConnTime(new Date());// 最后通讯时间
			deviceTaskDao.updateDevice(device);// 更新设备信息
			AllDevicePool.ALL_DEVICE_POOL.put(sn, device);
		}
	}

	void updateLastTime99(byte state) {
		// 修改设备信息
		Device device = AllDevicePool.ALL_DEVICE_POOL_99.get(sn);// 通过设备序列号获取到服务器上登记过的设备
		if (device != null) {
			device.setState(state);// 设备状态
			device.setLastConnTime(new Date());// 最后通讯时间
			deviceTaskDao99.updateDevice(device);// 更新设备信息
			AllDevicePool.ALL_DEVICE_POOL_99.put(sn, device);
		}
	}
}
