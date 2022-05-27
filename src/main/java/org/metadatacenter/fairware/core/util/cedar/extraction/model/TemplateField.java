package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import com.google.auto.value.AutoOneOf;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoOneOf(TemplateField.Kind.class)
public abstract class TemplateField {

  @Nonnull
  public static TemplateField ofValueField(@Nonnull FieldSpecification fieldSpecification) {
    return AutoOneOf_TemplateField.valueField(fieldSpecification);
  }

  @Nonnull
  public static TemplateField ofObjectField(@Nonnull FieldSpecification fieldSpecification) {
    return AutoOneOf_TemplateField.objectField(fieldSpecification);
  }

  @Nonnull
  public abstract Kind getKind();

  @Nonnull
  public abstract FieldSpecification valueField();

  @Nonnull
  public abstract FieldSpecification objectField();

  @Nonnull
  public String getName() {
    switch (getKind()) {
      case VALUE_FIELD:
        return valueField().getName();
      case OBJECT_FIELD:
        return objectField().getName();
      default:
        throw throwIllegalKindException();
    }
  }

  @Nonnull
  public Optional<String> getPrefLabel() {
    switch (getKind()) {
      case VALUE_FIELD:
        return valueField().getPrefLabel();
      case OBJECT_FIELD:
        return objectField().getPrefLabel();
      default:
        throw throwIllegalKindException();
    }
  }

  @Nonnull
  public Optional<String> getContainerName() {
    var containerName = getJsonPath().substring(0, getJsonPath().lastIndexOf(".") + 1);
    if (containerName.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(containerName);
    }
  }

  @Nonnull
  public String getJsonPath() {
    StringBuilder sb = new StringBuilder();
    var parentField = getParentField();
    if (parentField.isPresent()) {
      sb.append(parentField.get().getJsonPath()).append(".");
    }
    sb.append(getName());
    return sb.toString();
  }

  private Optional<TemplateField> getParentField() {
    switch (getKind()) {
      case VALUE_FIELD:
        return valueField().getParentField();
      case OBJECT_FIELD:
        return objectField().getParentField();
      default:
        throw throwIllegalKindException();
    }
  }

  private IllegalArgumentException throwIllegalKindException() {
    return new IllegalArgumentException("Field is neither a value nor object type");
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(getJsonPath());
    if (isRequired()) {
      sb.append("*");
    }
    return sb.toString();
  }

  private boolean isRequired() {
    switch (getKind()) {
      case VALUE_FIELD:
        return valueField().isRequired();
      case OBJECT_FIELD:
        return objectField().isRequired();
      default:
        throw throwIllegalKindException();
    }
  }

  public enum Kind {
    VALUE_FIELD,
    OBJECT_FIELD
  }
}
