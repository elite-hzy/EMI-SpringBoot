package EMISpringBoot.ExpressDelivery.service;


import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDtoUser;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDeliveryConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author heima
 * @since 2022-12-08
 */
public interface ExpressDeliveryConfigService extends IService<ExpressDeliveryConfig> {

    Result UserFindDelivery(ExpressDeliveryConfigDtoUser dto);



    Result adminFindExpress(ExpressDeliveryConfigDto dto);

    Result<ExpressDeliveryConfig> longIdFindOne(Long deliveryId);

    Result selectInf();

}
