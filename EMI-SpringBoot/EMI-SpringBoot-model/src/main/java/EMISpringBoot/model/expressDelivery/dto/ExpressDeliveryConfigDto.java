package EMISpringBoot.model.expressDelivery.dto;

import lombok.Data;

@Data
public class ExpressDeliveryConfigDto {

    //1就是查看寄出去的快递 0就是查看收的快递
    private Integer type;
    //第几页
    private Integer page;
    private Integer size;

}
