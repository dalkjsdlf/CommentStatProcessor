package io.ratel.commentstatprocessor.domain.schoolstat.process;

import io.ratel.commentstatprocessor.common.constant.AppConst;
import io.ratel.commentstatprocessor.domain.extern.fetcher.SchoolFetcher;
import io.ratel.commentstatprocessor.domain.schoolstat.process.count.CountSchoolProcessingStrategy;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import io.ratel.commentstatprocessor.common.file.FileWriter;
import io.ratel.commentstatprocessor.domain.schoolstat.reader.CSVCommentReader;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
public class CommentStatProcessor {

    private final CountSchoolProcessingStrategy countSchoolProcessingStrategy;

    public CommentStatProcessor(CountSchoolProcessingStrategy strategy) {
        countSchoolProcessingStrategy = strategy;
    }

    public void aggregateSchoolCounts() {
        String defInputPath = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INPUT_FILE_NAME;
        String defOutputPath = AppConst.DEFAULT_OUTPUT_DIR + File.separator + AppConst.DEFAULT_OUTPUT_FILE_NAME;

        aggregateSchoolCounts(defInputPath, defOutputPath, true);
    }

    public void aggregateSchoolCounts(String inputPath, String outputPath, boolean overwrite) {
        /*
         * CSV 파일읽기 시작
         * */
        log.info("==================================================");
        log.info("CSV 파일 읽기 시작");
        log.info("--------------------------------------------------");
        List<String> comments = CSVCommentReader.readByCell(inputPath);
        log.info("--------------------------------------------------");
        log.info("CSV 파일 읽기완료");
        log.info("총 댓글 건수 : {}",comments.size());
        log.info("==================================================");

        /*
         * 학교명 추출하여 카운팅
         * */
        log.info("==================================================");
        log.info("학교명 카운팅 시작");
        log.info("--------------------------------------------------");
        countSchoolProcessingStrategy.process(comments);
        log.info("--------------------------------------------------");
        log.info("학교명 카운팅 완료");
        log.info("==================================================");


        log.info("==================================================");
        log.info("학교명 카운팅 결과 출력");
        log.info("--------------------------------------------------");
        /*
         * 카운팅 결과 출력
         * */
        SchoolStatResultStore.displayMap();
        log.info("--------------------------------------------------");
        log.info("==================================================");

        /*
        * 학교명 통계 데이터 포멧 변경 : Map to String
        * */
        log.info("==================================================");
        log.info("학교명 통계 데이터 포멧 변경 : Map to String");
        log.info("포멧 : [학교명]\t[건수]");
        log.info("==================================================");
        String result = SchoolStatResultStore.statMapToString();

        /*
         * 산출된 Count 통계 자료 내용 파일로 저장
         * */
        log.info("==================================================");
        log.info("산출된 Count 통계 자료 내용 파일로 저장");
        log.info("저장 파일경로 : {}",outputPath);
        log.info("덮어쓰기 여부 : {}",overwrite);
        log.info("==================================================");
        FileWriter.writeTextFile(outputPath,result, overwrite);
    }
}
