package de.nixis.commons.web;

/**
 *
 * @author Dummdoedel
 */
public class ParameterNotFoundException extends IllegalArgumentException {
    public ParameterNotFoundException() {
        super();
    }

    public ParameterNotFoundException(String message) {
        super(message);
    }
}
