package hobbibox.hobbibox.nonUI;

import android.content.Context;
import com.google.firebase.database.DatabaseReference;
import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.service.FirebaseService;

// Class for manipulating a box object
public class Box {

    private String boxName;
    private String company;
    private int price;
    private String description;
    private String category;
    private String imageString;
    private String boxID;

    public Box(){
        boxName = "";
        company = "";
        price = 0;
        description = "";
        category = "";
        imageString = "";
        boxID = "";
    }

    public Box(Box box){
        boxName = box.getBoxName();
        company = box.getCompany();
        price = box.getPrice();
        description = box.getDescription();
        category = box.getCategory();
        imageString = box.getImageString();
        boxID = box.getBoxID();
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getBoxID() {
        return boxID;
    }

    public void setBoxID(String boxID) {
        this.boxID = boxID;
    }

}
