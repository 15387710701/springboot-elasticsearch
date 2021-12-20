package com.ms.es.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
@ToString
@Getter
@Setter
@Document(indexName = "customer",type = "external", shards = 1,replicas = 0, refreshInterval = "-1")
public class UserEntity {
    @Id
    private String id;
    @Field
    private String name;
    @Field
    private Integer age;
    @Field
    private Integer blance = 0;

}
