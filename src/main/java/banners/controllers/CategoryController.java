package banners.controllers;

import banners.dto.CategoryDto;
import banners.model.Category;
import banners.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class CategoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/create")
    public String addCategoryPage(){
        return "addCategory";
    }

    @PostMapping("/category/create")
    public String addCategory(@RequestParam String name, @RequestParam String reqName){

        Category category = new Category();
        category.setDeleted(false);
        category.setReqName(reqName);
        category.setName(name);

        categoryService.saveCategory(category);

        LOGGER.info("created category with id = " + category.getId());

        return "redirect:/";
    }

    @GetMapping("/category/get")
    @ResponseBody
    public CategoryDto getCategory(@RequestParam Long categoryId){
        Category cat = categoryService.getCategoryById(categoryId);
        return new CategoryDto(cat.getId(), cat.getName(), cat.getReqName());
    }

    @GetMapping("/category/find")
    public Collection<CategoryDto> findCategories(@RequestParam String query){
        return categoryService.getCategoriesByNameOrReqName(query).stream()
                .map(cat -> new CategoryDto(cat.getId(), cat.getName(), cat.getReqName()))
                .collect(Collectors.toSet());
    }

    @PostMapping("/category/edit")
    public String editCategory(@RequestParam Long categoryId,
                               @RequestParam(required = false) String newName,
                               @RequestParam(required = false) String newReqName){

        //fixme: remove extra query
        Category category = categoryService.getCategoryById(categoryId);

        if(newName != null){
            category.setName(newName);
        }
        if(newReqName != null){
            category.setReqName(newReqName);
        }

        categoryService.saveCategory(category);

        return "redirect:/";
    }
}
