package EMISpringBoot.model.expressDelivery;

import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryDto;
import lombok.Data;

@Data
public class ExpressDeliveryChangeDto extends ExpressDeliveryDto {
    //已经继承了订单号
    //状态码
    //0拒绝接单
    //1确认接单 ->等待揽收
    //2站点收入
    //3运往下一个站点
    //4 快递异常
    //5 派件
    //6 签收
    private Integer status;
    //"date":"1670660596","where":"null","status"
    //寄件地点,或者派送 送达等一系列字符串
    private String location;

}
