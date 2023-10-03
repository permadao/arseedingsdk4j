package com.github.permadao.arseedingsdk.util;

import com.github.permadao.model.bundle.Tag;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shiwen.wy
 * @date 2023/10/3 23:17
 */
public class TagUtils {

    public static byte[] serializeTags(List<Tag> tags) throws IOException {
        if (tags.isEmpty()) {
            return new byte[0];
        }

        // Define Avro schema
        String schemaJson = "{\"type\": \"array\", \"items\": {\"type\": \"record\", \"name\": \"Tag\", \"fields\": [{\"name\": \"name\", \"type\": \"string\"}, {\"name\": \"value\", \"type\": \"string\"}]}}";
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(schemaJson);

        // Create Avro records
        List<GenericRecord> avroRecords = new ArrayList<>();
        for (Tag tag : tags) {
            GenericRecord avroRecord = new GenericData.Record(schema);
            avroRecord.put("name", tag.getName());
            avroRecord.put("value", tag.getValue());
            avroRecords.add(avroRecord);
        }

        // Serialize Avro records to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
        DatumWriter<GenericRecord> datumWriter = new org.apache.avro.generic.GenericDatumWriter<>(schema);
        for (GenericRecord avroRecord : avroRecords) {
            datumWriter.write(avroRecord, encoder);
        }
        encoder.flush();
        byteArrayOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }
}
