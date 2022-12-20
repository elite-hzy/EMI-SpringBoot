package EMISpringBoot.model.expressDelivery.pojos;

import EMISpringBoot.common.json.Long2StringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("express_delivery")
@Accessors(chain=true)
public class ExpressDelivery implements Serializable {


    //自定义json序列化器
    //快递单号id
    @TableId(value = "delivery_id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deliveryId;
    //允许操作的部门(json)
    @TableField("allow_station_change")
    private String allowStationChange;
    //物流信息(json)
    @TableField("delivery_message")
    private String deliveryMessage;
    @TableField("price")
    private Integer price;
    //单位kg
    @TableField("weight")
    private Integer weight;
    //发件人id
    @TableField("sender_name")
    private String senderName;
    //收件人id
    @TableField("addressee_name")
    private String addresseeName;
    //备注信息
    @TableField("express_notes")
    private String expressNotes;
     //收件人手机号
    @TableField("addressee_phone")
    private String addresseePhone;
    //寄件人手机号
    @TableField("sender_phone")
    private String senderPhone;
    //寄件人地址
    @TableField("sender_address")
    private String senderAddress;
    //收件人地址
    @TableField("addressee_address")
    private String addresseeAddress;

}
