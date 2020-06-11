import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {

    private String id;
    private String name;
    private double price;
    private String category;
    private String createdDate;
    private String updatedDate;

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.createdDate = dateFormat.format(createdDate);
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.updatedDate = dateFormat.format(updatedDate);
    }
}
