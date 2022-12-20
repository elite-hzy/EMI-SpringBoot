package EMISpringBoot.ExpressDelivery.controller;


import EMISpringBoot.ExpressDelivery.service.ExpressDeliveryService;
import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import EMISpringBoot.common.controller.AbstractController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* <p>
*  控制器</p>
* @author hzy
* @since 2022-11-10
*/
@RestController
@RequestMapping("/api/express")
public class ExpressDeliveryController extends AbstractController<ExpressDelivery>{

    private ExpressDeliveryService expressDeliveryService;

    //注入
    @Autowired
    public ExpressDeliveryController(ExpressDeliveryService expressDeliveryService) {
        super(expressDeliveryService);
        this.expressDeliveryService=expressDeliveryService;
    }
    //根据id查找一个
    @PostMapping("/findOne")
    public Result<ExpressDelivery> longIdFindOne(@RequestParam("deliveryId")Long deliveryId){
        return expressDeliveryService.longIdFindOne(deliveryId);
    }
    //管理员修改订单(用json,1揽收,9拒绝)
    @PutMapping("/update1")
    public Result<ExpressDelivery> update(@RequestBody ExpressDelivery record){
        boolean flag = expressDeliveryService.updateById(record);
        if (!flag) {
            return Result.error();
        }
        return Result.ok(record);
    }
    //获取整个订单号
    @GetMapping("/one1/{id}")
    public Result<ExpressDelivery> findOne(@PathVariable("id")Integer id){
        ExpressDelivery t = coreService.getById(id);
        return Result.ok(t);
    }
}

