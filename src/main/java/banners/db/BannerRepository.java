package banners.db;

import banners.model.Banner;
import banners.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    /**
     * Finds all banners belonging to the given category that are not marked as deleted
     * @param cat - category which banners belong to
     * @return found active banners
     */
    @Query("SELECT b FROM Banner b WHERE b.deleted=false AND category=:cat")
    Set<Banner> getActiveBannersByCategory(@Param("cat") Category cat);
}
