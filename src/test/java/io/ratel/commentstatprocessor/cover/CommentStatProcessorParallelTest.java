package io.ratel.commentstatprocessor.cover;

import io.ratel.commentstatprocessor.common.constant.AppConst;
import io.ratel.commentstatprocessor.domain.schoolstat.process.CommentStatProcessor;
import io.ratel.commentstatprocessor.domain.schoolstat.process.count.CountSchoolProcessingStrategy;
import io.ratel.commentstatprocessor.domain.schoolstat.process.count.ParallelProcessingStrategy;
import io.ratel.commentstatprocessor.domain.schoolstat.process.count.SingleThreadProcessingStrategy;
import io.ratel.commentstatprocessor.domain.schoolstat.reader.CSVCommentReader;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * CommentStatProcessorTest
 *
 * 댓글을 이용해 학교명을 추출하고 통계 파일을 생성하는 전체 흐름을 테스트합니다.
 *
 * ✅ 테스트 목록
 * ------------------------------------------------------------------------------
 * [기본 흐름]
 * 1. givenCommentsOfHardCoding_whenCountAllSchoolsName_thenTotalStatMap
 *    - 하드코딩 댓글에서 학교명 추출 후 통계 누적
 *
 * 2. givenCommentsFromCsv_whenCountAllSchoolsName_thenTotalStatMap
 *    - CSV 파일로부터 읽은 댓글에서 학교명 통계 추출
 *
 * 3. givenCsvFile_whenCountProcessor_thenGetResultTxt
 *    - aggregateSchoolCounts 기본 호출로 결과 파일 생성
 *
 * 4. givenValidInput_whenAggregateSchoolCounts_thenResultFileCreated
 *    - 유효한 입력/출력 경로 사용 시 결과 파일 생성 확인
 *
 * 5. givenInvalidInputPath_whenAggregate_thenThrowsException
 *    - 존재하지 않는 입력 파일 경로 사용 시 예외 발생
 *
 * 6. givenExistingOutputFileAndNoOverwrite_whenAggregate_thenSkipsWriting
 *    - 기존 파일 있고 overwrite = false 시 내용 유지 확인
 *
 * [엣지 케이스]
 * 7. givenEmptyComments_whenCountSchools_thenNoException
 *    - 댓글이 빈 리스트여도 예외 없이 종료
 *
 * 8. givenNullList_whenCountSchools_thenThrowsNPE
 *    - null 전달 시 NullPointerException 발생
 *
 * 9. givenDirectoryAsOutputPath_whenAggregate_thenThrowsException
 *    - 출력 경로가 디렉토리일 경우 예외 발생
 *
 * 10. givenReadOnlyOutputFile_whenAggregate_thenThrowsException
 *     - 읽기 전용 파일에 쓰기 시도 시 예외 발생
 *
 * 11. givenDuplicateSchoolName_whenCounted_thenAccurateAggregation
 *     - 동일 학교명이 여러 번 등장 시 정확히 누적됨
 * ------------------------------------------------------------------------------
 */

@DisplayName("CommentStatProcessor 통합 + 엣지 테스트")
public class CommentStatProcessorParallelTest {

    private CommentStatProcessor commentStatProcessor;
    private List<String> comments;
    private CountSchoolProcessingStrategy countSchoolProcessingStrategy;

    @BeforeEach
    public void init() {
        SchoolStatResultStore.clear();
        countSchoolProcessingStrategy = new ParallelProcessingStrategy();
        commentStatProcessor = new CommentStatProcessor(countSchoolProcessingStrategy);

        comments = List.of(
                "서울대학교, 연세대학교, 고려대학교, 부산고등학교, 충암고등학교에서 모임합니다.",
                "서울대, 연대, 고대, 부산고, 충암고",
                "서울 대학교 연대~~ 고대,, 부산고... 충암고??",
                "서울 대학교 연대~~ 고대,, 부산고... 충암고?? 그래 내가 오늘 충암고에 갔어 충암고등학교에서 서울대 간 사람이 얼마나 되더라? 연대도 갔었나?",
                "불광동 살고있는 우리 불광중학우분들 저 좀 도와주세요 ㅠㅠ "
        );
    }

    @Test
    @DisplayName("[기본] 하드코딩된 댓글에서 학교명 통계")
    void givenCommentsOfHardCoding_whenCountAllSchoolsName_thenTotalStatMap() {
        countSchoolProcessingStrategy.process(comments);
        int count = SchoolStatResultStore.getCount("충암고등학교");
        assertThat(count).isEqualTo(6);
    }

