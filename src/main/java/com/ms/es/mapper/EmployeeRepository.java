package com.ms.es.mapper;

import com.ms.es.domain.UserEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface EmployeeRepository extends ElasticsearchRepository<UserEntity,String> {
    UserEntity queryEmployeeById(String id);

}