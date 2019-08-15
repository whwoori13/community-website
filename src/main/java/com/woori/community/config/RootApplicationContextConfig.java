package com.woori.community.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;





@Configuration
@ComponentScan("com.woori.community")
@MapperScan(basePackages = { "com.woori.community.DAOImpl" })
@EnableTransactionManagement
public class RootApplicationContextConfig {

	@Autowired
	private ApplicationContext ac;


	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
		ds.setUrl("jdbc:mysql://localhost:3306/shopping?serverTimezone=UTC&"
				+ "useUnicode=true&characterEncoding=utf8");
		ds.setUsername("root");
		ds.setPassword("1024");                  
		return ds;                          
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());

		// factoryBean.setMapperLocations(ac.getResources("classpath:/mappers/customer-mapper.xml"));
		factoryBean.setMapperLocations(ac.getResources("classpath:/mappers/**/*"));
		factoryBean.setConfigLocation(ac.getResource("classpath:/mybatis-config.xml"));
		return factoryBean.getObject();
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}
	
	///////////////////////////////////////////////////////
	/*
	@Bean
    public NavMenuHandler navMenuHandler(){ // aspect µî·Ï.
        return new NavMenuHandler();
    }
    */
	///////////////////////////////////////////////////////    
	

	

}