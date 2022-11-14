package EMISpringBoot.model.admin.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("admin_station")
public class AdminStation implements Serializable {

    @TableId(value = "station_id", type = IdType.AUTO)
    private Integer expressId;

    @TableField("station_name")
    private String stationName;
}
