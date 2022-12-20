package EMISpringBoot.ExpressDelivery.controller;



import EMISpringBoot.ExpressDelivery.service.ExpressDeliveryConfigService;
import EMISpringBoot.common.controller.AbstractController;
import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDtoUser;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDeliveryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* <p>
*  控制器</p>
* @author heima
* @since 2022-12-08
*/
@RestController
@RequestMapping("/api/expressDeliveryConfig")
public class ExpressDeliveryConfigController extends AbstractController<ExpressDeliveryConfig> {

    private ExpressDeliveryConfigService expressDeliveryConfigService;

    //注入
    @Autowired
    public ExpressDeliveryConfigController(ExpressDeliveryConfigService expressDeliveryConfigService) {
        super(expressDeliveryConfigService);
        this.expressDeliveryConfigService=expressDeliveryConfigService;
    }
    //根据用户id来查询相关信息,一次查10条,
    @PostMapping("/userGetById")
    public Result UserFindDelivery(@RequestBody ExpressDeliveryConfigDtoUser dto){
        System.out.println("dto = " + dto);
        return expressDeliveryConfigService.UserFindDelivery(dto);
    }
    //管理员随便查
    @PostMapping("/adminFindExpress")
    public Result adminFindExpress(@RequestBody ExpressDeliveryConfigDto dto){
        return expressDeliveryConfigService.adminFindExpress(dto);
    }
    @PostMapping("/findOne")
    public Result<ExpressDeliveryConfig> longIdFindOne(@RequestParam("deliveryId")Long deliveryId){
        return expressDeliveryConfigService.longIdFindOne(deliveryId);
    }
    @GetMapping("/selectInf")
    public Result selectInf(){
        return expressDeliveryConfigService.selectInf();
    }
}

