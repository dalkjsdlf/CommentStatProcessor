package io.ratel.commentstatprocessor.domain.schoolstat.store;

import com.opencsv.CSVReader;
import io.ratel.commentstatprocessor.common.constant.AppConst;
import io.ratel.commentstatprocessor.common.exception.CommentProcessorErrorCode;
import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;

import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * packageName  : io.ratel.commentstatprocessor.domain.schoolstat.store
 * fileName    : SchoolInfoLoader
 * author      : dorris
 * date        : 2025. 3. 23.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 23.          dorris             최초생성
 */
public class SchoolInfoLoader {
    public static void loadOfficialSchoolNames() {
        String csvPath = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INFO_FILE_NAME;
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> rows = reader.readAll();

            for (String[] row : rows) {
                if (row.length >= 2) {
                    String schoolName = row[1].trim();
                    if (!schoolName.isEmpty()) {
                        SchoolInfoStore.add(schoolName);
                    }
                }
            }

        } catch (Exception e) {
            throw new CommentProcessorException(CommentProcessorErrorCode.SCHOOL_INFO_LOAD_FAIL);
        }
    }
}
