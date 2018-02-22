package com.paysafe.business;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimerTask;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.paysafe.apimonitor.ws.dao.ApplicationConstants;
import com.paysafe.apimonitor.ws.dao.ApplicationMessages;
import com.paysafe.apimonitor.ws.dao.ServiceStatusDAO;
import com.paysafe.apimonitor.ws.model.ServerResponse;
import com.paysafe.apimonitor.ws.model.ServiceStatusVO;
/**
 * This is a scheduler task which helps in initializing the Monitoring of the service/server.
 * It extend TimerTask which creates a thread per object of this class.
 * Each thread has one-to-one correspondence to a service/server being monitored.
 * The application ensures that there is only one thread for a unique service/server.
 * @author Tushar
 * @version 1.0
 */
public class SchedulerTask extends TimerTask {

	private boolean isSchedulerRunning;
	private long delay;
	private String serviceURL;
	private ServiceStatusDAO ssDAO = MonitoringFactory.getServiceStatusDAO(ApplicationConstants.SERVICE_STATUS_DAO);

	public SchedulerTask(long delay, String serviceURL) {
		this.isSchedulerRunning = false;
		this.delay = delay*ApplicationConstants.MILLISECONDS;
		this.serviceURL = serviceURL;
	}

	public boolean getSchedulerState() {
		return isSchedulerRunning;
	}

	public void markSchedulerTaskToStop() {
		this.isSchedulerRunning = false;
	}

	public void run() {
		this.isSchedulerRunning=true;
		while(this.isSchedulerRunning) {
			ssDAO.insertServiceStatus(serviceURL, invokeService());
			try { 
				Thread.sleep(delay); 
			} catch (Exception e) { 
				break;
			}
		}      
	}

	/**
	 * When invoked, this method calls the service which meant to be monitored by the thread and
	 * return the response as ServiceStatusVO 
	 */
	private ServiceStatusVO invokeService() {
		String sStatus = ApplicationConstants.MONITORING_DOWN_MSG;
		int sCode = 0;
		String sMessage = ApplicationConstants.EMPTY_STRING;
		RestTemplate restTemplate = new RestTemplate();
		try {
			ServerResponse response = restTemplate.getForObject(serviceURL, ServerResponse.class );
			if(ApplicationConstants.SERVICE_READY.equals(response.getStatus())) {
				sStatus = ApplicationConstants.MONITORING_UP_MSG;
				sCode = ApplicationMessages.HEALTHY_SERVER.getId();
				sMessage = ApplicationMessages.HEALTHY_SERVER.getMsg();
			}
		}catch (HttpStatusCodeException exception) {
			sCode = exception.getStatusCode().value();
			sMessage = exception.getMessage();
		}catch (ResourceAccessException exception) {
			sCode = ApplicationMessages.INVALID_DNS.getId();
			sMessage = ApplicationMessages.INVALID_DNS.getMsg();
		}catch (Exception exception) {
			sCode = ApplicationMessages.INTERNAL_SERVER_ERROR.getId();
			sMessage = ApplicationMessages.INTERNAL_SERVER_ERROR.getMsg();
		}
		
		return new ServiceStatusVO(
				LocalDateTime.now(ZoneId.of(ApplicationConstants.TIME_ZONE)).toString(), 
				sStatus, sCode, sMessage);
	}

}