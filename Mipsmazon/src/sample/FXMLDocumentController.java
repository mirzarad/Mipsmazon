package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.fxml.Initializable;
import java.awt.event.ActionEvent;
import java.net.InterfaceAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.awt.SystemColor.text;
import static sample.Main.conn;

/**
 * Created by mirza on 5/1/2019.
 */

public class FXMLDocumentController implements Initializable {
    @FXML
    private ListView<String> manufacturers_table;


    @FXML
    private ImageView mipsmazon_logo;

    @FXML
    private Button confirm_order_button;

    @FXML
    private Label popular_products_label;

    @FXML
    private Label manufacturers_label;

    @FXML
    private Label cart_label;

    @FXML
    private Label address_street_label;

    @FXML
    private Label address_zip_label;

    @FXML
    private Label customer_name_label;

    @FXML
    private Label address_city_label;

    @FXML
    private Label login_label;

    @FXML
    private TextField customerID_textfield;

    @FXML
    private Label customerID_label;

    @FXML
    private Label customerID_current_label;

    @FXML
    private Button add_to_cart_button_popular_products;

    @FXML
    private Button add_to_cart_button_products;

    @FXML
    private Button remove_from_cart_button;

    @FXML
    private Button checkout_button;

    @FXML
    private Label checkout_summary_label;

    @FXML
    private Label customer_name_db_label;

    @FXML
    private Label address_street_db_label;

    @FXML
    private Label address_zip_db_label;

    @FXML
    private Label address_city_db_label;

    @FXML
    private Label total_label;

    @FXML
    private Label dollarsign_label;

    @FXML
    private Label total_from_cart_label;

    @FXML
    private Label order_confiorm_notice_label;

    @FXML
    private Button go_back_button;

    @FXML
    private Label products_label;

    @FXML
    private Label login_error_label;

    @FXML
    private Button login_button;

    @FXML
    private Button update;

    @FXML
    private Button view_all_manufacturers;

    @FXML
    private Label enter_card_info_label;

    @FXML
    private TextField card_info_textfield;

