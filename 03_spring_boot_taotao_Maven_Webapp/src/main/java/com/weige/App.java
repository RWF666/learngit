package com.weige;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.weige.interceptor.CartInterceptor;
import com.weige.interceptor.UserLoginInterceptor;




@SpringBootApplication
@MapperScan("com.weige.mapper") //��Ҫָ��������
@EnableTransactionManagement//��������
/**ʹ�÷��� mybatis-generator:generate -e */
public class App extends WebMvcConfigurerAdapter{
	/**
	 * ����������ʹ�� @Beanע�� fastJsonHttpMessageConvert
	 * @return
	 *//*
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		// 1����Ҫ�ȶ���һ�� convert ת����Ϣ�Ķ���;
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		
		//2�����fastJson ��������Ϣ�����磺�Ƿ�Ҫ��ʽ�����ص�json����;
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		
		//3����convert�����������Ϣ.
		fastConverter.setFastJsonConfig(fastJsonConfig);
		
		
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
	}*/
	
	public void addInterceptors(InterceptorRegistry registry){
		//ע����������ͨ�����������������˳��������������ִ�е�˳��	
		registry.addInterceptor(new UserLoginInterceptor()).addPathPatterns("/customer/order/**");
		registry.addInterceptor(new CartInterceptor()).addPathPatterns("/customer/cart/**");
		super.addInterceptors(registry);
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
