package EMISpringBoot.admin.feign;

import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import org.apache.ibatis.annotations.Update;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//管理员的接口
@FeignClient(name = "EMISpringBoot-expressDelivery",path = "/api/expressDelivery",contextId = "AdminFeign")
public interface AdminFeign {
    /**
     * 添加作者
     * @param record
     * @return
     */

    //管理员添加订单消息
    @PostMapping("/save")
    public Result<ExpressDelivery> save(@RequestBody ExpressDelivery record);

    //管理员修改订单(用json,1揽收,9拒绝)
    @PutMapping("/update")
    public Result update(@RequestBody ExpressDelivery record);

    //获取整个订单号
    @GetMapping("/one/{id}")
    public Result findOne(@PathVariable("id")Integer id);
}
