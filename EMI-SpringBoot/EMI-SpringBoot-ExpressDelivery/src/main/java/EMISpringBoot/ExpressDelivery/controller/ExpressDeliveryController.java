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
    public Result longIdFindOne(@RequestParam("deliveryId")Long deliveryId){
        return expressDeliveryService.longIdFindOne(deliveryId);
    }
}

