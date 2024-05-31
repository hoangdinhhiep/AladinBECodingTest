package com.tatoeba;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ImportDb {
    public static void main(String[] args) throws SQLException {
//        String url = "jdbc:postgresql://localhost:5432/tatoeba_db";
        String url = args[0];
        Properties props = new Properties();
//        String username = "postgres";
        String username = args[1];
        props.setProperty("user", username);
//        String password = "123qwe";
        String password = args[2];
        props.setProperty("password", password);
        props.setProperty("reWriteBatchedInserts", "true");

        Connection conn = DriverManager.getConnection(url, props);
        conn.setSchema("public");

        Statement statement = conn.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS eng_vie_translation(id BIGSERIAL primary key, eng_id int, eng_text text, eng_audio_url varchar, vie_id int, vie_text text)");
        if (!statement.isClosed()) {
            statement.close();
        }

        PreparedStatement insert = conn.prepareStatement("insert into eng_vie_translation(eng_id,eng_text,eng_audio_url,vie_id,vie_text) values(?,?,?,?,?)");
//        String inputFilePath = "/home/hoangdinh/projects/aladin_becodingtest/result.csv";
        String inputFilePath = args[3];
        readCsvFile(inputFilePath, split -> {
            try {
                try {
                    insert.setInt(1, Integer.parseInt(split[0]));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                insert.setString(2, split[1]);
                String audioUrl = split[2];
                if (!"\\N".equals(audioUrl)) {
                    insert.setString(3, audioUrl);
                } else {
                    insert.setString(3, null);
                }
                try {
                    insert.setInt(4, Integer.parseInt(split[3]));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                insert.setString(5, split[4]);

                insert.addBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        insert.executeBatch();
        insert.closeOnCompletion();

        while (insert.isCloseOnCompletion() && !conn.isClosed()) {
            conn.close();
        }
    }

    private static void readCsvFile(String filePath, Consumer<String[]> consumer) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.forEach(line -> {
                String[] split = line.split("\t");
                consumer.accept(split);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
