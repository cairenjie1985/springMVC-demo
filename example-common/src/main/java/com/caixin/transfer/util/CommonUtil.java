package com.caixin.transfer.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class CommonUtil {
	/**
	 * @param str
	 * @param splitStr
	 * @return
	 * @Description 将传入字符中根据传入分隔符分开，并且返回的是原字符实际分隔数的列表（不管是不是空字符）
	 */
	public static List<String> split(String str, String splitStr) {
		Pattern pattern = Pattern.compile(splitStr);
		Matcher matcher = pattern.matcher(str);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		int len = count + 1;
		List<String> list = new ArrayList<String>(len);
		list.addAll(Arrays.asList(str.split(splitStr)));
		if (list.size() < len) {
			for (int i = list.size(); i < len; i++) {
				list.add("");
			}
		}
		return list;
	}
	
    public static String returnDefaultIfEmpty(String value, String emptyDefaultValue) {
        if (StringUtils.isEmpty(value)) {
            return emptyDefaultValue;
        }
        return value;
    }
    
    public static BigDecimal returnDefaultIfEmpty(BigDecimal value, BigDecimal emptyDefaultValue) {
        if (value == null) {
            return emptyDefaultValue;
        }
        return value;
    }
    
    public static Long returnDefaultIfEmpty(Long value, Long emptyDefaultValue) {
        if (value == null) {
            return emptyDefaultValue;
        }
        return value;
    }
    
    public static Integer returnDefaultIfEmpty(Integer value, Integer emptyDefaultValue) {
        if (value == null) {
            return emptyDefaultValue;
        }
        return value;
    }
    
    /**
     * 获取访问用户的客户端IP（适用于公网与局域网）.
     */
    public static final String getIpAddr(final HttpServletRequest request) throws Exception {
        if (request == null) {
            throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
        }
        String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }

        return ipString;
    }

	/**
	 * 生成id
	 * @return
	 *//*
	public static String IdGenerate() {
		return IdGenerate(BussCodeEnum.DEFAULT_BUSS_CODE);
	}

	*//**
	 * 生成id
	 * @param type
	 * @return
	 *//*
	public static String IdGenerate(BussCodeEnum type) {
		Random r = new Random();
		double d = r.nextDouble();
		String s = (String.valueOf(d)).substring(3, 8);
		String id = DateTools.D2S(new Date(), DateTools.DateMode_3)
				+ type.getValue() + s;
		return id;
	}*/
}
