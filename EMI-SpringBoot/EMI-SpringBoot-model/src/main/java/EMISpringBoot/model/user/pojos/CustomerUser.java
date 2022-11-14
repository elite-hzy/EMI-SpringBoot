package EMISpringBoot.model.user.pojos;

import EMISpringBoot.common.dtos.Result;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author hzy
 * @since 2022-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("customer_user")
public class CustomerUser implements Serializable {


    @TableId(value = "userId", type = IdType.AUTO)
    private Integer userid;

    @TableField("name")
    private String name;

    @TableField("phone")
    private String phone;

    @TableField("gender")
    private String gender;

    @TableField("password")
    private String password;

}
