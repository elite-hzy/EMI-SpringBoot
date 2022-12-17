package test;

import EMISpringBoot.model.user.pojos.CustomerUser;
import EMISpringBoot.user.service.CustomerUserService;
import EMISpringBoot.user.userApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = userApplication.class)
public class test1 {
    @Autowired
    private CustomerUserService customerUserService;

    @Test
    public void test(){
        CustomerUser NewUser = new CustomerUser();
        String password = DigestUtils.md5DigestAsHex("123".getBytes());
        NewUser.setPassword(password);
        NewUser.setAccount("13231");
        NewUser.setName("abc");
        customerUserService.save(NewUser);
    }
}
