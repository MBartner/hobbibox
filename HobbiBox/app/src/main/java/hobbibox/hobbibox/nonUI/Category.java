package hobbibox.hobbibox.nonUI;

// Class for manipulating a Category Object
public class Category {
    private String name;
    private String imageString;

    public Category(){
        name = "";
        imageString = "";
    }

    public Category(Category category){
        name = category.getName();
        imageString = category.getImageString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}
