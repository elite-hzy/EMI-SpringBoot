package EMISpringBoot.admin.service;

import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.model.expressDelivery.ExpressDeliveryChangeDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryDto;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface AdminUserService extends IService<AdminUser> {
    Result<Map<String, Object>> login(AdminUser user);

    Result adminSubmitDelivery(Integer id, Long deliveryId);

    Result adminCancel(String request, Integer uid);

    Result adminFindExpress(ExpressDeliveryConfigDto dto);

    Result selectInf();

    Result changeExpress(ExpressDeliveryChangeDto dto);

    Result checkOneById(ExpressDeliveryDto dto);

}
