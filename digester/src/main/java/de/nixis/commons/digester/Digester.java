/*
 * Part of nixis.commons
 */
package de.nixis.commons.digester;

import org.jasypt.digest.StandardStringDigester;

/**
 * This class provides interface to produces salted encrypted strings,
 * generate hashes and check for equality of a pair of
 * (encrypted, unencrypted) strings. 
 * 
 * @author nico.rehwaldt
 */
public class Digester {
    
    private static final StandardStringDigester digester;
    private static final StandardStringDigester hashDigester;
    
    static {
        digester = new StandardStringDigester();
        digester.setIterations(4);
        digester.setAlgorithm("SHA-1");

        hashDigester = new StandardStringDigester();
        hashDigester.setAlgorithm("SHA-1");
        hashDigester.setSaltSizeBytes(0);
    }

    /**
     * Encrypt specified string. Encryption takes place using salt and stuff.
     *
     * Two strings encrypted will <b>NOT</b> result in two identical encrypted
     * strings.
     * 
     * @param str
     * @return
     */
    public static String digest(String str) {
        return digester.digest(str);
    }

    /**
     * Returns a hash of the argument.
     * 
     * @param message the basis for creating the hash
     *
     * @return hash of message
     */
    public static String hash(String message) {
        return hashDigester.digest(message);
    }
    
    /**
     * Generate random string with the specified length.
     * 
     * @param length of the string
     * @return random string with specified length
     */
    public static String generateRandom(int length) {
        String str = Double.toString(Math.random());
        return digest(str).substring(0, length);
    }

    /**
     * Returns true if string and digest match, that is if digest is encrypted
     * version of string.
     * 
     * @param message the string in unencrypted form
     * @param digest the string in encrypted form (encrypted via {@link Digest#digest(String)})
     * 
     * @return true if digest is the encrypted version of message, false
     *         otherwise
     */
    public static boolean matches(String message, String digest) {
        return digester.matches(message, digest);
    }
}
