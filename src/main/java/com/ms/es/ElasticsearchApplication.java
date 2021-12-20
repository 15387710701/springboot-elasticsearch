package com.ms.es;

import com.ms.es.mapper.EmployeeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author:SmartV
 * @date:2021/12/20 11:38
 */
@SpringBootApplication
public class ElasticsearchApplication {
    public static void main(String[] args) {

        SpringApplication.run(ElasticsearchApplication.class,args);
    }
}
