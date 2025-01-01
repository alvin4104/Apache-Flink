package org.ShopSphereFlinkHDFS;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.common.functions.MapFunction;
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

            // Xử lý dữ liệu (chuyển toàn bộ ký tự thành chữ hoa)
            inputData
                    .map(new MapFunction<String, String>() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public String map(String value) {
                            return value.toUpperCase();
                        }
                    })
                    .rebalance() // Phân phối lại các phân vùng của dữ liệu
                    .setParallelism(8) // Thiết lập mức độ parallelism cho quá trình xử lý
                    .writeAsText(hdfsOutputPath, WriteMode.OVERWRITE) // Ghi kết quả ra HDFS
                    .setParallelism(8); // Cũng có thể thiết lập parallelism cho quá trình ghi

            // Thực thi chương trình Flink
            env.execute("ShopSphere Flink-HDFS Integration");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
