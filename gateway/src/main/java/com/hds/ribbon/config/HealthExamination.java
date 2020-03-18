package com.hds.ribbon.config;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HealthExamination implements IPing {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean isAlive(Server server) {
        String url = "http://" + server.getId() + "/actuator/health";
        try {
            ResponseEntity<String> health = restTemplate.getForEntity(url, String.class);
            if (health.getStatusCode() == HttpStatus.OK) {
                //System.out.println("ping " + url + " success and response is " + health.getBody());
                return true;
            }
            //System.out.println("ping " + url + " error and response is " + health.getBody());
            return false;
        } catch (Exception e) {
            //System.out.println("ping " + url + " failed");
            return false;
        }
    }
}
