package banners.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "NAME")
    private String name;
    @Column(name = "REQ_NAME")
    private String reqName;
    @Column(name = "DELETED")
    private boolean deleted;

    @OneToMany(mappedBy = "category")
    private Set<Banner> banners = new HashSet<>();


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void addBanner(Banner banner){
        this.banners.add(banner);
    }

    public Set<Banner> getBanners() {
        return banners;
    }
}
