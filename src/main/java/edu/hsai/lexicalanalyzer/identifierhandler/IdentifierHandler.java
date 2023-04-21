package edu.hsai.lexicalanalyzer.identifierhandler;

public class IdentifierHandler {
    public static boolean isValid(String identifier) {
        return (identifier.length() <= 16) && identifier.matches("[a-zA-Z]+\\d*[a-zA-Z]*");
    }

    public static void main(String[] args) {
        System.out.println(isValid("hello"));
        System.out.println(isValid("h12ello"));
        System.out.println(isValid("123213"));
        System.out.println(isValid("12hello"));
        System.out.println(isValid("hello0123456789"));
        System.out.println(isValid("hello01234567890"));
        System.out.println(isValid("hello012345678901"));
        System.out.println(isValid("лол"));
    }
}
