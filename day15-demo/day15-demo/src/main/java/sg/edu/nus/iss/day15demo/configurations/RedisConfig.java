package sg.edu.nus.iss.day15demo.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import sg.edu.nus.iss.day15demo.utils.Util;

@Configuration
public class RedisConfig {
    
    // inject application.properties into this configuration
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private Integer redisPort;
    @Value("${spring.data.redis.username}")
    private String redisUsername;
    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {

        // set up the configuration
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);

        if (!"NOT_SET".equals(redisUsername.trim())) {
            redisConfig.setUsername(redisUsername);
            redisConfig.setPassword(redisPassword);
        }

        // pass in config in order to create client and factory
        JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        JedisConnectionFactory jedisConnFac = new JedisConnectionFactory(redisConfig, jedisClient);

        return jedisConnFac;
    }

    @Bean(Util.REDIS_ONE)   // names the template (function) "myredisone" as defined as constant in Util.java
    public RedisTemplate<String,String> redisTemplate() {
        RedisTemplate<String,String> template = new RedisTemplate<>();

        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        return template;
    }

    @Bean(Util.REDIS_TWO)   // names the template (function) "myredistwo" as defined as constant in Util.java
    public RedisTemplate<String,Object> redisObjectTemplate() {
        RedisTemplate<String,Object> template = new RedisTemplate<>();

        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        return template;
    }
}