    @FXML
    void login() {
        System.out.println("hello");
        boolean foundCustId = false;
        try {
            int id = Integer.parseInt(customerID_textfield.getText());
            login_error_label.setText("");

            try{
                System.out.print("Popular:");
                //Connection con = DBConnector.getConnection();
                ResultSet rs = conn.createStatement().executeQuery("SELECT Customer_ID FROM customer"); // Double Check this
                ResultSet rs5;
                while(rs.next()){
                    if(rs.getString(1).equals(Integer.toString(id))) {
                        foundCustId = true;
                        break;
                    }
                }
                if(!foundCustId) {
                    login_error_label.setText("Error: That CustomerID does not exist");
                    return;
                }

                //IF WE MADE IT THIS FAR. NOW WE ARE GOING TO LOAD THE ITEMS INTO THE CART APPROPRIATELY. AND ENABLE FUNCTIONALITY FOR OUR OTHER BUTTONS
                //THIS IS BECAUSE WE CANNOT ADD ITEMS TO OUR CART UNTIL WE HAVE LOADED A CUSTOMERID/LOGGED IN SO OUR CHANGES CAN BE REFLECTED IN MYSQL
                add_to_cart_button_popular_products.setDisable(false);
                update.setDisable(false);
                view_all_manufacturers.setDisable(false);
                add_to_cart_button_products.setDisable(false);
                remove_from_cart_button.setDisable(false);
                checkout_button.setDisable(false);
                //WE START BY CLEARING THE CART FIRST.
                //Id is id
                cart_tableview.getItems().clear();
                int cartTotal = 0;

                //rs = conn.createStatement().executeQuery("SELECT Customer_ID FROM customer"); // Double Check this
                //SELECT Product_Names from cart WHERE CustomerId = 5
                rs = conn.createStatement().executeQuery(("SELECT Product_Names from cart WHERE CustomerId =" + Integer.toString(id)));
                rs5 = conn.createStatement().executeQuery("SELECT custName, Address_street, Address_city, Address_zip FROM customer WHERE Customer_ID =" + Integer.toString(id));
                if(rs5.next()) {
                    customer_name_db_label.setText(rs5.getString("custName"));
                    address_street_db_label.setText(rs5.getString("Address_street"));
                    address_zip_db_label.setText(rs5.getString("Address_zip"));
                    address_city_db_label.setText(rs5.getString("Address_city"));

                }
                if(!rs.next()) {
                    System.out.println("empty");
                    login_error_label.setText("Success logging in! Please note the cart is empty");
                }
                else {
                    System.out.println("About to Query ");
                    String productList = rs.getString("Product_Names");
                    System.out.println(productList);
                    System.out.println("About to Query 2");
                    String[] currencies = productList.split(" ");
                    ObservableList<ProductsModelTable> oblistCart = FXCollections.observableArrayList();
                    for(String s : currencies) {
                        //From here, we are going use an SQL statement for each Product where the product name is each entry
                        //in the array, we will populate the entire cart tableView row, with the full product entry.

                        System.out.println("About to Query " + s);
                        //rs = conn.createStatement().executeQuery(("SELECT Product_name, Manufacturer, Rating, Price FROM Product WHERE Product.Product_name =" + s));
                        //-----------------------------------------------------------------
                        rs = conn.createStatement().executeQuery(("SELECT Product_name, Manufacturer, Rating, Price FROM Product"));
                        while(rs.next()) {
                            if(rs.getString("Product_name").equals(s)) break;
                        }

                        oblistCart.add(new ProductsModelTable(rs.getString("Product_name"), rs.getString("Manufacturer"),
                                rs.getString("Rating"), rs.getString("Price")));

                        //System.out.println("Queried " + s);
                        //-----------------------------------------------------------------------

                        /*
                        while(rs.next()){
                            oblistCart.add(new ProductsModelTable(rs.getString("Product_name"), rs.getString("Manufacturer"),
                                    rs.getString("Rating"), rs.getString("Price")));
                        }*/

                    }

                    cart_tableview.setItems(null);
                    cart_tableview.setItems(oblistCart);
                    cart_tableview.refresh();

                    add_to_cart_button_popular_products.setDisable(false);
                    update.setDisable(false);
                    view_all_manufacturers.setDisable(false);
                    add_to_cart_button_products.setDisable(false);
                    remove_from_cart_button.setDisable(false);
                    checkout_button.setDisable(false);

                }


                /*while(rs.next()){
                    //we are trying to populate our cart

                }*/
                customerID_current_label.setText(customerID_textfield.getText());

                // Update customer information


            }
            catch(SQLException ex){
                Logger.getLogger(PopularProductsModelTable.class.getName()).log(Level.SEVERE,null,ex);
            }

        } catch (NumberFormatException e) {
            System.out.println("he11ll");
            login_error_label.setText("Error in format of ID. Input must be integer characters [0-9]");
            return;
        }

    }

