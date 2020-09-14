import banners.Main;
import banners.db.BannerRepository;
import banners.db.CategoryRepository;
import banners.db.RequestRepository;
import banners.dto.ClientData;
import banners.model.Banner;
import banners.model.Category;
import banners.model.Request;
import banners.services.BannerService;
import banners.services.CategoryService;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
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

    @Autowired
    private BannerService bannerService;


    @Test
    public void testBannerInsertion() {
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
    public void testRequestInsertion() {
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
    public void testEmptyCategoryDeletion() {
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
    public void testNotEmptyCategoryDeletion() {
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

    @Test
    public void testCategorySearch() {
        Category firstCategory = new Category();
        firstCategory.setDeleted(false);
        firstCategory.setName("TestCategory");
        firstCategory.setReqName("SomeReqName");
        categoryRepository.save(firstCategory);

        Category secondCategory = new Category();
        secondCategory.setDeleted(false);
        secondCategory.setName("SecondTestCategory");
        secondCategory.setReqName("SomeReqName2");
        categoryRepository.save(secondCategory);

        Set<Category> categories = categoryService.getCategoriesByName("second");

        Assert.assertEquals("Wrong number of found categories!", categories.size(), 1);
        Assert.assertTrue("wrong category found!", categories.contains(secondCategory));

    }

    @Test
    public void testBannerSearch() {
        Category firstCategory = new Category();
        firstCategory.setDeleted(false);
        firstCategory.setName("TestCategory");
        firstCategory.setReqName("SomeReqName");
        categoryService.saveCategory(firstCategory);

        Category secondCategory = new Category();
        secondCategory.setDeleted(false);
        secondCategory.setName("SecondTestCategory");
        secondCategory.setReqName("SomeReqName2");
        categoryService.saveCategory(secondCategory);

        Banner banner = new Banner();
        banner.setName("SomeBannerName");
        banner.setCategory(firstCategory);
        banner.setContent("SomeBannerContent");
        banner.setDeleted(false);
        banner.setPrice(BigDecimal.valueOf(12.6d));
        bannerService.saveBanner(banner);

        Banner secondBanner = new Banner();
        secondBanner.setName("SecondBannerName");
        secondBanner.setCategory(secondCategory);
        secondBanner.setContent("SecondBannerContent");
        secondBanner.setDeleted(false);
        secondBanner.setPrice(BigDecimal.valueOf(32.6d));
        bannerService.saveBanner(secondBanner);

        Banner thirdBanner = new Banner();
        thirdBanner.setName("ThirdBannerName");
        thirdBanner.setCategory(secondCategory);
        thirdBanner.setContent("ThirdBannerContent");
        thirdBanner.setDeleted(false);
        thirdBanner.setPrice(BigDecimal.valueOf(22.8d));
        bannerService.saveBanner(thirdBanner);

        Set<Banner> foundBanners = bannerService.getBannersByName("Third");

        Assert.assertEquals("Wrong number of found banners!", foundBanners.size(), 1);
        Assert.assertTrue("Wrong banner found!", foundBanners.contains(thirdBanner));

        ClientData clientData = new ClientData("11", "22");
        Banner bannerWithMaxPriceInCategory = bannerService.getMostExpensiveBannerInCategory(secondCategory, clientData).get();

        Assert.assertEquals("Wrong banner found! (expected the most expensive in category)", bannerWithMaxPriceInCategory.getId(), secondBanner.getId());
    }

    @Test
    public void testAlreadyShownBanner(){
        Category firstCategory = new Category();
        firstCategory.setDeleted(false);
        firstCategory.setName("TestCategory");
        firstCategory.setReqName("SomeReqName");
        categoryService.saveCategory(firstCategory);

        Banner firstBanner = new Banner();
        firstBanner.setName("SomeBannerName");
        firstBanner.setCategory(firstCategory);
        firstBanner.setContent("SomeBannerContent");
        firstBanner.setDeleted(false);
        firstBanner.setPrice(BigDecimal.valueOf(12.6d));
        bannerService.saveBanner(firstBanner);

        Banner secondBanner = new Banner();
        secondBanner.setName("SecondBannerName");
        secondBanner.setCategory(firstCategory);
        secondBanner.setContent("SecondBannerContent");
        secondBanner.setDeleted(false);
        secondBanner.setPrice(BigDecimal.valueOf(32.6d));
        bannerService.saveBanner(secondBanner);

        ClientData clientData = new ClientData("11", "22");
        Banner firstReadBanner = bannerService.getMostExpensiveBannerInCategory(firstCategory, clientData).get();
        Banner secondReadBanner = bannerService.getMostExpensiveBannerInCategory(firstCategory, clientData).get();
        Optional<Banner> thirdReadBanner = bannerService.getMostExpensiveBannerInCategory(firstCategory, clientData);

        Assert.assertEquals("Wrong banner shown!", firstReadBanner, secondBanner);
        Assert.assertEquals("Wrong banner shown!", secondReadBanner, firstBanner);
        Assert.assertFalse("Must not be available banners!", thirdReadBanner.isPresent());

    }
}



