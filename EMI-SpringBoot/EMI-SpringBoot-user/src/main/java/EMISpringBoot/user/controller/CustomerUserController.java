package EMISpringBoot.user.controller;


import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDto;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import EMISpringBoot.model.user.pojos.CustomerUser;
import EMISpringBoot.user.service.CustomerUserService;
import EMISpringBoot.common.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 控制器</p>
 *
 * @author hzy
 * @since 2022-11-10
 */
@RestController
@RequestMapping("/api/user")
public class CustomerUserController extends AbstractController<CustomerUser> {

    private CustomerUserService customerUserService;

    //注入
    @Autowired
    public CustomerUserController(CustomerUserService customerUserService) {
        super(customerUserService);
        this.customerUserService = customerUserService;
    }

    //新建用户
    @PostMapping("/save")
    public Result<CustomerUser> save(@RequestBody CustomerUser user) {
        String s = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(s);
        System.out.println("user = " + user);
        return super.save(user);
//        boolean flag = adminUserService.save(adminUser);
//        if (!flag) {
//            return Result.error();
//        }
//        return Result.ok(adminUser);
    }

    //登录方法
    @PostMapping("/login")
    public Result<Map<String, Object>> UserLogin(@RequestBody CustomerUser user) {
        return customerUserService.login(user);
    }

    //用户进行发货
    @PostMapping("/submit")
    public Result submit(@RequestBody ExpressDelivery expressDelivery) {
        return customerUserService.submit(expressDelivery);
    }

    //根据用户id来查询相关信息,一次查10条,
    @PostMapping("/userGetById")
    public Result UserFindDelivery(@RequestBody ExpressDeliveryConfigDto dto) {
        System.out.println("ExpressDeliveryConfigDto = " + dto);
        return customerUserService.UserFindDelivery(dto);
    }
}

