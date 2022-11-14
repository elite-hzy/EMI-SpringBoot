package EMISpringBoot.admin.service.serviceImpl;

import EMISpringBoot.admin.feign.AdminFeign;
import EMISpringBoot.admin.service.AdminUserService;
import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.common.exception.AppHttpCodeEnum;
import EMISpringBoot.common.exception.LeadNewsException;
import EMISpringBoot.common.utils.JsonUtils;
import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.admin.mapper.AdminUserMapper;

import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;

@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {
    @Autowired
    private AdminFeign adminFeign;

    @Override
    public Result<Map<String, Object>> login(AdminUser user) {
        //先判断有没有传入用户内容
        if (user == null) {
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())) {
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //1.校验用户名和密码是否正确
        //这里找密码是通过名字来找用户
        QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", user.getName());
        AdminUser adminUser = this.getOne(queryWrapper);
        //判断用户名
        if (adminUser == null) {
            throw new LeadNewsException(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);//用户不存在
        }
        //判断密码,这里用md5来判断
        if (!adminUser.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
            throw new LeadNewsException(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);//密码错误
        }
        //2.生成token字符串
        adminUser.setPassword(null);
        try {
            String userJson = JSON.toJSONString(adminUser);//序列化user
            JwtBuilder jwtBuilder = Jwts.builder(); //获得JWT构造器
            Map<String, Object> map = new Hashtable<>();
            map.put("key", userJson);
            map.put("status", "admin");
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
            returnMap.put("user", adminUser);
            return Result.ok(returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public Result adminSubmitDelivery(Integer id, Integer uid, Integer adminId) {

        if (id == null || uid == null) {
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //这里可以直接把json转为实体类
        ObjectMapper objectMapper = new ObjectMapper();
        Object data = adminFeign.findOne(uid).getData();
        ExpressDelivery record = objectMapper.convertValue(data, ExpressDelivery.class);


        System.out.println("record = " + record);
        HashMap<String, String> map = new HashMap<>();
        if (id == 1) {
            map.put("text", "1");
            map.put("message", "已审批");
            //这里同时还修改一下人家能修改的权限
            HashMap<String, String> map1 = new HashMap<>();
            String position = this.getById(adminId).getPosition().toString();
            //传入快递员部门的id

            // 编号 :"" ,"disabled":"flase"
            map1.put("id", position);
            map1.put("disabled","true");
            ArrayList<Map> MapList = new ArrayList<>();
            MapList.add(map1);
            record.setAllowStationChange(JSONObject.toJSONString(MapList));

        }
        if (id == 9) {
            map.put("text", "9");
            map.put("message", "已拒绝");
        }
        String s = JSONObject.toJSONString(map);
        System.out.println("s = " + s);
        record.setExpressNotes(s);
        record.setDeliveryMessage(s);
        adminFeign.update(record);
        return Result.ok();
    }

    //起码要传入快递员所属的站点,给我一个订单号,快递员id
    public boolean checkAuth(Integer expressDeliveryId, Integer expressUserId) {
        //获取快递员所属的站点
        AdminUser adminUser = this.getById(expressUserId);
        //总管理员直接不用权限
        if (adminUser.getLevel() == 0) {
            return true;
        }
        Integer position = adminUser.getPosition();
        //订单号来获取map数据
        ObjectMapper objectMapper = new ObjectMapper();
        Object data = adminFeign.findOne(expressDeliveryId).getData();
        ExpressDelivery record = objectMapper.convertValue(data, ExpressDelivery.class);

        Map<String,Integer> map = JSON.parseObject(record.getAllowStationChange(), Map.class);

        return map.containsValue(position);
    }
}
