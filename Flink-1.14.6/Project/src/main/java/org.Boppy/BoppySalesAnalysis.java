package org.Boppy;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple6;

public class BoppySalesAnalysis {
    public static void main(String[] args) throws Exception {
        // Tạo Execution Environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // Đọc dữ liệu từ CSV
        DataSet<Tuple6<Integer, String, String, String, Integer, Double>> salesData = env
                .readCsvFile("MedicineSales.csv")
                .ignoreFirstLine()
                .types(Integer.class, String.class, String.class, String.class, Integer.class, Double.class);

        // Tính tổng doanh thu theo từng loại thuốc
        DataSet<Tuple2<String, Double>> revenuePerMedicine = salesData
                .map((MapFunction<Tuple6<Integer, String, String, String, Integer, Double>, Tuple2<String, Double>>) value ->
                        new Tuple2<>(value.f1, value.f4 * value.f5))
                .groupBy(0)
                .reduce((ReduceFunction<Tuple2<String, Double>>) (value1, value2) ->
                        new Tuple2<>(value1.f0, value1.f1 + value2.f1));

        // In ra kết quả
        revenuePerMedicine.print();
    }
}
