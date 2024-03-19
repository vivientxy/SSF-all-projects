package sg.edu.nus.iss.day15demo.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day15demo.utils.Util;

@Repository
public class TestRepo {

    @Autowired
    @Qualifier(Util.REDIS_ONE)  // using template with the bean name same as "myredis"
    RedisTemplate<String,String> redisTemplate;

    public void storeValueData(String key, String value) {
        ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, value);
    }

    public String retrieveValueData(String key) {
        ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
        return valueOps.get(key).toString();
    }

    public void addToList(String key, String value) {
        ListOperations<String,String> listOps = redisTemplate.opsForList();
        listOps.rightPush(key, value);
    }

    public List<String> getList(String key) {
        ListOperations<String,String> listOps = redisTemplate.opsForList();
        return listOps.range(key, 0, -1);
    }

    public void incrementValueData(String key, Long value) {
        ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
        valueOps.increment(key, value);
    }

    public Boolean deleteKey(String key) {
        Boolean isKeyPresent = redisTemplate.hasKey(key);
        Boolean isDeleted = false;
        if (isKeyPresent) {
            redisTemplate.delete(key);
            isDeleted = true;
        }
        return isDeleted;
    }

    public Long getListSize(String key) {
        ListOperations<String,String> listOps = redisTemplate.opsForList();
        return listOps.size(key);
    }

}
