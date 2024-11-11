package com.algon.j2sql;

import com.algon.j2sql.service.FileServiceImpl;
import com.algon.j2sql.service.RunnerService;
import com.algon.j2sql.service.SqlGeneratorServiceImpl;

public class J2sqlApplication {

	public static void main(String[] args) {
		new RunnerService(new SqlGeneratorServiceImpl(), new FileServiceImpl()).run(args);
	}

}
