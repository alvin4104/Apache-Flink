package org.Boppy;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.core.fs.FileSystem;

import java.io.File;

public class BoppySalesAnalysis {
    public static void main(String[] args) throws Exception {
        String inputPath;
        String outputPath;

        // Kiểm tra hệ điều hành
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            inputPath = "C:/data/MedicineSales.csv";
            outputPath = "C:/Users/Alvin Nguyen/Documents/output.csv";
        } else {
            inputPath = "/mnt/c/data/MedicineSales.csv";
            outputPath = "/mnt/c/data/output.csv";
        }

        // Tạo thư mục nếu chưa tồn tại
        File outputDir = new File(outputPath).getParentFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Đọc dữ liệu CSV dưới dạng stream
        DataStream<Tuple6<Integer, String, String, String, Integer, Double>> salesData = env
                .readTextFile(inputPath)
                .map(new MapFunction<String, Tuple6<Integer, String, String, String, Integer, Double>>() {
                    @Override
                    public Tuple6<Integer, String, String, String, Integer, Double> map(String value) throws Exception {
                        String[] fields = value.split(",");
                        Integer medicineId = 0;
                        Integer quantity = 0;
                        Double price = 0.0;

                        try {
                            medicineId = Integer.parseInt(fields[0]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid medicine_id: " + fields[0]);
                        }

                        String medicineName = fields[1];
                        String medicineType = fields[2];
                        String manufacturer = fields[3];

                        try {
                            quantity = Integer.parseInt(fields[4]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid quantity: " + fields[4]);
                        }

                        try {
                            price = Double.parseDouble(fields[5]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid price: " + fields[5]);
                        }

                        return new Tuple6<>(medicineId, medicineName, medicineType, manufacturer, quantity, price);
                    }
                })
                .returns(TypeInformation.of(new TypeHint<Tuple6<Integer, String, String, String, Integer, Double>>() {}));

        // Tính tổng doanh thu theo loại thuốc
        DataStream<Tuple2<String, Double>> revenuePerMedicine = salesData
                .map(new MapFunction<Tuple6<Integer, String, String, String, Integer, Double>, Tuple2<String, Double>>() {
                    @Override
                    public Tuple2<String, Double> map(Tuple6<Integer, String, String, String, Integer, Double> value) {
                        return new Tuple2<>(value.f1, value.f4 * value.f5);
                    }
                })
                .returns(TypeInformation.of(new TypeHint<Tuple2<String, Double>>() {}))
                .keyBy(0)
                .sum(1);

        revenuePerMedicine.print();

        // Ghi kết quả ra file CSV với chế độ ghi đè
        revenuePerMedicine.writeAsCsv(outputPath, FileSystem.WriteMode.OVERWRITE).setParallelism(1);

        // Kích hoạt quá trình thực thi
        env.execute("Boppy Sales Analysis");
    }
}
