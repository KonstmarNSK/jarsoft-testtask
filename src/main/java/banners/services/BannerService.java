package banners.services;

import banners.db.BannerRepository;
import banners.model.Banner;
import banners.model.Category;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BannerService {
    private BannerRepository bannerRepository;

    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public void saveBanner(Banner banner){
        bannerRepository.save(banner);
    }

    public void deleteBanner(Banner banner){
        banner.setDeleted(true);
        bannerRepository.save(banner);
    }

    /**
     * @return set of banners belonging to the given category that are not marked as deleted
     */
    public Set<Banner> getNotDeletedBannersByCategory(Category category){
        return bannerRepository.getActiveBannersByCategory(category);
    }
}
