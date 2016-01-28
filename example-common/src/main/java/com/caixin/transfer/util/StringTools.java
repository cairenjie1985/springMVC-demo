package com.caixin.transfer.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 常见的字符串处理方法集合类
 */
@SuppressWarnings("restriction")
public class StringTools extends StringUtils
{
    // 一个空的字符串。
    public static final String EMPTY_STRING     = "";

    // ================================将符号也作为常量，避免半角全角符号导致的狗血异常======================================
    // 逗号
    public static final String SYMBOL_COMMA     = ",";

    // 等于号
    public static final String SYMBOL_EQUAL     = "=";

    // 点号
    public static final String SYMBOL_DOT       = ".";

    // 问号
    public static final String SYMBOL_QUESTION  = "?";

    // 分号
    public static final String SYMBOL_SEMICOLON = ";";

    /**
     * 验证EMAIL正则表达式
     */
    public static final String PATTERN_EMAIL    = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";

    /**
     * 如果传入的值是null，返回空字符串，如果不是null，返回本身。
     * 
     * @param word 传入的源字符串。
     * @return
     */
    public static String getNotNullValue(String word) {
        return (word == null || word.equalsIgnoreCase("null")) ? "" : word;
    }

    /**
     * 如果传入的值是null，返回空字符串，如果不是null，返回本身。
     * 
     * @param word 传入的源字符串。
     * @return
     */
    public static String getNotNullValue(String word, String defaultWord) {
        return (word == null || word.equalsIgnoreCase("null")) ? defaultWord : word;
    }

    /**
     * 根据分隔符从一段字符串拿到对应的列表。应用于以下场景。 2,3,4,5 ==> [2,3,4,5]
     * 
     * @param originWord
     * @param symbol
     * @return
     */
    public static List<String> getSplitListFromString(String originWord, String symbol) {
        List<String> result = new ArrayList<String>();
        if (isBlank(originWord)) {
            return result;
        }

        String[] splitData = originWord.split(symbol);
        if (splitData == null || splitData.length == 0) {
            return result;
        }

        for (String word : splitData) {
            if (isNotBlank(word)) {
                result.add(word);
            }
        }
        return result;
    }

    /**
     * @param originalStr
     * @param symbol
     * @return
     */
    public static List<Long> getLongListFromString(String originalStr, String symbol) {
        List<Long> result = new ArrayList<Long>();
        if (isBlank(originalStr)) {
            return result;
        }

        String[] splitData = originalStr.split(symbol);

        for (String word : splitData) {
            if (isNotBlank(word)) {
                result.add(Long.parseLong(word));
            }
        }
        return result;
    }

