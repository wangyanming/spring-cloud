package com.hds;

import com.hds.limit.HostAddrKeyResolver;
import com.hds.limit.UserKeyResolver;
import com.hds.ribbon.rule.Rule;
import com.netflix.loadbalancer.IRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author admin
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public IRule myRule() {
		return new Rule();
	}

	@Bean
	public HostAddrKeyResolver hostAddrKeyResolver() {
		return new HostAddrKeyResolver();
	}

	/*@Bean
	public UserKeyResolver userKeyResolver() {
		return new UserKeyResolver();
	}*/

	/*@Bean
	public IpKeyResolver ipKeyResolver() {
		return new IpKeyResolver();
	}

	@Bean
	public UriKeyResolver uriKeyResolver() {
		return new UriKeyResolver();
	}*/
}

