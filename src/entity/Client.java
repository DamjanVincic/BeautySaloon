package entity;

public class Client extends User {
    private boolean loyaltyCard;

    public Client(String name, String surname, String gender, String phone, String address, String username, String password) {
        super(Role.CLIENT, name, surname, gender, phone, address, username, password);
        this.loyaltyCard = false;
    }
    public Client(int id, String name, String surname, String gender, String phone, String address, String username, String password) {
        super(id, Role.CLIENT, name, surname, gender, phone, address, username, password);
        this.loyaltyCard = false;
    }
    public Client(int id, String name, String surname, String gender, String phone, String address, String username, String password, boolean loyaltyCard) {
        super(id, Role.CLIENT, name, surname, gender, phone, address, username, password);
        this.loyaltyCard = loyaltyCard;
    }

    public boolean hasLoyaltyCard() {
        return this.loyaltyCard;
    }
    public void setLoyaltyCard(boolean loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
    }

    @Override
    public String toFileString() {
        return super.toFileString() + "," + this.loyaltyCard;
    }
}
