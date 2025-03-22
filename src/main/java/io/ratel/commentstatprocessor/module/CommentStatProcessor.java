package io.ratel.commentstatprocessor.module;

import io.ratel.commentstatprocessor.module.context.SchoolStatContext;
import io.ratel.commentstatprocessor.module.file.CommentFileWriter;
import io.ratel.commentstatprocessor.module.reader.CSVCommentReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * packageName  : io.ratel.commentstatprocessor.business.comment.service
 * fileName    : CommentStatProcessor
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */
@Slf4j
@Component
public class CommentStatProcessor {

    public void countSchoolsInComments(List<String> comments) {
        SchoolStatCounter counter = new SchoolStatCounter();
        for (String comment : comments) {
            counter.countSchools(comment);
        }
    }

    /**
     *
     * */
    public void aggregateSchoolCounts(String inputPath, String outputPath, String logPath, boolean overwrite) {
        /*
         * CSV 파일읽기 시작
         * */
        log.debug("==================================================");
        log.debug("CSV 파일 읽기 시작");
        log.debug("CSV 파일 읽기 시작");
        log.debug("==================================================");
        List<String> comments = CSVCommentReader.read(inputPath);

        log.debug("==================================================");
        log.debug("CSV 파일 읽기완료");
        log.debug("총 댓글 건수 : {}",comments.size());
        log.debug("==================================================");

        /*
         * 학교명 추출하여 카운팅
         * */
        log.debug("==================================================");
        log.debug("학교명 카운팅 시작");
        log.debug("==================================================");
        SchoolStatCounter counter = new SchoolStatCounter();
        for (String comment : comments) {
            counter.countSchools(comment);
        }

        log.debug("==================================================");
        log.debug("학교명 카운팅 완료");
        log.debug("학교명 카운팅 결과 출력");
        /*
         * 카운팅 결과 출력
         * */
        SchoolStatContext.displayMap();
        log.debug("==================================================");

        /*
        * 학교명 통계 데이터 포멧 변경 : Map to String
        * */
        log.debug("==================================================");
        log.debug("학교명 통계 데이터 포멧 변경 : Map to String");
        log.debug("포멧 : [학교명]\t[건수]");
        log.debug("==================================================");
        String result = SchoolStatContext.statMapToString();

        /*
         * 산출된 Count 통계 자료 내용 파일로 저장
         * */
        log.debug("==================================================");
        log.debug("산출된 Count 통계 자료 내용 파일로 저장");
        log.debug("저장 파일경로 : {}",outputPath);
        log.debug("덮어쓰기 여부 : {}",overwrite);
        log.debug("==================================================");
        CommentFileWriter.writeTextFile(outputPath,result, overwrite);
    }
}
