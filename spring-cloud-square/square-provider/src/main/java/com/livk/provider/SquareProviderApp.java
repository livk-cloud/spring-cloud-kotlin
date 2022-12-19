package com.livk.provider;


import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SquareProviderApp
 * </p>
 *
 * @author livk
 * @date 2022/4/13
 */
@SpringBootApplication
public class SquareProviderApp {

    public static void main(String[] args) {
        LivkSpring.run(SquareProviderApp.class, args);
    }

}
