package sg.nus.vttp.ssfworkshoptrial.repo;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.nus.vttp.ssfworkshoptrial.Utils;
import sg.nus.vttp.ssfworkshoptrial.model.Article;
import sg.nus.vttp.ssfworkshoptrial.model.Country;

@Repository
public class newsRepo {

    @Autowired @Qualifier(Utils.REDIS)//ask spring to look for something, look for myredis bean
	private RedisTemplate<String,Object> template;

    Map<String, Country> countryMap = new HashMap<String, Country>();

    public void addCountry(String countryName, String countryCode){
        countryMap.put(countryName, new Country(countryName,countryCode));
    }
    
    public Country getCountry(String countryName){
        return(countryMap.get(countryName));
    }

    public void addToRedis(Article article){
        String title = article.getTitle();
        template.opsForValue().set(("Article: "+title),article,Duration.ofMinutes(30));
    }

    public Set<String> getKeys(){
        Set<String> keys = template.keys("Article: *");
        return keys;
    }

    public Article getArticle(String key){
        Article article = (Article)template.opsForValue().get(key);
        return article;
    }
}
