package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import io.ratel.commentstatprocessor.domain.schoolstat.processor.SchoolStatCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName  : io.ratel.commentstatprocessor
 * fileName    : SchoolStatCounterTest
 * author      : dorris
 * date        : 2025. 3. 22.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 22.          dorris             최초생성
 */
@DisplayName("학교명을 추출하여 통계를 내는 테스트")
public class SchoolStatCounterTest {

    private SchoolStatCounter schoolStatCounter;

    @BeforeEach
    public void init() {
        schoolStatCounter = new SchoolStatCounter();
    
    @DisplayName("[예외] 빈 문자열이 입력되었을 때 카운팅하지 않는다")
    @Test
    public void givenEmptyComment_whenCountSchools_thenNoResult() {
        SchoolStatResultStore.clear();

        schoolStatCounter.countSchools("");
        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map).isEmpty();
    }

    @DisplayName("[예외] 학교명이 포함되지 않은 일반 문장")
    @Test
    public void givenNoSchoolNameComment_whenCountSchools_thenNoResult() {
        SchoolStatResultStore.clear();

        schoolStatCounter.countSchools("오늘은 날씨가 참 맑다. 우리는 함께 점심을 먹었다.");
        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map).isEmpty();
    }

    @DisplayName("[예외] 특수문자만 포함된 문장")
    @Test
    public void givenSpecialCharComment_whenCountSchools_thenNoResult() {
        SchoolStatResultStore.clear();

        schoolStatCounter.countSchools("@#$%^&*()_+!@#");
        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map).isEmpty();
    }

    @DisplayName("[예외] null이 입력되면 예외 발생")
    @Test
    public void givenNullComment_whenCountSchools_thenHandleGracefully() {
        SchoolStatResultStore.clear();

        // null 처리에 대한 명확한 정책이 없다면 NullPointerException 예외 발생 테스트
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> {
            schoolStatCounter.countSchools(null);
        });
    }

}

    @DisplayName("[성공] List에 있는 학교명의 개수를 세주는 기능")
    @Test
    public void givenComment_whenCountSchools_thenStatMap(){
        SchoolStatResultStore.clear();

        //given
        // 공백, 특수문자, 약어 커버됨
        String comment = "서울 대학교 연대~~ 고대,, 부산고... 충암고?? 그래 내가 오늘 충암고에 갔어 충암고등학교에서 서울대 간 사람이 얼마나 되더라? 연대도 갔었나?";

        //when
        schoolStatCounter.countSchools(comment);

        Map<String, Integer> map = SchoolStatResultStore.getMap();
        // then
        // 충암고등학교 3 ,서울대학교 2, 연세대학교2, 고려대학교1, 부산대학교1
        assertThat(map.get("충암고등학교")).isEqualTo(3);
        assertThat(map.get("서울대학교")).isEqualTo(2);
        assertThat(map.get("연세대학교")).isEqualTo(2);
        assertThat(map.get("고려대학교")).isEqualTo(1);
        assertThat(map.get("부산고등학교")).isEqualTo(1);
    
    @DisplayName("[예외] 빈 문자열이 입력되었을 때 카운팅하지 않는다")
    @Test
    public void givenEmptyComment_whenCountSchools_thenNoResult() {
        SchoolStatResultStore.clear();

        schoolStatCounter.countSchools("");
        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map).isEmpty();
    }

    @DisplayName("[예외] 학교명이 포함되지 않은 일반 문장")
    @Test
    public void givenNoSchoolNameComment_whenCountSchools_thenNoResult() {
        SchoolStatResultStore.clear();

        schoolStatCounter.countSchools("오늘은 날씨가 참 맑다. 우리는 함께 점심을 먹었다.");
        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map).isEmpty();
    }

    @DisplayName("[예외] 특수문자만 포함된 문장")
    @Test
    public void givenSpecialCharComment_whenCountSchools_thenNoResult() {
        SchoolStatResultStore.clear();

        schoolStatCounter.countSchools("@#$%^&*()_+!@#");
        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map).isEmpty();
    }

    @DisplayName("[예외] null이 입력되면 예외 발생")
    @Test
    public void givenNullComment_whenCountSchools_thenHandleGracefully() {
        SchoolStatResultStore.clear();

        // null 처리에 대한 명확한 정책이 없다면 NullPointerException 예외 발생 테스트
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> {
            schoolStatCounter.countSchools(null);
        });
    }

}
    @DisplayName("[예외] 빈 문자열이 입력되었을 때 카운팅하지 않는다")
    @Test
    public void givenEmptyComment_whenCountSchools_thenNoResult() {
        SchoolStatResultStore.clear();

        schoolStatCounter.countSchools("");
        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map).isEmpty();
    }

    @DisplayName("[예외] 학교명이 포함되지 않은 일반 문장")
    @Test
    public void givenNoSchoolNameComment_whenCountSchools_thenNoResult() {
        SchoolStatResultStore.clear();

        schoolStatCounter.countSchools("오늘은 날씨가 참 맑다. 우리는 함께 점심을 먹었다.");
        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map).isEmpty();
    }

    @DisplayName("[예외] 특수문자만 포함된 문장")
    @Test
    public void givenSpecialCharComment_whenCountSchools_thenNoResult() {
        SchoolStatResultStore.clear();

        schoolStatCounter.countSchools("@#$%^&*()_+!@#");
        Map<String, Integer> map = SchoolStatResultStore.getMap();

        assertThat(map).isEmpty();
    }

    @DisplayName("[예외] null이 입력되면 예외 발생")
    @Test
    public void givenNullComment_whenCountSchools_thenHandleGracefully() {
        SchoolStatResultStore.clear();

        // null 처리에 대한 명확한 정책이 없다면 NullPointerException 예외 발생 테스트
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> {
            schoolStatCounter.countSchools(null);
        });
    }

}