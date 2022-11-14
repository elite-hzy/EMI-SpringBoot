package EMISpringBoot.admin.controller;

import EMISpringBoot.admin.feign.AdminFeign;
import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.admin.service.AdminUserService;
import EMISpringBoot.common.controller.AbstractController;

import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController extends AbstractController<AdminUser> {
    private AdminUserService adminUserService;

    @Autowired
    private AdminFeign adminFeign;


    @Autowired
    public AdminUserController(AdminUserService adminUserService) {
        super(adminUserService);
        this.adminUserService = adminUserService;
    }

    //添加一条记录,使用md5
    @PostMapping("/save")
    public Result<AdminUser> save(@RequestBody AdminUser adminUser) {
        String s = DigestUtils.md5DigestAsHex(adminUser.getPassword().getBytes());
        adminUser.setPassword(s);
        System.out.println("adminUser = " + adminUser);
        return super.save(adminUser);
//        boolean flag = adminUserService.save(adminUser);
//        if (!flag) {
//            return Result.error();
//        }
//        return Result.ok(adminUser);
    }

    //登录方法
    @PostMapping("/login")
    public Result<Map<String, Object>> UserLogin(@RequestBody AdminUser user) {
        return adminUserService.login(user);
    }

    @PostMapping("/test")
    public void simpleTest(@RequestBody ExpressDelivery record) {
        Result<ExpressDelivery> save = adminFeign.save(record);
        System.out.println("save = " + save);
    }

    @PutMapping("auth_access/{id}")
    public Result adminSubmitDelivery(@PathVariable("id") Integer id,Integer positionId,Integer adminId){
        return adminUserService.adminSubmitDelivery(id,positionId,adminId);
    }




}
