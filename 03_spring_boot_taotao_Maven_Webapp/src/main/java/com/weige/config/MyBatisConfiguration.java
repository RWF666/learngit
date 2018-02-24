package com.weige.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;

@Configuration
public class MyBatisConfiguration {
	
	
	@Bean
	public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		
		Interceptor[] plugins =  new Interceptor[]{pageInterceptor()};
		sqlSessionFactoryBean.setPlugins(plugins);
		
	   sqlSessionFactoryBean.setMapperLocations(resolver
				.getResources("classpath:/mapper/*.xml"));
		return sqlSessionFactoryBean.getObject();
		
	}

	
	
	/**
	 * -≈‰÷√¿πΩÿµƒPageInterceptor
	 * @return
	 */
	@Bean
	public PageInterceptor pageInterceptor(){
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
		pageInterceptor.setProperties(p);
		return pageInterceptor;
	}

}
