package org.metadatacenter.fairware.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateInstanceContentExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UnsupportedEncodingException;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class MetadataContentExtractorTest {

  private MapBasedMetadataContentExtractor mapBasedMetadataContentExtractor = new MapBasedMetadataContentExtractor();
  private CedarTemplateInstanceContentExtractor cedarTemplateInstanceContentExtractor = new CedarTemplateInstanceContentExtractor();
  private MetadataContentExtractor metadataContentExtractor =
      new MetadataContentExtractor(mapBasedMetadataContentExtractor, cedarTemplateInstanceContentExtractor);

  @Test
  void testExtractMetadataFieldsInfoFlatStrings() throws UnsupportedEncodingException {
    final Map<String, Object> metadataRecord = new HashMap<>();
    metadataRecord.put("f1", "v1");
    metadataRecord.put("f2", "v2");

    List<MetadataFieldInfo> fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(2, fieldsInfo.size());

    MetadataFieldInfo f1Info = MetadataFieldInfo.create("f1", null, ImmutableList.of(), "v1", null);
    MetadataFieldInfo f2Info = MetadataFieldInfo.create("f2", null, ImmutableList.of(), "v2", null);
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
  }

  @Test
  void testExtractMetadataFieldsInfoFlatNumeric() throws UnsupportedEncodingException {
    final Map<String, Object> metadataRecord = new HashMap<>();
    metadataRecord.put("f1", 1);
    metadataRecord.put("f2", 2);

    List<MetadataFieldInfo> fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(2, fieldsInfo.size());

    MetadataFieldInfo f1Info = MetadataFieldInfo.create("f1", null, ImmutableList.of(), 1, null);
    MetadataFieldInfo f2Info = MetadataFieldInfo.create("f2", null, ImmutableList.of(), 2, null);
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
  }

  @Test
  void testExtractMetadataFieldsInfoFlatArrays() throws UnsupportedEncodingException {
    final Map<String, Object> metadataRecord = new HashMap<>();
    metadataRecord.put("f1", Arrays.asList("v1", "v2"));
    metadataRecord.put("f2", Arrays.asList(1, 2));
    metadataRecord.put("f3", Arrays.asList(0.1, 0.2));

    List<MetadataFieldInfo> fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(3, fieldsInfo.size());

    MetadataFieldInfo f1Info = MetadataFieldInfo.create("f1", null, ImmutableList.of(), Arrays.asList("v1", "v2"), null);
    MetadataFieldInfo f2Info = MetadataFieldInfo.create("f2", null, ImmutableList.of(), Arrays.asList(1, 2), null);
    MetadataFieldInfo f3Info = MetadataFieldInfo.create("f3", null, ImmutableList.of(), Arrays.asList(0.1, 0.2), null);
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
    Assertions.assertTrue(fieldsInfo.contains(f3Info));
  }

  @Test
  @Disabled
  void testExtractMetadataFieldsInfoArrayOfObjects() throws UnsupportedEncodingException {
    /*
    {
      "f0": "v0",
      "arrayOfObjects": [{"f1": "v11", "f2": "v12"}, {"f1": "v21", "f2": "v22"}]
    }
    */
    final Map<String, Object> arrayObject1 = new HashMap<>();
    arrayObject1.put("f1", "v11");
    arrayObject1.put("f2", "v12");
    final Map<String, Object> arrayObject2 = new HashMap<>();
    arrayObject2.put("f1", "v21");
    arrayObject2.put("f2", "v22");
    List<Map<String,Object>> arrayOfObjects = new ArrayList<>();
    arrayOfObjects.add(arrayObject1);
    arrayOfObjects.add(arrayObject2);

    final Map<String, Object> metadataRecord = new HashMap<>();
    metadataRecord.put("f0", "v0");
    metadataRecord.put("arrayOfObjects", arrayOfObjects);


    List<MetadataFieldInfo> fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(5, fieldsInfo.size());

    MetadataFieldInfo f0Info = MetadataFieldInfo.create("f0", null, ImmutableList.of(), "v0", null);
    MetadataFieldInfo f1Info = MetadataFieldInfo.create("f1", null, ImmutableList.of("arrayOfObjects"), "v11", null);
    MetadataFieldInfo f2Info = MetadataFieldInfo.create("f2", null, ImmutableList.of("arrayOfObjects"), "v12", null);
    MetadataFieldInfo f3Info = MetadataFieldInfo.create("f1", null, ImmutableList.of("arrayOfObjects"), "v21", null);
    MetadataFieldInfo f4Info = MetadataFieldInfo.create("f2", null, ImmutableList.of("arrayOfObjects"), "v22", null);
    Assertions.assertTrue(fieldsInfo.contains(f0Info));
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
    Assertions.assertTrue(fieldsInfo.contains(f3Info));
    Assertions.assertTrue(fieldsInfo.contains(f4Info));
  }

  @Test
  void testExtractMetadataFieldsInfoNestedFields() throws UnsupportedEncodingException {
    final Map<String, Object> nestedMap = new HashMap<>();
    nestedMap.put("f2", "v3");
    nestedMap.put("f3", 4);
    nestedMap.put("f4", 0.5);
    nestedMap.put("f5", Lists.newArrayList(3,4));
    final Map<String, Object> metadataRecord = new HashMap<>();
    metadataRecord.put("f1", Lists.newArrayList("v1", "v2"));
    metadataRecord.put("nestedFields", nestedMap);

    List<MetadataFieldInfo> fieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(5, fieldsInfo.size());

    MetadataFieldInfo f1Info = MetadataFieldInfo.create("f1", null, ImmutableList.of(), Lists.newArrayList("v1", "v2"), null);
    MetadataFieldInfo f2Info = MetadataFieldInfo.create("f2", null, ImmutableList.of("nestedFields"), "v3", null);
    MetadataFieldInfo f3Info = MetadataFieldInfo.create("f3", null, ImmutableList.of("nestedFields"), 4, null);
    MetadataFieldInfo f4Info = MetadataFieldInfo.create("f4", null, ImmutableList.of("nestedFields"), 0.5, null);
    MetadataFieldInfo f5Info = MetadataFieldInfo.create("f5", null, ImmutableList.of("nestedFields"), Lists.newArrayList(3, 4), null);
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
    Assertions.assertTrue(fieldsInfo.contains(f3Info));
    Assertions.assertTrue(fieldsInfo.contains(f4Info));
    Assertions.assertTrue(fieldsInfo.contains(f5Info));
  }

}
