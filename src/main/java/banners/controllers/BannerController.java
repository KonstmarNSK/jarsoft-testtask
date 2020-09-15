package banners.controllers;

import banners.dto.BannerDto;
import banners.model.Banner;
import banners.model.Category;
import banners.model.Request;
import banners.services.BannerService;
import banners.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;

@Controller
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/banner/create")
    public String createBannerPage(){
        return "addBanner";
    }

    @PostMapping("/banner/create")
    public String createBanner(
            @RequestParam String name,
            @RequestParam BigDecimal price,
            @RequestParam String content,
            @RequestParam Long categoryId

    ){
        Banner banner = new Banner();
        banner.setName(name);
        banner.setContent(content);
        banner.setPrice(price);

        Category category = categoryService.getCategoryById(categoryId);
        banner.setCategory(category);
        bannerService.saveBanner(banner);

        return "redirect:/";
    }

    @GetMapping("/banner/get")
    @ResponseBody
    public BannerDto getBanner(@RequestParam Long categoryId,
                               @RequestHeader(value = "User-Agent", required = false) String userAgent,
                               HttpServletRequest httpRequest){

        Request request = new Request();
        request.setUserAgent(userAgent);
        request.setIpAddress(httpRequest.getRemoteAddr());
        request.setDate(Calendar.getInstance().getTime());

        Category category = new Category();
        category.setId(categoryId);

        Optional<Banner> result = bannerService.getMostExpensiveBannerInCategory(category, request);

//        if(result.isPresent()){
            Banner b = result.get();
            return new BannerDto(b.getName(), b.getContent(), b.getPrice(), b.getId());
//        }
    }
}
