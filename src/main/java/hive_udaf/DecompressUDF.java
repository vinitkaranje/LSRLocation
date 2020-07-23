package hive_udaf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.json.simple.JSONObject;

import location_search.*;
import parquet.io.api.Binary;

public class DecompressUDF extends UDF{
    
    public Text evaluate(Text compressedTimestamps) {
    	byte[] times = compressedTimestamps.getBytes();
    	long firstTime = times[0];
        Compressor cmp = new Compressor();
        Iterable<Long> values = cmp.decompressTimestamps(times, firstTime);
        JSONObject valuesObject = new JSONObject();;
        for (Long tmp : values) {
        	valuesObject.put("local_timestamp",tmp);
        }
        return new Text(valuesObject.toJSONString());
    }
    
    /*public Iterable<Long> evaluate(Binary compressedTimestamps) {
        ByteBuffer buffer = compressedTimestamps.toByteBuffer();
        byte[] bytes = buffer.array();
        long firstTime = bytes[0];
        Compressor cmp = new Compressor();
        return cmp.decompressTimestamps(bytes, firstTime);
    }
    
    public Iterable<Long> evaluate(Object compressedTimestamps) {
        byte[] bytes = (byte[]) compressedTimestamps;
        long firstTime = bytes[0];
        Compressor cmp = new Compressor();
        return cmp.decompressTimestamps(bytes, firstTime);
    }
    
    public Iterable<Long> evaluate(InputStream  compressedTimestamps) throws IOException {
        byte[] bytes = IOUtils.toByteArray(compressedTimestamps);
        long firstTime = bytes[0];
        Compressor cmp = new Compressor();
        return cmp.decompressTimestamps(bytes, firstTime);
    }*/
}
