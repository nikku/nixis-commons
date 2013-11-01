/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nixis.common.digester;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author nico.rehwaldt
 */
public class RandomGenerator {
    private static SecureRandom RANDOM = new SecureRandom();
    
    public static String nextRandom() {
        return new BigInteger(130, RANDOM).toString(Character.MAX_RADIX); 
    }
}
