package io.ratel.commentstatprocessor.common.constant;

/**
 * packageName  : io.ratel.commentstatprocessor.module.constant
 * fileName    : StatProcessorConst
 * author      : dorris
 * date        : 2025. 3. 22.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 22.          dorris             최초생성
 */
public class AppConst {
    public static final String DEFAULT_OUTPUT_DIR = "./result";
    public static final String DEFAULT_INPUT_DIR = "./input";
    public static final String DEFAULT_LOG_DIR = "./logs";
    public static final String DEFAULT_OUTPUT_FILE_NAME = "result.txt";
    public static final String DEFAULT_INPUT_FILE_NAME = "comments.csv";
    public static final String DEFAULT_LOG_LEVEL = "INFO";
    public static final String DEFAULT_INFO_FILE_NAME = "official_school_list.csv";


    public static final String API_KEY = "3a98d01206c542b29e2ae4d2df96fa5e"; // 실제 API키
    public static final String BASE_URL = "https://open.neis.go.kr/hub/schoolInfo";
    public static final int DEFAULT_PAGE_SIZE = 1000;
}
