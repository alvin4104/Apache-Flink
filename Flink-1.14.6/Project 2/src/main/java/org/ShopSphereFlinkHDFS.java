package org.ShopSphereFlinkHDFS;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.core.fs.FileSystem.WriteMode;

public class ShopSphereFlinkHDFS {
    public static void main(String[] args) {
        try {
            // Khởi tạo môi trường Flink
            ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
            env.setParallelism(4); // Điều chỉnh số lượng parallelism phù hợp với môi trường của bạn

            String hdfsInputPath = "hdfs://localhost:9000/user/alvin/ShopSphere/input/ShopSphere_UK_Data.csv";
            String hdfsOutputPath = "hdfs://localhost:9000/user/alvin/ShopSphere/output";

            // Đọc dữ liệu từ HDFS
            DataSet<String> inputData = env.readTextFile(hdfsInputPath);

            // Xử lý dữ liệu (tách từ, đếm số lần xuất hiện của mỗi từ)
            DataSet<Tuple2<String, Integer>> wordCounts = inputData
                    .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                        @Override
                        public void flatMap(String line, Collector<Tuple2<String, Integer>> out) {
                            // Tách dòng thành các từ và gửi ra ngoài
                            String[] words = line.split("\\s+");
                            for (String word : words) {
                                out.collect(new Tuple2<>(word, 1)); // Thu thập từ và số lần xuất hiện (1)
                            }
                        }
                    })
                    .groupBy(0) // Nhóm theo từ (field 0)
                    .sum(1); // Tính tổng số lần xuất hiện của mỗi từ

            // Ghi kết quả ra HDFS
            wordCounts.writeAsText(hdfsOutputPath, WriteMode.OVERWRITE);

            // Thực thi chương trình Flink
            env.execute("ShopSphere Flink-HDFS Word Count");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
