
package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

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

        Integer count = SchoolStatResultStore.getCount("연세대학교");
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("맵이 비어 있을 때 getCount 호출 시 예외 발생")
    void givenEmptyStore_whenGetCount_thenThrowsException() {
        assertThatThrownBy(() -> {
            SchoolStatResultStore.getCount("서울고");
        }).isInstanceOf(CommentProcessorException.class);
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
}
