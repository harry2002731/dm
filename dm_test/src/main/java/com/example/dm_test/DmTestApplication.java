package com.example.dm_test;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DmTestApplication {
//	@Bean
//	public Interceptor[] plugins() {
//		return new Interceptor[]{new PageInterceptor()};
//	}

	public static void main(String[] args) {
		SpringApplication.run(DmTestApplication.class, args);
	}

}
