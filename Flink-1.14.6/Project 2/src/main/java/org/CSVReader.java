package org.shopsphere;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.common.functions.MapFunction;

public class CSVReader {
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
