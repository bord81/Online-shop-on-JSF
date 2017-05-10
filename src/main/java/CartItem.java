
public class CartItem {

    private int id;
    private int count;
    private String image;
    private String sImage;
    private String name;
    private String fullName;
    private String descript;
    private int totalprice;

    public CartItem(int id, int count, String image, String sImage, String name, String fullName, String descript, int totalprice) {
        this.id = id;
        this.count = count;
        this.image = image;
        this.sImage = sImage;
        this.name = name;
        this.fullName = fullName;
        this.descript = descript;
        this.totalprice = totalprice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }
}
