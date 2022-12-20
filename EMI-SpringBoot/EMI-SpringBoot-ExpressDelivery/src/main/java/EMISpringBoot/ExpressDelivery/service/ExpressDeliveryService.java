package EMISpringBoot.ExpressDelivery.service;


import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hzy
 * @since 2022-11-10
 */
public interface ExpressDeliveryService extends IService<ExpressDelivery> {

    Result<ExpressDelivery> longIdFindOne(Long deliveryId);

}
