/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nixis.commons.web.flash.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nico
 */
public class Utils {
    
    public static String FLASH_MESSAGES_NAME = "messages";
    public static String FLASH_ERRORS_NAME = "errors";

    /**
     * Adds a message to flash
     * @param message
     * @param args
     */
    public static void addFlashMessage(Map<String, Object> flash, String message, Object ... args) {
        addToFlashContainer(flash, message, FLASH_MESSAGES_NAME, args);
    }

    private static void addToFlashContainer(Map<String, Object> flash, String message, String containerName, Object ... args) {
        List<String> container = (List<String>) flash.get(containerName);
        if (container == null) {
            container = new ArrayList<String>();
            flash.put(containerName, container);
        }

        container.add(String.format(message, args));
    }

    /**
     * Adds a error to flash
     * 
     * @param error
     * @param args
     */
    public static void addFlashError(Map<String, Object> flash, String message, Object ... args) {
        addToFlashContainer(flash, message, FLASH_ERRORS_NAME, args);
    }
}
