package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.metadatacenter.fairware.core.domain.CedarTemplateFieldSpecification;
import org.metadatacenter.fairware.core.domain.CedarTemplateField;
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
    var fieldSpecification = mock(CedarTemplateFieldSpecification.class);
    var valueField = CedarTemplateField.ofValueField(fieldSpecification);
    assertThat(valueField.getKind(), equalTo(CedarTemplateField.Kind.VALUE_FIELD));
  }

  @Test
  public void shouldCreateObjectField() {
    var fieldSpecification = mock(CedarTemplateFieldSpecification.class);
    var objectField = CedarTemplateField.ofObjectField(fieldSpecification);
    assertThat(objectField.getKind(), equalTo(CedarTemplateField.Kind.OBJECT_FIELD));
  }

  @Test
  public void shouldPrintJsonPath() {
    var personFieldSpecification = mock(CedarTemplateFieldSpecification.class);
    when(personFieldSpecification.getName()).thenReturn("person");
    var personField = CedarTemplateField.ofObjectField(personFieldSpecification);

    var fullNameFieldSpecification = mock(CedarTemplateFieldSpecification.class);
    when(fullNameFieldSpecification.getName()).thenReturn("fullName");
    when(fullNameFieldSpecification.getParentField()).thenReturn(Optional.of(personField));
    var fullNameField = CedarTemplateField.ofValueField(fullNameFieldSpecification);

    var homeAddressFieldSpecification = mock(CedarTemplateFieldSpecification.class);
    when(homeAddressFieldSpecification.getName()).thenReturn("homeAddress");
    when(homeAddressFieldSpecification.getParentField()).thenReturn(Optional.of(personField));
    var homeAddressField = CedarTemplateField.ofObjectField(homeAddressFieldSpecification);

    var streetFieldSpecification = mock(CedarTemplateFieldSpecification.class);
    when(streetFieldSpecification.getName()).thenReturn("street");
    when(streetFieldSpecification.getParentField()).thenReturn(Optional.of(homeAddressField));
    var streetField = CedarTemplateField.ofValueField(streetFieldSpecification);

    var cityFieldSpecification = mock(CedarTemplateFieldSpecification.class);
    when(cityFieldSpecification.getName()).thenReturn("city");
    when(cityFieldSpecification.getParentField()).thenReturn(Optional.of(homeAddressField));
    var cityField = CedarTemplateField.ofValueField(cityFieldSpecification);

    assertThat(personField.getJsonPath(), equalTo("person"));
    assertThat(fullNameField.getJsonPath(), equalTo("person.fullName"));
    assertThat(homeAddressField.getJsonPath(), equalTo("person.homeAddress"));
    assertThat(streetField.getJsonPath(), equalTo("person.homeAddress.street"));
    assertThat(cityField.getJsonPath(), equalTo("person.homeAddress.city"));
  }
}