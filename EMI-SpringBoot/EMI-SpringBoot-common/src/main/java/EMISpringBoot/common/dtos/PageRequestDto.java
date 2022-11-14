package EMISpringBoot.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageRequestDto<T> implements Serializable {
    //当前页码
    private Long page = 1L;
    //每页显示的行
    private Long size = 10L;
    //请求体实体对象
    private T body;
}
