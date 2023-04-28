package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 条件查询
     *
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新商品数量
     *
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插⼊购物⻋数据
     *
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 查看购物车
     *
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> listShoppingCart(ShoppingCart shoppingCart);

    /**
     * 删除购物车中一种商品
     *
     * @param shoppingCartDTO
     * @return
     */
    void delete(ShoppingCartDTO shoppingCartDTO);

    /**
     * 删除购物车中一个商品
     *
     * @param shoppingCartDTO
     * @return
     */
    void deleteOne(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     *
     * @return
     */
    void deleteByUserId(Long userId);
}
