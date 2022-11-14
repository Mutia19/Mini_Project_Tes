package com.miniproject.demo.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountResponse implements Serializable {

    private static final long serialVersionUID = 4232323000166775865L;

    private Integer user;
    private Integer product;
    private Integer transaction;
}
