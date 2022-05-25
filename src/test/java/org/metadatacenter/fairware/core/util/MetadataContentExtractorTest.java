package org.metadatacenter.fairware.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateInstanceContentExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MetadataContentExtractorTest {

  private MapBasedMetadataContentExtractor mapBasedMetadataContentExtractor = new MapBasedMetadataContentExtractor();
  private CedarTemplateInstanceContentExtractor cedarTemplateInstanceContentExtractor = new CedarTemplateInstanceContentExtractor();
  private MetadataContentExtractor metadataContentExtractor =
      new MetadataContentExtractor(mapBasedMetadataContentExtractor, cedarTemplateInstanceContentExtractor);

  @Test
  public void testExtractMetadataFieldsInfoFlatStrings() {
    /*
    {
      "f1": "v1",
      "f2": "v2"
    }
     */
    final var metadataRecord = ImmutableMap.<String, Object>of(
        "f1", "v1",
        "f2", "v2");

    var fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(2, fieldsInfo.size());

    var f1Info = MetadataFieldInfo.create(
        "f1", Optional.empty(), ImmutableList.of(), Optional.of("v1"), Optional.empty());
    var f2Info = MetadataFieldInfo.create(
        "f2", Optional.empty(), ImmutableList.of(), Optional.of("v2"), Optional.empty());
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
  }

  @Test
  public void testExtractMetadataFieldsInfoFlatNumeric() {
    /*
    {
      "f1": 1,
      "f2": 2
    }
     */
    final var metadataRecord = ImmutableMap.<String, Object>of(
        "f1", 1,
        "f2", 2);

    var fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(2, fieldsInfo.size());

    var f1Info = MetadataFieldInfo.create(
        "f1", Optional.empty(), ImmutableList.of(), Optional.of(1), Optional.empty());
    var f2Info = MetadataFieldInfo.create(
        "f2", Optional.empty(), ImmutableList.of(), Optional.of(2), Optional.empty());
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
  }

  @Test
  public void testExtractMetadataFieldsInfoFlatArrays() {
    /*
    {
      "f1": ["v1", "v2"],
      "f2": [1, 2],
      "f3": [0.1, 0.2]
    }
     */
    var f1Array = ImmutableList.of("v1", "v2");
    var f2Array = ImmutableList.of(1, 2);
    var f3Array = ImmutableList.of(0.1, 0.2);
    final var metadataRecord = ImmutableMap.<String, Object>of(
        "f1", f1Array,
        "f2", f2Array,
        "f3", f3Array);

    var fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(3, fieldsInfo.size());

    var f1Info = MetadataFieldInfo.create(
        "f1", Optional.empty(), ImmutableList.of(), Optional.of(f1Array), Optional.empty());
    var f2Info = MetadataFieldInfo.create(
        "f2", Optional.empty(), ImmutableList.of(), Optional.of(f2Array), Optional.empty());
    var f3Info = MetadataFieldInfo.create(
        "f3", Optional.empty(), ImmutableList.of(), Optional.of(f3Array), Optional.empty());
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
    Assertions.assertTrue(fieldsInfo.contains(f3Info));
  }

  @Test
  public void testExtractMetadataFieldsInfoArrayOfObjects() {
    /*
    {
      "arrayOfObjects": [{"f1": "v11", "f2": "v12"}, {"f1": "v21", "f2": "v22"}]
    }
    */
    final var object1 = ImmutableMap.of(
        "f1", "v11",
        "f2", "v12");
    final var object2 = ImmutableMap.of(
        "f1", "v21",
        "f2", "v22");
    final var arrayOfObjects = ImmutableList.of(object1, object2);
    final var metadataRecord = ImmutableMap.<String, Object>of(
        "arrayOfObjects", arrayOfObjects);

    var fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(4, fieldsInfo.size());

    var f1Info = MetadataFieldInfo.create(
        "f1", Optional.empty(), ImmutableList.of("arrayOfObjects"), Optional.of("v11"), Optional.empty());
    var f2Info = MetadataFieldInfo.create(
        "f2", Optional.empty(), ImmutableList.of("arrayOfObjects"), Optional.of("v12"), Optional.empty());
    var f3Info = MetadataFieldInfo.create(
        "f1", Optional.empty(), ImmutableList.of("arrayOfObjects"), Optional.of("v21"), Optional.empty());
    var f4Info = MetadataFieldInfo.create(
        "f2", Optional.empty(), ImmutableList.of("arrayOfObjects"), Optional.of("v22"), Optional.empty());
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
    Assertions.assertTrue(fieldsInfo.contains(f3Info));
    Assertions.assertTrue(fieldsInfo.contains(f4Info));
  }

  @Test
  public void testExtractMetadataFieldsInfoNestedFields() {
    /*
    {
      "nestedField": {
        "f1": {
          "f2": "v1",
          "f3": 1,
          "f4": 0.5
        },
        "f5": ["v2", "v3"]
      }
    }
     */
    final var f5Array = ImmutableList.of("v2", "v3");
    final var f1Object = ImmutableMap.<String, Object>of(
        "f2", "v1",
        "f3", 1,
        "f4", 0.5);
    final var nestedFieldObject = ImmutableMap.<String, Object>of(
        "f1", f1Object,
        "f5", f5Array);
    final var metadataRecord = ImmutableMap.<String, Object>of(
        "nestedField", nestedFieldObject);

    var fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(4, fieldsInfo.size());

    var f2Info = MetadataFieldInfo.create(
        "f2", Optional.empty(), ImmutableList.of("nestedField", "f1"), Optional.of("v1"), Optional.empty());
    var f3Info = MetadataFieldInfo.create(
        "f3", Optional.empty(), ImmutableList.of("nestedField", "f1"), Optional.of(1), Optional.empty());
    var f4Info = MetadataFieldInfo.create(
        "f4", Optional.empty(), ImmutableList.of("nestedField", "f1"), Optional.of(0.5), Optional.empty());
    var f5Info = MetadataFieldInfo.create(
        "f5", Optional.empty(), ImmutableList.of("nestedField"), Optional.of(f5Array), Optional.empty());
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
    Assertions.assertTrue(fieldsInfo.contains(f3Info));
    Assertions.assertTrue(fieldsInfo.contains(f4Info));
    Assertions.assertTrue(fieldsInfo.contains(f5Info));
  }

  @Test
  public void testExtractMetadataFieldsWithNullValues() {
    /*
    {
      "f1": null,
      "f2": {
        "f3": null
      }
    }
    */
    final var f2Object = ImmutableMap.of(
        "f3", Optional.empty());
    final var metadataRecord = ImmutableMap.<String, Object>of(
        "f1", Optional.empty(),
        "f2", f2Object);

    var fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(2, fieldsInfo.size());

    var f1Info = MetadataFieldInfo.create(
        "f1", Optional.empty(), ImmutableList.of(), Optional.empty(), Optional.empty());
    var f2Info = MetadataFieldInfo.create(
        "f3", Optional.empty(), ImmutableList.of("f2"), Optional.empty(), Optional.empty());
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
  }

  @Test
  public void testExtractMetadataFieldsWithEmptyValues() {
    /*
    {
      "f1": "",
      "f2": {
        "f3": ""
      }
    }
    */
    final var f2Object = ImmutableMap.of(
        "f3", "");
    final var metadataRecord = ImmutableMap.<String, Object>of(
        "f1", "",
        "f2", f2Object);

    var fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(2, fieldsInfo.size());

    var f1Info = MetadataFieldInfo.create(
        "f1", Optional.empty(), ImmutableList.of(), Optional.of(""), Optional.empty());
    var f2Info = MetadataFieldInfo.create(
        "f3", Optional.empty(), ImmutableList.of("f2"), Optional.of(""), Optional.empty());
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
  }
}
