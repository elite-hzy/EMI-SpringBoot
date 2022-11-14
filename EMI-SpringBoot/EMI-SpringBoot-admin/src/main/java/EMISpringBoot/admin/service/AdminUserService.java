package EMISpringBoot.admin.service;

import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface AdminUserService extends IService<AdminUser> {
    Result<Map<String, Object>> login(AdminUser user);

    Result adminSubmitDelivery(Integer id, Integer uid,Integer adminId);
}
