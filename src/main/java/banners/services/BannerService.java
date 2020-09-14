package banners.services;

import banners.db.BannerRepository;
import banners.model.Banner;
import banners.model.Category;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BannerService {
    private BannerRepository bannerRepository;

    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public void saveBanner(Banner banner) {
        bannerRepository.save(banner);
    }

    public void deleteBanner(Banner banner) {
        banner.setDeleted(true);
        bannerRepository.save(banner);
    }

    /**
     * @return set of banners belonging to the given category that are not marked as deleted
     */
    public Set<Banner> getNotDeletedBannersByCategory(Category category) {
        return bannerRepository.getActiveBannersByCategory(category);
    }

    public Set<Banner> getBannersByName(String name) {
        return bannerRepository.getActiveBannersByName(name);
    }

    public Banner getMostExpensiveBannerInCategory(Category category) {
        Pageable pageable = PageRequest.of(0, 1);
        pageable.getSort().and(Sort.by("price").descending());

        return bannerRepository.getActiveBannersByCategoryWithSorting(
                category,
                pageable)
                .get(0);
    }
}
