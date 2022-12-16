package EMISpringBoot.user.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 用于扫描全局异常处理类的配置类
 */
@Configuration
@ComponentScan("EMISpringBoot.common.exception")  // 指定IOC对象的扫描目录
public class ExceptionConfig {
}
