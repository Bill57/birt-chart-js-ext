package org.bcje.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.birt.chart.viewer.internal.util.ChartImageManager;

/**
 * 
 */

public class ChartServletContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent event) {
		ChartImageManager.dispose(event.getServletContext());
	}

	public void contextInitialized(ServletContextEvent event) {
		ChartImageManager.init(event.getServletContext());
	}

}
