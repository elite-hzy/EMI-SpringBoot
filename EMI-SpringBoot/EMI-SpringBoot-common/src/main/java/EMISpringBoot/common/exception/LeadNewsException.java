package EMISpringBoot.common.exception;

import lombok.Getter;
/*
定义业务异常
 */
@Getter
public class LeadNewsException extends RuntimeException{
    private Integer status;//状态码

    public LeadNewsException(Integer status,String message){
        super(message);
        this.status = status;
    }
    public LeadNewsException(AppHttpCodeEnum appHttpCodeEnum){
        super(appHttpCodeEnum.getMessage());
        this.status = appHttpCodeEnum.getCode();
    }
}
