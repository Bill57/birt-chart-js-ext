package org.bcje.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.eclipse.birt.chart.viewer.internal.util.ChartImageManager;

/**
 * 
 */

public class ChartHttpSessionListener implements HttpSessionListener {

	/**
	 * After session created
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent event) {
	}

	/**
	 * When session destroyed
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		String sessionId = event.getSession().getId();
		ChartImageManager.clearSessionFiles(sessionId);
	}

}
