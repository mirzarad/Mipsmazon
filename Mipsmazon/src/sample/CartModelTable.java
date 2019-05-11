package sample;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mirza on 5/3/2019.
 */
public class CartModelTable{
    private String product_name, manufacturer, rating, price;

    public CartModelTable(String product_name, String manufacturer, String rating, String price){
        this.product_name = product_name;
        this.manufacturer = manufacturer;
        this.rating = rating;
        this.price = price;
    }

    public String getCartProduct_name() {
        return product_name;
    }

    public void setCartProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCartManufacturer() {
        return manufacturer;
    }

    public void setCartManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCartRating() {
        return rating;
    }

    public void setCartRating(String rating) {
        this.rating = rating;
    }

    public String getCartPrice() {
        return price;
    }

    public void setCartPrice(String price) {
        this.price = price;
    }

}
