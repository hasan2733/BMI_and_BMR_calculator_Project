package bd.edu.seu.models;

public class User {
    private String uid;
    private String name;
    private String email;
    private String gender;
    private String role; // "admin" or "user"

    // Empty constructor is REQUIRED for Firebase Realtime Database
    public User() {}

    public User(String uid, String name, String email, String gender, String role) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.role = role;
    }

    public String getUid() { return uid; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getRole() { return role; }
}