package com.paysafe.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.paysafe.apimonitor.ws.dao.ApplicationConstants;
import com.paysafe.apimonitor.ws.dao.ApplicationMessages;
import com.paysafe.apimonitor.ws.dao.ServiceMonitorDAO;
import com.paysafe.apimonitor.ws.dao.ServiceStatusDAO;
import com.paysafe.apimonitor.ws.model.ExecutionResponseVO;
import com.paysafe.apimonitor.ws.model.SchedulerVO;
import com.paysafe.apimonitor.ws.model.ServiceStatusVO;
import com.paysafe.apimonitor.ws.model.StartRequestVO;

/**
 * This class instantiates the business processing for all the requests made by
 * the exposed services.
 * 
 * @author Tushar
 * @version 1.0
 */
public class MonitoringBO {
	/**
	 * this dynamically provides the type of persistence/data-management mechanism
	 * should be used. Makes use of Factory Design pattern. this object provides
	 * access to the list of all the services/servers monitored by the application
	 * and their current states
	 */
	private ServiceMonitorDAO smDAO = MonitoringFactory.getServiceMonitorDAO(ApplicationConstants.SERVICE_MONITOR_DAO);
	/**
	 * this dynamically provides the type of persistence/data-management mechanism
	 * should be used. Makes use of Factory Design pattern. this object provides
	 * access to all the status logged in for all the services/servers at a given
	 * instance. The monitoring for a particular service may or may not have stopped
	 * but it provides log since most recent monitoring-start
	 */
	private ServiceStatusDAO ssDAO = MonitoringFactory.getServiceStatusDAO(ApplicationConstants.SERVICE_STATUS_DAO);

	public MonitoringBO() {

	}

	/**
	 * this method invokes the creation of a Monitor for a particular
	 * service/server. it first validates the time interval, it should be minimum of
	 * 1 second. it then checks if a monitor already exists for a particular
	 * service/server. if exists and is currently monitoring then informs return the
	 * status the the monitor already exists and does not create a new monitor,
	 * otherwise it starts the monitoring of the service/server returns an
	 * appropriate message in terms of ExecutionResponseVO object to the calling
	 * function This method has been made synchronized because the process involves
	 * creation of a monitoring thread per service/server
	 * 
	 * @author Tushar
	 * @param srVO
	 * @return ExecutionResponseVO
	 */
	public synchronized ExecutionResponseVO createServiceMonitor(StartRequestVO srVO) {

		Map<String, SchedulerVO> serviceMonitorMap = smDAO.getAllServiceMonitors();
		ExecutionResponseVO erVO = new ExecutionResponseVO();
		if (srVO.getInterval() < 1) {
			erVO.setCode(ApplicationMessages.INVALID_INTERVAL.getId());
			erVO.setMessage(ApplicationMessages.INVALID_INTERVAL.getMsg());
			return erVO;
		}
		if (serviceMonitorMap.containsKey(srVO.getServiceURL())) {
			if (isServerMonitored(serviceMonitorMap.get(srVO.getServiceURL()))) {
				erVO.setCode(ApplicationMessages.ALREADY_MONITORING.getId());
				erVO.setMessage(ApplicationMessages.ALREADY_MONITORING.getMsg());
			} else {
				startMonitoring(srVO, serviceMonitorMap);
				erVO.setCode(ApplicationMessages.MONITORING_STARTED.getId());
				erVO.setMessage(ApplicationMessages.MONITORING_STARTED.getMsg());
			}
		} else {
			startMonitoring(srVO, serviceMonitorMap);
			erVO.setCode(ApplicationMessages.MONITORING_STARTED.getId());
			erVO.setMessage(ApplicationMessages.MONITORING_STARTED.getMsg());
		}
		return erVO;
	}

	private void startMonitoring(StartRequestVO srVO, Map<String, SchedulerVO> serviceMonitorMap) {
		SchedulerVO schVO = new SchedulerVO(srVO.getServiceURL(), srVO.getInterval());
		schVO.getTimer().schedule(schVO.getSchedulerTask(), 0);
		ssDAO.initializeServiceLog(srVO.getServiceURL());
		serviceMonitorMap.put(srVO.getServiceURL(), schVO);
	}

	/**
	 * this method tries to stop the Monitor for a particular service/server. it
	 * takes in the url of the service/server. it then checks if a monitor already
	 * exists for a particular service/server. if exists and is currently monitoring
	 * then it stop the corresponding monitoring thread if the monitor is already
	 * stopped, it informs the calling function appropriately
	 * 
	 * @author Tushar
	 * @param serviceURL
	 * @return ExecutionResponseVO
	 */
	public ExecutionResponseVO stopMonitoring(String serviceURL) {
		Map<String, SchedulerVO> serviceMonitorMap = smDAO.getAllServiceMonitors();
		ExecutionResponseVO erVO = new ExecutionResponseVO();
		if (serviceMonitorMap.containsKey(serviceURL)) {
			SchedulerVO sch = serviceMonitorMap.get(serviceURL);
			if (sch.getSchedulerTask().getSchedulerState()) {
				sch.getSchedulerTask().markSchedulerTaskToStop();
				sch.getSchedulerTask().cancel();
				sch.getTimer().cancel();
				sch.getTimer().purge();
				interruptThread(serviceURL);
				erVO.setCode(ApplicationMessages.MONITORING_STOPPED.getId());
				erVO.setMessage(ApplicationMessages.MONITORING_STOPPED.getMsg());
			} else {
				erVO.setCode(ApplicationMessages.ALREADY_STOPPED.getId());
				erVO.setMessage(ApplicationMessages.ALREADY_STOPPED.getMsg());
			}
		} else {
			erVO.setCode(ApplicationMessages.NO_MONITORING_HISTORY.getId());
			erVO.setMessage(ApplicationMessages.NO_MONITORING_HISTORY.getMsg());
		}
		return erVO;
	}

	/**
	 * This method queries the status log of monitoring a particular server/service.
	 * If there is no history/log for a service an empty collection is returned
	 * 
	 * @author Tushar
	 * @version 1.0
	 * @param serviceURL
	 * @return List<ServiceStatusVO>
	 */
	public List<ServiceStatusVO> queryStatusLog(String serviceURL) {
		List<ServiceStatusVO> statusLog = new ArrayList<>();
		Map<String, List<ServiceStatusVO>> serviceMonitorMap = ssDAO.getAllServiceStatusLogs();
		if (serviceMonitorMap.containsKey(serviceURL)) {
			statusLog = serviceMonitorMap.get(serviceURL);
		}
		return statusLog;

	}

	private boolean isServerMonitored(SchedulerVO schVO) {
		SchedulerTask rTask = schVO.getSchedulerTask();
		if (rTask.getSchedulerState() && isThreadRunning(schVO.getServiceName())) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isThreadRunning(String serviceURL) {
		boolean isthreadRunning = false;
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(serviceURL)) {
				if (t.isAlive()) {
					isthreadRunning = true;
					break;
				} else {

				}
			}
		}
		return isthreadRunning;
	}

	private static void interruptThread(String serviceURL) {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(serviceURL)) {
				if (t.isAlive()) {
					t.interrupt();
					break;
				} else {
				}
			}
		}
	}
	
}
