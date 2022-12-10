package EMISpringBoot.ExpressDelivery.feign;

import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDtoUser;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDeliveryConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "EMISpringBoot-expressDelivery", path = "/api/expressDeliveryConfig", contextId = "ExpressDeliveryFeign")
public interface ExpressDeliveryFeign {
    //根据用户id来查询相关信息,一次查10条,
    @PostMapping("/userGetById")
    public Result UserFindDelivery(@RequestBody ExpressDeliveryConfigDtoUser dto);

    //管理员随便查
    @PostMapping("/adminFindExpress")
    public Result adminFindExpress(@RequestBody ExpressDeliveryConfigDto dto);

    //根据id来查询快递完整信息
    @PostMapping("/findOne")
    public Result longIdFindOne(@RequestParam("deliveryId")Long deliveryId);

    //修改快递配置表
    @PutMapping("/update")
    public Result update(@RequestBody ExpressDeliveryConfig record);

    //保存快递配置表
    @PostMapping("/save")
    public Result save(@RequestBody ExpressDeliveryConfig record);
}
