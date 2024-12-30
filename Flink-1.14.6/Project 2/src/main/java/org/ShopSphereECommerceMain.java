package org.ShopSphere;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple3;

public class ShopSphereECommerceMain {
public static void main(String[] args) throws Exception {
        // Khởi tạo môi trường thực thi Flink
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // Kiểm tra hệ điều hành và đặt đường dẫn cho input/output
        String inputPath;
        String outputPath;

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            inputPath = "C:/data/ShopSphere_UK_Data.csv"; // Đường dẫn cho Windows
            outputPath = "C:/Users/Alvin Nguyen/Documents/output.csv"; // Đường dẫn cho Windows
        } else {
            inputPath = "/mnt/c/data/ShopSphere_UK_Data.csv"; // Đường dẫn cho Linux
            outputPath = "/mnt/c/data/output.csv"; // Đường dẫn cho Linux
        }

        // Đọc dữ liệu từ CSV
        DataSet<Tuple3<String, String, Double>> parsedData = readCSV(inputPath, env);

        // Tính toán doanh thu theo thành phố và danh mục sản phẩm
        DataSet<Tuple3<String, String, Double>> aggregatedData = calculateRevenue(parsedData);

        // Ghi kết quả vào file CSV
        writeCSV(aggregatedData, outputPath);

        // Thực thi Flink job
        env.execute("ShopSphere E-Commerce Revenue Calculation");
    }
}
