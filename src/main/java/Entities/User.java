package Entities;

public class User extends Person {
    private Boolean adminAccess;

    public User(String name, String surname, Boolean adminAccess) {
        super(name, surname);
        this.adminAccess = adminAccess;
    }

    public Boolean getAdminAccess() {
        return adminAccess;
    }
    public void setAdminAccess(Boolean adminAccess) {
        this.adminAccess = adminAccess;
    }
}




