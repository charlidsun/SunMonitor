package com.sun.monitorServer;

import com.sun.monitorServer.server.MonitorServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MonitorServerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MonitorServerApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		new MonitorServer().run();
	}
}

