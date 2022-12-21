package EMISpringBoot.admin.service;

import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.model.expressDelivery.ExpressDeliveryChangeDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressAdminSubmitDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryDto;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface AdminUserService extends IService<AdminUser> {
    Result<Map<String, Object>> login(AdminUser user);

    Result adminSubmitDelivery(ExpressAdminSubmitDto dto);

    Result adminCancel(String request, Integer uid);

    Result adminFindExpress(ExpressDeliveryConfigDto dto);

    Result selectInf();

    Result changeExpress(ExpressDeliveryChangeDto dto);

    Result<Map<String,Object>> checkOneById(ExpressDeliveryDto dto);

}
