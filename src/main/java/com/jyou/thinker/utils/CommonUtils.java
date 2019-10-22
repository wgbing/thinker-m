package com.jyou.thinker.utils;

import com.jyou.thinker.common.ApiResult;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TODO: 通用工具类
 * @author wgbing
 * @date 2019/4/18 14:58
 */
public class CommonUtils {

	/**
	 * 判断整数是否大于零
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isIntThanZero(int number) {
		if (number > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 新增，修改提示
	 * @param count
	 * @return
	 */
	public static ApiResult msg(int count) {
		if(isIntThanZero(count)){
			return ApiResult.success();
		}
		return ApiResult.failure();
	}

	/**
	 * 删除提示
	 * @param total
	 * @param count
	 * @return
	 */
	public static ApiResult msg(Object[] total, int count) {
		if(total.length == count){
			return ApiResult.success();
		}else{
			if(isIntThanZero(count)){
				String msg = "本次共处理："+ String.valueOf(total)+"条，成功："+ String.valueOf(count)+"条！";
				return ApiResult.failure(msg);
			}else{
				return ApiResult.failure();
			}
		}
	}

	// 以下为服务器端判断客户端浏览器类型的方法
	public static String getBrowser(HttpServletRequest request) {
		String UserAgent = request.getHeader("USER-AGENT").toLowerCase();
		if (UserAgent != null) {
			if (UserAgent.indexOf("msie") >= 0)
				return "IE";
			if (UserAgent.indexOf("firefox") >= 0)
				return "FF";
			if (UserAgent.indexOf("safari") >= 0)
				return "SF";
		}
		return null;
	}

	/**
	 * 将驼峰字符串转化为下滑下
	 * @param src
	 * @return
	 */
	public static String convertHumpStr(String src) {
		StringBuilder result = new StringBuilder();
		if (src != null && src.length() > 0) {
			// 将第一个字符处理成大写
			result.append(src.substring(0, 1).toLowerCase());
			// 循环处理其余字符
			for (int i = 1; i < src.length(); i++) {
				String s = src.substring(i, i + 1);
				// 在大写字母前添加下划线
				if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
					result.append("_");
				}
				// 其他字符直接转成大写
				result.append(s.toLowerCase());
			}
		}
		return result.toString();
	}
	/**
	 * @TODO: 生成唯一时间戳可作为编号使用
	 *
	 * @return:
	 * @auther: GuoChongYang
	 * @date 2019/5/31 16:31
	 */
	public static String createNumberStr(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSS");
		StringBuffer timeStr = new StringBuffer(sdf.format(new Date()));
		if(timeStr.length()<17){
			int count = timeStr.length();
			for(int i=0;i<(17-count);i++){
				int num=(int)(1+ Math.random()*(10));
				if(num==10){
					num=num-1;
				}
				timeStr.append(num);
			}
		}
		return timeStr.toString();
	}
	/**
	 * @TODO: 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
	 * 
	 * @return: 重组后的数据
	 * @auther: GuoChongYang
	 * @date 2019/6/21 9:44 
	 */
	public static String frontCompWithZore(int sourceDate, int formatLength){
		//0 指前面补充零
		//formatLength 字符总长度为 formatLength
		//d 代表为正数
		String newString = String.format("%0"+formatLength+"d", sourceDate);
		
		return newString;

	}
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	/**
	 * 获取指定日期当月的第一天
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static String getFirstDayOfGivenMonth(String dateStr, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH,1);
			calendar.add(Calendar.MONTH, 0);
			return sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
