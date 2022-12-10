package EMISpringBoot.model.expressDelivery.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author hzy
 * @since 2022-12-8
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("express_delivery_config")
@Accessors(chain=true)
public class ExpressDeliveryConfig implements Serializable {
    /**
     * 主id
     */
    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;

    /**
     * 绑定的主表id(运单号:)
     */
    @TableField("express_id")
    private Long expressId;

    /**
     * 快递状态
     */
    @TableField("status")
    private String status;

    /**
     * 收件人id
     */
    @TableField("addressee")
    private Integer addressee;

    /**
     * 寄件人id
     */
    @TableField("sender")
    private Integer sender;
    /**
     * 寄件人姓名
     */
    @TableField("sender_name")
    private String senderName;
    /**
     * 寄件人姓名
     */
    @TableField("addressee_name")
    private String addresseeName;
    /**
     * 更新时间
     */
    @TableField("create_time")
    private Date createTime;


}