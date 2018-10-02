package org.businessanddecisions.common;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomPasswordEncoder {
    private String hashedPassword;



    private CustomPasswordEncoder(String hashedPassword) {
        this.hashedPassword = hashedPassword;

    }

    public static CustomPasswordEncoder hash(String password) {
         BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
         String crypted = bcryptEncoder.encode(password);


        return new CustomPasswordEncoder(crypted);
    }

    public static boolean checkEquals(String clientPassword,String databasePassword,String salt) {
         String hashed = BCrypt.hashpw(clientPassword,salt);

         return hashed.equals(databasePassword);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
