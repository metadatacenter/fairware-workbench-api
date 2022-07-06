package org.metadatacenter.fairware.api.response.recommendation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ResourceExtract {

  private static final String CEDAR_ID = "@id";
  private static final String SCHEMA_IDENTIFIER = "schema:identifier";
  private static final String SCHEMA_NAME = "schema:name";
  private static final String SCHEMA_DESCRIPTION = "schema:description";
  private static final String PAV_VERSION = "pav:version";
  private static final String BIBO_STATUS = "bibo:status";

  @Nonnull
  @JsonCreator
  public static ResourceExtract create(@Nonnull @JsonProperty(CEDAR_ID) String cedarId,
                                       @Nullable @JsonProperty(SCHEMA_IDENTIFIER) String id,
                                       @Nonnull @JsonProperty(SCHEMA_NAME) String name,
                                       @Nonnull @JsonProperty(SCHEMA_DESCRIPTION) String description,
                                       @Nonnull @JsonProperty(PAV_VERSION) String version,
                                       @Nonnull @JsonProperty(BIBO_STATUS) String status) {
    return new AutoValue_ResourceExtract(cedarId, id, name, description, version, status);
  }

  @Nonnull
  @JsonProperty(CEDAR_ID)
  public abstract String getCedarId();

  @Nullable
  @JsonProperty(SCHEMA_IDENTIFIER)
  public abstract String getId();

  @Nonnull
  @JsonProperty(SCHEMA_NAME)
  public abstract String getName();

  @Nonnull
  @JsonProperty(SCHEMA_DESCRIPTION)
  public abstract String getDescription();

  @Nonnull
  @JsonProperty(PAV_VERSION)
  public abstract String getVersion();

  @Nonnull
  @JsonProperty(BIBO_STATUS)
  public abstract String getStatus();
}
