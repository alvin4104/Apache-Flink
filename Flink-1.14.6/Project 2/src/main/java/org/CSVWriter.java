package org.ShopSphere;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.DataSet;

public class CSVWriter {
    public static void writeCSV(DataSet<Tuple3<String, String, Double>> aggregatedData, String outputPath) throws Exception {
        // Ghi kết quả vào CSV
        aggregatedData.writeAsCsv(outputPath).setParallelism(1);
    }
}
