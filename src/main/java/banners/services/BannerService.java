package banners.services;

import banners.repos.BannerRepository;
import banners.repos.RequestRepository;
import banners.model.Banner;
import banners.model.Category;
import banners.model.Request;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class BannerService {
    private BannerRepository bannerRepository;
    private RequestRepository requestRepository;

    public BannerService(BannerRepository bannerRepository, RequestRepository requestRepository) {
        this.bannerRepository = bannerRepository;
        this.requestRepository = requestRepository;
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

    /**
     * Finds banner with given category with highest price.
     * Doesn't return one banner twice in a day to one user.
     * Saves given request to the db.
     *
     * @param category category which banner belongs to
     * @param request request's ip address and user agent
     * @return Optional that contains found banner (or empty optional if nothing's found)
     */
    public Optional<Banner> getMostExpensiveBannerInCategory(Category category, Request request) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("price").descending());

        List<Banner> foundBanners = bannerRepository.getActiveBannersByCategoryWithSorting(
                category,
                request,
                pageable);

        Banner foundBanner = null;

        if(!foundBanners.isEmpty()) {
            foundBanner = foundBanners.get(0);

            request.setBanner(foundBanner);
        }
        requestRepository.save(request);

        return Optional.ofNullable(foundBanner);
    }
}
