package com.escst.socket.utils;

public class ParamsConvertUtil {

	public static String envorimentParamsConvert(String str){
		String paramValue = "";
		String alarmtype = "";
		/** alarmtype 0 噪音 1 粉尘 2 温度 3 湿度  4有毒气体 5可燃气体 6风速*/
		switch(str){
			case "A" :
				paramValue = "粉尘PM2.5";
				alarmtype = "1";
				break;
			case "B" :
				paramValue = "粉尘PM10";
				alarmtype = "7";
				break;
			case "C" :
				paramValue = "噪音";
				alarmtype = "0";
				break;
			case "D" :
				paramValue = "风速";
				alarmtype = "6";
				break;
			case "E" :
				paramValue = "有毒气体";
				alarmtype = "4";
				break;
			case "F" :
				paramValue = "可燃气体";
				alarmtype = "5";
				break;
			case "G" :
				paramValue = "湿度";
				alarmtype = "3";
				break;
			case "H" :
				paramValue = "温度";
				alarmtype = "2";
				break;
			default:
				paramValue = "无报警";
				break;
		}

		return paramValue +"#"+ alarmtype;
	}


	public static String towerParamsConvert(String str){
		String warningValue = "";
		String alarmValue = "";
		String sensoralarm = "";

		/** warningValue塔吊预警状态*/
		switch(str){
			case "A0" :
				warningValue = "大臂和大臂预警";
				sensoralarm = "转角传感器";
				break;
			case "A2" :
				warningValue = "大臂和小车预警";
				sensoralarm = "转角传感器";
				break;
			case "B0" :
				warningValue = "小车和大臂预警";
				sensoralarm = "转角传感器";
				break;
			case "B2" :
				warningValue = "小车和环境预警";
				sensoralarm = "转角传感器";
				break;
			case "C0" :
				warningValue = "塔机倾角预警";
				sensoralarm = "倾角传感器";
				break;
			case "D0" :
				warningValue = "风速预警";
				sensoralarm = "风速传感器";
				break;
			case "E0" :
				warningValue = "吊钩下限位预警";
				sensoralarm = "转角传感器";
				break;
			case "F0" :
				warningValue = "小车远端限位预警";
				sensoralarm = "转角传感器";
				break;
			case "G0" :
				warningValue = "超重预警";
				sensoralarm = "吊重传感器";
				break;
			default:
				warningValue = "无预警";
				break;
		}

		/** warningValue塔吊报警状态*/
		switch(str){
			case "A1" :
				alarmValue = "大臂和大臂碰撞";
				sensoralarm = "转角传感器";
				break;
			case "A3" :
				alarmValue = "大臂和小车碰撞";
				sensoralarm = "转角传感器";
				break;
			case "B1" :
				alarmValue = "小车和大臂碰撞";
				sensoralarm = "转角传感器";
				break;
			case "B3" :
				alarmValue = "小车和环境碰撞";
				sensoralarm = "转角传感器";
				break;
			case "G1" :
				alarmValue = "超重报警";
				sensoralarm = "吊重传感器";
				break;
			default:
				alarmValue = "无报警";
				break;
		}

		if(sensoralarm.equals("")){
			sensoralarm = "无传感器报警";
		}

		return warningValue +"#"+ alarmValue + "#" +sensoralarm;
	}

	public  static String liftParamsConvert(String str){
		String  earl="";
		String  alarm="";
		/**
		 * 升降机预警状态
		 */
		switch (str){
			case "A":
				earl="超重预警";
				break;
			case "D":
				earl="高低上限预警";
				break;
			default:
				earl="无预警";
				break;
		}
		switch (str){
			case  "B":
				alarm="超重切断报警";
				break;
			case "C":
				alarm="风速过膏报警";
				break;
			case "E":
				alarm="高度上限切断报警";
				break;
			case "F":
				alarm="人数超过限制报警";
				break;
			case "G":
				alarm="倾角过大报警";
				break;
			case "H":
				alarm="加速度过高报警";
				break;
			case "N":
				alarm="无报警";
				break;
			case "A0":
				alarm="称重左故障";
				break;
			case "A1":
				alarm="称重右故障";
				break;
			case "A2":
				alarm="高度传感器故障";
				break;
			case  "A3":
				alarm="倾角传感器故障";
				break;
			case "A4":
				alarm="风速传感器故障";
				break;
			case "A5":
				alarm="位移传感器故障";
				break;
			case "A6":
				alarm="加速度传感器故障";
				break;
			default:
				alarm="无报警";
				break;
		}

		return  earl+"#"+alarm;
	}
	
}
