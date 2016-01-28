package com.caixin.transfer.util.parser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

/**
 *数据转化基类
 */
public class BaseParser {
	
	/**
	 * 将Map转化为json字符串
	 * @param map 
	 * @param out
	 */
	public static void parserMapToJsonResponse(Map<Object,Object> map,HttpServletResponse response){
		String jsonStr = JSONObject.toJSONString(map);
		PrintWriter out = null;
			try {
				response.setContentType("application/json; charset=utf-8");
				out = response.getWriter();
			} catch (IOException e) {
				e.printStackTrace();
			}
		out.print(jsonStr);
	}
	
}