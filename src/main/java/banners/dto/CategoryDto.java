package banners.dto;

public class CategoryDto {
    public final Long id;
    public final String name;
    public final String reqName;

    public CategoryDto(Long id, String name, String reqName) {
        this.id = id;
        this.name = name;
        this.reqName = reqName;
    }
}
