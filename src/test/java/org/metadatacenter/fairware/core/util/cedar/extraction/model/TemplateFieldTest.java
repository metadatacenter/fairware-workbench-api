package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@ExtendWith(MockitoExtension.class)
class TemplateFieldTest {

  @Test
  public void shouldCreateValueField() {
    var fieldSpecification = mock(FieldSpecification.class);
    var valueField = TemplateField.ofValueField(fieldSpecification);
    assertThat(valueField.getKind(), equalTo(TemplateField.Kind.VALUE_FIELD));
  }

  @Test
  public void shouldCreateObjectField() {
    var fieldSpecification = mock(FieldSpecification.class);
    var objectField = TemplateField.ofObjectField(fieldSpecification);
    assertThat(objectField.getKind(), equalTo(TemplateField.Kind.OBJECT_FIELD));
  }

  @Test
  public void shouldPrintJsonPath() {
    var personFieldSpecification = mock(FieldSpecification.class);
    when(personFieldSpecification.getName()).thenReturn("person");
    var personField = TemplateField.ofObjectField(personFieldSpecification);

    var fullNameFieldSpecification = mock(FieldSpecification.class);
    when(fullNameFieldSpecification.getName()).thenReturn("fullName");
    when(fullNameFieldSpecification.getParentField()).thenReturn(Optional.of(personField));
    var fullNameField = TemplateField.ofValueField(fullNameFieldSpecification);

    var homeAddressFieldSpecification = mock(FieldSpecification.class);
    when(homeAddressFieldSpecification.getName()).thenReturn("homeAddress");
    when(homeAddressFieldSpecification.getParentField()).thenReturn(Optional.of(personField));
    var homeAddressField = TemplateField.ofObjectField(homeAddressFieldSpecification);

    var streetFieldSpecification = mock(FieldSpecification.class);
    when(streetFieldSpecification.getName()).thenReturn("street");
    when(streetFieldSpecification.getParentField()).thenReturn(Optional.of(homeAddressField));
    var streetField = TemplateField.ofValueField(streetFieldSpecification);

    var cityFieldSpecification = mock(FieldSpecification.class);
    when(cityFieldSpecification.getName()).thenReturn("city");
    when(cityFieldSpecification.getParentField()).thenReturn(Optional.of(homeAddressField));
    var cityField = TemplateField.ofValueField(cityFieldSpecification);

    assertThat(personField.getJsonPath(), equalTo("person"));
    assertThat(fullNameField.getJsonPath(), equalTo("person.fullName"));
    assertThat(homeAddressField.getJsonPath(), equalTo("person.homeAddress"));
    assertThat(streetField.getJsonPath(), equalTo("person.homeAddress.street"));
    assertThat(cityField.getJsonPath(), equalTo("person.homeAddress.city"));
  }
}