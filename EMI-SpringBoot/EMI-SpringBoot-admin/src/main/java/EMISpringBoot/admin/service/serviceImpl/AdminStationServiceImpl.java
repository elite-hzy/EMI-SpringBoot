package EMISpringBoot.admin.service.serviceImpl;

import EMISpringBoot.admin.mapper.AdminStationMapper;
import EMISpringBoot.admin.service.AdminStationService;
import EMISpringBoot.model.admin.pojos.AdminStation;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminStationServiceImpl extends ServiceImpl<AdminStationMapper, AdminStation>implements AdminStationService {
}
