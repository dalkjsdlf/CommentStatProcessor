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
    FILE_WRITE_FAIL("E006", "파일 쓰기실패 : %s"),
    COMMENT_NULL_INPUT("E007", "댓글(comment)은 null일 수 없습니다."),
    SCHOOL_NOT_EXIST_IN_INFO_SET("E008", "학교정보 set에 해당하는 학교가 없습니다. %s"),
    SCHOOL_INFO_LOAD_FAIL("E009", "csv에서 학교정보를 로드하던 중 오류가 발생하였습니다. %s");

    private final String code;
    private final String message;

    CommentProcessorErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String formatMessage(Object... args) {
        return String.format("[%s] %s", code, String.format(message, args));
    }

    public String getSimpleMessage() {
        return message.replaceAll("[:：]\\s*%s.*", "").trim();
    }
}
