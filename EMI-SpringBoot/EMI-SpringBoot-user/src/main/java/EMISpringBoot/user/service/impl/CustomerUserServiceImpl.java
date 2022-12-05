package EMISpringBoot.user.service.impl;

import EMISpringBoot.admin.feign.AdminFeign;
import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.common.exception.AppHttpCodeEnum;
import EMISpringBoot.common.exception.LeadNewsException;
import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import EMISpringBoot.model.user.pojos.CustomerUser;
import EMISpringBoot.user.mapper.CustomerUserMapper;
import EMISpringBoot.user.service.CustomerUserService;
import EMISpringBoot.utils.ThreadLocalUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.*;

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
public class CustomerUserServiceImpl extends ServiceImpl<CustomerUserMapper, CustomerUser> implements CustomerUserService {
    @Autowired
    private AdminFeign adminFeign;

    @Override
    public Result<Map<String, Object>> login(CustomerUser user) {
        //先判断有没有传入用户内容
        if (user == null) {
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (StringUtils.isEmpty(user.getAccount()) || StringUtils.isEmpty(user.getPassword())) {
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //1.校验用户名和密码是否正确
        //这里找密码是通过名字来找用户
        QueryWrapper<CustomerUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", user.getAccount());
        CustomerUser customerUser = this.getOne(queryWrapper);
        //判断用户名
        if (customerUser == null) {
            throw new LeadNewsException(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);//用户不存在
        }
        //判断密码,这里用md5来判断
        if (!customerUser.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
            throw new LeadNewsException(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);//密码错误
        }
        //2.生成token字符串
        customerUser.setPassword(null);
        try {
            String userJson = JSON.toJSONString(customerUser);//序列化user
            JwtBuilder jwtBuilder = Jwts.builder(); //获得JWT构造器
            Map<String, Object> map = new Hashtable<>();
            map.put("key", userJson);
            map.put("status","customer");
            String token = jwtBuilder.setSubject("hello") //设置用户数据
                    .setIssuedAt(new Date()) //设置jwt生成时间
                    .setId("1") //设置id为token id
                    .setClaims(map) //通过map传值
                    .setExpiration(new Date(System.currentTimeMillis() + 5000000)) //设置token有效期
                    .signWith(SignatureAlgorithm.HS256, "qianfeng") //设置token加密方式和密码
                    .compact(); //生成token字符串
            //3.封装token和用户信息返回给前端
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("token", token);
            returnMap.put("user", customerUser);
            return Result.ok(returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result submit(ExpressDelivery expressDelivery) {
        CustomerUser customerUser = (CustomerUser) ThreadLocalUtils.get();
        HashMap<String, String> hashMap = new HashMap<>();
        Date date1 = new Date();
        List<Map> list1=new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = format.format(date1);
        hashMap.put("where","null");
        hashMap.put("status","用户申请寄件");
        hashMap.put("date",format1);
        list1.add(hashMap);
        expressDelivery.setDeliveryMessage(JSON.toJSONString(list1));
        expressDelivery.setAddressee(customerUser.getUserid());
        //这里要通知下管理员
        adminFeign.save(expressDelivery);
        ThreadLocalUtils.remove();
        return Result.ok();
    }
}
