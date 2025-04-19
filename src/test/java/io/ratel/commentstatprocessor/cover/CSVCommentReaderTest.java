package io.ratel.commentstatprocessor.cover;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;
import io.ratel.commentstatprocessor.domain.schoolstat.reader.CSVCommentReader;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static io.ratel.commentstatprocessor.common.exception.CommentProcessorErrorCode.COMMENT_NULL_INPUT;
import static io.ratel.commentstatprocessor.common.exception.CommentProcessorErrorCode.CSV_READ_FAILED;
import static org.assertj.core.api.Assertions.*;

/**
 * CSVCommentReaderTest
 *
 * CSV 파일로부터 댓글 목록을 올바르게 파싱하는지 검증합니다.
 *
 * ✅ 테스트 목록
 * ------------------------------------------------------------------------------
 * 1. 정상 CSV 파일에서 댓글 목록 추출
 * 2. 빈 셀, 공백 셀 무시
 * 3. 존재하지 않는 파일 → 예외 발생
 * 4. 빈 파일 → 빈 리스트
 * 5. 특수문자 포함된 댓글 → 그대로 읽힘
 * 6. 여러 줄에 걸친 CSV → 줄 단위로 읽기 가능
 * 7. 읽기 권한 없는 파일 → 예외 발생
 * ------------------------------------------------------------------------------
 */
public class CSVCommentReaderTest {

    private static final String basePath = "test/tmp/csv";

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(Path.of(basePath));
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.walk(Path.of(basePath))
                .map(Path::toFile)
                .sorted((a, b) -> -a.compareTo(b)) // 파일 먼저 삭제
                .forEach(File::delete);
    }

    @Test
    @DisplayName("정상적인 CSV 파일에서 댓글을 읽어온다")
    void givenValidCSV_whenRead_thenReturnsList() throws IOException {
        String path = basePath + "/valid.csv";
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("\"댓글1\",\"댓글2\",\"댓글3\"");
        }

        List<String> result = CSVCommentReader.readByCell(path);
        assertThat(result).containsExactly("댓글1", "댓글2", "댓글3");
    }

    @Test
    @DisplayName("빈 셀이나 공백 셀은 무시된다")
    void givenEmptyCells_whenRead_thenIgnoresThem() throws IOException {
        String path = basePath + "/empty-cells.csv";
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("\"댓글1\",\"   \",\"\",\"댓글2\"");
        }

        List<String> result = CSVCommentReader.readByCell(path);
        assertThat(result).containsExactly("댓글1", "댓글2");
    }

    @Test
    @DisplayName("[예외] 파일이 없을 경우 예외를 던진다")
    void givenInvalidPath_whenRead_thenThrowsException() {
        String path = basePath + "/no-file.csv";

        assertThatThrownBy(() -> CSVCommentReader.readByCell(path))
                .isInstanceOf(CommentProcessorException.class)
                .hasMessageContaining(CSV_READ_FAILED.getSimpleMessage())
                .hasFieldOrPropertyWithValue("errorCode", CSV_READ_FAILED);
    }

    @Test
    @DisplayName("빈 파일을 읽을 경우 빈 리스트를 반환한다")
    void givenEmptyFile_whenRead_thenReturnsEmptyList() throws IOException {
        String path = basePath + "/empty.csv";
        Files.createFile(Path.of(path));

        List<String> result = CSVCommentReader.readByCell(path);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("특수문자가 포함된 댓글도 정상적으로 읽는다")
    void givenSpecialCharComments_whenRead_thenKeepsSpecialChars() throws IOException {
        String path = basePath + "/special.csv";
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("\"창현고 화이팅! 💪\",\"서울대 가즈아~!@#$\"");
        }

        List<String> result = CSVCommentReader.readByCell(path);
        assertThat(result).contains("창현고 화이팅! 💪", "서울대 가즈아~!@#$");
    }

    @Test
    @DisplayName("여러 줄로 구성된 CSV도 줄마다 파싱 가능하다")
    void givenMultiLineCSV_whenRead_thenParsesEachLine() throws IOException {
        String path = basePath + "/multiline.csv";
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("\"댓글1\"\n\"댓글2\"\n\"댓글3\"");
        }

        List<String> result = CSVCommentReader.readByCell(path);
        assertThat(result).containsExactly("댓글1", "댓글2", "댓글3");
    }

    @Test
    @DisplayName("[예외] 읽기 권한이 없는 파일은 예외 발생")
    void givenUnreadableFile_whenRead_thenThrowsException() throws IOException {
        String path = basePath + "/unreadable.csv";
        File file = new File(path);
        Files.writeString(Path.of(path), "\"댓글1\"");

        file.setReadable(false);

        try {
            assertThatThrownBy(() -> CSVCommentReader.readByCell(path))
                    .isInstanceOf(CommentProcessorException.class)
                    .hasMessageContaining(CSV_READ_FAILED.getSimpleMessage())
                    .hasFieldOrPropertyWithValue("errorCode", CSV_READ_FAILED);
        } finally {
            file.setReadable(true); // 권한 복구
        }
    }
}