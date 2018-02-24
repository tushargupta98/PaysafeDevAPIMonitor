package com.paysafe.apimonitor.ws.business;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.paysafe.apimonitor.ws.AbstractTest;
import com.paysafe.apimonitor.ws.dao.ApplicationConstants;
import com.paysafe.apimonitor.ws.dao.ServiceMonitorDAO;
import com.paysafe.apimonitor.ws.dao.ServiceStatusDAO;
import com.paysafe.apimonitor.ws.model.ExecutionResponseVO;
import com.paysafe.apimonitor.ws.model.ServiceStatusVO;
import com.paysafe.apimonitor.ws.model.StartRequestVO;
import com.paysafe.business.MonitoringBO;
import com.paysafe.business.MonitoringFactory;

import org.junit.Assert;

public class MonitoringBOTest extends AbstractTest {


	MonitoringBO mBO;
	
	StartRequestVO srVO;
	
	ExecutionResponseVO erVO;
	ServiceMonitorDAO smDAO;
	
	ServiceStatusDAO ssDAO;
	
	@Before
	public void setUp() {
		smDAO = MonitoringFactory.getServiceMonitorDAO(ApplicationConstants.SERVICE_MONITOR_DAO);
		smDAO.cleanCache();
		ssDAO = MonitoringFactory.getServiceStatusDAO(ApplicationConstants.SERVICE_STATUS_DAO);
		ssDAO.cleanCache();
	}

	@After
	public void cleanUp() {
		smDAO = MonitoringFactory.getServiceMonitorDAO(ApplicationConstants.SERVICE_MONITOR_DAO);
		smDAO.cleanCache();
		ssDAO = MonitoringFactory.getServiceStatusDAO(ApplicationConstants.SERVICE_STATUS_DAO);
		ssDAO.cleanCache();
	}

	@Test
	public void testMonitoring() {
		srVO = new StartRequestVO();
		srVO.setServiceURL("https://api.test.paysafe.com/accountmanagement/monitor");
		srVO.setInterval(2);
		erVO = new ExecutionResponseVO();
		mBO = new MonitoringBO();
		erVO = mBO.createServiceMonitor(srVO);
		Assert.assertEquals("Monitoring did not start correctly", 1001, erVO.getCode());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<ServiceStatusVO> sLog = mBO.queryStatusLog(srVO.getServiceURL());
		Assert.assertNotEquals("Service Status Not logged", 0, sLog.size());
		erVO = mBO.stopMonitoring(srVO.getServiceURL());
		Assert.assertEquals("Monitoring did not stop correctly", 2001, erVO.getCode());

	}

	@Test
	public void testStartMonitoringScenarios() {
		srVO = new StartRequestVO();
		srVO.setServiceURL("https://api.test.paysafe.com/accountmanagement/monitor2");
		srVO.setInterval(2);
		erVO = new ExecutionResponseVO();
		mBO = new MonitoringBO();
		mBO.createServiceMonitor(srVO);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		erVO = mBO.createServiceMonitor(srVO);
		Assert.assertEquals("Monitoring did not detect duplicate start request", 1002, erVO.getCode());
		mBO.stopMonitoring(srVO.getServiceURL());
		srVO.setServiceURL("https://api.test.paysafe.com/accountmanagement/monitor");
		srVO.setInterval(-1);
		erVO = mBO.createServiceMonitor(srVO);
		Assert.assertEquals("Monitoring did not detect invalid time interval parameter", 3001, erVO.getCode());
	}
	
	@Test
	public void testStopMonitoringScenarios() {
		srVO = new StartRequestVO();
		srVO.setServiceURL("Service1");
		srVO.setInterval(2);
		mBO = new MonitoringBO();
		erVO = new ExecutionResponseVO();
		erVO = mBO.stopMonitoring(srVO.getServiceURL());
		Assert.assertEquals("Monitorind did not detect that no history exists", 2003, erVO.getCode());
		mBO.createServiceMonitor(srVO);
		mBO.stopMonitoring(srVO.getServiceURL());
		erVO = mBO.stopMonitoring(srVO.getServiceURL());
		Assert.assertEquals("Monitoring did not detect duplicate stop request", 2002, erVO.getCode());
	}
}

