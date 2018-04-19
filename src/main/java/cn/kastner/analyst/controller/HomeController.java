package cn.kastner.analyst.controller;

import cn.kastner.analyst.crawler.JdCrawler;
import cn.kastner.analyst.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.regex.*;

@Controller
public class HomeController {

    @Autowired
    ItemRepository itemRepository;

    JdCrawler jdCrawler;

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/search")
    public String search(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        Boolean isMatch = Pattern.matches(".*https?.*", keyword);
//        System.out.println(keyword);
//        System.out.println(isMatch);

        if (isMatch) {
            String itemId = jdCrawler.crawItemByUrl(keyword);
            model.addAttribute("itemId", itemId);
            return "item";
        }
        return "itemList";
    }

}

