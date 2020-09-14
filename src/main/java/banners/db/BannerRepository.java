package banners.db;

import banners.dto.ClientData;
import banners.model.Banner;
import banners.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    @Query("SELECT b FROM Banner b WHERE b.deleted=false AND category=:cat AND b NOT IN " +
            "(SELECT r.banner FROM Request r WHERE r.userAgent=:#{#cl.userAgent} AND r.ipAddress=:#{#cl.address} AND r.date >= CURRENT_DATE)")
    List<Banner> getActiveBannersByCategoryWithSorting(@Param("cat") Category category, @Param("cl") ClientData clData, Pageable pageable);

    @Query("SELECT b FROM Banner b WHERE b.deleted=false AND lower(b.name) like lower(concat('%', :name,'%'))")
    Set<Banner> getActiveBannersByName(@Param("name") String name);
}
