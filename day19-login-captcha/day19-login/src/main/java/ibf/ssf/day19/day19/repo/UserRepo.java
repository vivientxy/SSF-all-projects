package ibf.ssf.day19.day19.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import ibf.ssf.day19.day19.util.Utils;

@Repository
public class UserRepo {

    @Autowired @Qualifier(Utils.REDIS_STRING)
    RedisTemplate<String,String> redisTemplate;

    ValueOperations<String, String> valueOps;

    // CRUD
    public void createUser(String username, String password) {
        valueOps = redisTemplate.opsForValue();
        valueOps.set(username, password);
    }

    public String retrieveUserPassword(String username) {
        valueOps = redisTemplate.opsForValue();
        return valueOps.get(username);
    }

    public void updateUserPassword(String username, String password) {
        valueOps = redisTemplate.opsForValue();
        valueOps.setIfPresent(username, password);
    }

    public void deleteUser(String username) {
        valueOps = redisTemplate.opsForValue();
        valueOps.getAndDelete(username);
    }

}
