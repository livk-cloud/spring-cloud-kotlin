package com.livk.provider.api.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Bean
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@Data
public class Users implements Serializable {

    private Long id;

    private String username;

    private String password;

}
