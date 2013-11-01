package de.nixis.common.web.flash.helpers;

import java.util.ArrayList;
import java.util.List;

import de.nixis.common.web.flash.Flash;

/**
 *
 * @author nico.rehwaldt
 */
public class FlashMessages {

  public static String FLASH_MESSAGES_NAME = "messages";

  public static String FLASH_ERRORS_NAME = "errors";

  /**
   * Adds a message to flash
   *
   * @param message
   * @param args
   */
  public static void addMessage(Flash flash, String message, Object... args) {
    addElement(flash, message, FLASH_MESSAGES_NAME, args);
  }

  private static void addElement(Flash flash, String message, String containerName, Object... args) {
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
  public static void addError(Flash flash, String message, Object... args) {
    addElement(flash, message, FLASH_ERRORS_NAME, args);
  }
}
