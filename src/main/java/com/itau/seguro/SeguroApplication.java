package com.itau.seguro;

import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SeguroApplication {

	public static void main(String[] args) {


		Thread threadRunDatabaseManager = new Thread() {

			@Override
			public void run() {
				org.hsqldb.util.DatabaseManagerSwing.main(new String[]
					{ "--url", "jdbc:hsqldb:mem:segurodb", "--user", "sa", "--password", ""}
				);
			}

		};

		threadRunDatabaseManager.start();

		SpringApplication.run(SeguroApplication.class, args);
	}

}
