package com.sxw.springbootproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.sxw.springbootproducer")
@SpringBootApplication
public class ProducerApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(ProducerApplication.class, args);
    }
}
