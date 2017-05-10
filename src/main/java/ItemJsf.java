
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ItemJsf {

    @Id
    private int id;
    private String image;
    private String sImage;
    private String name;
    private String fullName;
    private String descript;
    private int price;

    public ItemJsf() {
    }

    public ItemJsf(int id, String image, String sImage, String name, String fullName, String descript, int price) {
        this.id = id;
        this.image = image;
        this.sImage = sImage;
        this.name = name;
        this.fullName = fullName;
        this.descript = descript;
        this.price = price;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
