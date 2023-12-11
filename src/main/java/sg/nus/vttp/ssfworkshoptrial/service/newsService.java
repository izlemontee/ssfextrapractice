package sg.nus.vttp.ssfworkshoptrial.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.nus.vttp.ssfworkshoptrial.model.Article;
import sg.nus.vttp.ssfworkshoptrial.model.Country;
import sg.nus.vttp.ssfworkshoptrial.repo.newsRepo;

@Service
public class newsService {

    @Autowired
    newsRepo newsrepo;

    private final String API_KEY="&apiKey=29e1e97463724781a7eb8a0b3c4560dd";
    private final String COUNTRIES_API = "https://restcountries.com/v3.1/all";
    private final String NEWS_API = "https://newsapi.org/v2/everything?q=";
    private final String NEWS_API_TOP = "https://newsapi.org/v2/top-headlines?";
    private final String SEARCH_BY_CATEGORY = "category=";
    private final String SEARCH_BY_COUNTRY = "country=";
    RestTemplate restTemplate = new RestTemplate();

    public List<String> getCountries(){
        List<String> countryList = new ArrayList<String>();
        Map<String, Country> countryMap = new HashMap<String, Country>();
        ResponseEntity<String> result = restTemplate.getForEntity(COUNTRIES_API, String.class);
        String resultBody = result.getBody().toString();
        //System.out.println(resultBody);
        JsonReader jsonReader = Json.createReader(new StringReader(resultBody));
        //System.out.println(jsonReader.readArray().getClass());
        JsonArray jsonArray = jsonReader.readArray();
        for(int i = 0; i <jsonArray.size(); i++){
            //System.out.println(jsonArray.getJsonObject(i));
            JsonObject country = jsonArray.getJsonObject(i);
            // this is the country name you want
            String countryName = country.getJsonObject("name").getString("common");
            String countryCode = country.getString("cca2");
            System.out.println(countryCode);
            //System.out.println(countryName);
            countryList.add(countryName);
            newsrepo.addCountry(countryName, countryCode);
        }
        return countryList;
    }

    public List<Article> getNews(String countryName, String category){
        List<Article> articleList = new ArrayList<Article>();
        Set<String> keys = newsrepo.getKeys();
        System.out.println("Keys: "+keys);
        if(keys.size()>0){
            for(String key:keys){
                Article article = newsrepo.getArticle(key);
                articleList.add(article);
            }
        }
        else{
            Country country = newsrepo.getCountry(countryName);
            String countryCode = country.getCountryCode();
            String query_url = NEWS_API_TOP+SEARCH_BY_COUNTRY+countryCode+"&"+SEARCH_BY_CATEGORY+category+API_KEY;
            ResponseEntity<String> response = restTemplate.getForEntity(query_url, String.class);
            String responseBody = response.getBody().toString();
            JsonReader jsonReader = Json.createReader(new StringReader(responseBody));
            JsonArray jsonArray = (jsonReader.readObject().getJsonArray("articles"));
            int arraySize = 0;
            if(jsonArray.size()<5){
                arraySize = jsonArray.size();
            }
            else{
                arraySize = 5;
            }
    
            for(int i=0; i<arraySize; i++){
                JsonObject jsonObj = jsonArray.getJsonObject(i);
                String author = jsonObj.get("author").toString();
                String title = jsonObj.get("title").toString();
                String description = jsonObj.get("description").toString();
                String url = jsonObj.get("url").toString();
                String image = jsonObj.get("urlToImage").toString();
                String timestamp = jsonObj.get("publishedAt").toString();
                
                Article article = new Article(title, author, image, description, timestamp, url);
                articleList.add(article);
                newsrepo.addToRedis(article);
                // System.out.println("AUTHOR: "+ author);
                // System.out.println("TITLE: "+title);
                // System.out.println("DESCRIPTION: "+description);
                // System.out.println("URL: "+url);
                // System.out.println("IMAGE: "+image);
                // System.out.println("TIMESTAMP: "+timestamp);
            }

            Article articleTest = articleList.get(0);
            System.out.println("AUTHOR: "+ articleTest.getAuthor());
            System.out.println("TITLE: "+articleTest.getTitle());
            System.out.println("DESCRIPTION: "+articleTest.getDescription());
            System.out.println("URL: "+articleTest.getUrl());
            System.out.println("IMAGE: "+articleTest.getImage());
            System.out.println("TIMESTAMP: "+articleTest.getTimestamp());
        }
        System.out.println("yeah you good");
        return articleList;
        //readArticle(responseBody);
        //System.out.println(responseBody);
    }

    public void readArticle(String body){

    }

    
}
