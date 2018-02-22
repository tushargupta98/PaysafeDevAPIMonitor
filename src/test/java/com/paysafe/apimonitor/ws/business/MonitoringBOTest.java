package com.paysafe.apimonitor.ws.business;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

	@Before
	public void setUp() {
		ServiceMonitorDAO smDAO = MonitoringFactory.getServiceMonitorDAO(ApplicationConstants.SERVICE_MONITOR_DAO);
		smDAO.cleanCache();
		ServiceStatusDAO ssDAO = MonitoringFactory.getServiceStatusDAO(ApplicationConstants.SERVICE_STATUS_DAO);
		ssDAO.cleanCache();
	}

	@After
	public void cleanUp() {
		ServiceMonitorDAO smDAO = MonitoringFactory.getServiceMonitorDAO(ApplicationConstants.SERVICE_MONITOR_DAO);
		smDAO.cleanCache();
		ServiceStatusDAO ssDAO = MonitoringFactory.getServiceStatusDAO(ApplicationConstants.SERVICE_STATUS_DAO);
		ssDAO.cleanCache();
	}

	@Test
	public void testMonitoring() {
		MonitoringBO mBO = new MonitoringBO();
		StartRequestVO srVO = new StartRequestVO();
		srVO.setServiceURL("Service1");
		srVO.setInterval(2);
		ExecutionResponseVO erVO = mBO.createServiceMonitor(srVO);
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

}