package com.paysafe.business;

import com.paysafe.apimonitor.ws.dao.ServiceMonitorDAO;
import com.paysafe.apimonitor.ws.dao.ServiceStatusDAO;

/**
 * Ideally a design of an application should be such that the persistence mechanism should be independent of business logic
 * Any change in the persistence mechanism should not mandate a change in the business logic. 
 * Keeping this in mind I have made use of Factory design pattern to achieve this. 
 * A particular DAO implementation(Class)(Which implements an interface) could be passed through a class name saved in a system constant variable.
 * This class helps to retrieve a specific DAO instance in the run-time. 
 * @author Tushar
 * @version 1.0
 */
public class MonitoringFactory {

	/**
	 * 
	 * This method returns an instance of ServiceMonitorDAO's implementation
	 * The instance helps in accessing all the monitoring threads and corresponding objects.
	 * @param fullClassName
	 * @return
	 */
	public static ServiceMonitorDAO getServiceMonitorDAO(String fullClassName) {
		Class cls;
		ServiceMonitorDAO smDAO=null;
		try {
			cls = Class.forName(fullClassName);
			smDAO = (ServiceMonitorDAO)cls.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  smDAO;
	}
	
	/**
	 * This method returns an instance of ServiceStatusDAO's implementation
	 * The instance helps in accessing all status logs for all the services that were started recently.
	 * @param fullClassName
	 * @return
	 */
	public static ServiceStatusDAO getServiceStatusDAO(String fullClassName) {
		Class cls;
		ServiceStatusDAO smDAO=null;
		try {
			cls = Class.forName(fullClassName);
			smDAO = (ServiceStatusDAO)cls.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  smDAO;
	}
}