    @FXML
    void addToCartPopularProducts() {
        cart_tableview.getItems().add(popular_product_tableview.getSelectionModel().getSelectedItem());
        String cartItemToAdd = popular_product_tableview.getSelectionModel().getSelectedItem().getProduct_name();
        System.out.println(cartItemToAdd);
        int cartItemToAddPrice = (int)(Double.parseDouble(popular_product_tableview.getSelectionModel().getSelectedItem().getPrice()));

        //we also want to add that item from the ProductList to the current user's cart

        //we need cartID which we'll already have since logged in
        int customerID = Integer.parseInt(customerID_current_label.getText());


        //product name, which we clearly have
        //total cost which will need to be UPDATED
        //cartID which we'll already have
        try {
            ResultSet rs;// = conn.createStatement().executeQuery("SELECT Customer_ID FROM customer");
            rs = conn.createStatement().executeQuery("SELECT Cart_ID, Product_Names, Total_cost, CustomerId FROM cart WHERE CustomerId =" + Integer.toString(customerID));
            int newID = 0;
            if(!rs.next()) {
                //this means that there is not a cart, so instead of updating we insert a new
                boolean worked = false;
                while(!worked) {
                    try {
                        newID = (int)Math.floor(Math.random() * 9999);
                        System.out.println(Integer.toString(newID) + ",\"" + cartItemToAdd + "\"," + Integer.toString(cartItemToAddPrice) + Integer.toString(customerID) + ")");
                        conn.createStatement().executeUpdate("INSERT INTO cart(Cart_ID, Product_Names, Total_cost, CustomerId) VALUES (" + Integer.toString(newID) + ",\"" + cartItemToAdd + "\"," + Integer.toString(cartItemToAddPrice) + "," + Integer.toString(customerID) + ")");
                        worked = true;
                    }
                    catch (SQLException ex1) {
                        System.out.println("That cartID already exists, generating a new one");

                    }
                }

            }
            else {
                //this means a cart already exists, so we must update it, but by deleting and inserting
                String cartID = rs.getString("Cart_ID");
                String productNames = rs.getString("Product_Names");
                String totalCost = rs.getString("Total_cost");

                conn.createStatement().executeUpdate("DELETE FROM cart WHERE Cart_ID =" + cartID);
                productNames = productNames.concat(" " + cartItemToAdd);

                totalCost = Integer.toString((int)(Double.parseDouble(totalCost) + (double)cartItemToAddPrice));

                System.out.println(cartID + "," + productNames + "," + totalCost + "," + Integer.toString(customerID) + ")");
                conn.createStatement().executeUpdate("INSERT INTO cart(Cart_ID, Product_Names, Total_cost, CustomerId) VALUES (" + cartID + ",\"" + productNames + "\"," + totalCost + "," + Integer.toString(customerID) + ")");

            }
        }
        catch(SQLException ex){
            Logger.getLogger(PopularProductsModelTable.class.getName()).log(Level.SEVERE,null,ex);
        }

        checkout_button.setDisable(false);

    }

    @FXML
    void addToCartProducts() {
        cart_tableview.getItems().add(products_tableview.getSelectionModel().getSelectedItem());
        String cartItemToAdd = products_tableview.getSelectionModel().getSelectedItem().getProduct_name();
        System.out.println(cartItemToAdd);
        int cartItemToAddPrice = (int)(Double.parseDouble(products_tableview.getSelectionModel().getSelectedItem().getPrice()));

        //we also want to add that item from the ProductList to the current user's cart

        //we need cartID which we'll already have since logged in
        int customerID = Integer.parseInt(customerID_current_label.getText());


        //product name, which we clearly have
        //total cost which will need to be UPDATED
        //cartID which we'll already have
        try {
            ResultSet rs;// = conn.createStatement().executeQuery("SELECT Customer_ID FROM customer");
            rs = conn.createStatement().executeQuery("SELECT Cart_ID, Product_Names, Total_cost, CustomerId FROM cart WHERE CustomerId =" + Integer.toString(customerID));
            int newID = 0;
            if(!rs.next()) {
                //this means that there is not a cart, so instead of updating we insert a new
                boolean worked = false;
                while(!worked) {
                    try {
                        newID = (int)Math.floor(Math.random() * 9999);
                        System.out.println(Integer.toString(newID) + ",\"" + cartItemToAdd + "\"," + Integer.toString(cartItemToAddPrice) + Integer.toString(customerID) + ")");
                        conn.createStatement().executeUpdate("INSERT INTO cart(Cart_ID, Product_Names, Total_cost, CustomerId) VALUES (" + Integer.toString(newID) + ",\"" + cartItemToAdd + "\"," + Integer.toString(cartItemToAddPrice) + "," + Integer.toString(customerID) + ")");
                        worked = true;
                    }
                    catch (SQLException ex1) {
                        System.out.println("That cartID already exists, generating a new one");

                    }
                }

            }
            else {
                //this means a cart already exists, so we must update it, but by deleting and inserting
                String cartID = rs.getString("Cart_ID");
                String productNames = rs.getString("Product_Names");
                String totalCost = rs.getString("Total_cost");

                conn.createStatement().executeUpdate("DELETE FROM cart WHERE Cart_ID =" + cartID);
                productNames = productNames.concat(" " + cartItemToAdd);

                totalCost = Integer.toString((int)(Double.parseDouble(totalCost) + (double)cartItemToAddPrice));

                System.out.println(cartID + "," + productNames + "," + totalCost + "," + Integer.toString(customerID) + ")");
                conn.createStatement().executeUpdate("INSERT INTO cart(Cart_ID, Product_Names, Total_cost, CustomerId) VALUES (" + cartID + ",\"" + productNames + "\"," + totalCost + "," + Integer.toString(customerID) + ")");

            }
        }
        catch(SQLException ex){
            Logger.getLogger(PopularProductsModelTable.class.getName()).log(Level.SEVERE,null,ex);
        }
        checkout_button.setDisable(false);

    }

