package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.domain.schoolstat.processor.CommentStatProcessor;
import io.ratel.commentstatprocessor.common.constant.AppConst;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import io.ratel.commentstatprocessor.domain.schoolstat.reader.CSVCommentReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName  : io.ratel.commentstatprocessor
 * fileName    : CommentStatProcessorTest
 * author      : dorris
 * date        : 2025. 3. 22.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 22.          dorris             최초생성
 */
@DisplayName("댓글 하나가 아니라 여러 목록을 한번에 통계하는 테스트")
public class CommentStatProcessorTest {

    private CommentStatProcessor commentStatProcessor;
    private List<String> comments;

    @BeforeEach
    public void init () {
        commentStatProcessor = new CommentStatProcessor();
        comments = List.of(
                "서울대학교, 연세대학교, 고려대학교, 부산고등학교, 충암고등학교에서 모임합니다.",
                "서울대, 연대, 고대, 부산고, 충암고",
                "서울 대학교 연대~~ 고대,, 부산고... 충암고??",
                "서울 대학교 연대~~ 고대,, 부산고... 충암고?? 그래 내가 오늘 충암고에 갔어 충암고등학교에서 서울대 간 사람이 얼마나 되더라? 연대도 갔었나?",
                "불광동 살고있는 우리 불광중학우분들 저 좀 도와주세요 ㅠㅠ "
        );
    
    @DisplayName("[정상] 전체 흐름 테스트 - 기본 파일로 처리")
    @Test
    public void givenValidInput_whenAggregateSchoolCounts_thenResultFileCreated() {
        String inputPath = "src/test/resources/comments.csv";
        String outputPath = "test/tmp/output-aggregate.txt";

        CommentStatProcessor processor = new CommentStatProcessor();
        processor.aggregateSchoolCounts(inputPath, outputPath, null, true);

        File outputFile = new File(outputPath);
        assertThat(outputFile.exists()).isTrue();
        assertThat(outputFile.length()).isGreaterThan(0);
    }

    @DisplayName("[예외] 잘못된 input 경로가 주어졌을 때 RuntimeException 발생")
    @Test
    public void givenInvalidInputPath_whenAggregate_thenThrowsException() {
        String inputPath = "src/test/resources/nonexistent.csv";
        String outputPath = "test/tmp/shouldNotCreate.txt";

        CommentStatProcessor processor = new CommentStatProcessor();

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            processor.aggregateSchoolCounts(inputPath, outputPath, null, true);
        });
    }

    @DisplayName("[기능] 덮어쓰기 비활성화 시 파일 존재하면 스킵")
    @Test
    public void givenExistingOutputFileAndNoOverwrite_whenAggregate_thenSkipsWriting() throws IOException {
        String inputPath = "src/test/resources/comments.csv";
        String outputPath = "test/tmp/no-overwrite.txt";

        // 먼저 파일 생성
        java.nio.file.Files.write(Path.of(outputPath), "PREVIOUS".getBytes());

        CommentStatProcessor processor = new CommentStatProcessor();
        processor.aggregateSchoolCounts(inputPath, outputPath, null, false); // 덮어쓰기 비활성화

        // 내용이 이전과 같아야 함
        String result = java.nio.file.Files.readString(Path.of(outputPath));
        assertThat(result).isEqualTo("PREVIOUS");
    }

}
    @DisplayName("[성공] 하드코딩된 댓글 내용에서 학교명을 추출하여 통계")
    @Test
    public void givenCommentsOfHardCoding_whenCountAllSchoolsName_thenTotalStatMap(){
        //given

        String schoolNameForValidation = "충암고등학교";

        //when
        commentStatProcessor.countSchoolsInComments(comments);
        SchoolStatResultStore.displayMap();
        int count = SchoolStatResultStore.getCount(schoolNameForValidation);

        //then
        assertThat(count).isEqualTo(6);
    
    @DisplayName("[정상] 전체 흐름 테스트 - 기본 파일로 처리")
    @Test
    public void givenValidInput_whenAggregateSchoolCounts_thenResultFileCreated() {
        String inputPath = "src/test/resources/comments.csv";
        String outputPath = "test/tmp/output-aggregate.txt";

        CommentStatProcessor processor = new CommentStatProcessor();
        processor.aggregateSchoolCounts(inputPath, outputPath, null, true);

        File outputFile = new File(outputPath);
        assertThat(outputFile.exists()).isTrue();
        assertThat(outputFile.length()).isGreaterThan(0);
    }

    @DisplayName("[예외] 잘못된 input 경로가 주어졌을 때 RuntimeException 발생")
    @Test
    public void givenInvalidInputPath_whenAggregate_thenThrowsException() {
        String inputPath = "src/test/resources/nonexistent.csv";
        String outputPath = "test/tmp/shouldNotCreate.txt";

        CommentStatProcessor processor = new CommentStatProcessor();

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            processor.aggregateSchoolCounts(inputPath, outputPath, null, true);
        });
    }

    @DisplayName("[기능] 덮어쓰기 비활성화 시 파일 존재하면 스킵")
    @Test
    public void givenExistingOutputFileAndNoOverwrite_whenAggregate_thenSkipsWriting() throws IOException {
        String inputPath = "src/test/resources/comments.csv";
        String outputPath = "test/tmp/no-overwrite.txt";

        // 먼저 파일 생성
        java.nio.file.Files.write(Path.of(outputPath), "PREVIOUS".getBytes());

        CommentStatProcessor processor = new CommentStatProcessor();
        processor.aggregateSchoolCounts(inputPath, outputPath, null, false); // 덮어쓰기 비활성화

        // 내용이 이전과 같아야 함
        String result = java.nio.file.Files.readString(Path.of(outputPath));
        assertThat(result).isEqualTo("PREVIOUS");
    }

}
    @DisplayName("[성공] Csv의 모든 댓글 내용에서 학교명을 추출하여 통계")
    @Test
    public void givenCommentsFromCsv_whenCountAllSchoolsName_thenTotalStatMap() throws IOException {
        //given

        String schoolNameForValidation = "창현고등학교";
        String inputFilePath = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INPUT_FILE_NAME;
        File file = Path.of(inputFilePath).toFile();
        //when
        List<String> commentsFromCsv = CSVCommentReader.read(file.getPath());
        //when
        commentStatProcessor.countSchoolsInComments(commentsFromCsv);
        SchoolStatResultStore.displayMap();
        int count = SchoolStatResultStore.getCount(schoolNameForValidation);

        //then
        assertThat(count).isEqualTo(1);
    
    @DisplayName("[정상] 전체 흐름 테스트 - 기본 파일로 처리")
    @Test
    public void givenValidInput_whenAggregateSchoolCounts_thenResultFileCreated() {
        String inputPath = "src/test/resources/comments.csv";
        String outputPath = "test/tmp/output-aggregate.txt";

        CommentStatProcessor processor = new CommentStatProcessor();
        processor.aggregateSchoolCounts(inputPath, outputPath, null, true);

        File outputFile = new File(outputPath);
        assertThat(outputFile.exists()).isTrue();
        assertThat(outputFile.length()).isGreaterThan(0);
    }

    @DisplayName("[예외] 잘못된 input 경로가 주어졌을 때 RuntimeException 발생")
    @Test
    public void givenInvalidInputPath_whenAggregate_thenThrowsException() {
        String inputPath = "src/test/resources/nonexistent.csv";
        String outputPath = "test/tmp/shouldNotCreate.txt";

        CommentStatProcessor processor = new CommentStatProcessor();

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            processor.aggregateSchoolCounts(inputPath, outputPath, null, true);
        });
    }

    @DisplayName("[기능] 덮어쓰기 비활성화 시 파일 존재하면 스킵")
    @Test
    public void givenExistingOutputFileAndNoOverwrite_whenAggregate_thenSkipsWriting() throws IOException {
        String inputPath = "src/test/resources/comments.csv";
        String outputPath = "test/tmp/no-overwrite.txt";

        // 먼저 파일 생성
        java.nio.file.Files.write(Path.of(outputPath), "PREVIOUS".getBytes());

        CommentStatProcessor processor = new CommentStatProcessor();
        processor.aggregateSchoolCounts(inputPath, outputPath, null, false); // 덮어쓰기 비활성화

        // 내용이 이전과 같아야 함
        String result = java.nio.file.Files.readString(Path.of(outputPath));
        assertThat(result).isEqualTo("PREVIOUS");
    }

}
    @DisplayName("[성공] CommentStatProcessor 메인 테스트")
    @Test
    public void givenCsvFile_whenCountProcessor_thenGetResultTxt() throws IOException {
        //given

        String schoolNameForValidation = "창현고등학교";
        //when
        commentStatProcessor.aggregateSchoolCounts();

        //when

        int count = SchoolStatResultStore.getCount(schoolNameForValidation);

        //then
        assertThat(count).isEqualTo(1);
    
    @DisplayName("[정상] 전체 흐름 테스트 - 기본 파일로 처리")
    @Test
    public void givenValidInput_whenAggregateSchoolCounts_thenResultFileCreated() {
        String inputPath = "src/test/resources/comments.csv";
        String outputPath = "test/tmp/output-aggregate.txt";

        CommentStatProcessor processor = new CommentStatProcessor();
        processor.aggregateSchoolCounts(inputPath, outputPath, null, true);

        File outputFile = new File(outputPath);
        assertThat(outputFile.exists()).isTrue();
        assertThat(outputFile.length()).isGreaterThan(0);
    }

    @DisplayName("[예외] 잘못된 input 경로가 주어졌을 때 RuntimeException 발생")
    @Test
    public void givenInvalidInputPath_whenAggregate_thenThrowsException() {
        String inputPath = "src/test/resources/nonexistent.csv";
        String outputPath = "test/tmp/shouldNotCreate.txt";

        CommentStatProcessor processor = new CommentStatProcessor();

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            processor.aggregateSchoolCounts(inputPath, outputPath, null, true);
        });
    }

    @DisplayName("[기능] 덮어쓰기 비활성화 시 파일 존재하면 스킵")
    @Test
    public void givenExistingOutputFileAndNoOverwrite_whenAggregate_thenSkipsWriting() throws IOException {
        String inputPath = "src/test/resources/comments.csv";
        String outputPath = "test/tmp/no-overwrite.txt";

        // 먼저 파일 생성
        java.nio.file.Files.write(Path.of(outputPath), "PREVIOUS".getBytes());

        CommentStatProcessor processor = new CommentStatProcessor();
        processor.aggregateSchoolCounts(inputPath, outputPath, null, false); // 덮어쓰기 비활성화

        // 내용이 이전과 같아야 함
        String result = java.nio.file.Files.readString(Path.of(outputPath));
        assertThat(result).isEqualTo("PREVIOUS");
    }

}
    @DisplayName("[정상] 전체 흐름 테스트 - 기본 파일로 처리")
    @Test
    public void givenValidInput_whenAggregateSchoolCounts_thenResultFileCreated() {
        String inputPath = "src/test/resources/comments.csv";
        String outputPath = "test/tmp/output-aggregate.txt";

        CommentStatProcessor processor = new CommentStatProcessor();
        processor.aggregateSchoolCounts(inputPath, outputPath, null, true);

        File outputFile = new File(outputPath);
        assertThat(outputFile.exists()).isTrue();
        assertThat(outputFile.length()).isGreaterThan(0);
    }

    @DisplayName("[예외] 잘못된 input 경로가 주어졌을 때 RuntimeException 발생")
    @Test
    public void givenInvalidInputPath_whenAggregate_thenThrowsException() {
        String inputPath = "src/test/resources/nonexistent.csv";
        String outputPath = "test/tmp/shouldNotCreate.txt";

        CommentStatProcessor processor = new CommentStatProcessor();

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            processor.aggregateSchoolCounts(inputPath, outputPath, null, true);
        });
    }

    @DisplayName("[기능] 덮어쓰기 비활성화 시 파일 존재하면 스킵")
    @Test
    public void givenExistingOutputFileAndNoOverwrite_whenAggregate_thenSkipsWriting() throws IOException {
        String inputPath = "src/test/resources/comments.csv";
        String outputPath = "test/tmp/no-overwrite.txt";

        // 먼저 파일 생성
        java.nio.file.Files.write(Path.of(outputPath), "PREVIOUS".getBytes());

        CommentStatProcessor processor = new CommentStatProcessor();
        processor.aggregateSchoolCounts(inputPath, outputPath, null, false); // 덮어쓰기 비활성화

        // 내용이 이전과 같아야 함
        String result = java.nio.file.Files.readString(Path.of(outputPath));
        assertThat(result).isEqualTo("PREVIOUS");
    }

}