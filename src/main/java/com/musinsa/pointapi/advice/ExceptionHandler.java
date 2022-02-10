package com.musinsa.pointapi.advice;

import com.musinsa.pointapi.advice.exception.NotFoundException;
import com.musinsa.pointapi.http.BaseResponse;
import com.musinsa.pointapi.http.CodeEnum;
import com.musinsa.pointapi.point.response.GetPointsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {

        BaseResponse<String> response = new BaseResponse(
                false,
                CodeEnum.ERROR_MISSING_PARAMETER,
                "[정확한 파라미터를 입력해주세요] => " + exception.getMessage()
        );

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {

        BaseResponse<String> response = new BaseResponse(
                false,
                CodeEnum.ERROR_TYPE_MISMATCH,
                "[파라미터의 타입이 일치하지 않습니다] => " + exception.getMessage()
        );

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleNotFoundException(NotFoundException exception) {

        BaseResponse<String> response = new BaseResponse(
                false,
                CodeEnum.ERROR_NOT_FOUND,
                "[개체를 찾을 수 없습니다.] => " + exception.getMessage()
        );

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<String>> handleIllegalArgumentException(IllegalArgumentException exception) {

        BaseResponse<String> response = new BaseResponse(
                false,
                CodeEnum.ERROR_NOT_FOUND,
                "[올바르지 않은 상태입니다.] => " + exception.getMessage()
        );

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
}
