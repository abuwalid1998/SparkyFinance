package com.DataExtractor.SparkyFinance.SparkyFinance;

import Controller.MainController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = MainController.class)
public class SparkyFinanceApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(SparkyFinanceApplication.class, args);
	}

}
