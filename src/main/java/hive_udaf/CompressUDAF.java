package hive_udaf;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.io.Text;

import location_search.*;

public class CompressUDAF extends UDAF{
    
    public static class CompressUDAFEvaluator extends Compressor implements UDAFEvaluator {

        public static class Column {
            List<Long> timestamps = new ArrayList<Long>();
        }

        private Column col = null;
        
        public CompressUDAFEvaluator() {
            super();
            init();
        }

        public void init() {
            // TODO Auto-generated method stub
            col = new Column();
        }
        
        public boolean iterate(String value) throws HiveException {
            if (col == null)
                throw new HiveException("Item is not initialized");
            
            long timestamp = Long.parseLong(value);
            col.timestamps.add(timestamp);
            
            return true;
        }
        
        public boolean merge(Column other) {
            if(other == null)
                return true;
            
            col.timestamps.addAll(other.timestamps);
            return true;
        }
        
        public Column terminatePartial() {
            return col;
        }
        
        public Text terminate() throws CharacterCodingException {
            long firstTime = col.timestamps.get(0);
            //return compressTimestamps(col.timestamps, firstTime);
            return new Text(compressTimestamps(col.timestamps, firstTime));
        }
    }

}
