package demo;

public class User {
    // public class user {
    //public static class demo.User {

    public String date_of_birth;
    public String full_name;
    public String nickname;

    public User(String dateOfBirth, String fullName) {
        // ...
        this.date_of_birth = dateOfBirth;
        this.full_name = fullName;

    }

    public User(String dateOfBirth, String fullName, String nickname) {
        // ...
        this.date_of_birth = dateOfBirth;
        this.full_name = fullName;
        this.nickname = nickname;
    }

}


