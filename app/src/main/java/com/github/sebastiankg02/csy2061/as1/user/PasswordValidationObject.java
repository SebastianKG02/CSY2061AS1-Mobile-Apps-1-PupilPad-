package com.github.sebastiankg02.csy2061.as1.user;

public class PasswordValidationObject {
    public boolean containsUppercase;
    public boolean containsLowercase;
    public boolean containsNumber;
    public boolean containsMinimumChars;
    private boolean isPasswordValid;

    public PasswordValidationObject(boolean hasUppercase, boolean hasLowercase, boolean hasNumber, boolean hasMinChars) {
        this.containsUppercase = hasUppercase;
        this.containsLowercase = hasLowercase;
        this.containsNumber = hasNumber;
        this.containsMinimumChars = hasMinChars;
    }

    public PasswordValidationObject() {
        this(false, false, false, false);
    }

    public boolean setPasswordAsValid() {
        return setPasswordValid(true);
    }

    public boolean setPasswordAsInvalid() {
        return setPasswordValid(false);
    }

    public boolean isPasswordValid() {
        if (containsUppercase && containsLowercase && containsNumber && containsMinimumChars) {
            return setPasswordAsValid();
        } else {
            return setPasswordAsInvalid();
        }
    }

    private boolean setPasswordValid(boolean passwordState) {
        return (isPasswordValid = passwordState);
    }
}
