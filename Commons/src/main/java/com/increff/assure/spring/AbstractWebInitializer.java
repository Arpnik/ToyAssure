package com.increff.assure.spring;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This class is a hook for <b>Servlet 3.0</b> specification, to initialize
 * Spring configuration without any <code>web.xml</code> configuration. Note
 * that {@link #getServletConfigClasses} method returns {@link SpringConfig},
 * which is the starting poInteger for Spring configuration <br>
 * <b>Note:</b> You can also implement the {@link WebApplicationInitializer }
 * Integererface for more control
 */

public abstract class AbstractWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {};
	}


	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}