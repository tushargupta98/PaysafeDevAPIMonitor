package com.paysafe.apimonitor.ws.dao;

import java.util.HashMap;
import java.util.Map;

import com.paysafe.apimonitor.ws.model.SchedulerVO;

public class ServiceMonitorDAOImpl implements ServiceMonitorDAO {

	private static Map<String, SchedulerVO> serviceMonitorMap = new HashMap<>();

	@Override
	public Map<String, SchedulerVO> getAllServiceMonitors() {
		return serviceMonitorMap;
	}

	@Override
	public boolean insertServiceMonitor(String serviceURL, SchedulerVO schedulerVO) {
		serviceMonitorMap.put(serviceURL, schedulerVO);
		return true;
	}

	@Override
	public void cleanCache() {
		for (Map.Entry<String, SchedulerVO> entry : serviceMonitorMap.entrySet()) {
			SchedulerVO schVO = entry.getValue();
			schVO.getSchedulerTask().markSchedulerTaskToStop();
			schVO.getSchedulerTask().cancel();
			schVO.getTimer().cancel();
			schVO.getTimer().purge();
		}
		serviceMonitorMap = new HashMap<>();
	}

}