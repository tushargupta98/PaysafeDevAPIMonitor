package com.paysafe.apimonitor.ws.model;

import java.util.Timer;

import com.paysafe.business.SchedulerTask;

public class SchedulerVO {

	private String serviceName;
	private SchedulerTask schedulerTask;
	private Timer timer;

	public SchedulerVO(String serviceName, long timeInterval) {
		this.serviceName = new String(serviceName);
		this.schedulerTask = new SchedulerTask(timeInterval, serviceName);
		this.timer = new Timer(serviceName);
	}

	public String getServiceName() {
		return serviceName;
	}

	public SchedulerTask getSchedulerTask() {
		return schedulerTask;
	}

	public Timer getTimer() {
		return timer;
	}
}
