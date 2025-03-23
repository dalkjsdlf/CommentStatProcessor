package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import io.ratel.commentstatprocessor.common.constant.AppConst;
import io.ratel.commentstatprocessor.common.file.FileReader;
import io.ratel.commentstatprocessor.common.file.FileWriter;
import io.ratel.commentstatprocessor.domain.schoolstat.reader.CSVCommentReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * packageName  : io.ratel.commentstatprocessor
 * fileName    : FileReadTest
 * author      : dorris
 * date        : 2025. 3. 22.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 22.          dorris             최초생성
 */
@DisplayName("파일을 잘 읽어오는지 확인하기 위한 테스트")
@Slf4j
public class FileReadAndWriteTest {

    @DisplayName("[성공] 파일을 잘 읽어오는지 확인하기 위한 테스트")
    @Test
    public void givenCsvFile_whenGetReadCommentsInFile_thenSizeGreaterThan0() throws IOException {
        //given
        String inputFilePath = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INPUT_FILE_NAME;
        log.debug("inputFilePath >> {} " , inputFilePath);
        File file = Path.of(inputFilePath).toFile();
        //when
        List<String> commentsFromCsv = CSVCommentReader.read(file.getPath());

        //then
        assertThat(commentsFromCsv.size()).isGreaterThan(0);
    }

    @DisplayName("[성공] map에 잘 저장된 결과파일이 잘 write 됐는지 확인")
    @Test
    public void givenMapCount_whenWriteInTmpFolder_thenCheckFile(@TempDir Path tempDir) throws IOException {
        //given
        String outputFilePath = AppConst.DEFAULT_RESULT_DIR + File.separator + AppConst.DEFAULT_OUTPUT_FILE_NAME;
        SchoolStatResultStore.put("충암고등학교");
        SchoolStatResultStore.put("명성중학교");
        SchoolStatResultStore.put("선일여자고등학교");
        SchoolStatResultStore.put("충암고등학교");

        String result = SchoolStatResultStore.statMapToString();
        Path testFile = Paths.get(outputFilePath);

        //when
        FileWriter.writeTextFile(testFile.toFile().getPath(),result, true);

        //then
        assertTrue(Files.exists(testFile));

        List<String> resultText = FileReader.readTextFileLines(testFile.toFile().getPath());
        assertThat(resultText.size()).isGreaterThan(0);
    }
}
