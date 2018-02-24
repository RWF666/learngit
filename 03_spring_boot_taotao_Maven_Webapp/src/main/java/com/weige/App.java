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
@MapperScan("com.weige.mapper") //需要指定包名。
@EnableTransactionManagement//启动事务
/**使用方法 mybatis-generator:generate -e */
public class App extends WebMvcConfigurerAdapter{
	/**
	 * 在这里我们使用 @Bean注入 fastJsonHttpMessageConvert
	 * @return
	 *//*
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		// 1、需要先定义一个 convert 转换消息的对象;
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		
		//2、添加fastJson 的配置信息，比如：是否要格式化返回的json数据;
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		
		//3、在convert中添加配置信息.
		fastConverter.setFastJsonConfig(fastJsonConfig);
		
		
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
	}*/
	
	public void addInterceptors(InterceptorRegistry registry){
		//注册拦截器，通过控制添加拦截器的顺序，来控制拦截器执行的顺序	
		registry.addInterceptor(new UserLoginInterceptor()).addPathPatterns("/customer/order/**");
		registry.addInterceptor(new CartInterceptor()).addPathPatterns("/customer/cart/**");
		super.addInterceptors(registry);
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
