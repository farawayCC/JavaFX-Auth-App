package sample;

import sample.Utils.PasswordAuthentication;

public class MyUser {
    private final String username;
    private final String pass;

    public MyUser(String username, String pass) {
        this.username=username;
        this.pass=pass;
    }

    public String getUsername() {
        return username.toLowerCase();
    }


    // --- Password hashing section ---

    // Implemented here to ensure that the same settings for PasswordAuthentication() used
    public boolean checkCredentials(String passToken) {
        PasswordAuthentication passAuth = new PasswordAuthentication();
        return passAuth.authenticate(pass.toCharArray(), passToken);
    }

    public String hashPass() {
        PasswordAuthentication passAuth = new PasswordAuthentication();
        return passAuth.hash(pass.toCharArray());
    }


}
