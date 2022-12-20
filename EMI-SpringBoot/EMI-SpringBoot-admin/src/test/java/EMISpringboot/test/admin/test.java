package EMISpringboot.test.admin;

import EMISpringBoot.admin.feign.AdminFeign;
import EMISpringBoot.admin.service.AdminUserService;
import EMISpringBoot.utils.JsonUtils;
import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.model.expressDelivery.pojos.ExpressDelivery;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = adminApplication.class)
public class test {

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private AdminUserService adminUserService;

    @Test
    public void test1() {
        String abc = "Abc";
        String userJson = JSON.toJSONString(abc);//序列化user
        JwtBuilder jwtBuilder = Jwts.builder(); //获得JWT构造器
        Map<String, Object> map = new Hashtable<>();
//        Map<String,Object> map=new Hashtable<>();
        map.put("key", userJson);
        map.put("int", new Integer(1));
        String token = jwtBuilder.setSubject("hello") //设置用户数据
                .setIssuedAt(new Date()) //设置jwt生成时间
                .setId("1") //设置id为token id
                .setClaims(map) //通过map传值
                .setExpiration(new Date(System.currentTimeMillis() + 5000)) //设置token有效期
                .signWith(SignatureAlgorithm.HS256, "qianfeng") //设置token加密方式和密码
                .compact(); //生成token字符串
        System.out.println(token);
        token = token;

        JwtParser jwtParser = Jwts.parser(); //获取jwt解析器
        jwtParser.setSigningKey("qianfeng");

        try {

            //如果token正确(密码，有效期)则正常运行，否则抛出异常
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            Claims body = claimsJws.getBody();//获取body

            String subject = body.getSubject();//获取body中subject中的值
            System.out.println("subject = " + subject);
            String key = body.get("key", String.class);//获取Claims中map的值
            Integer anInt = body.get("int", Integer.class);
            System.out.println("anInt = " + anInt);
            System.out.println("key = " + key);
//            User user = JSON.parseObject(key, User.class);//反序列化user
//            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (SignatureException e) {
            System.out.println("签名错误");
            e.printStackTrace();
//            return new ResponseEntity<User>(new User(),HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @Test
    public void test02() {
        //3.校验token是否合法,（包含是否过期），不合法，返回401状态码（代表权限不足）'
        JwtParser jwtParser = Jwts.parser(); //获取jwt解析器
        jwtParser.setSigningKey("qianfeng");
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJrZXkiOiJ7XCJuYW1lXCI6XCIxMjMyMTNcIixcInBvc2l0aW9uXCI6XCIxMzIxXCIsXCJ1c2VySWRcIjo0fSIsImV4cCI6MTY2ODI0NjcxMX0.VT17KT78YdmZmwDTgFKKd9Uj0doN8pgiAa7YozJPyT0";
        try {
            //如果token正确(密码，有效期)则正常运行，否则抛出异常
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            Claims body = claimsJws.getBody();//获取body
            System.out.println("body = " + body);
            String key = body.get("key", String.class);
            AdminUser adminUser = JSON.parseObject(key, AdminUser.class);
            System.out.println("adminUser = " + adminUser);
        } catch (Exception e) {
            System.out.println("签名错误");
            throw new RuntimeException(e);
        }

    }

    @Test
    public void test03() {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "1");
        String s = JSON.toJSONString(map);
        System.out.println("s = " + s);
        Map<String, String> map1 = JSON.parseObject(s, Map.class);
        map1.put("2", "2");
        System.out.println("map1 = " + map1);

    }

    @Test
    public void test04() {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "1");
        String s = JSON.toJSONString(map);
        //{"1":"1"}
        System.out.println(s);
        Map map1 = JSON.parseObject(s, Map.class);
        if (map1.containsValue(1)) {
            System.out.println("true");
        } else {
            System.out.println(map1);
        }
    }

    @Test
    public void test05() throws JsonProcessingException {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "1");
        String json = JSON.toJSONString(map);
        JSONObject jsonObject = JSONObject.parseObject(json);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNodes = objectMapper.readValue(json, ObjectNode.class);
        //删除是remove 添加是put
        jsonNodes.put("2", "2");
        String json1 = objectMapper.writeValueAsString(jsonNodes);
        System.out.println("json1 = " + json1);
        JsonNode jsonNode = jsonNodes.get("2");

        Map map1 = JSON.parseObject(json1, Map.class);
        System.out.println("map1 = " + map1);


