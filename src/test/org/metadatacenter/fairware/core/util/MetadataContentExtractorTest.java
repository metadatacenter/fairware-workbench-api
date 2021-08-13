package org.metadatacenter.fairware.core.util;

import org.junit.jupiter.api.*;
import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;

import java.util.*;

public class MetadataContentExtractorTest {

  @BeforeAll
  static void setup() {
  }

  @BeforeEach
  void setupThis() {
  }

  @Test
  void testExtractMetadataFieldsInfoFlatStrings() {
    final Map<String, Object> metadataRecord = new HashMap<>();
    metadataRecord.put("f1", "v1");
    metadataRecord.put("f2", "v2");

    List<MetadataFieldInfo> fieldsInfo = MetadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(2, fieldsInfo.size());

    MetadataFieldInfo f1Info = new MetadataFieldInfo("f1", new ArrayList<>());
    MetadataFieldInfo f2Info = new MetadataFieldInfo("f2", new ArrayList<>());
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
  }

  @Test
  void testExtractMetadataFieldsInfoFlatNumeric() {
    final Map<String, Object> metadataRecord = new HashMap<>();
    metadataRecord.put("f1", 1);
    metadataRecord.put("f2", 2);

    List<MetadataFieldInfo> fieldsInfo = MetadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(2, fieldsInfo.size());

    MetadataFieldInfo f1Info = new MetadataFieldInfo("f1", new ArrayList<>());
    MetadataFieldInfo f2Info = new MetadataFieldInfo("f2", new ArrayList<>());
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
  }

  @Test
  void testExtractMetadataFieldsInfoFlatArrays() {
    final Map<String, Object> metadataRecord = new HashMap<>();
    metadataRecord.put("f1", Arrays.asList(new String[]{"v1", "v2"}));
    metadataRecord.put("f2", Arrays.asList(new Integer[]{1, 2}));
    metadataRecord.put("f3", Arrays.asList(new Double[]{0.1, 0.2}));

    List<MetadataFieldInfo> fieldsInfo = MetadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(3, fieldsInfo.size());

    MetadataFieldInfo f1Info = new MetadataFieldInfo("f1", new ArrayList<>());
    MetadataFieldInfo f2Info = new MetadataFieldInfo("f2", new ArrayList<>());
    MetadataFieldInfo f3Info = new MetadataFieldInfo("f3", new ArrayList<>());
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
    Assertions.assertTrue(fieldsInfo.contains(f3Info));
  }

  @Test
  void testExtractMetadataFieldsInfoArrayOfObjects() {

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


    List<MetadataFieldInfo> fieldsInfo = MetadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(3, fieldsInfo.size());

    MetadataFieldInfo f0Info = new MetadataFieldInfo("f0", new ArrayList<>());
    MetadataFieldInfo f1Info = new MetadataFieldInfo("f1", new ArrayList<>(Arrays.asList("arrayOfObjects")));
    MetadataFieldInfo f2Info = new MetadataFieldInfo("f2", new ArrayList<>(Arrays.asList("arrayOfObjects")));
    Assertions.assertTrue(fieldsInfo.contains(f0Info));
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
  }

  @Test
  void testExtractMetadataFieldsInfoNestedFields() {
    final Map<String, Object> nestedMap = new HashMap<>();
    nestedMap.put("f2", "v3");
    nestedMap.put("f3", 4);
    nestedMap.put("f4", 0.5);
    nestedMap.put("f5", Arrays.asList(new Integer[]{3,4}));
    final Map<String, Object> metadataRecord = new HashMap<>();
    metadataRecord.put("f1", Arrays.asList(new String[]{"v1", "v2"}));
    metadataRecord.put("nestedFields", nestedMap);

    List<MetadataFieldInfo> fieldsInfo = MetadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);
    Assertions.assertEquals(5, fieldsInfo.size());

    MetadataFieldInfo f1Info = new MetadataFieldInfo("f1", new ArrayList<>());
    MetadataFieldInfo f2Info = new MetadataFieldInfo("f2", new ArrayList<>(Arrays.asList("nestedFields")));
    MetadataFieldInfo f3Info = new MetadataFieldInfo("f3", new ArrayList<>(Arrays.asList("nestedFields")));
    MetadataFieldInfo f4Info = new MetadataFieldInfo("f4", new ArrayList<>(Arrays.asList("nestedFields")));
    MetadataFieldInfo f5Info = new MetadataFieldInfo("f5", new ArrayList<>(Arrays.asList("nestedFields")));
    Assertions.assertTrue(fieldsInfo.contains(f1Info));
    Assertions.assertTrue(fieldsInfo.contains(f2Info));
    Assertions.assertTrue(fieldsInfo.contains(f3Info));
    Assertions.assertTrue(fieldsInfo.contains(f4Info));
    Assertions.assertTrue(fieldsInfo.contains(f5Info));
  }

  @AfterEach
  void tearThis() {
  }

  @AfterAll
  static void tear() {
  }


}
