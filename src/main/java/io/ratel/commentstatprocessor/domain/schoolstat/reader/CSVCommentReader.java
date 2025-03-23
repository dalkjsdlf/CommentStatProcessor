package io.ratel.commentstatprocessor.domain.schoolstat.reader;

import com.opencsv.CSVReader;
import io.ratel.commentstatprocessor.common.exception.CommentProcessorErrorCode;
import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
            throw new CommentProcessorException(CommentProcessorErrorCode.CSV_READ_FAILED, e);
        }

        return comments;
    }
}
