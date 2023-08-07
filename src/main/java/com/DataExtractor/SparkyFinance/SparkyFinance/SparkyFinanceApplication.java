package com.DataExtractor.SparkyFinance.SparkyFinance;

import Controller.MainController;
import Interfaces.MongoFileRepository;
import Services.FileServiceMongo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackageClasses = {MainController.class, FileServiceMongo.class, MongoFileRepository.class})
public class SparkyFinanceApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(SparkyFinanceApplication.class, args);
	}

}
