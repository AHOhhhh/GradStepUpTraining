package com.thoughtworks.mall_order.endpoint;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrderHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        if (check()) {
            return Health.up().build();
        } else {
            return Health.down().build();
        }
    }

    public boolean check() {
        RestTemplate restTemplate = new RestTemplate();
        String status;
        try {
            status = restTemplate.getForObject("http://localhost:8888/actuator/health", String.class);
        } catch (Exception ex) {
            return false;
        }
        return status.substring(11, status.length() - 2).equals("UP");
    }

}
