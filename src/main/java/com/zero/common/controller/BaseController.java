package com.zero.common.controller;



import com.zero.common.core.error.BusinessException;
import com.zero.common.core.error.EmBusinessError;
import com.zero.common.core.error.ErrorMsgType;
import com.zero.common.core.response.CommonReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataAccessException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author: Zero
 * @Date: 2021/4/2 - 21:39
 * @since: jdk 1.8
 */
@RestControllerAdvice
public class BaseController {

    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(Exception ex) {
        ErrorMsgType responseData ;
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData = packErrorCommonReturnType(businessException.getErrorCode()
                    , businessException.getErrorMsg());
        } else if (ex instanceof DataAccessException) { //数据库连接错误
            logger.error(ex.getMessage());
            responseData = packErrorCommonReturnType(EmBusinessError.DATARESOURCE_CONNECT_FAILURE.getErrorCode()
                    , EmBusinessError.DATARESOURCE_CONNECT_FAILURE.getErrorMsg());
        } else if (ex instanceof HttpMessageNotReadableException) { // 序列化异常
            logger.error(ex.getMessage());
            responseData = packErrorCommonReturnType(EmBusinessError.JSON_SEQUENCE_WRONG.getErrorCode()
                    , EmBusinessError.JSON_SEQUENCE_WRONG.getErrorMsg());
        } else if (ex instanceof MethodArgumentNotValidException) { // 参数校验异常
            logger.error(ex.getMessage());
            Map<String,Object> map = new HashMap<>();
            ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().forEach(fieldError -> map.put(fieldError.getField(),fieldError.getDefaultMessage()));
            responseData = packErrorCommonReturnType(EmBusinessError.PARAMETER_VALIDATION_ERROR.getErrorCode()
                    , map.toString());
        } else {
            logger.error(ex.getMessage());
            responseData = packErrorCommonReturnType(EmBusinessError.UNKNOWN_ERROR.getErrorCode()
                    , ex.getMessage());
        }
        logger.error("{"+responseData.toString()+"}");
        return CommonReturnType.fail(responseData);
    }

    protected ErrorMsgType packErrorCommonReturnType(int errorCode, String errorMsg){
        return new ErrorMsgType(errorCode,errorMsg);
    }
}