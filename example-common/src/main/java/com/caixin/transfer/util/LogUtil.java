package com.caixin.transfer.util;

import java.text.MessageFormat;
import java.util.Map;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class LogUtil {
	private static final Logger logger = Logger.getLogger(LogUtil.class);

	/**
	 * 
	 * @since 2014-12-25 下午2:21:12
	 * @description
	 * @version
	 * @param log
	 * @param inputJson
	 */
	public static void error(Map<String, Object> info) {
		StackTraceElement a = getStackTraceElement();
		Logger log = getLogger(a.getClassName());
		String infoJson = mapToJson(info);
		error(log, a, infoJson, null);
	}

	/**
	 * 
	 * @since 2014-12-25 下午2:21:09
	 * @description
	 * @version
	 * @param log
	 * @param inputJson
	 * @param e
	 */
	public static void error(Map<String, Object> info, Throwable e) {
		StackTraceElement a = getStackTraceElement();
		Logger log = getLogger(a.getClassName());
		String infoJson = mapToJson(info);
		error(log, a, infoJson, e);
	}

	/**
	 * 
	 * @since 2015-2-10 上午10:44:02
	 * @description
	 * @version
	 * @param info
	 * @param e
	 */
	public static void error(String info, Throwable e) {
		StackTraceElement a = getStackTraceElement();
		Logger log = getLogger(a.getClassName());
		error(log, a, info, e);
	}

	/**
	 * 
	 * @since 2015-2-10 上午10:44:06
	 * @description
	 * @version
	 * @param info
	 */
	public static void error(String info) {
		StackTraceElement a = getStackTraceElement();
		Logger log = getLogger(a.getClassName());
		error(log, a, info, null);
	}

	/**
	 * 
	 * @since 2015-2-6 上午9:51:45
	 * @description info日志的输出
	 * @version
	 * @param info
	 */
	public static void info(Map<String, Object> info) {
		StackTraceElement a = getStackTraceElement();
		Logger log = getLogger(a.getClassName());
		String infoJson = mapToJson(info);
		error(log, a, infoJson, null);
	}

	/**
	 * 
	 * @since 2015-2-10 上午10:21:38
	 * @description
	 * @version
	 * @param info
	 */
	public static void info(String info) {
		StackTraceElement a = getStackTraceElement();
		Logger log = getLogger(a.getClassName());
		info(log, a, info, null);
	}

	/**
	 * 
	 * @since 2015-2-10 上午10:49:28
	 * @description INFO日志格式处理
	 * @version
	 * @param log
	 * @param a
	 * @param info
	 * @param e
	 */
	private static void info(Logger log, StackTraceElement a, String info, Throwable e) {
		StackTraceElement showSte = a;
		if (e == null) {
			String infoMsg = MessageFormat.format("{0}.{1}({2}:{3}) userinfo is => {4}.", new Object[] { showSte.getClassName(),
					showSte.getMethodName(), showSte.getFileName(), showSte.getLineNumber() + "", info });
			log.info(infoMsg);
		} else {
			StackTraceElement[] eStes = e.getStackTrace();
			int steLen = eStes.length;
			for (int i = steLen - 1; i >= 0; i--) {
				StackTraceElement eSte = eStes[i];
				if (eSte.getClassName().indexOf(".qingfeng.") != -1) {
					showSte = eSte;
				}
			}
			String infoMsg = MessageFormat.format("{0}.{1}({2}:{3}) ({4}:{5}) userinfo is => {6}.",
					new Object[] { showSte.getClassName(), showSte.getMethodName(), showSte.getFileName(),
							showSte.getLineNumber() + "", e.getClass().getName(), e.getMessage(), info });
			log.info(infoMsg);
		}
	}

	/**
	 * 
	 * @since 2015-2-10 上午10:49:45
	 * @description ERROR日志格式处理
	 * @version
	 * @param log
	 * @param a
	 * @param info
	 * @param e
	 */
	private static void error(Logger log, StackTraceElement a, String info, Throwable e) {
		StackTraceElement showSte = a;
		if (e == null) {
			String errorMsg = MessageFormat.format("{0}.{1}({2}:{3}) userinfo is => {4}.", new Object[] { showSte.getClassName(),
					showSte.getMethodName(), showSte.getFileName(), showSte.getLineNumber() + "", info });
			log.error(errorMsg);

		} else {
			StackTraceElement[] eStes = e.getStackTrace();
			int steLen = eStes.length;
			for (int i = steLen - 1; i >= 0; i--) {
				StackTraceElement eSte = eStes[i];
				if (eSte.getClassName().indexOf(".qingfeng.") != -1) {
					showSte = eSte;
				}
			}
			String errorMsg = MessageFormat.format("{0}.{1}({2}:{3}) ({4}:{5}) userinfo is => {6}.",
					new Object[] { showSte.getClassName(), showSte.getMethodName(), showSte.getFileName(),
							showSte.getLineNumber() + "", e.getClass().getName(), e.getMessage(), info });
			log.error(errorMsg);
		}
	}

	/**
	 * 
	 * @since 2015-2-10 上午10:48:53
	 * @description 将MAP转换成JSON字符串
	 * @version
	 * @param info
	 * @return
	 */
	private static String mapToJson(Map<String, Object> info) {
		String infoJson = "";
		if (info != null) {
			infoJson = JSONObject.toJSONString(info).toString();
		}
		return infoJson;
	}

	/**
	 * 
	 * @since 2015-2-10 上午10:47:35
	 * @description 获取logger类
	 * @version
	 * @param className
	 * @return
	 */
	private static Logger getLogger(String className) {
		Logger log = null;
		try {
			log = Logger.getLogger(className);
		} catch (Exception e) {
			log = logger;
		}
		return log;
	}

	/**
	 * 
	 * @since 2015-2-10 上午10:22:32
	 * @description 获取上一级的上一级的方法堆栈
	 * @version
	 * @return
	 */
	private static StackTraceElement getStackTraceElement() {
		StackTraceElement[] temp = Thread.currentThread().getStackTrace();
		StackTraceElement a = (StackTraceElement) temp[3];// 这就是上一级的上一级的方法堆栈
		return a;
	}
	

}
