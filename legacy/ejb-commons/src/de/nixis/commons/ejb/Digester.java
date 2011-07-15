package de.nixis.commons.ejb;

import org.jasypt.digest.StandardStringDigester;

/**
 * This class provides interface to encyption of files and to
 * checking of password validity.
 * 
 * @author Dummdoedel
 */
public class Digester {

    private static StandardStringDigester digester;

    static {
        digester = new StandardStringDigester();
        digester.setIterations(2);
        digester.setAlgorithm("SHA-1");
    }

    /**
     * Encrypt specified string
     * 
     * @param str
     * @return
     */
    public static String digest(String str) {
        return digester.digest(str);
    }

    /**
     * Generate random password
     * 
     * @param length
     * @return
     */
    public static String generatePassword(int length) {
        String str = Double.toString(Math.random());
        return digest(str).substring(0, length);
    }

    /**
     * Returns true if string and digest match, that is if digest ist encrypted
     * version of string.
     * 
     * @param message
     * @param digest
     * @return
     */
    public static boolean matches(String message, String digest) {
        return digester.matches(message, digest);
    }
}
