package com.eureka.mindbloom.common.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;
@EnableCaching
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfig {

	private final RedisProperties redisProperties;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory factory = new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
		factory.setPassword(redisProperties.getPassword());
		return factory;
	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		return redisTemplate;
	}

	@Bean(name = "redisCacheManager")
	@Primary
	public CacheManager redisCacheManager(RedisConnectionFactory factory) {
		RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));


		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
		cacheConfigurations.put("categoryBooks", cacheConfig.entryTtl(Duration.ofMinutes(1440)));
		cacheConfigurations.put("TraitBooks", cacheConfig.entryTtl(Duration.ofMinutes(1440)));
		cacheConfigurations.put("TopBook", cacheConfig.entryTtl(Duration.ofMinutes(1440)));
		cacheConfigurations.put("bookLikes", cacheConfig.entryTtl(Duration.ofMinutes(1440)));


		return RedisCacheManager
			.RedisCacheManagerBuilder
			.fromConnectionFactory(factory)
			.cacheDefaults(cacheConfig)
			.withInitialCacheConfigurations(cacheConfigurations)
			.build();
	}
}