        //json转实体类
        //        ObjectMapper objectMapper = new ObjectMapper();
        //        Object data = adminFeign.findOne(uid).getData();
        //        ExpressDelivery record = objectMapper.convertValue(data, ExpressDelivery.class);
    }

    @Test
    public void test06() {
        //修改
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "1");
        String json = JSON.toJSONString(map);
        JSONObject jsonObject = JSONObject.parseObject(json);
        System.out.println("jsonObject = " + jsonObject);
        jsonObject.put("123", "12123");
        String nodeJson = (String) JsonUtils.getNodeJson(jsonObject, "123");
        System.out.println("nodeJson = " + nodeJson);
        JsonUtils.updateJson(jsonObject, "1", "老师你为什么那么懒");
        System.out.println("jsonObject = " + jsonObject);

        //     String json="{\"unitDTO\":{\"name\":\"顺德门诊部\",\"unitNumber\":10007},\"loginId\":\"sddella\",\"previewflag\":\"0\",\"busiStage\":\"\",\"isOnline\":\"1\",\"personBaseInfoDTO\":{\"transferAccountType\":\"1\",\"householdAddress\":\"广东省未知\",\"migrantFlag\":\"0\"},\"busiState\":\"1\",\"riskViewParams\":\"[{\\\"aac001\\\":123456}]\",\"officeDTO\":{\"contactNoteNumber\":\"441284JY031\",\"tgtAgencyState\":\"440606\",\"householdAddress\":\"广东省未知\",\"changeDate\":20220211,\"srcAgencyState\":\"441284\",\"newInsurance\":\"120\",\"sysTraceId\":\"10332134\",\"sex\":\"1\"},\"settleDTO\":{\"insurance\":\"110\",\"remark\":\"\",\"transDirection\":\"0\",\"changeDate\":20220211,\"transRegion\":\"1\",\"unitName\":\"顺德门诊部\",\"transWay\":\"1\"},\"officeList\":[{\"remark\":\"\",\"contactPhone\":\"1234\"},{\"remark\":\"\",\"contactPhone\":\"1234\"}]}";
        //        JSONObject val = JSONObject.parseObject(json);
        //        String node="unitDTO";
        //        //判断节点是否存在
        //        if (!val.containsKey(node)){
        //            System.out.println("节点数据为空");
        //            return;
        //        }
        //        System.out.println("=============根据节点修改数据=============");
        //        System.out.println("更新前数据：" + val);
        //        Object obj = JsonUtils.updateNodeJson(val, node, "name", "123456");
        //        System.out.println("数据更新成功："+JsonUtils.flag);
        //        System.out.println("更新后数据：" + obj);
        //        System.out.println("=============根据节点获取数据=============");
        //        Object nodeJson = JsonUtils.getNodeJson(val, node);
        //        System.out.println("节点数据：" + nodeJson);
        //        System.out.println("============根据字段名获取数据============");
        //        String contactNoteNumber = JsonUtils.getJsonParam(json, "contactNoteNumber");
        //        System.out.println("字段数据：" + contactNoteNumber);
        //        System.out.println("==========修改数据中所有匹配的key==========");
        //        Object updateJson=JsonUtils.updateJson(val,"householdAddress","广东省深圳市");
        //        System.out.println("更新后数据：" + updateJson);
    }

    // 时间 状态 去哪里的路程 转成map,可以增加 存进list
    // 编号 :"" ,"disabled":"flase"
    @Test
    public void test07() {
        HashMap<String, String> hashMap = new HashMap<>();
        Date date = new Date();
        String s = date.toString();
//        System.out.println("s = " + s);

        hashMap.put("time", new Date().toString());
        hashMap.put("status", "131");
        hashMap.put("local", "131");
        ArrayList<Map> maps = new ArrayList<>();
        maps.add(hashMap);

        HashMap<String, String> map1 = new HashMap<>();
        map1.put("time", new Date().toString());
        map1.put("status", "131");
        map1.put("local", "131");

        maps.add(map1);
        String s1 = JSON.toJSONString(maps);
        System.out.println("s1 = " + s1);
        //修改 +删
        List<Map> maps1 = JsonUtils.toList(s1, Map.class);
        for (Map map : maps1) {
            if (map.get("local").equals("131")) {
//                map.put("local", "111");
                maps1.remove(map);
            }
        }
        String s2 = JSON.toJSONString(maps1);
        System.out.println("s2 = " + s2);

    }


    @Test
    public void test09() {
        //封装成一个统一的添加库,要传入 我们要获取到人家的可修改名单
        Integer uid = 4;
        ObjectMapper objectMapper = new ObjectMapper();
        ExpressDelivery delivery = objectMapper.convertValue(adminFeign.findOne(uid).getData(), ExpressDelivery.class);
        addAuthCheckPosition(5, false, delivery);
    }

    //快递员所属的站点编号(添加或修改)
    //编号 :"" ,"disabled":"flase"
    //实际传肯定要有个数据表单实体类,返回值可以是一个快递信息的实体类
    public void addAuthCheckPosition(Integer deliveryId, boolean disabled, ExpressDelivery delivery) {

        //这是一个map集合的json,可以先转为map
        String change = delivery.getAllowStationChange();
        System.out.println("change = " + change);
        HashMap<String, String> map = new HashMap<>();
        List<Map> maps = new ArrayList<>();
        if (StringUtils.isNotEmpty(change)) {
            maps = JsonUtils.toList(change, Map.class);
        }
        String position = adminUserService.getById(deliveryId).getPosition().toString();
        if (StringUtils.isNotEmpty(change)){
            //先判断这个list里有没有,如果有就直接修改
            for (Map<String, String> map1 : maps) {
                if (StringUtils.isNotEmpty(map1.get("position"))){
                    map1.put(position, String.valueOf(disabled));
                }
            }
        }
        //没有就要自己加
        map.put(adminUserService.getById(deliveryId).getPosition().toString(), String.valueOf(disabled));
        System.out.println("maps = " + maps);
        System.out.println("map = " + map);
        maps.add(map);
        String jsonString = JSON.toJSONString(maps);
        delivery.setAllowStationChange(jsonString);
        adminFeign.update(delivery);
    }

    @Test
    public void test10() {
//        DateFormat df3 = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);
//        DateFormat df7 = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.CHINA);
//        String date3 = df3.format(new Date());
//        String time3 = df7.format(new Date());
//        String date = date3+" "+time3;
//
//        Date date1 = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
//        String format1 = format.format(date1);
        ObjectMapper objectMapper = new ObjectMapper();
        ExpressDelivery delivery = objectMapper.convertValue(adminFeign.findOne(1).getData(), ExpressDelivery.class);

        addDeliveryAddress("派送中",delivery,"华商");
    }

    // 时间 状态 去哪里的路程 转成map,可以增加 存进list
    // 这里是可以添加
    public void addDeliveryAddress(String status, ExpressDelivery delivery, String where) {
        //包裹着map的list json
        String deliveryMessage = delivery.getDeliveryMessage();
        List<Map> maps = new ArrayList<>();
        if (StringUtils.isNotEmpty(deliveryMessage)) {
            maps = JsonUtils.toList(deliveryMessage, Map.class);
        }
        Date date1 = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String format1 = format.format(date1);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("date", format1);
        hashMap.put("status", status);
        hashMap.put("where", where);

        maps.add(hashMap);
        String s = JSON.toJSONString(maps);
        delivery.setDeliveryMessage(s);
        adminFeign.update(delivery);
    }

    //起码要传入快递员所属的站点,给我一个订单号,快递员id,直接就是要实体类吧
    //调用方法:调用上面的checkAuth(判断管理员或快递员是否有调用的权限)
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

    /*
        调用方法:调用上面的checkAuth(判断管理员或快递员是否有调用的权限)
     */
    @Test
    public void checkAuthChoose() {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpressDelivery delivery = objectMapper.convertValue(adminFeign.findOne(3).getData(), ExpressDelivery.class);
        AdminUser adminUser = new AdminUser();
        adminUser.setUserId(5);
        adminUser.setLevel(1);
        adminUser.setPosition(1);
        boolean b = checkAuth(delivery, adminUser);
        System.out.println("b = " + b);
    }


    public boolean DeliveryIsDisable() {
        //判断expressNote
        ObjectMapper objectMapper = new ObjectMapper();
        ExpressDelivery delivery = objectMapper.convertValue(adminFeign.findOne(10).getData(), ExpressDelivery.class);
        String json = delivery.getExpressNotes();
        Map map = JSON.parseObject(json, Map.class);
        if (map.get("text").equals("9")) {
            return false;
        }
        return true;
    }


    //
    @Test
    public void test14() {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpressDelivery delivery = objectMapper.convertValue(adminFeign.findOne(10).getData(), ExpressDelivery.class);
        String expressNotes = delivery.getExpressNotes();
        Map<String, String> map = JSON.parseObject(expressNotes, Map.class);
        System.out.println("map = " + map);
        if (map.get("text").equals("9")) {
            //备注消息为拒绝,所以无法使用
            System.out.println("false");
        } else {

            System.out.println("true");
        }
    }
    //测试时间格式
    @Test
    public void test15(){
        Date date1 = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String format1 = format.format(date1);
        System.out.println("format1 = " + format1);
    }
}
