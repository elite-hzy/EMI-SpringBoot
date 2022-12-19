package EMISpringBoot.model.admin.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin_user")
public class AdminUser {

    //实体类用小驼峰,数据库用下划线
    @TableId(value = "user_id",type = IdType.AUTO)
    private Integer userId;
    @TableField("account")
    private String  account;
    //0总管理员, 1为管理员 2为普通快递员
    @TableField("level")
    private Integer  level;
    //分配的站点 总管理员不需要分配站点,为0
    @TableField("position")
    private Integer  position;
    @TableField("password")
    private String  password;

}
