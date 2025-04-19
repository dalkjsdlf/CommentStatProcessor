package io.ratel.commentstatprocessor.cover;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;
import io.ratel.commentstatprocessor.domain.schoolstat.process.SchoolStatCounter;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import org.junit.jupiter.api.*;

import java.util.Map;

import static io.ratel.commentstatprocessor.common.exception.CommentProcessorErrorCode.COMMENT_NULL_INPUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * SchoolStatCounterTest
 *
 * 댓글에서 학교명을 추출하고 카운팅하는 기능을 테스트합니다.
 *
 * ✅ 테스트 목록
 * ------------------------------------------------------------------------------
 * [정상]
 * 1. givenValidComment_whenCountSchools_thenCorrectStat
 *    - 다양한 형태의 학교명을 정확히 추출하고 누적 카운트
 *
 * 2. givenOnlyAbbreviation_whenCountSchools_thenResolvesProperly
 *    - 약어만 있는 댓글도 정규화되어 카운트됨
 *
 * [엣지 + 파라미터화]
 * 3. givenInvalidComments_whenCountSchools_thenMapIsEmpty
 *    - 빈 문자열, 일반문장, 특수문자만 포함된 문장은 무시됨 (파라미터화)
 *
 * 4. givenNullComment_whenCountSchools_thenThrowsException
 *    - null 입력 시 NullPointerException 발생
 * ------------------------------------------------------------------------------
 */
@DisplayName("학교명을 추출하여 통계를 내는 테스트")
public class SchoolStatCounterTest {

    private SchoolStatCounter schoolStatCounter;

    @BeforeEach
    public void init() {
        schoolStatCounter = new SchoolStatCounter();
        SchoolStatResultStore.clear(); // 항상 초기화
    }

    @Test
    @DisplayName("[정상] 다양한 형태의 학교명을 정확히 추출하여 카운팅 - Phase2")
    @Disabled
    public void givenValidComment_whenCountSchools_thenCorrectStat() {
        String comment = "서울 대학교 연대~~ 고대,, 부산고... 충암고?? " +
                "충암고등학교에서 서울대 간 사람이 얼마나 되더라? 연대도 갔었나?";

        schoolStatCounter.countSchools(comment);
        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map.get("충암고등학교")).isEqualTo(2);
        assertThat(map.get("서울대학교")).isEqualTo(2);
        assertThat(map.get("연세대학교")).isEqualTo(2);
        assertThat(map.get("고려대학교")).isEqualTo(1);
        assertThat(map.get("부산고등학교")).isEqualTo(1);
    }

    @Test
    @DisplayName("[정상] 약어만 포함된 댓글도 정확히 정규화되어 카운트됨 - Phase2")
    @Disabled
    public void givenOnlyAbbreviation_whenCountSchools_thenResolvesProperly() {
        String comment = "서울대 고대 연대 충암고";
        schoolStatCounter.countSchools(comment);

        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map.get("서울대학교")).isEqualTo(1);
        assertThat(map.get("고려대학교")).isEqualTo(1);
        assertThat(map.get("연세대학교")).isEqualTo(1);
        assertThat(map.get("충암고등학교")).isEqualTo(1);
    }

    @ParameterizedTest(name = "[엣지] 무시되어야 하는 입력: \"{0}\"")
    @ValueSource(strings = {
            "", // 빈 문자열
            "오늘은 날씨가 참 맑다. 우리는 함께 점심을 먹었다.", // 일반 문장
            "@#$%^&*()_+!@#" // 특수문자만
    })
    @DisplayName("[엣지] 유효하지 않은 댓글은 통계에 반영되지 않아야 함 (파라미터화)")
    void givenInvalidComments_whenCountSchools_thenMapIsEmpty(String comment) {
        schoolStatCounter.countSchools(comment);
        Map<String, Integer> map = SchoolStatResultStore.getMap();
        assertThat(map).isEmpty();
    }

    @Test
    @DisplayName("null 댓글 입력 시 COMMENT_NULL_INPUT 예외가 발생해야 한다")
    void givenNullComment_whenCountSchools_thenThrowsCustomException() {
        assertThatThrownBy(() -> schoolStatCounter.countSchools(null))
                .isInstanceOf(CommentProcessorException.class)
                .hasMessageContaining(COMMENT_NULL_INPUT.getMessage())
                .hasFieldOrPropertyWithValue("errorCode", COMMENT_NULL_INPUT);
    }
}