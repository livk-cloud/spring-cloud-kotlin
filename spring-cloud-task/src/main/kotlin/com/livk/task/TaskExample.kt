package com.livk.task

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.task.configuration.EnableTask
import org.springframework.context.annotation.Bean

/**
 * <p>
 * TaskExample
 * </p>
 *
 * @author livk
 * @date 2022/12/10
 */
@EnableTask
@SpringBootApplication
open class TaskExample {

    @Bean
    open fun commandLineRunner(): CommandLineRunner {
        return HelloWorldCommandLineRunner();
    }

    class HelloWorldCommandLineRunner : CommandLineRunner {

        private val logger = LoggerFactory.getLogger(HelloWorldCommandLineRunner::class.java)

        override fun run(vararg args: String?) {
            logger.info("{}", "hello,world!")
        }

    }
}

fun main(args: Array<String>) {
    runApplication<TaskExample>(*args)
}
