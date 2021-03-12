package com.backend.aggregatorservice.config;

import java.util.concurrent.Executor;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableAsync
public class ConfigClass {
	
	@Autowired
    CloseableHttpClient httpClient;
	
	 	@Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate(clientHttpRequestFactory());
	    }

	    private ClientHttpRequestFactory clientHttpRequestFactory() {
	        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
	        clientHttpRequestFactory.setHttpClient(httpClient);
	        return clientHttpRequestFactory;
	    }
	    
	    @Bean(name = "taskExecutor")
	    public TaskExecutor getAsyncExecutor() {
	    	ThreadPoolTaskExecutor executor =  new ThreadPoolTaskExecutor();
	    	 executor.setCorePoolSize(4);
	         executor.setMaxPoolSize(20);
	         //executor.setWaitForTasksToCompleteOnShutdown(true);
	         executor.setThreadNamePrefix("Async-");
	         return executor;
	    }
	    
	    @Bean(name="pooledClient")
	    public CloseableHttpClient httpClient() {
	        return HttpClientBuilder.create().build();
	    }
	    

}
