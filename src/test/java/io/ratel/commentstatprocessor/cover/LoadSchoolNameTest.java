package io.ratel.commentstatprocessor.cover;

import io.ratel.commentstatprocessor.domain.extern.fetcher.SchoolFetcher;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolInfoStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName  : io.ratel.commentstatprocessor
 * fileName    : LoadSchoolNameTest
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */
@DisplayName("OPEN API로 학교명을 가져오는 테스트")
@Disabled
public class LoadSchoolNameTest {

    private SchoolFetcher schoolFetcher;

    @BeforeEach
    public void init () {
        schoolFetcher = new SchoolFetcher();
    }

    @DisplayName("학교정보를 불러온다.")
    @Test
    public void givenPageSize_whenGetSchoolName_then(){
        //given
        String searchName = "충암고등학교";
        int pageSize = 1000;

        SchoolInfoStore.clear();
        //when
        SchoolFetcher.fetchAllSchools(pageSize);
        //then
        assertThat(SchoolInfoStore.isContains(searchName)).isTrue();

    }
}
