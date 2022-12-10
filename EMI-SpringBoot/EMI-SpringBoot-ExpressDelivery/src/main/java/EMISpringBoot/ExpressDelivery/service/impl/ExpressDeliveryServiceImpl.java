package EMISpringBoot.ExpressDelivery.service.impl;


import EMISpringBoot.ExpressDelivery.mapper.ExpressDeliveryMapper;
import EMISpringBoot.ExpressDelivery.service.ExpressDeliveryService;
import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.common.exception.AppHttpCodeEnum;
import EMISpringBoot.common.exception.LeadNewsException;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDeliveryConfig;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hzy
 * @since 2022-11-10
 */
@Service
@Transactional
public class ExpressDeliveryServiceImpl extends ServiceImpl<ExpressDeliveryMapper, ExpressDelivery> implements ExpressDeliveryService {

    @Override
    public Result longIdFindOne(Long deliveryId) {
        ExpressDelivery expressDelivery = getById(deliveryId);
        if (expressDelivery==null){
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        return Result.ok(expressDelivery);
    }
}
