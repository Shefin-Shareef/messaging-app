package com.shefin.messagingApp;

import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import com.shefin.messagingApp.entity.Folder;
import com.shefin.messagingApp.repo.FolderRepo;


@SpringBootApplication
public class MessagingApp {


	public static void main(String[] args) {
		SpringApplication.run(MessagingApp.class, args);
	}
	
	
	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}
	
	
	

}
