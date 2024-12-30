package org.ShopSphereKafkaProducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ShopSphereKafkaProducer {
    public static void main(String[] args) {
        // Cấu hình Kafka Producer
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "kafkahost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Kiểm tra hệ điều hành và thiết lập đường dẫn tệp
        String inputPath;
        String outputPath;

        // Lựa chọn đường dẫn theo hệ điều hành
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            inputPath = "C:/data/ShopSphere_UK_Data.csv"; // Đường dẫn cho Windows
            outputPath = "C:/Users/Alvin Nguyen/Documents/output.csv"; // Đường dẫn cho Windows
        } else {
            inputPath = "/mnt/c/data/ShopSphere_UK_Data.csv"; // Đường dẫn cho Linux (WSL)
            outputPath = "/mnt/c/data/output.csv"; // Đường dẫn cho Linux (WSL)
        }

        // Kiểm tra sự tồn tại của tệp
        java.io.File csvFile = new java.io.File(inputPath);
        if (!csvFile.exists() || !csvFile.isFile()) {
            System.out.println("Lỗi: File không tồn tại hoặc không hợp lệ: " + inputPath);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Gửi mỗi dòng dữ liệu vào Kafka
                producer.send(new ProducerRecord<>("shop-sphere-topic", line));
                System.out.println("Sent: " + line);
            }
        } catch (IOException e) {
            System.err.println("Đã xảy ra lỗi khi đọc file CSV: " + e.getMessage());
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
