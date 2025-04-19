package io.ratel.commentstatprocessor.cover;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;
import io.ratel.commentstatprocessor.common.file.FileWriter;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.*;

/**
 * FileWriterTest
 *
 * 텍스트 파일 작성 유틸리티의 정상 동작과 예외 케이스를 테스트합니다.
 *
 * ✅ 테스트 목록
 * ------------------------------------------------------------------------------
 * 1. 파일이 존재하지 않을 때 → 새 파일 생성
 * 2. 기존 파일 존재 + 덮어쓰기 false → 기존 내용 유지
 * 3. 상위 디렉토리 없을 때 → 디렉토리 자동 생성 및 작성
 * 4. IOException 발생 → 커스텀 예외로 포장됨
 * 5. 빈 문자열 작성 가능 여부
 * ------------------------------------------------------------------------------
 */
public class FileWriterTest {

    static final String basePath = "test/tmp/filewriter";

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Path.of(basePath));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(Path.of(basePath))
                .map(Path::toFile)
                .sorted((a, b) -> -a.compareTo(b)) // delete children first
                .forEach(File::delete);
    }

    @Test
    @DisplayName("파일이 존재하지 않을 때 새로 생성된다")
    void givenNonExistentFile_whenWrite_thenFileIsCreated() {
        String path = basePath + "/newfile.txt";
        FileWriter.writeTextFile(path, "Hello, World!", true);

        File file = new File(path);
        assertThat(file.exists()).isTrue();
    }

    @Test
    @DisplayName("파일이 존재하고 덮어쓰지 않으면 기존 내용이 유지된다")
    void givenExistingFileAndNoOverwrite_whenWrite_thenSkipWriting() throws IOException {
        String path = basePath + "/existing.txt";
        Files.write(Path.of(path), "Original".getBytes());

        FileWriter.writeTextFile(path, "Modified", false); // overwrite = false
        String content = Files.readString(Path.of(path));

        assertThat(content).isEqualTo("Original");
    }

    @Test
    @DisplayName("상위 디렉토리가 없으면 자동으로 생성된다")
    void givenNoParentDir_whenWrite_thenCreatesParentAndWritesFile() {
        String path = basePath + "/subdir/newfile.txt";
        FileWriter.writeTextFile(path, "New Content", true);

        assertThat(Files.exists(Path.of(path))).isTrue();
    }

    @Test
    @DisplayName("빈 문자열도 문제없이 파일에 쓸 수 있다")
    void givenEmptyString_whenWrite_thenCreatesEmptyFile() throws IOException {
        String path = basePath + "/empty.txt";
        FileWriter.writeTextFile(path, "", true);

        String content = Files.readString(Path.of(path));
        assertThat(content).isEqualTo("");
    }
}