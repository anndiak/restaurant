package com.application.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotNull
    @NotEmpty
    //@ValidEmail
    private String email;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private Roles roles;

    private String password;

    private String verificationCode;

    private boolean enabled;
}

