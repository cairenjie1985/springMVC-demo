package com.caixin.transfer.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class SpringBeanFactory implements BeanFactoryAware {
	
	public static final String BEAN_NAME_SESSION_MANAGE = "sessionManage";
	
	public static final String BEAN_NAME_SYSTEM_PROPERTIES = "mdpSystemProperties";
	
	public static final String BEAN_NAME_USER_MAPPING = "bussMembersInfoServiceImpl";

	private static BeanFactory beanFactory;

	@SuppressWarnings("static-access")
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * 根据beanName名字取得bean
	 * 
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		if (null != beanFactory) {
			return (T) beanFactory.getBean(beanName);
		}
		return null;
	}

}