    @Test
    @DisplayName("[기본] CSV 파일에서 학교명 추출")
    void givenCommentsFromCsv_whenCountAllSchoolsName_thenTotalStatMap() throws IOException {
        String inputFilePath = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INPUT_FILE_NAME;
        List<String> commentsFromCsv = CSVCommentReader.readByCell(inputFilePath);
        countSchoolProcessingStrategy.process(commentsFromCsv);
        int count = SchoolStatResultStore.getCount("창현고등학교");
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("[기본] 기본 aggregateSchoolCounts() 실행")
    void givenCsvFile_whenCountProcessor_thenGetResultTxt() {
        SchoolStatResultStore.clear();
        commentStatProcessor.aggregateSchoolCounts();
        int count = SchoolStatResultStore.getCount("창현고등학교");
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("[기본] 유효한 경로로 파일 생성 확인")
    void givenValidInput_whenAggregateSchoolCounts_thenResultFileCreated() {
        String inputPath = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INPUT_FILE_NAME;
        String outputPath = AppConst.DEFAULT_OUTPUT_DIR + File.separator + AppConst.DEFAULT_OUTPUT_FILE_NAME;

        commentStatProcessor.aggregateSchoolCounts(inputPath, outputPath, true);

        File outputFile = new File(outputPath);
        assertThat(outputFile.exists()).isTrue();
        assertThat(outputFile.length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("[기본] 잘못된 입력 경로로 예외 발생")
    void givenInvalidInputPath_whenAggregate_thenThrowsException() {
        String inputPath = "src/test/resources/nonexistent.csv";
        String outputPath = "test/tmp/shouldNotCreate.txt";

        assertThrows(RuntimeException.class, () -> {
            commentStatProcessor.aggregateSchoolCounts(inputPath, outputPath, true);
        });
    }

    @Test
    @DisplayName("[기본] 덮어쓰기 비활성화 시 기존 파일 유지")
    void givenExistingOutputFileAndNoOverwrite_whenAggregate_thenSkipsWriting() throws IOException {
        String inputPath = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INPUT_FILE_NAME;
        String outputPath = "test/tmp/no-overwrite.txt";

        Files.write(Path.of(outputPath), "PREVIOUS".getBytes());
        commentStatProcessor.aggregateSchoolCounts(inputPath, outputPath, false);

        String result = Files.readString(Path.of(outputPath));
        assertThat(result).isEqualTo("PREVIOUS");
    }

    // ===== 엣지 케이스 =====

    @Test
    @DisplayName("[엣지] 빈 댓글 리스트 처리")
    void givenEmptyComments_whenCountSchools_thenNoException() {
        SchoolStatResultStore.clear();
        countSchoolProcessingStrategy.process(Collections.emptyList());
        assertThat(SchoolStatResultStore.getMap()).isEmpty();
    }

    @Test
    @DisplayName("[엣지] null 전달 시 NPE 발생")
    void givenNullList_whenCountSchools_thenThrowsNPE() {
        assertThrows(NullPointerException.class, () -> {
            countSchoolProcessingStrategy.process(null);
        });
    }

    @Test
    @DisplayName("[엣지] 출력 경로가 디렉토리일 경우 예외")
    void givenDirectoryAsOutputPath_whenAggregate_thenThrowsException() {
        String inputPath = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INPUT_FILE_NAME;
        String outputPath = "test/tmp/";

        assertThrows(RuntimeException.class, () -> {
            commentStatProcessor.aggregateSchoolCounts(inputPath, outputPath, true);
        });
    }

    @Test
    @DisplayName("[엣지] 읽기 전용 파일에 쓰기 시도 시 예외")
    @Disabled
    void givenReadOnlyOutputFile_whenAggregate_thenThrowsException() throws IOException {
        String inputPath = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INPUT_FILE_NAME;
        String outputPath = "test/tmp/readonly-output.txt";

        Path path = Path.of(outputPath);
        Files.writeString(path, "cannot overwrite this");
        path.toFile().setReadOnly();

        assertThrows(RuntimeException.class, () -> {
            commentStatProcessor.aggregateSchoolCounts(inputPath, outputPath, true);
        });

        path.toFile().setWritable(true); // cleanup
        Files.deleteIfExists(path);
    }

    @Test
    @DisplayName("[엣지] 동일 학교명 누적 카운트 정확히 증가")
    void givenDuplicateSchoolName_whenCounted_thenAccurateAggregation() {
        List<String> duplicateComments = List.of(
                "연세대학교 화이팅!",
                "연세대학교 정말 좋아요",
                "연세대학교 졸업생입니다"
        );
        countSchoolProcessingStrategy.process(duplicateComments);

        int count = SchoolStatResultStore.getCount("연세대학교");
        assertThat(count).isEqualTo(3);
    }
}