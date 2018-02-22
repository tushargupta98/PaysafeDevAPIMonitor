package com.paysafe.apimonitor.ws.dao;

import java.util.List;
import java.util.Map;

import com.paysafe.apimonitor.ws.model.ServiceStatusVO;

public interface ServiceStatusDAO {
	public Map<String, List<ServiceStatusVO>> getAllServiceStatusLogs();
	public boolean insertServiceStatus(String serviceURL, ServiceStatusVO serviceStatusVO);
	public void initializeServiceLog(String serviceURL);
	public void cleanCache();
}
