package io.ratel.commentstatprocessor.domain.schoolstat.processor;

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
        SchoolStatCounter counter = new SchoolStatCounter();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<CompletableFuture<Void>> futures = comments.stream()
                .map(comment -> CompletableFuture.runAsync(() -> counter.countSchools(comment), executor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
    }
}