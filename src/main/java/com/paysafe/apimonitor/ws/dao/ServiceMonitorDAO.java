package com.paysafe.apimonitor.ws.dao;
import java.util.Map;
import com.paysafe.apimonitor.ws.model.SchedulerVO;
public interface ServiceMonitorDAO {

	public Map<String, SchedulerVO> getAllServiceMonitors();
	public boolean insertServiceMonitor(String serviceURL, SchedulerVO schedulerVO);
	public void cleanCache();
}
