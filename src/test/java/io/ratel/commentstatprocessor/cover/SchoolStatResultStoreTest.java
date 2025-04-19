package io.ratel.commentstatprocessor.cover;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

/**
 * SchoolStatResultStoreTest
 *
 * 학교명 → 카운트 저장소에 대한 테스트
 *
 * 테스트 목록
 * ------------------------------------------------------------------------------
 * 1. put() 호출 시 카운트 누적
 * 2. 존재하지 않는 학교 → getCount() = 0
 * 3. 빈 저장소에서 getCount() → 예외 발생
 * 4. clear() → 저장소 초기화 확인
 * 5. statMapToString() → 포맷 확인
 * 6. null 학교명 저장 시 예외
 * 7. 빈 문자열 학교명 저장 시 동작 확인
 * 8. getMap() 반환값 수정 시 실제 저장소와의 연동 여부
 * ------------------------------------------------------------------------------
 */
public class SchoolStatResultStoreTest {

    @BeforeEach
    void clearStore() {
        SchoolStatResultStore.clear();
    }

    @Test
    @DisplayName("학교명을 저장하면 카운트가 누적된다")
    void givenSchoolName_whenPut_thenCountIncreases() {
        SchoolStatResultStore.put("창현고등학교");
        SchoolStatResultStore.put("창현고등학교");

        Map<String, Integer> map = SchoolStatResultStore.getMap();
        assertThat(map.get("창현고등학교")).isEqualTo(2);
    }

    @Test
    @DisplayName("존재하지 않는 학교명 조회시 0 반환")
    void givenNonexistentSchool_whenGetCount_thenReturnsZero() {
        SchoolStatResultStore.put("서울과학고");

        int count = SchoolStatResultStore.getCount("연세대학교");
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("맵이 비어 있을 때 getCount 호출 시 예외 발생")
    void givenEmptyStore_whenGetCount_thenThrowsException() {
        assertThatThrownBy(() -> SchoolStatResultStore.getCount("서울고"))
                .isInstanceOf(CommentProcessorException.class);
    }

    @Test
    @DisplayName("clear 호출 시 모든 데이터가 삭제된다")
    void givenStoreWithData_whenClear_thenEmptyMap() {
        SchoolStatResultStore.put("창현고등학교");
        SchoolStatResultStore.clear();

        assertThat(SchoolStatResultStore.getMap()).isEmpty();
    }

    @Test
    @DisplayName("statMapToString은 학교명과 카운트를 포맷팅하여 반환한다")
    void givenStoreWithData_whenStatMapToString_thenReturnsFormattedString() {
        SchoolStatResultStore.put("창현고등학교");
        SchoolStatResultStore.put("창현고등학교");

        String result = SchoolStatResultStore.statMapToString();

        assertThat(result).contains("창현고등학교\t2");
    }

    @Test
    @DisplayName("null 학교명 저장 시 예외 발생")
    void givenNullSchoolName_whenPut_thenThrowsException() {
        assertThatThrownBy(() -> SchoolStatResultStore.put(null))
                .isInstanceOf(NullPointerException.class); // 구현에 따라 예외 종류 맞춰주세요
    }

    @Test
    @DisplayName("빈 문자열 학교명도 저장은 가능하다")
    void givenEmptyStringSchoolName_whenPut_thenStored() {
        SchoolStatResultStore.put("");
        SchoolStatResultStore.put("");

        Map<String, Integer> map = SchoolStatResultStore.getMap();
        assertThat(map.get("")).isEqualTo(2);
    }

    @Test
    @DisplayName("getMap()으로 받은 객체 수정이 원본에 영향을 준다 (직접 참조)")
    void whenGetMap_thenModify_thenAffectsStore() {
        SchoolStatResultStore.put("서울대학교");

        Map<String, Integer> map = SchoolStatResultStore.getMap();
        map.put("연세대학교", 5); // 외부에서 추가

        assertThat(SchoolStatResultStore.getCount("연세대학교")).isEqualTo(5);
    }

    @Test
    @DisplayName("statMapToString 결과는 줄바꿈을 포함한 포맷으로 반환된다")
    void givenMultipleSchools_whenStatMapToString_thenReturnsMultiLineString() {
        SchoolStatResultStore.put("A고등학교");
        SchoolStatResultStore.put("B고등학교");
        SchoolStatResultStore.put("A고등학교");

        String result = SchoolStatResultStore.statMapToString();
        assertThat(result).contains("A고등학교\t2")
                .contains("B고등학교\t1")
                .contains("\n");
    }

    @Test
    @DisplayName("[동시성 테스트] 멀티스레드 환경에서도 카운트가 정확히 누적되는지 확인")
    void givenMultiThread_whenPutSameSchool_thenCorrectlyCounted() throws InterruptedException {
        // given
        int threadCount = 1000;
        String schoolName = "창현고등학교";
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when: 1000개의 스레드가 동시에 같은 학교 put()
        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    SchoolStatResultStore.put(schoolName);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 작업이 끝날 때까지 대기
        executor.shutdown();

        // then: 정확히 1000건이 누적돼야 함
        int count = SchoolStatResultStore.getCount(schoolName);
        assertThat(count).isEqualTo(threadCount);
    }
}