package org.metadatacenter.fairware.core.util;

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

    MetadataFieldInfo f1Info = new MetadataFieldInfo("f1", null, new ArrayList<>(), "v1", null);
    MetadataFieldInfo f2Info = new MetadataFieldInfo("f2", null, new ArrayList<>(), "v2", null);
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

    MetadataFieldInfo f1Info = new MetadataFieldInfo("f1", null, new ArrayList<>(), 1, null);
    MetadataFieldInfo f2Info = new MetadataFieldInfo("f2", null, new ArrayList<>(), 2, null);
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

    MetadataFieldInfo f1Info = new MetadataFieldInfo("f1", null, new ArrayList<>(), Arrays.asList("v1", "v2"), null);
    MetadataFieldInfo f2Info = new MetadataFieldInfo("f2", null, new ArrayList<>(), Arrays.asList(1, 2), null);
    MetadataFieldInfo f3Info = new MetadataFieldInfo("f3", null, new ArrayList<>(), Arrays.asList(0.1, 0.2), null);
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
    Assertions.assertEquals(3, fieldsInfo.size());

    MetadataFieldInfo f0Info = new MetadataFieldInfo("f0", null, new ArrayList<>(), "v0", null);
    MetadataFieldInfo f1Info = new MetadataFieldInfo("f1", null, Arrays.asList(new String[]{"arrayOfObjects"}), arrayObject1, null);
    MetadataFieldInfo f2Info = new MetadataFieldInfo("f2", null, Arrays.asList(new String[]{"arrayOfObjects"}), arrayObject2, null);
    Assertions.assertTrue(fieldsInfo.contains(f0Info));
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
  }

  // TODO: fix
//  @Test
//  void testExtractMetadataFieldsInfoNestedFields() throws UnsupportedEncodingException {
//    final Map<String, Object> nestedMap = new HashMap<>();
//    nestedMap.put("f2", "v3");
//    nestedMap.put("f3", 4);
//    nestedMap.put("f4", 0.5);
//    nestedMap.put("f5", Arrays.asList(3,4));
//    final Map<String, Object> metadataRecord = new HashMap<>();
//    metadataRecord.put("f1", Arrays.asList("v1", "v2"));
//    metadataRecord.put("nestedFields", nestedMap);
//
//    List<MetadataFieldInfo> fieldsInfo = MetadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
//    Assertions.assertEquals(5, fieldsInfo.size());
//
//    MetadataFieldInfo f1Info = new MetadataFieldInfo("f1", new ArrayList<>());
//    MetadataFieldInfo f2Info = new MetadataFieldInfo("f2", new ArrayList<>(Collections.singletonList("nestedFields")));
//    MetadataFieldInfo f3Info = new MetadataFieldInfo("f3", new ArrayList<>(Collections.singletonList("nestedFields")));
//    MetadataFieldInfo f4Info = new MetadataFieldInfo("f4", new ArrayList<>(Collections.singletonList("nestedFields")));
//    MetadataFieldInfo f5Info = new MetadataFieldInfo("f5", new ArrayList<>(Collections.singletonList("nestedFields")));
//    Assertions.assertTrue(fieldsInfo.contains(f1Info));
//    Assertions.assertTrue(fieldsInfo.contains(f2Info));
//    Assertions.assertTrue(fieldsInfo.contains(f3Info));
//    Assertions.assertTrue(fieldsInfo.contains(f4Info));
//    Assertions.assertTrue(fieldsInfo.contains(f5Info));
//  }

}
