package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BaseException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        log.info(this.toString());
        log.info(Thread.currentThread().toString());
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        // 只能查询⾃⼰的购物⻋数据
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 判断当前商品是否在购物⻋中
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList != null && shoppingCartList.size() == 1) {
            // 如果已经存在，就更新数量，数量加 1
            shoppingCart = shoppingCartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        } else {
            // 如果不存在，插⼊数据，数量就是 1
            // 判断当前添加到购物⻋的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // 添加到购物⻋的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 添加到购物⻋的是套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public List<ShoppingCart> listShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();

        return shoppingCartMapper.listShoppingCart(shoppingCart);
    }

    /**
     * 删除购物车中一个商品
     *
     * @param shoppingCartDTO
     * @return
     */
    @Override
    public void deleteOne(ShoppingCartDTO shoppingCartDTO) {
        if (shoppingCartDTO == null) {
            // 删除购物车中商品参数有误
            throw new BaseException(MessageConstant.SHOPPING_CART_DELETE_ILLEGAL);
        }

        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.listShoppingCart(shoppingCart);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            // 删除购物车中商品参数有误
            throw new BaseException(MessageConstant.SHOPPING_CART_DELETE_ILLEGAL);
        }

        // 判断所删除商品的个数
        shoppingCarts.forEach(sc -> {
            if (sc.getNumber().equals(1)) {
                // 如果只有一个，就直接删除
                shoppingCartMapper.delete(shoppingCartDTO);
            } else {
                // 如果个数不止一个，就删除一个
                shoppingCartMapper.deleteOne(shoppingCartDTO);
            }
        });

    }

    /**
     * 清空购物车
     *
     * @return
     */
    @Override
    public void clean() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }
}
