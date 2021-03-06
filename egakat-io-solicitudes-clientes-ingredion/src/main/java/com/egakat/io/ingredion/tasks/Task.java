package com.egakat.io.ingredion.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.egakat.io.silogtran.service.api.RemesasPushService;

@Component
public class Task {

	@Autowired
	private RemesasPushService service;
	
	@Scheduled(cron = "${cron-ordenes-alistamiento}")
	public void run() {
		service.push();
	}
}