    @FXML
    void removeFromCart() {
        // Remove the current selected item the cart from the cart table
        //  String highlighted = .getProduct_name();
        // System.out.println(highlighted);
        System.out.println("ITEM SELECTED TO BE REMOVED\n");

        String cartItemToDelete = cart_tableview.getSelectionModel().getSelectedItem().getProduct_name();
        int cartItemToDeletePrice = (int)(Double.parseDouble(cart_tableview.getSelectionModel().getSelectedItem().getPrice()));
        int customerID = Integer.parseInt(customerID_current_label.getText());


        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT Cart_ID, Product_Names, Total_cost, CustomerId FROM cart WHERE CustomerId =" + Integer.toString(customerID));
            if(!rs.next()) return;
            String cartID = rs.getString("Cart_ID");
            String productNames = rs.getString("Product_Names");
            String totalCost = rs.getString("Total_cost");
            if(productNames.contains(cartItemToDelete + " ")) {
                System.out.println("here");
                productNames = productNames.replaceFirst(cartItemToDelete + " ", "");
                totalCost = Integer.toString((int)(Double.parseDouble(totalCost)) - cartItemToDeletePrice);
                conn.createStatement().executeUpdate("DELETE FROM cart WHERE Cart_ID =" + cartID);
                conn.createStatement().executeUpdate("INSERT INTO cart(Cart_ID, Product_Names, Total_cost, CustomerId) VALUES (" + cartID + ",\"" + productNames + "\"," + totalCost + "," + Integer.toString(customerID) + ")");

                cart_tableview.getItems().remove(cart_tableview.getSelectionModel().getSelectedItem());

            }
            else if(productNames.contains(" " + cartItemToDelete)) {
                System.out.println("here1");

                productNames = productNames.replaceFirst(" " + cartItemToDelete, "");
                totalCost = Integer.toString((int)(Double.parseDouble(totalCost)) - cartItemToDeletePrice);

                conn.createStatement().executeUpdate("DELETE FROM cart WHERE Cart_ID =" + cartID);
                conn.createStatement().executeUpdate("INSERT INTO cart(Cart_ID, Product_Names, Total_cost, CustomerId) VALUES (" + cartID + ",\"" + productNames + "\"," + totalCost + "," + Integer.toString(customerID) + ")");

                cart_tableview.getItems().remove(cart_tableview.getSelectionModel().getSelectedItem());


            }
            else {
                System.out.println("here2");

                //THIS MEANS THE CART ONLY HAS ONE ITEM. MAD EZ. JUST DELETE THE WHOLE CART
                conn.createStatement().executeUpdate("DELETE FROM cart WHERE Cart_ID =" + cartID);
                totalCost = Integer.toString((int)(Double.parseDouble(totalCost)) - cartItemToDeletePrice);

                cart_tableview.getItems().remove(cart_tableview.getSelectionModel().getSelectedItem());
                checkout_button.setDisable(true);

            }

        }
        catch (SQLException ex) {
            Logger.getLogger(ProductsModelTable.class.getName()).log(Level.SEVERE,null,ex);
        }

    }

    @FXML
    void checkOut() {
        double sum=0;
        for(int j=0;j<cart_tableview.getItems().size();j++){
           // System.out.println(cart_tableview.getItems().get(j).getPrice());
            sum+=Double.parseDouble(cart_tableview.getItems().get(j).getPrice());
        }
        System.out.println(sum);
        // Query to Customer's cart the current table's status
        // Enable goBackButton and Confirm Order Button
        // Disable all addToCart / removeFromCart Buttons
        // Add all of the prices into a total variable, convert it to a String and update the total_from_cart_label label.
        total_from_cart_label.setText(Double.toString(sum));

        add_to_cart_button_popular_products.setDisable(true);
        update.setDisable(true);
        view_all_manufacturers.setDisable(true);
        add_to_cart_button_products.setDisable(true);
        remove_from_cart_button.setDisable(true);
        checkout_button.setDisable(true);

        go_back_button.setDisable(false);
        confirm_order_button.setDisable(false);
    }

    @FXML
    void goBack() {
        // Enable all addToCart / removeFromCart Buttons
        // Disable goBackButton and Confirm Order Button

        add_to_cart_button_popular_products.setDisable(false);
        update.setDisable(false);
        view_all_manufacturers.setDisable(false);
        add_to_cart_button_products.setDisable(false);
        remove_from_cart_button.setDisable(false);
        checkout_button.setDisable(false);

        go_back_button.setDisable(true);
        confirm_order_button.setDisable(true);
    }

    @FXML
    void confirmOrder() {
        // Retrieve the CartItems and Query them to corder
        // Enable all addToCart / removeFromCart Buttons



        if(card_info_textfield.getText().isEmpty()) {
            order_confiorm_notice_label.setText("PLEASE ENTER A CARD NUMBER");
            return;
        }
        else if(card_info_textfield.getText().length() != 16) {
            order_confiorm_notice_label.setText("Please enter a VALID card number; a valid card has 16 digits 0-9");
            return;
        }
        else if (card_info_textfield.getText().matches("[0-9]+") && card_info_textfield.getText().length() > 2) {
            String cardnum = card_info_textfield.getText();
            int orderID;
            boolean worked = false;
            Date today = Calendar.getInstance().getTime();
            String date = today.toString();
            System.out.println(date);
            String cartid;
            String totalCost = "";
            while(!worked) {
                try {
                    ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM cart WHERE CustomerId =" + customerID_current_label.getText());
                    rs.next();
                    cartid = rs.getString("Cart_ID");
                    totalCost = rs.getString("Total_cost");


                    orderID = (int)Math.floor(Math.random() * 9999);
                    System.out.println("INSERT INTO corder(Order_number, Cardnum, custname, Cartid,To_street,To_city,To_state,To_zip,Ship_date) VALUES (" + Integer.toString(orderID) + ",\"" + cardnum + "\",\"" + customer_name_db_label.getText() + "\"," +  cartid + ",\"" + address_street_db_label.getText() + "\",\"" + address_city_db_label.getText() + "\",\"" +  "NY" + "\"," + address_zip_db_label.getText() + ",\"" + date + "\")");
                    conn.createStatement().executeUpdate("INSERT INTO corder(Order_number, Cardnum, custname, Cartid,To_street,To_city,To_state,To_zip,Ship_date) VALUES (" + Integer.toString(orderID) + ",\"" + cardnum + "\",\"" + customer_name_db_label.getText() + "\"," +  cartid + ",\"" + address_street_db_label.getText() + "\",\"" + address_city_db_label.getText() + "\",\"" +  "NY" + "\"," + address_zip_db_label.getText() + ",\"" + date + "\")");
                    worked = true;
                    cart_tableview.getItems().clear();
                    conn.createStatement().executeUpdate("DELETE FROM cart WHERE Cart_ID =" + cartid);
                }
                catch (SQLException ex1) {
                    System.out.println("That corderID already exists, generating a new one");
                    Logger.getLogger(ProductsModelTable.class.getName()).log(Level.SEVERE,null,ex1);



                }
            }

            order_confiorm_notice_label.setText("Success! Card number: " + cardnum + " was charged " + totalCost + " dollars.\n Please allow 5-7 business days for delivery.");
            add_to_cart_button_popular_products.setDisable(false);
            update.setDisable(false);
            view_all_manufacturers.setDisable(false);
            add_to_cart_button_products.setDisable(false);
            remove_from_cart_button.setDisable(false);
            checkout_button.setDisable(false);

            go_back_button.setDisable(true);
            confirm_order_button.setDisable(true);
        }
        else {
            order_confiorm_notice_label.setText("Please enter a VALID card number; a valid card has 16 digits 0-9");
            return;
        }



// enter_card_info_label;
// card_info_textfield;


        // Create SQL query for adding

    }

    @FXML
    void updateProducts() {
        // From the selected manufacturer's table, populate the products table for all items from that manufacturer
        String highlighted = manufacturers_table.getSelectionModel().getSelectedItem();
        if(highlighted != null) {
            try{
                System.out.print("Products:");
                // Connection con = DBConnector.getConnection();
                ResultSet rs2 = conn.createStatement().executeQuery("SELECT Product_name, Manufacturer, Rating, Price FROM Product");

                oblist2.clear();
                while(rs2.next()){
                    if(rs2.getString("Manufacturer").equals(highlighted) != true) continue;
                    oblist2.add(new ProductsModelTable(rs2.getString("Product_name"), rs2.getString("Manufacturer"),
                            rs2.getString("Rating"), rs2.getString("Price")));
                }
            }
            catch(SQLException ex){
                Logger.getLogger(ProductsModelTable.class.getName()).log(Level.SEVERE,null,ex);
            }
            product_products.setCellValueFactory(new PropertyValueFactory<>("Product_name"));
            manufacturer_products.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
            rating_products.setCellValueFactory(new PropertyValueFactory<>("Rating"));
            price_products.setCellValueFactory(new PropertyValueFactory<>("Price"));

            products_tableview.setItems(null);
            products_tableview.setItems(oblist2);
            products_tableview.refresh();
        }
    }

    @FXML
    void viewAllManufacturers() {
        // Just populate the products table with all the products
        try{
            System.out.print("Products:");
            // Connection con = DBConnector.getConnection();
            ResultSet rs2 = conn.createStatement().executeQuery("SELECT Product_name, Manufacturer, Rating, Price FROM Product");

            oblist2.clear();
            while(rs2.next()){
                oblist2.add(new ProductsModelTable(rs2.getString("Product_name"), rs2.getString("Manufacturer"),
                        rs2.getString("Rating"), rs2.getString("Price")));
            }
        }
        catch(SQLException ex){
            Logger.getLogger(ProductsModelTable.class.getName()).log(Level.SEVERE,null,ex);
        }

        product_products.setCellValueFactory(new PropertyValueFactory<>("Product_name"));
        manufacturer_products.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
        rating_products.setCellValueFactory(new PropertyValueFactory<>("Rating"));
        price_products.setCellValueFactory(new PropertyValueFactory<>("Price"));

        products_tableview.setItems(null);
        products_tableview.setItems(oblist2);
        products_tableview.refresh();

    }

    // -----------------------------------------------------------------------------------------------------------------
    //                                      POPULAR PRODUCTS PRODUCTS MODEL TABLE
    // -----------------------------------------------------------------------------------------------------------------
    @FXML
    private TableView<ProductsModelTable> popular_product_tableview;
    @FXML
    private TableColumn<ProductsModelTable, String> product_popularproducts;
    @FXML
    private TableColumn<ProductsModelTable, String> manufacturer_popularproducts;
    @FXML
    private TableColumn<ProductsModelTable, String> rating_popularproducts;
    @FXML
    private TableColumn<ProductsModelTable, String> price_popularprodutcs;

    private ObservableList<ProductsModelTable> oblist = FXCollections.observableArrayList();
    //private ObservableList<PopularProductsModelTable> oblist9 = FXCollections.observableArrayList();

    private ObservableList<String> manufacturersList = FXCollections.observableArrayList();


    // -----------------------------------------------------------------------------------------------------------------
    //                                             PRODUCTS MODEL TABLE
    // -----------------------------------------------------------------------------------------------------------------
    @FXML
    public TableView<ProductsModelTable> products_tableview;
    @FXML
    private TableColumn<ProductsModelTable, String> product_products;
    @FXML
    private TableColumn<ProductsModelTable, String> manufacturer_products;
    @FXML
    private TableColumn<ProductsModelTable, String> rating_products;
    @FXML
    private TableColumn<ProductsModelTable, String> price_products;

    private ObservableList<ProductsModelTable> oblist2 = FXCollections.observableArrayList();

    // -----------------------------------------------------------------------------------------------------------------
    //                                                CART MODEL TABLE
    // -----------------------------------------------------------------------------------------------------------------
    @FXML
    private TableView<ProductsModelTable> cart_tableview;
    // -----------------------------------------------------------------------------------------------------------------
    @FXML
    private TableColumn<ProductsModelTable, String> product_cart;
    @FXML
    private TableColumn<ProductsModelTable, String> manufacturer_cart;
    @FXML
    private TableColumn<ProductsModelTable, String> rating_cart;
    @FXML
    private TableColumn<ProductsModelTable, String> price_cart;

    private ObservableList<ProductsModelTable> oblist3 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb){

        // Main Code
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            System.out.print("Main:");
            //conn = (Connection) DriverManager.getConnection(url, "root","Phantomdatabase1!");
            conn = (Connection) DBConnector.getConnection();
            //ResultSet rs = conn.createStatement().executeQuery("select Product, Manufacturer, Rating, Price from product");

        }catch (SQLException e){
            System.out.println("Connection to the database failed: " + e.getMessage());
        }

        //oblist9.add(new PopularProductsModelTable("hello", "hi", "yo", "we"));

        // -------------------------------------------------------------------------------------------------------------
        //                                      POPULAR PRODUCTS PRODUCTS MODEL TABLE
        // -------------------------------------------------------------------------------------------------------------
        try{
            System.out.print("Popular:");
            //Connection con = DBConnector.getConnection();
            ResultSet rs1 = conn.createStatement().executeQuery("SELECT Product_name, Manufacturer, Rating, Price FROM Product WHERE Product.Rating >= 4.0"); // Double Check this

            while(rs1.next()){
                oblist.add(new ProductsModelTable(rs1.getString("Product_name"), rs1.getString("Manufacturer"),
                        rs1.getString("Rating"), rs1.getString("Price")));
            }
        }
        catch(SQLException ex){
            Logger.getLogger(PopularProductsModelTable.class.getName()).log(Level.SEVERE,null,ex);
        }

        product_popularproducts.setCellValueFactory(new PropertyValueFactory<>("Product_name"));
        manufacturer_popularproducts.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
        rating_popularproducts.setCellValueFactory(new PropertyValueFactory<>("Rating"));
        price_popularprodutcs.setCellValueFactory(new PropertyValueFactory<>("Price"));

        popular_product_tableview.setItems(null);
        popular_product_tableview.setItems(oblist);
        popular_product_tableview.refresh();

        // -------------------------------------------------------------------------------------------------------------
        //                                             PRODUCTS MODEL TABLE
        // -------------------------------------------------------------------------------------------------------------

        try{
            System.out.print("Products:");
            // Connection con = DBConnector.getConnection();
            ResultSet rs2 = conn.createStatement().executeQuery("SELECT Product_name, Manufacturer, Rating, Price FROM Product"); // Double Check this

            while(rs2.next()){
                oblist2.add(new ProductsModelTable(rs2.getString("Product_name"), rs2.getString("Manufacturer"),
                        rs2.getString("Rating"), rs2.getString("Price")));
            }
        }
        catch(SQLException ex){
            Logger.getLogger(ProductsModelTable.class.getName()).log(Level.SEVERE,null,ex);
        }

        product_products.setCellValueFactory(new PropertyValueFactory<>("Product_name"));
        manufacturer_products.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
        rating_products.setCellValueFactory(new PropertyValueFactory<>("Rating"));
        price_products.setCellValueFactory(new PropertyValueFactory<>("Price"));

        products_tableview.setItems(null);
        products_tableview.setItems(oblist2);
        products_tableview.refresh();


        // Just populate the products table with all the products
        try{
            System.out.print("Products:");
            // Connection con = DBConnector.getConnection();
            ResultSet rs2 = conn.createStatement().executeQuery("SELECT Product_name, Manufacturer, Rating, Price FROM Product");
            // Double Check this
            while(rs2.next()){
                if(!manufacturersList.contains(rs2.getString("Manufacturer")))
                    manufacturersList.add(rs2.getString("Manufacturer"));
            }
        }
        catch(SQLException ex){
            Logger.getLogger(ProductsModelTable.class.getName()).log(Level.SEVERE,null,ex);
        }

        product_products.setCellValueFactory(new PropertyValueFactory<>("Product_name"));
        manufacturer_products.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
        rating_products.setCellValueFactory(new PropertyValueFactory<>("Rating"));
        price_products.setCellValueFactory(new PropertyValueFactory<>("Price"));

        manufacturers_table.setItems(null);
        manufacturers_table.setItems(manufacturersList);
        manufacturers_table.refresh();




        // -----------------------------------------------------------------------------------------------------------------
        //                                                CART MODEL TABLE
        // -----------------------------------------------------------------------------------------------------------------

        try{
            System.out.print("Cart:");
            //Connection con = DBConnector.getConnection();
            ResultSet rs3 = conn.createStatement().executeQuery("SELECT Product_name, Manufacturer, Rating, Price FROM Product"); // Double Check this

            while(rs3.next()){
                oblist3.add(new ProductsModelTable(rs3.getString("Product_name"), rs3.getString("Manufacturer"),
                        rs3.getString("Rating"), rs3.getString("Price")));
            }
        }

        catch(SQLException ex) {
            Logger.getLogger(CartModelTable.class.getName()).log(Level.SEVERE, null, ex);
        }

        product_cart.setCellValueFactory(new PropertyValueFactory<>("Product_name"));
        manufacturer_cart.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
        rating_cart.setCellValueFactory(new PropertyValueFactory<>("Rating"));
        price_cart.setCellValueFactory(new PropertyValueFactory<>("Price"));

        cart_tableview.setItems(oblist3);
        cart_tableview.refresh();

        // Set all Disables for buttons:
        add_to_cart_button_popular_products.setDisable(true);
        update.setDisable(true);
        view_all_manufacturers.setDisable(true);
        add_to_cart_button_products.setDisable(true);
        remove_from_cart_button.setDisable(true);
        checkout_button.setDisable(true);
        go_back_button.setDisable(true);
        confirm_order_button.setDisable(true);

    }

}