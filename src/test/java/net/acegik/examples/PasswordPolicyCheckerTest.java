package net.acegik.examples;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PasswordPolicyCheckerTest {

    private PasswordPolicyChecker checker;
    
    @Before
    public void startup() {
        checker = new PasswordPolicyChecker();
    }
    
    @Test
    public void test_min_number_of_characters() {
        // case: minNumberOfChars = 8
        checker.setMinNumberOfChars(8);
        Assert.assertTrue(checker.validate("12345678"));
        Assert.assertTrue(checker.validate("123456789"));
        Assert.assertFalse(checker.validate("1234567"));
        
        String seed = "this-string-will-be-clone-and-concat-to-it--";
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<100; i++) {
            sb.append(seed);
        }
        Assert.assertTrue(checker.validate(sb.toString()));
        
        // case: maxNumberOfChars = 16
        checker.setMaxNumberOfChars(16);
        Assert.assertTrue(checker.validate("123456789abcdef0"));
        Assert.assertFalse(checker.validate("123456789abcdef0x"));
    }
    
    @Test
    public void test_default_checker() {
        String[] sample_data = new String[] {
            "abcdef",               // at least 6 characters 
            "verylongpassword-will-be-clone-and-concat-to-this-string---", 
            "anycharacter-without-whitespace-#@$%^&*()-_+=~<>/?:;|{}[]"};

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<100; i++) {
            sb.append(sample_data[1]);
        }
        sample_data[1] = sb.toString();
        
        for(String sample:sample_data) {
            Assert.assertTrue(checker.validate(sample));
        }
        
        Assert.assertFalse(checker.validate("abcde"));
        Assert.assertFalse(checker.validate("abcde fghij"));
    }
}
