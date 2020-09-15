package banners.dto;

import java.math.BigDecimal;

public class BannerDto {
    public final String name;
    public final String content;
    public final BigDecimal price;
    public final Long id;

    public BannerDto(String name, String content, BigDecimal price, Long id) {
        this.name = name;
        this.content = content;
        this.price = price;
        this.id = id;
    }
}