    /**
     * 移除左边的0, eg：00000jakjdkf89000988000 转换之后变为 jakjdkf89000988000
     * 
     * @param str
     * @return
     */
    public static String removeLeftZero(String str) {
        int start = 0;
        if (isNotEmpty(str)) {
            char[] chars = str.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] != '0') {
                    start = i;
                    break;
                }
            }
            return str.substring(start);
        }
        return "";
    }

    /**
     * 格式化大数据为len个小数
     * 
     * @param obj
     * @param len
     * @return
     */
    public static String formatLagerNumberToStr(Object obj, int len) {
        if (obj != null) {
            if (obj instanceof String) {
                String str = String.valueOf(obj);
                return str.substring(0, str.indexOf(".") + len + 1);
            } else {
                StringBuffer pattern = new StringBuffer("0");
                for (int i = 0; i < len; i++) {
                    if (i == 0) {
                        pattern.append(".0");
                        continue;
                    }
                    pattern.append("0");
                }

                return new DecimalFormat(pattern.toString()).format(obj);
            }
        }

        return "";
    }

    /**
     * 金额格式化
     * 
     * @param s 金额
     * @param len 小数位数
     * @return 格式后的金额
     */
    public static String insertComma(String money, int len) {
        if (money == null || money.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(money);
        if (len == 0) {
            formater = new DecimalFormat("###,###");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,###.");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        return formater.format(num);
    }

    /**
     * 金额去掉“,”
     * 
     * @param s 金额
     * @return 去掉“,”后的金额
     */
    public static String delComma(String money) {
        String formatString = "";
        if (money != null && money.length() >= 1) {
            formatString = money.replaceAll(",", "");
        }

        return formatString;
    }

    public static String toRMB(double money) {
        char[] s1 = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };
        char[] s4 = { '分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万' };
        String str = String.valueOf(Math.round(money * 100 + 0.00001));
        String result = "";

        for (int i = 0; i < str.length(); i++) {
            int n = str.charAt(str.length() - 1 - i) - '0';
            result = s1[n] + "" + s4[i] + result;
        }

        result = result.replaceAll("零仟", "零");
        result = result.replaceAll("零佰", "零");
        result = result.replaceAll("零拾", "零");
        result = result.replaceAll("零亿", "亿");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零元", "元");
        result = result.replaceAll("零角", "零");
        result = result.replaceAll("零分", "零");

        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零亿", "亿");
        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零元", "元");
        result = result.replaceAll("亿万", "亿");

        result = result.replaceAll("零$", "");
        result = result.replaceAll("元$", "元整");

        return result;
    }

    public static boolean containStr(String rawStr, String containStr) {
        if ((rawStr != null) && (containStr != null)) {
            return rawStr.contains(containStr);
        }
        return false;
    }

    /**
     * 判断是否为数字或小数
     * 
     * @param numbers
     * @return
     */
    public static boolean checkNumber(String number) {
        String str = "^[0-9]+(.[0-9]{2})?$";
        Pattern pattern = Pattern.compile(str);
        if (StringTools.isNotBlank(number)) {
            Matcher matcher = pattern.matcher(number);
            return matcher.find();
        }
        return false;

    }

    /**
     * 过程名称：ChineseLen(获得当前文字的长度，中文为2个字符)
     * 
     * @param FromStr
     * @return
     */

    public static boolean checkChineseLen(String FromStr, int maxLen)

    {
        if (StringTools.isBlank(FromStr)) return false;
        int FromLen = FromStr.length();

        int ChineseLen = 0;

        for (int i = 0; i < FromLen; i++)

        {

            if (gbValue(FromStr.charAt(i)) > 0) {

                ChineseLen = ChineseLen + 2;

            } else {

                ChineseLen++;

            }

        }

        if (ChineseLen <= maxLen) {
            return true;
        } else {
            return false;
        }

    }

    /*******
     * 过程名称：gbValue(返回GBK的编码)
     * 
     * @param ch
     * @return
     */

    public static int gbValue(char ch)

    {
        String str = new String();

        str += ch;

        try

        {
            byte[] bytes = str.getBytes("GBK");

            if (bytes.length < 2) return 0;

            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);

        } catch (Exception e)

        {

            return 0;

        }

    }

    /**
     * 半角转全角
     * 
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * 全角转半角
     * 
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {

        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        String returnString = new String(c);

        return returnString;
    }

    /**
     * LIST转换为字符串，转换时插入制定的分隔符
     * 
     * @param list 字符串LIST
     * @param symbol 分隔符
     * @return
     */
    public static String listToString(List<?> list, String symbol) {
        String res = null;
        if (CollectionUtils.isNotEmpty(list)) {
            int size = list.size();
            if (size == 1) return String.valueOf(list.get(0));
            StringBuilder sb = new StringBuilder();
            if (symbol == null) symbol = "";
            for (int i = 0; i < size; i++) {
                if (i > 0) sb.append(symbol);
                sb.append(String.valueOf(list.get(i)));
            }
            res = sb.toString();
        }

        return res;
    }

    /**
     * <p>
     * 判断是否为数字格式
     * </p>
     * 
     * <pre>
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = false
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = true
     * </pre>
     * 
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isDoubleNumeric(String str) {
        if (str == null) {
            return false;
        }

        try {
            Double.parseDouble(str.replace(",", ""));
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 判断是否为数字(有小数位或者有千分位)
     * 
     * @param str
     * @return
     */
    public static boolean isNumericDecimal(String str) {
        return isDoubleNumeric(str) && (str.indexOf(".") > 0 || str.indexOf(",") > 0);
    }

    /**
     * 判断是否为email格式
     * 
     * @param email 验证字符串
     * @return true|false
     */
    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile(PATTERN_EMAIL);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 字符数组转化为字符串
     * 
     * @param strs 字符数组
     * @param symbol 分隔符
     * @return
     */
    public static String arrayToStr(String[] strs, String symbol) {
        if (strs == null || strs.length == 0) {
            return null;
        }
        if (symbol == null) {
            symbol = "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            if (sb.length() > 0) {
                sb.append(symbol);
            }
            sb.append(str);
        }

        return sb.toString();
    }

    /**
     * 首字母大写
     * 
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) return s;
        else return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    
    /**
     * 取得指定字符串的md5值
     * @param piStr
     * @return
     */
	public static String md5(String piStr)
	{
	    if(StringTools.isNullOrSpace(piStr))	        
	        piStr="";
		String encodeStr = "";
		byte[] digesta = null;
		try
		{
			MessageDigest alg = MessageDigest.getInstance("MD5");
			alg.update(piStr.getBytes());
			digesta = alg.digest();
			encodeStr = byte2hex(digesta);
		} catch (Exception e)
		{
		}
		return encodeStr;
	}

	private static String byte2hex(byte[] piByte)
	{
		String reStr = "";
		for (int i = 0; i < piByte.length; i++)
		{
			int v = piByte[i] & 0xFF;
			if (v < 16)
				reStr += "0";
			reStr += Integer.toString(v, 16).toLowerCase();			
		}
		return reStr;
	}

	/**
	 * 转换一个ISO字符串到GB2312
	 * @param str
	 * @return
	 */
	public static String iso2GB(String str)
	{
		try
		{
			if (str != null)
			{
				str = new String(str.getBytes("ISO-8859-1"), "GB2312");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 转换一个GB2312字符串到ISO
	 * @param str
	 * @return
	 */
	public static String gb2ISO(String str)
	{
		try
		{
			if (str != null)
			{
				str = new String(str.getBytes("GB2312"), "ISO-8859-1");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 判断一个字符串是否为NULL或空
	 * @param str
	 * @return
	 */
	public static boolean isNullOrSpace(String str)
	{
		if(str==null || str.trim().length()<1)
			return true;
		else
			return false;
	}
	
    /**
     * 使用给定的字串替换源字符串中指定的字串。
     * @param mainString 源字符串
     * @param oldString 被替换的字串
     * @param newString 替换字串
     * @return String
     */
    public final static String replace(final String mainString, final String oldString, final String newString)
    {
        if(mainString == null)
            return null;
        int i = mainString.lastIndexOf(oldString);
        if(i < 0)
            return mainString;
        StringBuffer mainSb = new StringBuffer(mainString);
        while (i >= 0) {
            mainSb.replace(i, i + oldString.length(), newString);
            i = mainString.lastIndexOf(oldString, i - 1);
        }
        return mainSb.toString();
    }	
    
    /**
     * 去掉字符串的'和“
     * @param s
     * @return
     */
    public final static String safeStr(String s)
    {
    	String ret = replace(s, "'", "");
    	ret = replace(ret, "\"", "");
    	return ret;
    }
    
    /**
     * 将字符串中所以HTML标签清除
     * @param input
     * @return
     */
	public final static String removeInTag(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// 去掉所有html元素,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		return str;
	}
	
	// Base64加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// Base64解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 
	 * @author:zhangxj
	 * @since:2015-1-9 上午10:49:49 
	 * @param map
	 * @return 
	 * @return Map<String,String> 
	 * @Description:根据value对map进行排序
	 */
	public static Map<String, String> sortMapByValue(Map<String, String> map) {   
		if (map == null || map.isEmpty()) {   
			return null;   
		}   
		Map<String, String> sortedMap = new LinkedHashMap<String, String>();   
		List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(map.entrySet());   
		Collections.sort(entryList, new MapValueComparator());   
		Iterator<Map.Entry<String, String>> iter = entryList.iterator();   
		Map.Entry<String, String> tmpEntry = null;   
		while (iter.hasNext()) {   
		    tmpEntry = iter.next();   
		    sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());   
		}   
		return sortedMap;   
	} 
	
	/**
	 * 
	 * @author: zhangxj
	 * @date: 2015-1-14 下午1:00:29 
	 * @return
	 * @return:String
	 * @Description: 随机生成6位数
	 */
	public static String getRandom(){
		Random random = new Random();
		String code="";
		for(int i=0;i<6;i++){
			code+=random.nextInt(10);
		}
		return code;
	}

	/**
	 * 
	 * @author: linyt
	 * @date: 2015-1-18 上午9:35:29
	 * @param content
	 * @return:String
	 * @Description: 得到已过滤html、script、style 标签的内容（过滤包括标签及标签中的内容） 
	 */
	public static String getNoHTMLString(String content) {

		if (null == content)
			return "";

		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_script = "(<[\\s]*?script[^>]*?>)|(<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?)|(<[\\s]*?\\/[\\s]*?script[\\s]*?)>";
			// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
			String regEx_style = "(<[\\s]*?style[^>]*?>)|(<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?)|(<[\\s]*?\\/[\\s]*?style[\\s]*?)>";
			// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
			String regEx_html = "(<[\\s]*?html[^>]*?>)|(<[\\s]*?html[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?html[\\s]*?)|(<[\\s]*?\\/[\\s]*?html[\\s]*?)>";
			// 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			for(m_script = p_script.matcher(content);m_script.find();m_script = p_script.matcher(content)){
				content = m_script.replaceAll(""); // 过滤script标签
			}
			
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			for(m_style = p_style.matcher(content);m_style.find();m_style = p_style.matcher(content)){
				content = m_style.replaceAll(""); // 过滤style标签
			}

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			for(m_html = p_html.matcher(content);m_html.find();m_html = p_html.matcher(content)){
				content = m_html.replaceAll(""); // 过滤html标签
			}
		} catch (Exception e) {
			return "";
		}

		return content;
	}
	
	/**
	 * 
	 * @author: linyt
	 * @date: 2015-1-18 上午9:39:29
	 * @param html
	 * @return:String
	 * @Description: 将已转义的字符串转回HTML
	 */
	public static String htmlUnescape(String html){
		
		return HtmlUtils.htmlUnescape(html);
	}
	
	/**
	 * 
	 * @author: linyt
	 * @date: 2015-1-18 上午9:39:29
	 * @param html
	 * @return:String
	 * @Description: 将HTML的字符串转义
	 */
	public static String htmlEscape(String html){
		
		return HtmlUtils.htmlEscape(html);
	}
	
	/**
	 * 
	* @author zhangxj  
	* @date 2015-3-12 下午4:48:06 
	* @param decript
	* @return 
	* @return String
	* @throws 
	* @Description: SHA1加密算法
	 */
	public static String SHA1(String decript) {        
		try {            
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");            
			digest.update(decript.getBytes());            
			byte messageDigest[] = digest.digest();            
			// Create Hex String            
			StringBuffer hexString = new StringBuffer();            
			// 字节数组转换为 十六进制 数            
			for (int i = 0; i < messageDigest.length; i++) {                
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);                
				if (shaHex.length() < 2) {                    
					hexString.append(0);                
				}                
				hexString.append(shaHex);            
			}            
			return hexString.toString();         
			} catch (NoSuchAlgorithmException e) {            
				e.printStackTrace();        
			}        
		return "";
	}
	
/*	public static void main(String[] args) {
		String str = "jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VAE8IPEtCLEq2RGx-p8c4f2ihhhUf5FXA8d72SvQrH_WuKaKhtClIEAyEmOI-S8Ozg&noncestr=exfg6nm5dj5s&timestamp=1426151142&url=http://www.qingfeng.com/mdp/signatureCtrl/checkWeiXin.do?token=jun910614";
		System.out.println(SHA1(str));
	}*/
}
