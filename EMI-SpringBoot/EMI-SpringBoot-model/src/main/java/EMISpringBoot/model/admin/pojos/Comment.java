package EMISpringBoot.model.admin.pojos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment {
    private Integer id;
    private String content;
    private String author;
    private Integer aId;
}