package EMISpringBoot.common.exception;


import EMISpringBoot.common.dtos.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义全局异常拦截器类
 */
@RestControllerAdvice  // AOP思想
public class GlobalExceptionHandler {
    /**
     * 拦截业务异常
     */
    @ExceptionHandler(value = LeadNewsException.class)
    public Result handleLeadNewsException(LeadNewsException e){
        return Result.errorMessage(e.getMessage(),e.getStatus(),null);
    }

    /**
     * 拦截系统异常
     */
    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception e){
        return Result.error();
    }
}

