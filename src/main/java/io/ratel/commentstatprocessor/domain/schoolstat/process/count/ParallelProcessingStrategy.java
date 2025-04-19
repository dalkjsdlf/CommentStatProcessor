package io.ratel.commentstatprocessor.domain.schoolstat.process.count;

import io.ratel.commentstatprocessor.common.constant.AppConst;
import io.ratel.commentstatprocessor.domain.extern.fetcher.SchoolFetcher;
import io.ratel.commentstatprocessor.domain.schoolstat.process.SchoolStatCounter;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolInfoLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * packageName  : io.ratel.commentstatprocessor.domain.schoolstat.processor
 * fileName    : ParallelProcessingStrategy
 * author      : dorris
 * date        : 2025. 3. 23.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 23.          dorris             최초생성
 */
public class ParallelProcessingStrategy implements CountSchoolProcessingStrategy {

    @Override
    public void process(List<String> comments) {


        // 학교정보 불러오기
        SchoolInfoLoader.loadOfficialSchoolNames();

        SchoolStatCounter counter = new SchoolStatCounter();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<CompletableFuture<Void>> futures = comments.stream()
                .map(comment -> CompletableFuture.runAsync(() -> counter.countSchools(comment), executor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
    }
}