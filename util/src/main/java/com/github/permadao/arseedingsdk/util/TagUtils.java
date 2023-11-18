package com.github.permadao.arseedingsdk.util;

import com.github.permadao.model.bundle.Tag;

import com.google.common.collect.Lists;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author shiwen.wy
 * @date 2023/10/3 23:17
 */
public class TagUtils {

  private static final String SCHEMA =
      "{\"type\": \"record\", \"name\": \"Tag\", \"fields\": [{\"name\": \"name\", \"type\": \"string\"}, {\"name\": \"value\", \"type\": \"string\"}]}";
  private static final String SCHEMA_ARRAY =
          "{\"type\": \"array\", \"items\": {\"type\": \"record\", \"name\": \"Tag\", \"fields\": [{\"name\": \"name\", \"type\": \"string\"}, {\"name\": \"value\", \"type\": \"string\"}]}}";

  private static final String TAG_NAME = "name";
  private static final String TAG_VALUE = "value";

  public static byte[] serializeTagsList(List<Tag> tags) throws IOException {
    if (tags == null || tags.isEmpty()) {
      return new byte[0];
    }

    // Define Avro schema
    Schema schema = new Schema.Parser().parse(SCHEMA_ARRAY);
    Schema schemaItem = new Schema.Parser().parse(SCHEMA);
    GenericData.Array<GenericRecord> recordArray = new GenericData.Array<>(tags.size(), schema);

    // Serialize Avro records to byte array
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
    DatumWriter<GenericData.Array<GenericRecord>> datumWriter =
            new org.apache.avro.generic.GenericDatumWriter<>(schema);

    for (Tag tag : tags) {
      GenericRecord avroRecord = new GenericData.Record(schemaItem);
      avroRecord.put(TAG_NAME, tag.getName());
      avroRecord.put(TAG_VALUE, tag.getValue());
      recordArray.add(avroRecord);
    }
    datumWriter.write(recordArray, encoder);
    encoder.flush();
    byteArrayOutputStream.close();

    return byteArrayOutputStream.toByteArray();
  }

  public static void main(String[] args) throws IOException {
    Schema schema = new Schema.Parser().parse(SCHEMA_ARRAY);
    Tag a1 = new Tag("A1", "123");
    byte[] bytes = serializeTagsList(Lists.newArrayList(a1));
    DatumReader<GenericData.Array<GenericRecord>> datumReader =
            new org.apache.avro.generic.GenericDatumReader<>(schema);
    BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(new ByteArrayInputStream(bytes), null);

    GenericData.Array<GenericRecord> user = null;
    while(!binaryDecoder.isEnd()){
      user = datumReader.read(user,binaryDecoder);
      System.out.println(user);
    }
  }
}
