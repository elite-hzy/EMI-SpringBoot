package EMISpringBoot.user.service;


import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryDto;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import EMISpringBoot.model.user.pojos.CustomerUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hzy
 * @since 2022-11-10
 */
public interface CustomerUserService extends IService<CustomerUser> {


    Result<Map<String, Object>> login(CustomerUser user);

    Result submit(ExpressDelivery expressDelivery);

    Result UserFindDelivery(ExpressDeliveryConfigDto dto);

    Result checkOneById(ExpressDeliveryDto dto);
}
