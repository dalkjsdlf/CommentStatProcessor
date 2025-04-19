package io.ratel.commentstatprocessor.cover;

import io.ratel.commentstatprocessor.domain.schoolstat.process.count.CountSchoolProcessingStrategy;
import io.ratel.commentstatprocessor.domain.schoolstat.process.count.ParallelProcessingStrategy;
import io.ratel.commentstatprocessor.domain.schoolstat.process.count.SingleThreadProcessingStrategy;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName  : io.ratel.commentstatprocessor.cover
 * fileName    : CommentProcessingStrategyPerformanceTest
 * author      : dorris
 * date        : 2025. 3. 23.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 23.          dorris             최초생성
 */
@DisplayName("싱글쓰레드와 멀티쓰레드 성능 비교")
@Disabled
public class CommentProcessingStrategyPerformanceTest {

    private List<String> testComments;

    @BeforeEach
    void setUp() {
        testComments = new ArrayList<>();
        String baseComment = "서울대학교 연세대학교 고려대학교 충암고등학교";

        for (int i = 0; i < 100000; i++) {
            testComments.add(baseComment);
        }
    }

    @AfterEach
    void tearDown() {
        SchoolStatResultStore.clear();
    }

    @Test
    @DisplayName("싱글 vs 병렬 처리 전략 성능 비교")
    void comparePerformanceBetweenSingleAndParallel() {
        long singleTime = measureExecutionTime(new SingleThreadProcessingStrategy());
        long parallelTime = measureExecutionTime(new ParallelProcessingStrategy());

        System.out.println("싱글 쓰레드 처리 시간: " + singleTime + "ms");
        System.out.println("병렬 쓰레드 처리 시간: " + parallelTime + "ms");

        assertThat(singleTime).isGreaterThan((long) (parallelTime * 0.7)); // 병렬이 확실히 더 빠르거나 비슷
    }

    private long measureExecutionTime(CountSchoolProcessingStrategy strategy) {
        SchoolStatResultStore.clear();

        long start = System.currentTimeMillis();
        strategy.process(testComments);
        long end = System.currentTimeMillis();

        return end - start;
    }
}
