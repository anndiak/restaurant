package com.application.restaurant.email_validation;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email")
@Data
public class EmailProperties {

    private String userName;

    private String address;

    private String accountName;

    private String accountPassword;
}
