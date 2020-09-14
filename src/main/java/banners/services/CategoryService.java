package banners.services;

import banners.db.CategoryRepository;
import banners.model.Banner;
import banners.model.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    private CategoryRepository categoryRepository;
    private BannerService bannerService;

    public CategoryService(CategoryRepository categoryRepository, BannerService bannerService) {
        this.categoryRepository = categoryRepository;
        this.bannerService = bannerService;
    }

    public void saveCategory(Category category){
        categoryRepository.save(category);
    }

    /**
     * Deletes category (marks as deleted) and returns empty set if it has no active banners.
     * Returns set of banners' ids otherwise.
     *
     * @param category - category that must be marked as deleted
     * @return set of active banners (if any) or empty set
     */
    public Set<Long> deleteCategory(Category category){
        Set<Long> activeBannersIds = bannerService.getNotDeletedBannersByCategory(category)
                .stream()
                .map(Banner::getId)
                .collect(Collectors.toSet());

        // delete category if it has no active banners
        if(activeBannersIds.isEmpty()){
            category.setDeleted(true);
            categoryRepository.save(category);
        }

        return activeBannersIds;
    }

    public Set<Category> getCategoriesByName(String name){
        return categoryRepository.getCategoryByName(name);
    }

}
