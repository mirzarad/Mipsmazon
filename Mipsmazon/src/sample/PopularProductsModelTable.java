package sample;

/**
 * Created by mirza on 5/1/2019.
 */
public class PopularProductsModelTable{
    private String product_name, manufacturer, rating, price;

    public PopularProductsModelTable(String product_name, String manufacturer, String rating, String price){
        this.product_name = product_name;
        this.manufacturer = manufacturer;
        this.rating = rating;
        this.price = price;
    }

    public String getPopularProduct_name() {
        return product_name;
    }

    public void setPopularProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPopularManufacturer() {
        return manufacturer;
    }

    public void setPopularManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPopularRating() {
        return rating;
    }

    public void setPopularRating(String rating) {
        this.rating = rating;
    }

    public String getPopularPrice() {
        return price;
    }

    public void setPopularPrice(String price) {
        this.price = price;
    }

}
