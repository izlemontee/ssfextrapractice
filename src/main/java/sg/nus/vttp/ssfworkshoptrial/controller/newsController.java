package sg.nus.vttp.ssfworkshoptrial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.nus.vttp.ssfworkshoptrial.model.Article;
import sg.nus.vttp.ssfworkshoptrial.service.newsService;

@Controller
@RequestMapping
public class newsController {

    @Autowired
    newsService newsSvc;

    @GetMapping(path = "/")
    public String landingPage(Model model){
        List<String> countryList = newsSvc.getCountries();
        model.addAttribute("countrylist", countryList);
        return "index";
    }
    
    @GetMapping(path="/searchcountry")
    public String searchCountry(@RequestParam(name="countries") String country, 
    @RequestParam(name="category") String category, Model model){
        List<Article> articleList = newsSvc.getNews(country, category);
        if(articleList.size()>0){
            model.addAttribute("articles", articleList);
            return "articles";
        }
        return "ok";

    }
}
