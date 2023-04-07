package entity;

public class Client extends User {
    private double loyaltyCardBalance;

    public Client(String name, String surname, String gender, String phone, String address, String username, String password) {
        super(name, surname, gender, phone, address, username, password);
        this.loyaltyCardBalance = 0;
    }

    public double getLoyaltyCardBalance() {
        return this.loyaltyCardBalance;
    }
    public void setLoyaltyCardBalance(double loyaltyCardBalance) {
        this.loyaltyCardBalance = loyaltyCardBalance;
    }
}
