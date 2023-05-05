package entity;

public abstract class User {
    private static int count = 0;
    
    private int id;
    private String name, surname, gender, phone, address, username, password;
    private Role role;

    public User(int id, Role role, String name, String surname, String gender, String phone, String address, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.username = username;
        this.password = password;
        this.role = role;
        this.id = id;
    }

    public User(Role role, String name, String surname, String gender, String phone, String address, String username, String password) {
        this(0, role, name, surname, gender, phone, address, username, password);
        this.id = ++count;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Role getRole() {
        return this.role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public static void setCount(int count) {
        User.count = count;
    }


    @Override
    public String toString() {
        return String.format("Role: %s, Username: %s, Name: %s, Surname: %s, Gender: %s, Phone Number: %s, Address: %s", this.getRole(), this.getUsername(), this.getName(), this.getSurname(), this.getGender(), this.getPhone(), this.getAddress());
    }

    public String toFileString() {
        return this.id + "," + this.role + "," + this.name + "," + this.surname + "," + this.gender + "," + this.phone + "," + this.address + "," + this.username + "," + this.password;
    }
}
