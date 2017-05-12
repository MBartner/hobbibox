package hobbibox.hobbibox.nonUI;

import java.util.ArrayList;
import java.util.List;

// Class for manipulating a User object
public class User {
    private String email;
    private String phoneNumber;
    private List<String> interests;
    private int subscriptionValue;
    private String ship_fname;
    private String ship_lname;
    private String ship_add1;
    private String ship_add2;
    private String ship_city;
    private String ship_state;
    private String ship_country;
    private String ship_zipcode;
    private String bill_fname;
    private String bill_lname;
    private String bill_add1;
    private String bill_add2;
    private String bill_city;
    private String bill_state;
    private String bill_country;
    private String bill_zipcode;
    private List<String> history;
    private List<String> queue;
    private int cardType;
    private String cardCompany;
    private String nameOnCard;
    private String cardNumber;
    private String securityCode;
    private int expirationDateMonth;
    private int expirationDateYear;
    private int verified;

    public User(){
        history = new ArrayList<String>();
        queue = new ArrayList<String>();
        interests = new ArrayList<String>();
        email = "";
        ship_fname = "";
        ship_lname = "";
        ship_add1 = "";
        ship_add2 = "";
        ship_city = "";
        ship_state = "";
        ship_country = "";
        ship_zipcode = "";
        bill_fname = "";
        bill_lname = "";
        bill_add1 = "";
        bill_add2 = "";
        bill_city = "";
        bill_state = "";
        bill_country = "";
        bill_zipcode = "";
        history.add("Empty");
        phoneNumber = "";
        interests.add("Empty");
        subscriptionValue = 0;
        queue.add("Empty");
        cardType = 0;
        cardCompany = "";
        nameOnCard = "";
        cardNumber = "";
        securityCode = "";
        expirationDateMonth = 0;
        expirationDateYear = 0;
        verified = 0;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public void setSubscriptionValue(int subscriptionValue) {
        this.subscriptionValue = subscriptionValue;
    }

    public void setShip_fname(String ship_fname) {
        this.ship_fname = ship_fname;
    }

    public void setShip_lname(String ship_lname) {
        this.ship_lname = ship_lname;
    }

    public void setShip_add1(String ship_add1) {
        this.ship_add1 = ship_add1;
    }

    public void setShip_add2(String ship_add2) {
        this.ship_add2 = ship_add2;
    }

    public void setShip_city(String ship_city) {
        this.ship_city = ship_city;
    }

    public void setShip_state(String ship_state) {
        this.ship_state = ship_state;
    }

    public void setShip_country(String ship_country) {
        this.ship_country = ship_country;
    }

    public void setShip_zipcode(String ship_zipcode) {
        this.ship_zipcode = ship_zipcode;
    }

    public void setBill_fname(String bill_fname) {
        this.bill_fname = bill_fname;
    }

    public void setBill_lname(String bill_lname) {
        this.bill_lname = bill_lname;
    }

    public void setBill_add1(String bill_add1) {
        this.bill_add1 = bill_add1;
    }

    public void setBill_add2(String bill_add2) {
        this.bill_add2 = bill_add2;
    }

    public void setBill_city(String bill_city) {
        this.bill_city = bill_city;
    }

    public void setBill_state(String bill_state) {
        this.bill_state = bill_state;
    }

    public void setBill_country(String bill_country) {
        this.bill_country = bill_country;
    }

    public void setBill_zipcode(String bill_zipcode) {
        this.bill_zipcode = bill_zipcode;
    }

    public void addToHistory(String str) {
        history.add(str);
    }

    public void setHistory(List<String> list){
        history = list;
    }

    public void setQueue(List<String> list){
        queue = list;
    }

    public void addToQueue(String str) {
        if(!queue.contains(str)) {
            queue.add(str);
        }
    }

    public void addToQueue(int index, String str) {
        if(queue.contains(str)){
            queue.remove(str);
        }
        queue.add(index, str);
    }

    public void deleteFromHistory(String str) {
        history.remove(str);
    }

    public void deleteFromQueue(String str) {
        queue.remove(str);
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public void setCardCompany(String cardCompany) {
        this.cardCompany = cardCompany;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public void setExpirationDateMonth(int expirationDateMonth) {
        this.expirationDateMonth = expirationDateMonth;
    }

    public void setExpirationDateYear(int expirationDateYear) {
        this.expirationDateYear = expirationDateYear;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void addInterest(String str){
        interests.add(str);
    }

    public void deleteInterest(String str){
        interests.remove(str);
    }

    public int getSubscriptionValue() {
        return subscriptionValue;
    }

    public String getShip_fname() {
        return ship_fname;
    }

    public String getShip_lname() {
        return ship_lname;
    }

    public String getShip_add1() {
        return ship_add1;
    }

    public String getShip_add2() {
        return ship_add2;
    }

    public String getShip_city() {
        return ship_city;
    }

    public String getShip_state() {
        return ship_state;
    }

    public String getShip_country() {
        return ship_country;
    }

    public String getShip_zipcode() {
        return ship_zipcode;
    }

    public String getBill_fname() {
        return bill_fname;
    }

    public String getBill_lname() {
        return bill_lname;
    }

    public String getBill_add1() {
        return bill_add1;
    }

    public String getBill_add2() {
        return bill_add2;
    }

    public String getBill_city() {
        return bill_city;
    }

    public String getBill_state() {
        return bill_state;
    }

    public String getBill_country() {
        return bill_country;
    }

    public String getBill_zipcode() {
        return bill_zipcode;
    }

    public List<String> getHistory() {
        return history;
    }

    public List<String> getQueue() {
        return queue;
    }

    public int getCardType() {
        return cardType;
    }

    public String getCardCompany() {
        return cardCompany;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public int getExpirationDateMonth() {
        return expirationDateMonth;
    }

    public int getExpirationDateYear() {
        return expirationDateYear;
    }

    public int getVerified() {
        return verified;
    }
}