package ibf.ssf.day19.day19.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ibf.ssf.day19.day19.model.User;
import ibf.ssf.day19.day19.util.Utils;

@Repository
public class UserRepo {

    @Autowired @Qualifier(Utils.REDIS_STRING)
    RedisTemplate<String,String> redisTemplate;

    HashOperations<String, String, String> hashOps;

    // CRUD
    @SuppressWarnings("null")
    public void createUser(User user) {
        hashOps = redisTemplate.opsForHash();
        hashOps.put(Utils.REDIS_USER_KEY, user.getUsername(), user.toString());
    }

    @SuppressWarnings("null")
    public String retrieveUser(String username) {
        hashOps = redisTemplate.opsForHash();
        return hashOps.get(Utils.REDIS_USER_KEY, username);
    }

    @SuppressWarnings("null")
    public void updateUser(User user) {
        hashOps = redisTemplate.opsForHash();
        hashOps.put(Utils.REDIS_USER_KEY, user.getUsername(), user.toString());
    }

    public void deleteUser(String username) {
        hashOps = redisTemplate.opsForHash();
        hashOps.delete(Utils.REDIS_USER_KEY, username);
    }

    @SuppressWarnings("null")
    public Boolean doesUserExist(String username) {
        hashOps = redisTemplate.opsForHash();
        return hashOps.hasKey(Utils.REDIS_USER_KEY, username);
    }

}
