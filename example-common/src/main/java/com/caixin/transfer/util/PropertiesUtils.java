package com.caixin.transfer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {
	private static Map<String, Properties> propertiesMap = new HashMap<String, Properties>();
	private final static String PROPERTIES_FILE_LASTMODIFIED_KEY = "_PropertiesFileLastModified";
	private static String CLASSPATH_ROOT;

	public static Properties loadProperties(String fileInClassPath) {
		BufferedReader bf = null;
		Properties prop = propertiesMap.get(fileInClassPath);
		try {
			long lastModified = -1;
			if (CLASSPATH_ROOT == null) {
				CLASSPATH_ROOT = PropertiesUtils.class.getResource("/")
						.getPath();
			}
			String realFilePath = CLASSPATH_ROOT + fileInClassPath;
			if (prop != null) {
				File file = new File(realFilePath);
				lastModified = file.lastModified();
				Long lastModifiedInMap = (Long) prop
						.get(PROPERTIES_FILE_LASTMODIFIED_KEY);
				if (lastModifiedInMap != null
						&& lastModified == lastModifiedInMap) {
					return prop;
				}
			}
			prop = new Properties();

			bf = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File(realFilePath)), "utf-8"));
			prop.load(bf);

			if (lastModified == -1) {
				File file = new File(realFilePath);
				lastModified = file.lastModified();
			}
			prop.put(PROPERTIES_FILE_LASTMODIFIED_KEY, lastModified);
			propertiesMap.put(fileInClassPath, prop);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bf != null) {
					bf.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

}
