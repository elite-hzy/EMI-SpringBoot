package EMISpringBoot.ExpressDelivery.service.impl;


import EMISpringBoot.ExpressDelivery.mapper.ExpressDeliveryConfigMapper;
import EMISpringBoot.ExpressDelivery.service.ExpressDeliveryConfigService;
import EMISpringBoot.common.dtos.PageInfo;
import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.common.exception.AppHttpCodeEnum;
import EMISpringBoot.common.exception.LeadNewsException;
import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDtoUser;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDeliveryConfig;
import EMISpringBoot.model.user.pojos.CustomerUser;
import EMISpringBoot.utils.ThreadLocalUtils;
import com.alibaba.nacos.common.utils.ThreadUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author heima
 * @since 2022-12-08
 */
@Service
@Transactional
public class ExpressDeliveryConfigServiceImpl extends ServiceImpl<ExpressDeliveryConfigMapper, ExpressDeliveryConfig> implements ExpressDeliveryConfigService {

    @Override
    public Result UserFindDelivery(ExpressDeliveryConfigDtoUser dto) {
        try {
            //查询快递单号
            QueryWrapper<ExpressDeliveryConfig> queryWrapper = new QueryWrapper<>();
            //先判断是想看寄的快递,还是收的快递

            if (dto.getType().equals(1)) {
                //寄的快递
                queryWrapper.eq("addressee", dto.getCustomerUserId());
            } else if (dto.getType().equals(0)) {
                queryWrapper.eq("sender", dto.getCustomerUserId());
            }
            //时间降序
            queryWrapper.orderByDesc("create_time");
            //一次显示10条,一共显示5条
            IPage<ExpressDeliveryConfig> iPage = new Page<>(dto.getPage(), dto.getSize());
            iPage = page(iPage, queryWrapper);
            return Result.ok(iPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ThreadLocalUtils.remove();
        }
    }

    @Override
    public Result adminFindExpress(ExpressDeliveryConfigDto dto) {
        IPage<ExpressDeliveryConfig> iPage = new Page<>(dto.getPage(), dto.getSize());
        QueryWrapper<ExpressDeliveryConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        iPage = page(iPage, queryWrapper);
        return Result.ok(iPage.getRecords());
    }

    @Override
    public Result<ExpressDeliveryConfig> longIdFindOne(Long deliveryId) {
        QueryWrapper<ExpressDeliveryConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("express_id", deliveryId);
        ExpressDeliveryConfig deliveryConfig = getOne(queryWrapper);
        if (deliveryConfig == null) {
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        return Result.ok(deliveryConfig);
    }

    @Override
    public Result selectInf() {
        //统计快递数量
        //已送达数量: 111 异常数量: 111
        HashMap<String, String> map = new HashMap<>();
        map.put("number", Integer.toString(count()));


        QueryWrapper<ExpressDeliveryConfig> queryWrapper = new QueryWrapper<>();
        //送达为6
        queryWrapper.eq("status", 6);
        map.put("delivered", Integer.toString(count(queryWrapper)));
        queryWrapper = new QueryWrapper<ExpressDeliveryConfig>();
        //异常为4
        queryWrapper.eq("status", 4);
        map.put("exception", Integer.toString(count(queryWrapper)));
        map.put("delivered 送达数量","exception 异常数量,number快递数量");
        return Result.ok(map);
    }
}

