package banners.db;

import banners.model.Banner;
import banners.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    // fixme: too slow
    @Query("SELECT b FROM Banner b WHERE b.deleted=false AND category=:cat")
    List<Banner> getActiveBannersByCategoryWithSorting(@Param("cat") Category category, Pageable pageable);

    @Query("SELECT b FROM Banner b WHERE b.deleted=false AND lower(b.name) like lower(concat('%', :name,'%'))")
    Set<Banner> getActiveBannersByName(@Param("name") String name);
}
