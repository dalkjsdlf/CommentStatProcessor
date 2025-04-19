package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.domain.schoolstat.process.CommentStatProcessor;
import io.ratel.commentstatprocessor.domain.schoolstat.process.count.CountSchoolProcessingStrategy;
import io.ratel.commentstatprocessor.domain.schoolstat.process.count.ParallelProcessingStrategy;
import io.ratel.commentstatprocessor.domain.schoolstat.process.count.SingleThreadProcessingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import io.ratel.commentstatprocessor.common.constant.AppConst;

@Slf4j
public class CommentStatProcessorApplication {

    public static void main(String[] args) {
        Map<String, String> argMap = parseArgs(args);

        String inputPath = argMap.getOrDefault("input", AppConst.DEFAULT_INPUT_DIR);
        String outputPath = argMap.getOrDefault("output", AppConst.DEFAULT_OUTPUT_DIR);
        String logLevel = argMap.getOrDefault("loglevel", AppConst.DEFAULT_LOG_LEVEL);
        boolean parallel = Boolean.parseBoolean(argMap.getOrDefault("parallel", "false"));

        // 로그레벨 설정 (Logback의 경우 시스템 프로퍼티 설정 필요)
        System.setProperty("LOG_LEVEL", logLevel);

        // 로그 시작
        log.info("✅ CommentStatProcessor 시작");
        log.info("입력 경로: {}", inputPath);
        log.info("출력 경로: {}", outputPath);
        log.info("로그 레벨: {}", logLevel);
        log.info("멀티스레드 사용 여부: {}", parallel);

        CountSchoolProcessingStrategy strategy = parallel
                ? new ParallelProcessingStrategy()
                : new SingleThreadProcessingStrategy();

        try {
            CommentStatProcessor processor = new CommentStatProcessor(strategy);
            processor.aggregateSchoolCounts(inputPath, outputPath, true);
        } catch (Exception e) {
            log.error("🚨 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();

        for (String arg : args) {
            if (arg.startsWith("--") && arg.contains("=")) {
                String[] split = arg.substring(2).split("=", 2);
                map.put(split[0].toLowerCase(), split[1]);
            } else {
                System.out.println("잘못된 인자 형식입니다: " + arg);
                printUsageAndExit();
            }
        }
        return map;
    }

    private static void printUsageAndExit() {
        System.out.println("사용법:");
        System.out.println("  java -jar your-app.jar [--input=파일경로] [--output=파일경로] [--loglevel=INFO|DEBUG|ERROR] [--parallel=true|false]");
        System.out.println("예시:");
        System.out.println("  java -jar CommentStatProcessor.jar --input=input/comments.csv --output=result/out.txt --loglevel=DEBUG --parallel=true");
        System.exit(1);
    }
}