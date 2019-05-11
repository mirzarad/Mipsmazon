package sample;

/**
 * Created by mirza on 5/1/2019.
 */
public class ProductsModelTable{
    private String product_name, manufacturer, rating, price;

    public ProductsModelTable(String product_name, String manufacturer, String rating, String price){
        this.product_name = product_name;
        this.manufacturer = manufacturer;
        this.rating = rating;
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }



    public void setPrice(String price) {
        this.price = price;
    }

}
