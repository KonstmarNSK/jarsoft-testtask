package banners.controllers;

import banners.services.BannerService;
import banners.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/create")
    public String createBannerPage(){
        return "addBanner";
    }

}
