package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插⼊订单明细数据
     *
     * @param orderDetails
     */
    void insertBatch(List<OrderDetail> orderDetails);

    /**
     * 根据订单 id 获取菜品信息
     *
     * @param orderId
     * @return
     */
    List<DishVO> listDishesByOrderId(Long orderId);
}
