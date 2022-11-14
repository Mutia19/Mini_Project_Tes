package com.miniproject.demo.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {

    private static final long serialVersionUID = -7295118090882114533L;

    private Long id;
    private String email;
    private String username;
    private String password;
    private String name;
    private String address;
    private String phone;
    private Integer nik;
    private AccountDto account;
    private String role;
    private Boolean active;
}
