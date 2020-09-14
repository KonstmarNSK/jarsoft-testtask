package banners.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Controller
public class SomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SomeController.class);

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/banner")
    @ResponseBody
    public String getBanner(@RequestParam String categoryId,
                            @RequestHeader(value = "User-Agent", required = false) String userAgent,
                            HttpServletRequest request){

        Date now = Calendar.getInstance().getTime();


        return "";
    }
}