package com.escst.socket.utils;

public class ParamsSwitchUtil {
	
	/**
	 * 品茗系统输出控制状态码转换
	 * @param num 状态码
	 * @return
	 */
	public static String outControlParams(int num){
		String str = "";
		switch(num){
			case 0:
				str = "截断左转高速";
				break;
			case 1:
				str = "截断左转低速";
				break;
			case 2:
				str = "截断右转高速";
				break;
			case 3:
				str = "截断右转低速";
				break;
			case 4:
				str = "截断小车出高速";
				break;
			case 5:
				str = "截断小车出低速";
				break;
			case 6:
				str = "截断小车进高速";
				break;
			case 7:
				str = "截断小车进低速";
				break;
			case 8:
				str = "截断提升高速";
				break;
			case 9:
				str = "截断提升低速";
				break;
			case 10:
				str = "截断下绳低速";
				break;
			default:
				str = "未知";
				break;
		}
		
		return str;
	}
	
	/**
	 * 系统预警状态码转换
	 * @param num 状态码
	 * @return
	 */
	public static String earlyAlarmParam(int num){
		String str = "";
		switch(num){
			case 0:
				str = "防碰撞左转预警";
				break;
			case 1:
				str = "防碰撞右转预警";
				break;
			case 2:
				str = "防碰撞小车出预警";
				break;
			case 3:
				str = "防碰撞小车进预警";
				break;
			case 4:
				str = "左转环境预警";
				break;
			case 5:
				str = "右转环境预警";
				break;
			case 6:
				str = "小车出环境预警";
				break;
			case 7:
				str = "小车进环境预警";
				break;
			case 8:
				str = "最大幅度限位预警";
				break;
			case 9:
				str = "最小幅度限位预警";
				break;
			case 10:
				str = "高度上限位预警";
				break;
			case 11:
				str = "高度下限位预警";
				break;
			case 12:
				str = "回转左限位预警";
				break;
			case 13:
				str = "回转右限位预警";
				break;
			case 24:
				str = "力矩预警";
				break;
			case 25:
				str = "力预警";
				break;
			case 28:
				str = "倾角预警";
				break;
			case 29:
				str = "风速预警";
				break;
			default:
				str = "无预警";
				break;
		}
		
		return str;
	}
	
	/**
	 * 品茗系统报警状态码转换
	 * @param num 状态码
	 * @return
	 */
	public static String alarmParam(int num){
		String str = "";
		switch(num){
			case 0:
				str = "防碰撞左转报警";
				break;
			case 1:
				str = "防碰撞右转报警";
				break;
			case 2:
				str = "防碰撞小车出报警";
				break;
			case 3:
				str = "防碰撞小车进报警";
				break;
			case 4:
				str = "左转环境报警";
				break;
			case 5:
				str = "右转环境报警";
				break;
			case 6:
				str = "小车出环境报警";
				break;
			case 7:
				str = "小车进环境报警";
				break;
			case 8:
				str = "最大幅度限位报警";
				break;
			case 9:
				str = "最小幅度限位报警";
				break;
			case 10:
				str = "高度上限位报警";
				break;
			case 11:
				str = "高度下限位报警";
				break;
			case 12:
				str = "回转左限位报警";
				break;
			case 13:
				str = "回转右限位报警";
				break;
			case 24:
				str = "力矩报警";
				break;
			case 25:
				str = "力报警";
				break;
			case 28:
				str = "倾角报警";
				break;
			case 29:
				str = "风速报警";
				break;
			default:
				str = "无报警";
				break;
		}
		
		return str;
	}
	
	/**
	 *品茗 违章操作状态码转换
	 * @param num 状态码
	 * @return
	 */
	public static String peccancyParam(int num){
		String str = "";
		switch(num){
			case 4:
				str = "力矩违章";
				break;
			case 5:
				str = "力违章";
				break;
			default:
				str = "未知";
				break;
		}
		
		return str;
	}
	
	/**
	 * 品茗传感器报警状态码转换
	 * @param num 状态码
	 * @return
	 */
	public static String sensorAlarmParam(int num){
		String str = "";
		switch(num){
			case 0:
				str = "转角传感器";
				break;
			case 1:
				str = "变幅传感器";
				break;
			case 2:
				str = "高度传感器";
				break;
			case 3:
				str = "吊重传感器";
				break;
			case 4:
				str = "风速传感器";
				break;
			case 7:
				str = "倾角传感器";
				break;
			default:
				str = "无传感器报警";
				break;
		}
		
		return str;
	}
}
