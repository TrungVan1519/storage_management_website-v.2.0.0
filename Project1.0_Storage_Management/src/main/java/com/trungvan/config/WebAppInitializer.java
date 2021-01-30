package com.trungvan.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
public class WebAppInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext appContext = 
				new AnnotationConfigWebApplicationContext();
        appContext.register(WebAppConfig.class);
        appContext.setServletContext(servletContext);
        
        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
 
        // Dispatcher Servlet
        ServletRegistration.Dynamic dispatcher = 
        		servletContext.addServlet("SpringDispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        dispatcher.setInitParameter("contextClass", appContext.getClass().getName());
 
        servletContext.addListener(new ContextLoaderListener(appContext));
         
        // UTF8 Character Filter.
        FilterRegistration.Dynamic fr = 
        		servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter	("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");        
	}
}


//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//
//public class WebAppInitializer 
//			extends AbstractAnnotationConfigDispatcherServletInitializer{
//
//	@Override
//	protected Class<?>[] getRootConfigClasses() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected Class<?>[] getServletConfigClasses() {
//		return new Class[] { WebAppConfig.class };
//	}
//
//	@Override
//	protected String[] getServletMappings() {
//		return new String[] { "/" }; 
//	}
//
//}
