package test;

import EMISpringBoot.user.service.CustomerUserService;
import EMISpringBoot.user.userApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = userApplication.class)
public class test1 {
    @Autowired
    private CustomerUserService customerUserService;

    @Test
    public void  test(){

    }
}
