package nus.iss.day17weatherapi.service;

import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpbinService {

    // check for network access (e.g. if website is down, maybe every 100 checks make a ping to website)
    public Boolean isAlive() {
        RequestEntity<Void> req = RequestEntity
            .get("https://httpbin.org/get")
            .build();
        
        try {
            RestTemplate template = new RestTemplate();
            template.exchange(req, String.class); // don't need store the response, just want to make sure it goes through
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}
