package net.acegik.examples;

import java.util.regex.Pattern;

/**
 *
 * @author drupalex
 */
public class PasswordPolicyChecker {
    
    private final String[] PASSWORD_PATTERN_STRS = {
        "(?=.*[0-9])",      // a digit must occur at least once
        "(?=.*[a-z])",      // a lower case letter must occur at least once
        "(?=.*[A-Z])",      // an upper case letter must occur at least once
        "(?=.*[@#$%^&+=])", // a special character must occur at least once
        "(?=\\S+$)",        // no whitespace allowed in the entire string
    };
    
    public static final int DEFAULT_MIN_NUMBER_OF_CHARS = 6;
    
    public static final int FLAG_HAS_NO_WHITESPACE = 0x01;
    public static final int FLAG_HAS_DIGIT = 0x02;
    public static final int FLAG_HAS_LOWER_CASE = 0x04;
    public static final int FLAG_HAS_UPPER_CASE = 0x08;
    public static final int FLAG_HAS_SPECIAL_CHARS = 0x10;
    
    private int optionFlags = 1;
    private boolean hasChange = true;
    private int minNumberOfChars = 0;
    private int maxNumberOfChars = 0;
    private Pattern passwordPattern = null;
    
    public void setOptionFlags(int optionFlags) {
        if (this.optionFlags != optionFlags) {
            this.optionFlags = optionFlags;
            hasChange = true;
        }
    }
    
    public void setMinNumberOfChars(int minNumberOfChars) {
        if (this.minNumberOfChars != minNumberOfChars) {
            this.minNumberOfChars = minNumberOfChars;
            hasChange = true;
        }
    }

    public void setMaxNumberOfChars(int maxNumberOfChars) {
        if (this.maxNumberOfChars != maxNumberOfChars) {
            this.maxNumberOfChars = maxNumberOfChars;
            hasChange = true;
        }
    }
    
    public boolean verifyPassword(String password) {
        if (password == null) return false;
        return getPasswordPattern().matcher(password).matches();
    }
    
    private Pattern getPasswordPattern() {
        if (hasChange) {
            passwordPattern = rebuildPattern();
            hasChange = false;
        }
        return passwordPattern;
    }
    
    private Pattern rebuildPattern() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("^");
        
        if ((optionFlags & FLAG_HAS_DIGIT) != 0) {
            sb.append(PASSWORD_PATTERN_STRS[0]);
        }
        
        if ((optionFlags & FLAG_HAS_LOWER_CASE) != 0) {
            sb.append(PASSWORD_PATTERN_STRS[1]);
        }
        
        if ((optionFlags & FLAG_HAS_UPPER_CASE) != 0) {
            sb.append(PASSWORD_PATTERN_STRS[2]);
        }
        
        if ((optionFlags & FLAG_HAS_SPECIAL_CHARS) != 0) {
            sb.append(PASSWORD_PATTERN_STRS[3]);
        }
        
        if ((optionFlags & FLAG_HAS_NO_WHITESPACE) != 0) {
            sb.append(PASSWORD_PATTERN_STRS[4]);
        }
        
        if (minNumberOfChars <= 0) {
            minNumberOfChars = DEFAULT_MIN_NUMBER_OF_CHARS;
        }
        
        if (0 < maxNumberOfChars && maxNumberOfChars < minNumberOfChars) {
            maxNumberOfChars = minNumberOfChars;
        }
        
        sb.append(".{").append(minNumberOfChars).append(",")
                .append((maxNumberOfChars>0) ? maxNumberOfChars : "")
                .append("}")
                .append("$");
        
        System.out.println(sb.toString());
        
        return Pattern.compile(sb.toString());
    }
}
