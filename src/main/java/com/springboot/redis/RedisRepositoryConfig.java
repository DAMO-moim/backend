package com.springboot.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories // Redis 리포지토리를 활성화하여 Redis 데이터를 JPA처럼 다룰 수 있게 해주는 애너테이션
public class RedisRepositoryConfig {
    // application.yml 파일에서 Redis 서버의 호스트 주소를 가져온다.
    @Value("${spring.data.redis.host}")
    private String host;

    // application.yml 파일에서 Redis 서버의 포트를 가져온다.
    @Value("${spring.data.redis.port}")
    private int port;

    //RedisConnectionFactory 빈을 생성하는 메서드
    //Redis 서버와의 연결을 관리하는 역할, Spring Data Redis는 이 팩토리를 통해 Redis 서버와의 모든 상호작용을 수행한다.
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // RedisStandaloneConfiguration 객체를 생성하여 Redis 서버의 호스트와 포트를 설정
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host); // Redis 서버의 호스트 설정
        redisStandaloneConfiguration.setPort(port);     // Redis 서버의 포트 설정

        // LettuceConnectionFactory는 Redis와의 연결을 비동기적으로 관리하는 클라이언트 라이브러리
        // LettuceConnectionFactory를 사용하여 Redis 연결을 설정
        // LettuceConnectionFactory는 연결 풀을 지원하고, Redis 명령어를 비동기적으로 처리한다.
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    // RedisTemplate 빈을 생성하는 메서드
    // Redis 서버와 데이터를 읽고 쓰기 위한 주요 인터페이스
    // RedisTemplate은 내부적으로 RedisConnectionFactory를 사용하여 Redis 서버와 통신한다.
    // 이 템플릿을 이용하여 Redis에 데이터를 저장, 검색 할 수 있다.
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        // RedisTemplate 객체생성
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // RedisConnectionFactory를 RedisTemplate에 설정
        // RedisTemplate이 Redis 서버와의 연결을 사용할 수 있도록 한다.
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Redis의 키와 값 직렬화 방식설정
        // StringRedisSerializer를 사용하여 키와 값을 문자열로 직렬화
        // Redis에 데이터를 저장할 때 직렬화 방식을 지정한다. (데이터 저장 형식을 정의)
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}

