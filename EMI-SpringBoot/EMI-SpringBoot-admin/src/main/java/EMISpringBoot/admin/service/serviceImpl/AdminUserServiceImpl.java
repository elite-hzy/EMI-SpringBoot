package EMISpringBoot.admin.service.serviceImpl;

import EMISpringBoot.ExpressDelivery.feign.ExpressDeliveryFeign;
import EMISpringBoot.admin.feign.AdminFeign;
import EMISpringBoot.admin.service.AdminUserService;
import EMISpringBoot.common.dtos.Result;
import EMISpringBoot.common.exception.AppHttpCodeEnum;
import EMISpringBoot.common.exception.LeadNewsException;
import EMISpringBoot.common.utils.JsonUtils;
import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.admin.mapper.AdminUserMapper;

import EMISpringBoot.model.expressDelivery.ExpressDeliveryChangeDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryConfigDto;
import EMISpringBoot.model.expressDelivery.dto.ExpressDeliveryDto;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDeliveryConfig;
import EMISpringBoot.utils.ThreadLocalUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private ExpressDeliveryFeign expressDeliveryFeign;

    @Override
    public Result<Map<String, Object>> login(AdminUser user) {
        //先判断有没有传入用户内容
        if (user == null) {
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (StringUtils.isEmpty(user.getAccount()) || StringUtils.isEmpty(user.getPassword())) {
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //1.校验用户名和密码是否正确
        //这里找密码是通过名字来找用户
        QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", user.getAccount());
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
            map.put("username", "admin");
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

    /*
        id是判快递员是否同意揽件 1同意 9拒绝
        断和ExpressDeliveryId(生成订单号)
     */
    @Override
    public Result adminSubmitDelivery(Integer id, Long ExpressDeliveryId) {
        //首先判断管理员有没有判断权限
        AdminUser adminUser = (AdminUser) ThreadLocalUtils.get();
        if (adminUser == null) {
            throw new LeadNewsException(AppHttpCodeEnum.NEED_LOGIN);
        }
        Integer userId = adminUser.getUserId();

        if (id == null || ExpressDeliveryId == null) {
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //这里可以直接把json转为实体类
        ObjectMapper objectMapper = new ObjectMapper();
        Object data = adminFeign.longIdFindOne(ExpressDeliveryId).getData();
        ExpressDelivery record = objectMapper.convertValue(data, ExpressDelivery.class);
        System.out.println("record = " + record);
        Object data1 = expressDeliveryFeign.longIdFindOne(ExpressDeliveryId).getData();
        ExpressDeliveryConfig deliveryConfig = objectMapper.convertValue(data1, ExpressDeliveryConfig.class);
//        deliveryConfig.setCreateTime(new Date());
        deliveryConfig.setCreateTime(new Date());
        System.out.println("deliveryConfig = " + deliveryConfig);
        if (record == null || deliveryConfig == null) {
            throw new LeadNewsException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        HashMap<String, String> map = new HashMap<>();
        if (id == 1) {
            deliveryConfig.setStatus("1");
            map.put("text", "1");
            map.put("message", "管理员已揽收");
            //这里同时还修改一下人家能修改的权限
            HashMap<String, String> map1 = new HashMap<>();
            String position = this.getById(userId).getPosition().toString();
            //传入快递员部门的id

            // 编号 :"" ,"disabled":"flase"
            map1.put(position, "true");
            ArrayList<Map> MapList = new ArrayList<>();
            MapList.add(map1);
            record.setAllowStationChange(JSONObject.toJSONString(MapList));

        }
        if (id == 9) {
            map.put("text", "9");
            map.put("message", "已拒绝");
            deliveryConfig.setStatus("0");
        }
        expressDeliveryFeign.update(deliveryConfig);
        String s = JSONObject.toJSONString(map);
        record.setExpressNotes(s);
        record = addDeliveryAddress("已揽收", record, "快递员已揽收");
        adminFeign.update(record);
        return Result.ok();
    }

    @Override
    public Result adminCancel(String request, Integer ExpressDeliveryId) {
        //首先判断管理员有没有判断权限
        AdminUser adminUser = (AdminUser) ThreadLocalUtils.get();
        if (adminUser == null) {
            throw new LeadNewsException(AppHttpCodeEnum.NEED_LOGIN);
        }
        Integer userId = adminUser.getUserId();
        AdminUser user = this.getById(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        ExpressDelivery delivery = objectMapper.convertValue(adminFeign.findOne(ExpressDeliveryId).getData(), ExpressDelivery.class);
        System.out.println("接收到的delivery = " + delivery);
        if (!checkAuth(delivery, user)) {
            throw new LeadNewsException(233, "权限不够");
        }
        //然后就可以直接来进行修改
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("text", "9");
        hashMap.put("message", request);
        String jsonString = JSON.toJSONString(hashMap);
        delivery.setExpressNotes(jsonString);

        System.out.println("我想要修改的地方delivery = " + delivery);
        adminFeign.update(delivery);
        return Result.ok();
    }

    //管理员查找快递
    @Override
    public Result adminFindExpress(ExpressDeliveryConfigDto dto) {
        AdminUser adminUser = (AdminUser) ThreadLocalUtils.get();
        if (adminUser == null) {
            throw new LeadNewsException(AppHttpCodeEnum.NEED_LOGIN);
        }
        return expressDeliveryFeign.adminFindExpress(dto);
    }

    @Override
    public Result selectInf() {
        //统计快递数量
        //已送达数量: 111 异常数量: 111

        return null;
    }

    //修改订单状态码信息
    //状态码
    //0拒绝接单
    //1确认接单 ->等待揽收
    //2站点收入
    //3运往下一个站点
    //4 快递异常
    //5 派件
    //6 签收
    @Override
    public Result changeExpress(ExpressDeliveryChangeDto dto) {
        //首先判断管理员有没有判断权限
        AdminUser adminUser = (AdminUser) ThreadLocalUtils.get();
        if (adminUser == null) {
            throw new LeadNewsException(AppHttpCodeEnum.NEED_LOGIN);
        }
        try {
            Long expressId = dto.getExpressId();
            Object data = adminFeign.longIdFindOne(expressId).getData();
            String statusPosition = "";
            switch (dto.getStatus()) {
                case 1:
                    statusPosition = "快递员确认接单";
                    break;
                case 2:
                    statusPosition = "站点收件";
                    break;
                case 3:
                    statusPosition = "运往下一个站点";
                    break;
                case 4:
                    statusPosition = "快递状态异常";
                    break;
                case 5:
                    statusPosition = "派件中";
                    break;
                case 6:
                    statusPosition = "已签收";
                    break;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            ExpressDelivery record = objectMapper.convertValue(data, ExpressDelivery.class);

            Object data1 = expressDeliveryFeign.longIdFindOne(dto.getExpressId()).getData();
            ExpressDeliveryConfig deliveryConfig = objectMapper.convertValue(data1, ExpressDeliveryConfig.class);
//            deliveryConfig.setCreateTime(new Date());
            deliveryConfig.setCreateTime(new Date());
            deliveryConfig.setStatus(statusPosition);
            ExpressDelivery delivery = addDeliveryAddress(statusPosition, record, dto.getLocation());
            System.out.println("delivery = " + delivery);
            System.out.println("deliveryConfig = " + deliveryConfig);
            delivery.setExpressNotes("1");
            delivery.setAllowStationChange("");
            adminFeign.update(delivery);
            expressDeliveryFeign.update(deliveryConfig);
            return Result.ok();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ThreadLocalUtils.remove();
        }
    }

    @Override
    public Result checkOneById(ExpressDeliveryDto dto) {
        Object data = adminFeign.longIdFindOne(dto.getExpressId()).getData();
        ObjectMapper objectMapper = new ObjectMapper();
        ExpressDelivery record = objectMapper.convertValue(data, ExpressDelivery.class);
        return Result.ok(record);
    }


    //起码要传入快递员所属的站点,给我一个订单号,快递员id,直接就是要实体类吧
    public boolean checkAuth(ExpressDelivery delivery, AdminUser adminUser) {
        //总管理员直接不用权限
        if (adminUser.getLevel() == 0) {
            return true;
        }
        Integer position = adminUser.getPosition();
        String positionString = position.toString();
        //通过订单消息来获取允许操作的部门
//        格式为 "部门号":true  map形式
        String allowStationChange = delivery.getAllowStationChange();
        List<Map> maps = JsonUtils.toList(allowStationChange, Map.class);
        System.out.println("maps = " + maps);
        for (Map map : maps) {
            if (map.get(positionString) != null && map.get(positionString).equals("true")) {
                return true;
            }
        }
        return false;
        //先取出对应的
    }

    //快递单号添加-快递员所属的站点编号(添加或修改)
    //编号 :"" ,"disabled":"flase"
    //实际传肯定要有个数据表单实体类,返回值可以是一个快递信息的实体类
    public ExpressDelivery addAuthCheckPosition(Integer deliveryId, boolean disabled, ExpressDelivery delivery) {
        if (!DeliveryIsDisable(delivery)) {
            throw new LeadNewsException(233, "快递单号不能修改");
        }

        //这是一个map集合的json,可以先转为map
        String change = delivery.getAllowStationChange();
        System.out.println("change = " + change);
        HashMap<String, String> map = new HashMap<>();
        List<Map> maps = new ArrayList<>();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(change)) {
            maps = JsonUtils.toList(change, Map.class);
        }
        String position = this.getById(deliveryId).getPosition().toString();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(change)) {
            //先判断这个list里有没有,如果有就直接修改
            for (Map<String, String> map1 : maps) {
                if (org.apache.commons.lang.StringUtils.isNotEmpty(map1.get("position"))) {
                    map1.put(position, String.valueOf(disabled));
                }
            }
        }
        //没有就要自己加
        map.put(this.getById(deliveryId).getPosition().toString(), String.valueOf(disabled));
        System.out.println("maps = " + maps);
        System.out.println("map = " + map);
        maps.add(map);
        String jsonString = JSON.toJSONString(maps);
        delivery.setAllowStationChange(jsonString);
        return delivery;
    }

    // 时间 状态 去哪里的路程 转成map,可以增加 存进list
    // 这里是可以添加
    public ExpressDelivery addDeliveryAddress(String status, ExpressDelivery delivery, String where) {
        //先校验有没有这个订单号的管理权限
//        if (!DeliveryIsDisable(delivery)) {
//            throw new LeadNewsException(233, "快递单号不能修改");
//        }
        //包裹着map的list json
        String deliveryMessage = delivery.getDeliveryMessage();
        List<Map> maps = new ArrayList<>();
        if (StringUtils.isNotEmpty(deliveryMessage)) {
            maps = JsonUtils.toList(deliveryMessage, Map.class);
        }
        System.out.println("maps 修改前 = " + maps);
//        Date date1 = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String format1 = format.format(date1);
        Date date = new Date();
        long time = date.getTime();
        String format1 = Long.toString(time / 1000);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("date", format1);
        hashMap.put("status", status);
        hashMap.put("where", where);
        maps.add(hashMap);
        System.out.println("maps 修改后 = " + maps);
        String s = JSON.toJSONString(maps);
        delivery.setDeliveryMessage(s);
        return delivery;
    }

    public boolean DeliveryIsDisable(ExpressDelivery delivery) {
        String json = delivery.getExpressNotes();
        Map map = JSON.parseObject(json, Map.class);
        if (map.get("text").equals("9")) {
            return false;
        }
        return true;
    }
}
