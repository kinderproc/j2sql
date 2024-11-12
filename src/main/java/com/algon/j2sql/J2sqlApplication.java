package com.algon.j2sql;

import com.algon.j2sql.service.RunnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class J2sqlApplication implements CommandLineRunner {

	private final RunnerService runnerService;

	public static void main(String[] args) {
		SpringApplication.run(J2sqlApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		runnerService.run(args);
	}
}
