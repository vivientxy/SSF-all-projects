package sg.edu.nus.iss.day15demo.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day15demo.utils.Util;

@Repository
public class PersonRepo {

    @Autowired
    @Qualifier(Util.REDIS_ONE)
    RedisTemplate<String, String> template;

    // CRUD operations

    public Long addToList(String key, String value) {
        return template.opsForList().rightPush(key, value);
    }

    public List<String> getList(String key) {
        return template.opsForList().range(key, 0, -1);
    }

    public Boolean updateList(String key, String oldValue, String newValue) {
        ListOperations<String, String> listOps = template.opsForList();
        Boolean isUpdated = false;

        Long index = listOps.indexOf(key, oldValue);
        if (index >= 0) {
            listOps.set(key, index, newValue);
            isUpdated = true;
        }

        return isUpdated;
    }

    public Boolean deleteItem(String key, String value) {
        ListOperations<String, String> listOps = template.opsForList();
        Boolean isDeleted = false;

        Long index = listOps.indexOf(key, value);
        if (index >= 0) {
            listOps.remove(key, 1, value);
            isDeleted = true;
        }

        return isDeleted;
    }

}
