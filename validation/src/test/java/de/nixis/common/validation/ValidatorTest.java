package de.nixis.common.validation;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Locale;
import de.nixis.common.i18n.Localization;

/**
 *
 * @author nico.rehwaldt
 */
public class ValidatorTest {

  @Test
  public void shouldRecognizeValidEntity() throws Exception {
    MyEntity e = new MyEntity("Klaus");
    ValidationResult<MyEntity> result = Validator.validate(e);

    assertThat(result.isEmpty()).isTrue();
  }

  @Test
  public void shouldRecognizeInvalidEntity() throws Exception {
    MyEntity e = new MyEntity(null);
    ValidationResult<MyEntity> result = Validator.validate(e);

    assertThat(result.isEmpty()).isFalse();
  }

  @Test
  public void shouldLocalizeMessages() throws Exception {

    HashMap<Locale, String> expectedMessages = new HashMap<Locale, String>() {{
      put(Locale.GERMAN, "darf nicht null sein");
      put(Locale.ENGLISH, "may not be null");
    }};

    try {
      for (Map.Entry<Locale, String> entry : expectedMessages.entrySet()) {
        Localization.set(entry.getKey());
        final MyEntity e = new MyEntity(null);

        ValidationResult<MyEntity> result = Validator.validate(e);
        assertThat(result.isEmpty()).isFalse();

        String message = result.getViolations().iterator().next().getMessage();

        assertThat(entry.getValue()).isEqualTo(message);
      }
    } finally {
      Localization.set(null);
    }
  }
}
