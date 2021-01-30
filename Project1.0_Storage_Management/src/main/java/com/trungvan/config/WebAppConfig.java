package com.trungvan.config; 

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import com.trungvan.security.FilterSystem;
//import com.trungvan.security.FilterSystem;
import com.trungvan.utils.Constant;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.trungvan.*")
@PropertySource(value = {"classpath:database.properties"})
@EnableTransactionManagement
public class WebAppConfig implements WebMvcConfigurer{
	
	// Holder for data
	@Autowired
	private Environment env;

	//---------------------------------------------------------------------
	// View folder for containing view file (jsp file)
	@Bean()
	public ViewResolver pageViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		// setOrder to use other ViewResolvers at the same time
		// In this situation, we combine InternalResourceViewResolver and TilesViewResolver
		viewResolver.setOrder(2);
		
		return viewResolver;
	}

	//---------------------------------------------------------------------
	// Spring Tile 3
	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitions("classpath:tiles.xml");
		tilesConfigurer.setCheckRefresh(true);

		return tilesConfigurer;
	}

	@Bean("tilesViewResolver")
	public ViewResolver tilesViewResolver() {
		TilesViewResolver viewResolver = new TilesViewResolver();
		// setOrder to use other ViewResolvers at the same time
		// In this situation, we combine InternalResourceViewResolver and TilesViewResolver
		viewResolver.setOrder(1);
		
		return viewResolver;
	}

	//---------------------------------------------------------------------
	// Static Resources
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// system file in project 
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/source/");

		// system file not in project, it's in another folder in computer
		// Cach 1:
//		registry.addResourceHandler("/source-image/**")
//				.addResourceLocations("file:///C:/Users/Admin/Desktop/test upload file/");
		
		// Cach 2:
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:" + Constant.SERVER_STATIC_RESOURCES);
	}

	//---------------------------------------------------------------------
	// Uploading file
	@Bean(name = "multipartResolver")
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		// -1 means unlimited file size
		multipartResolver.setMaxUploadSize(-1);
		
		return multipartResolver;
	}

	//---------------------------------------------------------------------
	// Spring Mail
	final String SENDER_USERNAME = "senderEmail@gmail.com";
	final String SENDER_PASSWORD = "senderPassword";
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    mailSender.setUsername(SENDER_USERNAME);
	    mailSender.setPassword(SENDER_PASSWORD);
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	
	//---------------------------------------------------------------------
	// Reading message.properties file for overriding Annotation message
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource bundleMessageSource = 
				new ReloadableResourceBundleMessageSource();
		bundleMessageSource.setBasename("classpath:message");
		bundleMessageSource.setDefaultEncoding("utf-8");
		
		return bundleMessageSource;
	}

	//---------------------------------------------------------------------
	// Reading db.properties file from @PropertySource for connecting DB
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	// Connecting DB via info from database.properties file
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		// by hard code
//		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://localhost:3306/web_student_tracker");
//		dataSource.setUsername("webstudent");
//		dataSource.setPassword("webstudent");
		
		// by reading db.properties file
		dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
		
		return dataSource;
	}

	//---------------------------------------------------------------------
	// Hibernate with Spring Transaction
	public Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
		properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.enable_lazy_load_no_trans", env.getRequiredProperty("hibernate.enable_lazy_load_no_trans"));
		// > Cuc ky quan trong: No cho phep su dung Lazy Fetching 
		properties.put("hibernate.event.merge.entity_copy_observer", env.getRequiredProperty("hibernate.event.merge.entity_copy_observer"));
		
		return properties;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactoryBean() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setPackagesToScan(env.getRequiredProperty("hibernate.packagesToScan"));
		sessionFactoryBean.setHibernateProperties(hibernateProperties());
		
		return sessionFactoryBean; 
	}
	
	@Bean(name = "transactionManager")
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);
		return transactionManager;
	}

	//---------------------------------------------------------------------
	// Using Interceptor
	@Bean
    public FilterSystem filterSystemInterceptor() {
		
         return new FilterSystem();
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(filterSystemInterceptor())
				.addPathPatterns("/**")
		// > Bo cac endpoints nay khong can preHandle
				.excludePathPatterns("/login") 			
				.excludePathPatterns("/resources/**")
				.excludePathPatterns("/upload/**");
	}
}
