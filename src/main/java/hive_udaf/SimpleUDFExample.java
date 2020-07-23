package hive_udaf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

class SimpleUDFExample extends UDF {
	  
	  public Text evaluate(Text input) {
	    return new Text("Hello " + input.toString());
  }
}
