package com.github.sebastiankg02.csy2061.as1.user;

/**
 * Simple class used exclusively to hold date about user password validation
 */
public class PasswordValidationObject {
    //BEGIN Password flags -------------------- BEGIN
    public boolean containsUppercase;
    public boolean containsLowercase;
    public boolean containsNumber;
    public boolean containsMinimumChars;
    private boolean isPasswordValid;
    //END Password flags ----------------------- END

    //Full constructor
    public PasswordValidationObject(boolean hasUppercase, boolean hasLowercase, boolean hasNumber, boolean hasMinChars) {
        this.containsUppercase = hasUppercase;
        this.containsLowercase = hasLowercase;
        this.containsNumber = hasNumber;
        this.containsMinimumChars = hasMinChars;
    }

    //Confirm password validity
    public boolean isPasswordValid() {
        //If all flags checked and valid, return successful validation
        if (containsUppercase && containsLowercase && containsNumber && containsMinimumChars) {
            return (isPasswordValid = true);
        } else {
            return (isPasswordValid = false);
        }
    }
}
