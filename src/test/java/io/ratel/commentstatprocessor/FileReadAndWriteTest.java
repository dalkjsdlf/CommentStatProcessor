package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.module.context.SchoolStatContext;
import io.ratel.commentstatprocessor.module.constant.StatProcessorConst;
import io.ratel.commentstatprocessor.module.file.CommentFileReader;
import io.ratel.commentstatprocessor.module.file.CommentFileWriter;
import io.ratel.commentstatprocessor.module.reader.CSVCommentReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;

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
public class FileReadAndWriteTest {

    @DisplayName("[성공] 파일을 잘 읽어오는지 확인하기 위한 테스트")
    @Test
    public void givenCsvFile_whenGetReadCommentsInFile_thenSizeGreaterThan0() throws IOException {
        //given
        ClassPathResource resource = new ClassPathResource("comments.csv");
        File file = resource.getFile();
        //when
        List<String> commentsFromCsv = CSVCommentReader.read(file.getPath());

        //then
        assertThat(commentsFromCsv.size()).isGreaterThan(0);
    }

    @DisplayName("[성공] map에 잘 저장된 결과파일이 잘 write 됐는지 확인")
    @Test
    public void givenMapCount_whenWriteInTmpFolder_thenCheckFile(@TempDir Path tempDir) throws IOException {
        //given

        SchoolStatContext.put("충암고등학교");
        SchoolStatContext.put("명성중학교");
        SchoolStatContext.put("선일여자고등학교");
        SchoolStatContext.put("충암고등학교");

        String result = SchoolStatContext.statMapToString();
        Path testFile = Paths.get("src/test/tmp/" + StatProcessorConst.DEFAULT_OUTPUT_FILE_NAME);

        //when
        CommentFileWriter.writeTextFile(testFile.toFile().getPath(),result, true);

        //then
        assertTrue(Files.exists(testFile));

        List<String> resultText = CommentFileReader.readTextFileLines(testFile.toFile().getPath());
        assertThat(resultText.size()).isGreaterThan(0);
    }
}
