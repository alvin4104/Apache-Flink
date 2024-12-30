package org.ShopSphere;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.core.fs.FileSystem;

import java.util.logging.*;

public class ShopSphereMain {

    private static final Logger logger = Logger.getLogger(ShopSphereMain.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("error.log", true); // Ghi vào file log
            logger.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Đọc CSV từ đường dẫn
    public static DataSet<Tuple3<String, String, Double>> readCSV(String inputPath, ExecutionEnvironment env) throws Exception {
        // Đọc CSV từ đường dẫn và bỏ qua dòng tiêu đề
        DataSet<String> rawData = env.readTextFile(inputPath);

        // Phân tích và trả về dữ liệu đã chuyển đổi
        return rawData
                .filter(value -> !value.trim().startsWith("TransactionId")) // Bỏ qua dòng tiêu đề (hoặc sửa lại tên cột)
                .map(new MapFunction<String, Tuple3<String, String, Double>>() {
                    @Override
                    public Tuple3<String, String, Double> map(String value) {
                        String[] fields = value.split(",");
                        String category = fields[4];  // Danh mục sản phẩm
                        String city = fields[9];      // Thành phố

                        // Kiểm tra giá trị hợp lệ cho price và quantity
                        double revenue = 0;
                        try {
                            double price = Double.parseDouble(fields[6]);  // Giá sản phẩm
                            int quantity = Integer.parseInt(fields[5]);    // Số lượng
                            revenue = price * quantity;  // Doanh thu
                        } catch (NumberFormatException e) {
                            // Xử lý lỗi nếu dữ liệu không hợp lệ và ghi vào file log
                            logger.severe("Lỗi khi xử lý dòng: " + value); // Ghi vào file log
                            System.err.println("Lỗi khi xử lý dòng: " + value); // In ra console
                        }

                        return new Tuple3<>(category, city, revenue);
                    }
                });
    }

    // Tính toán doanh thu theo thành phố và danh mục sản phẩm
    public static DataSet<Tuple3<String, String, Double>> calculateRevenue(DataSet<Tuple3<String, String, Double>> parsedData) {
        // Tính tổng doanh thu theo thành phố và danh mục sản phẩm
        return parsedData
                .groupBy(0, 1)  // Nhóm theo category (index 0) và city (index 1)
                .sum(2); // Tính tổng revenue (index 2)
    }

    // Ghi dữ liệu ra file CSV
    public static void writeCSV(DataSet<Tuple3<String, String, Double>> data, String outputPath) throws Exception {
        // Ghi dữ liệu ra file CSV
        data.writeAsCsv(outputPath, FileSystem.WriteMode.OVERWRITE);
    }

    // Hàm main để chạy chương trình
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
