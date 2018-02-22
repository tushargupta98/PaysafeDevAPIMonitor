package com.paysafe.apimonitor.ws.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paysafe.apimonitor.ws.model.ServiceStatusVO;

public class ServiceStatusDAOImpl implements ServiceStatusDAO{

	private static Map<String, List<ServiceStatusVO>> serviceStatusMap = new HashMap<>();

	@Override
	public Map<String, List<ServiceStatusVO>> getAllServiceStatusLogs() {
		return serviceStatusMap;
	}

	@Override
	public boolean insertServiceStatus(String serviceURL, ServiceStatusVO serviceStatusVO) {
		List<ServiceStatusVO> sStatusList =  serviceStatusMap.get(serviceURL);
		if(sStatusList==null) {
			sStatusList = new ArrayList<ServiceStatusVO>();
			sStatusList.add(serviceStatusVO);
			serviceStatusMap.put(serviceURL, sStatusList);
		}else {
			sStatusList.add(serviceStatusVO);
		}
		return true;	
	}

	@Override
	public void initializeServiceLog(String serviceURL) {
		serviceStatusMap.put(serviceURL, new ArrayList<ServiceStatusVO>());
	}

	@Override
	public void cleanCache() {
		// TODO Auto-generated method stub
		serviceStatusMap = new HashMap<>();
	}

}
