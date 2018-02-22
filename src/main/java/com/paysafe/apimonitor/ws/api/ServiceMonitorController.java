package com.paysafe.apimonitor.ws.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paysafe.apimonitor.ws.model.ExecutionResponseVO;
import com.paysafe.apimonitor.ws.model.ServiceStatusVO;
import com.paysafe.apimonitor.ws.model.StartRequestVO;
import com.paysafe.business.MonitoringBO;

/**
 * This class exposes all the web services for this application
 * 
 * @author tushar
 * @version 1.0
 */
@RestController
public class ServiceMonitorController {

	/**
	 * This method exposes the service to start monitoring a server/service. This
	 * rest endpoint is of type POST. It takes in StartRequestVO(in JSON at Client).
	 * StartRequestVO has to fields, ServiceURL (String)(Which provides status of
	 * the server) and timer interval(long). Time interval needs to be in seconds
	 * with minimum value 1 It returns ExecutionResponseVO object as response. It
	 * contains two fields namely code(int) and message(String). The response is
	 * mapped with a code and a corresponding message in the application. It informs
	 * client if the monitoring start was successful or not. invocation example:
	 * POST::http://localhost:8080/apimonitorapp-1.0/api/monitoring/start
	 * {"serviceURL":"https://api.test.paysafe.com/accountmanagement/monitor",
	 * "interval":2}
	 * 
	 * @author Tushar
	 * @version 1.0
	 * @param startRequest
	 * @return ResponseEntity<ExecutionResponseVO>
	 */
	@RequestMapping(value = "/api/monitoring/start", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ExecutionResponseVO> startMonitoring(@RequestBody StartRequestVO startRequest) {
		MonitoringBO mBO = new MonitoringBO();
		ExecutionResponseVO er = mBO.createServiceMonitor(startRequest);
		return new ResponseEntity<ExecutionResponseVO>(er, HttpStatus.OK);
	}

	/**
	 * This method exposes the service to stop monitoring a server/service. This
	 * rest endpoint is of type GET. It takes in "serviceurl" as url parameter. It
	 * returns ExecutionResponseVO object as response. It contains two fields namely
	 * code(int) and message(String). The response is mapped with a code and a
	 * corresponding message in the application. It informs client if the
	 * monitoring-stop was successful or not.
	 * 
	 * @author Tushar
	 * @version 1.0
	 * @param serviceURL
	 * @return ResponseEntity<ExecutionResponseVO>
	 */
	@RequestMapping(value = "/api/monitoring/stop", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ExecutionResponseVO> stopMonitoring(
			@RequestParam(value = "serviceurl", required = true) String serviceURL) {
		MonitoringBO mBO = new MonitoringBO();
		ExecutionResponseVO er = mBO.stopMonitoring(serviceURL);
		return new ResponseEntity<ExecutionResponseVO>(er, HttpStatus.OK);
	}

	/**
	 * This method exposes the service to query the status log of monitoring a
	 * particular server/service. This rest endpoint is of type GET. It takes in
	 * "serviceurl" as url parameter. It returns a list of ServiceStatusVO objects
	 * in JSON format as response. Each entry in the list provides the timestamp on
	 * which the target service was called, the status that was returned, a
	 * corresponding message, and whether the service is deemed "UP" or "DOWN" If
	 * there is no history/log for a service an empty collection is returned
	 * 
	 * @author Tushar
	 * @version 1.0
	 * @param serviceURL
	 * @return List<ServiceStatusVO>
	 */
	@RequestMapping(value = "/api/monitoring/servicelog", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ServiceStatusVO> servicelog(@RequestParam(value = "serviceurl", required = false) String serviceURL) {
		MonitoringBO mBO = new MonitoringBO();
		return mBO.queryStatusLog(serviceURL);
	}
}
