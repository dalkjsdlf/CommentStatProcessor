package io.ratel.commentstatprocessor.module.reader;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * packageName  : io.ratel.commentstatprocessor.module
 * fileName    : CsvCommentReader
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */
public class CSVCommentReader {


    public static List<String> read(String filePath) {
        List<String> comments = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                for (String cell : row) {
                    if (cell != null && !cell.trim().isEmpty()) {
                        comments.add(cell.trim()); // 공백 제거
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("CSV 읽기 실패", e);
        }

        return comments;
    }
}
