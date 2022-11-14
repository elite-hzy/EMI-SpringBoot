package EMISpringBoot.admin.controller;

import EMISpringBoot.admin.service.AdminStationService;
import EMISpringBoot.common.controller.AbstractController;
import EMISpringBoot.model.admin.pojos.AdminStation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/adminStation")
public class AdminStationController extends AbstractController<AdminStation> {

    @Autowired
    private AdminStationService adminStationService;

    public AdminStationController(AdminStationService adminStationService) {
        super(adminStationService);
        this.adminStationService=adminStationService;
    }
}
