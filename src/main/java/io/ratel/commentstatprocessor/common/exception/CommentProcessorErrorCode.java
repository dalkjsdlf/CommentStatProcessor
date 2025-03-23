package io.ratel.commentstatprocessor.common.exception;

import lombok.Getter;

/**
 * packageName  : io.ratel.commentstatprocessor.common.exception
 * fileName    : CommentProcessorErrorCode
 * author      : dorris
 * date        : 2025. 3. 23.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 23.          dorris             최초생성
 */
@Getter
public enum CommentProcessorErrorCode {

    FILE_NOT_FOUND("E001", "파일을 찾을 수 없습니다: %s"),
    INVALID_FORMAT("E002", "잘못된 파일 형식입니다: %s"),
    SCHOOL_NOT_FILE_IN_RESULT("E003", "통계된 학교명이 없습니다." ),
    CSV_READ_FAILED("E004", "CSV 읽기실패 : %s"),
    SCHOOL_API_LOAD_FAIL("E005", "학교정도 API 로드중 에러 : %s"),
    FILE_WRITE_FAIL("E006", "파일 쓰기실패 %s");

    private final String code;
    private final String messageTemplate;

    CommentProcessorErrorCode(String code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    public String formatMessage(Object... args) {
        return String.format("[%s] %s", code, String.format(messageTemplate, args));
    }
}
