package fun.hercules.order.order.platform.order.dto;

import fun.hercules.order.order.platform.exports.BusinessOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageResponse<T extends BusinessOrder> {
    private List<T> content;
    private Integer totalPages;
    private Integer curPage;
    private Integer pageSize;
    private Long totalElements;

}
