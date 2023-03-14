package com.reactive.database.r2dbcpostgresqltodolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories
@SpringBootApplication
public class R2dbcPostgresqlTodoListApplication {

	public static void main(String[] args) {
		SpringApplication.run(R2dbcPostgresqlTodoListApplication.class, args);
	}

}
