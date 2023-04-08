package entity;

public abstract class User {
    private String name, surname, gender, phone, address, username, password;

    public User(String name, String surname, String gender, String phone, String address, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return String.format("Username: %s, Name: %s, Surname: %s, Gender: %s, Phone Number: %s, Address: %s", this.getUsername(), this.getName(), this.getSurname(), this.getGender(), this.getPhone(), this.getAddress());
    }

    public String toFileString() {
        return this.name + "," + this.surname + "," + this.gender + "," + this.phone + "," + this.address + "," + this.username + "," + this.password;
    }
}
