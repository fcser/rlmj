package cn.java.rlmj.listener;

import org.apache.logging.log4j.core.LogEventListener;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.ServletContextEvent;

public class LoggerListener extends LogEventListener {

	public void contextInitialized(ServletContextEvent event) {
		installJulToSlf4jBridge();
	}

	private void installJulToSlf4jBridge() {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}
}
