package banners.db;

import banners.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // fixme: too slow
    @Query("SELECT c FROM Category c WHERE c.deleted=false AND lower(c.name) like lower(concat('%', :name,'%'))")
    Set<Category> getCategoryByName(@Param("name") String name);
}
