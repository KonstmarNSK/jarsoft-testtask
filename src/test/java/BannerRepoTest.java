import banners.Main;
import banners.db.BannerRepository;
import banners.db.CategoryRepository;
import banners.db.RequestRepository;
import banners.model.Banner;
import banners.model.Category;
import banners.model.Request;
import banners.services.BannerService;
import banners.services.CategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;


@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Main.class, CategoryService.class, BannerService.class})
public class BannerRepoTest {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private CategoryService categoryService;


    @Test
    public void testBannerInsertion(){
        Category category = new Category();
        category.setDeleted(false);
        category.setName("TestCategory");
        category.setReqName("SomeReqName");
        categoryRepository.save(category);

        Banner banner = new Banner();
        banner.setName("SomeBannerName");
        banner.setCategory(category);
        banner.setContent("SomeBannerContent");
        banner.setDeleted(false);
        banner.setPrice(BigDecimal.valueOf(12.6d));

        bannerRepository.save(banner);

        Banner readBanner = bannerRepository.getOne(banner.getId());
        Category readCategory = categoryRepository.getOne(category.getId());

        Assert.assertEquals("Banners' IDs don't match!", banner.getId(), readBanner.getId());
        Assert.assertEquals("Banners' names don't match!", banner.getName(), readBanner.getName());
        Assert.assertEquals("Banners' contents don't match!", banner.getContent(), readBanner.getContent());
        Assert.assertEquals("Banners' deleted flags don't match!", banner.isDeleted(), readBanner.isDeleted());
        Assert.assertEquals("Banners' prices don't match!", banner.getPrice(), readBanner.getPrice());
        Assert.assertEquals("Banners' categories don't match!", banner.getCategory().getId(), readBanner.getCategory().getId());

        Assert.assertEquals("Categories' banners collections have different size!", category.getBanners().size(), readCategory.getBanners().size());
    }

    @Test
    public void testRequestInsertion(){
        Category category = new Category();
        category.setDeleted(false);
        category.setName("TestCategory");
        category.setReqName("SomeReqName");
        categoryRepository.save(category);

        Banner banner = new Banner();
        banner.setName("SomeBannerName");
        banner.setCategory(category);
        banner.setContent("SomeBannerContent");
        banner.setDeleted(false);
        banner.setPrice(BigDecimal.valueOf(12.6d));

        bannerRepository.save(banner);

        Date now = Calendar.getInstance().getTime();
        Request request = new Request();
        request.setDate(now);
        request.setBanner(banner);
        request.setIpAddress("127.0.0.1");
        request.setUserAgent("SomeUserAgent");

        requestRepository.save(request);

        Request readReq = requestRepository.getOne(request.getId());

        Assert.assertEquals("Banners' IDs don't match!", banner.getId(), readReq.getBanner().getId());
        Assert.assertEquals("Saved req time is wrong!", readReq.getDate(), now);
        Assert.assertEquals("Ips don't match!", readReq.getIpAddress(), request.getIpAddress());

    }

    @Test
    public void testEmptyCategoryDeletion(){
        Category category = new Category();
        category.setDeleted(false);
        category.setName("TestCategory");
        category.setReqName("SomeReqName");
        categoryRepository.save(category);

        Set<Long> categoryBannersIds = categoryService.deleteCategory(category);

        Assert.assertTrue("category's banners exist!", categoryBannersIds.isEmpty());
        Assert.assertTrue("category wasn't deleted!", categoryRepository.getOne(category.getId()).isDeleted());
    }

    @Test
    public void testNotEmptyCategoryDeletion(){
        Category category = new Category();
        category.setDeleted(false);
        category.setName("TestCategory");
        category.setReqName("SomeReqName");
        categoryRepository.save(category);


        Banner banner = new Banner();
        banner.setName("SomeBannerName");
        banner.setCategory(category);
        banner.setContent("SomeBannerContent");
        banner.setDeleted(false);
        banner.setPrice(BigDecimal.valueOf(12.6d));

        bannerRepository.save(banner);

        categoryService.deleteCategory(category);

        Set<Long> categoryBannersIds = categoryService.deleteCategory(category);

        Assert.assertEquals("category's banners are missing (or there are extra)!", categoryBannersIds.size(), 1);
        Assert.assertTrue("wrong banners' ids!", categoryBannersIds.contains(banner.getId()));
        Assert.assertFalse("category was deleted!", categoryRepository.getOne(category.getId()).isDeleted());
    }
}



