package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.BaseException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 异常情况的处理（收货地址为空、购物车为空）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);

        // 查询当前用户的购物车数据
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 构造订单数据
        com.sky.entity.Orders order = new com.sky.entity.Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(userId);
        order.setStatus(com.sky.entity.Orders.PENDING_PAYMENT);
        order.setPayStatus(com.sky.entity.Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        // 向订单表插入1条数据
        orderMapper.insert(order);

        // 订单明细数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }

        // 向明细表插入n条数据
        orderDetailMapper.insertBatch(orderDetailList);

        // 清理购物车中的数据
        shoppingCartMapper.deleteByUserId(userId);

        // 封装返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        // 调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                // 商户订单号
                ordersPaymentDTO.getOrderNumber(),
                // 支付金额，单位 元
                new BigDecimal(0.01),
                // 商品描述
                "苍穹外卖订单",
                // 微信用户的openid
                user.getOpenid()
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    @Override
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        com.sky.entity.Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        com.sky.entity.Orders orders = com.sky.entity.Orders.builder()
                .id(ordersDB.getId())
                .status(com.sky.entity.Orders.TO_BE_CONFIRMED)
                .payStatus(com.sky.entity.Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 订单条件搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult listByCondition(OrdersPageQueryDTO ordersPageQueryDTO) {
        if (ordersPageQueryDTO == null || ordersPageQueryDTO.getPage() == null || ordersPageQueryDTO.getPageSize() == null) {
            // 订单分页查询参数有误
            throw new BaseException(MessageConstant.ORDER_QUERY_PAGE_ILLEGAL_ARGUMENT);
        }

        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        // Mapper 层 TODO: 2023/4/29 orderDetailList 未填充
        List<OrderVO> orderVOList = orderMapper.listByCondition(ordersPageQueryDTO);
        orderVOList.forEach(orderVO -> {
            // Mapper 层
            List<OrderDetail> orderDetailList = orderDetailMapper.listByOrderId(orderVO.getId());
            orderDetailList.forEach(orderDetail -> orderVO.setOrderDishes(
                    (orderVO.getOrderDishes() == null ? "" : orderVO.getOrderDishes())
                            + orderDetail.getName() + "* " + orderDetail.getNumber() + "；"));
        });
        Page<OrderVO> page = (Page<OrderVO>) orderVOList;

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     * @return
     */
    @Override
    public void adminCancel(OrdersCancelDTO ordersCancelDTO) {
        if (ordersCancelDTO == null) {
            // 取消订单参数有误
            throw new BaseException(MessageConstant.ORDER_CANCLE_ILLEGAL_ARGUMENT);
        }

        orderMapper.update(
                Orders.builder()
                        .id(ordersCancelDTO.getId())
                        .status(6)
                        .cancelReason(ordersCancelDTO.getCancelReason())
                        .cancelTime(LocalDateTime.now())
                        .build());
    }

    /**
     * 根据 id 查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderVO selectById(Long id) {
        if (id == null) {
            // 订单 id 查询参数有误
            throw new BaseException(MessageConstant.ORDER_QUERY_BY_ID_ILLEGAL_ARGUMENT);
        }

        return orderMapper.selectById(id);
    }

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     * @return
     */
    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        if (ordersRejectionDTO == null) {
            // 拒绝订单参数有误
            throw new BaseException(MessageConstant.ORDER_REJECTION_ILLEGAL_ARGUMENT);
        }

        orderMapper.update(
                Orders.builder()
                        .id(ordersRejectionDTO.getId())
                        // TODO: 2023/4/29 拒单后状态
                        .status(6)
                        .rejectionReason(ordersRejectionDTO.getRejectionReason())
                        .build());

    }

    /**
     * 各个状态的订单数量统计
     *
     * @return OrderStatisticsVO
     */
    @Override
    public OrderStatisticsVO statistics() {
        return orderMapper.statistics();
    }

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     * @return
     */
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        if (ordersConfirmDTO == null || ordersConfirmDTO.getId() == null) {
            // 接单参数有误
            throw new BaseException(MessageConstant.ORDER_CONFIRM_ILLEGAL_ARGUMENT);
        }

        orderMapper.update(Orders.builder()
                // 订单状态 1待付款 2待接单 3 已接单 4 派送中 5 已完成 6 已取消 7 退款
                .status(ordersConfirmDTO.getStatus() == null ? 3 : ordersConfirmDTO.getStatus())
                .id(ordersConfirmDTO.getId())
                .build());
    }

    /**
     * 派送订单
     *
     * @param id
     * @return
     */
    @Override
    public void delivery(Long id) {
        if (id == null) {
            // 派送订单参数有误
            throw new BaseException(MessageConstant.ORDER_CONFIRM_DELIVERY_ILLEGAL_ARGUMENT);
        }

        orderMapper.update(Orders.builder()
                // 订单状态 1待付款 2待接单 3 已接单 4 派送中 5 已完成 6 已取消 7 退款
                .status(4)
                .id(id)
                .build());
    }

    /**
     * 完成订单
     *
     * @param id
     * @return
     */

    @Override
    public void complete(Long id) {
        if (id == null) {
            // 完成订单参数有误
            throw new BaseException(MessageConstant.ORDER_COMPLETE_DELIVERY_ILLEGAL_ARGUMENT);
        }

        orderMapper.update(Orders.builder()
                // 订单状态 1待付款 2待接单 3 已接单 4 派送中 5 已完成 6 已取消 7 退款
                .status(5)
                .id(id)
                .build());
    }

    /**
     * 历史订单查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        if (ordersPageQueryDTO == null || ordersPageQueryDTO.getPage() == null || ordersPageQueryDTO.getPageSize() == null) {
            // 订单分页查询参数有误
            throw new BaseException(MessageConstant.ORDER_QUERY_PAGE_ILLEGAL_ARGUMENT);
        }

        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<OrderVO> orderVOPage = (Page<OrderVO>) orderMapper.listByCondition(ordersPageQueryDTO);
        return new PageResult(orderVOPage.getTotal(), orderVOPage.getResult());
    }

    /**
     * 用户取消订单
     *
     * @param id
     * @return
     */
    @Override
    public void cancel(Long id) {
        if (id == null) {
            // 取消订单参数有误
            throw new BaseException(MessageConstant.ORDER_CANCLE_ILLEGAL_ARGUMENT);
        }

        adminCancel(OrdersCancelDTO.builder().id(id).build());
    }

    /**
     * 再来一单
     *
     * @param id
     * @return
     */
    @Override
    public void repetition(Long id) {
        if (id == null) {
            // 再来一单参数有误
            throw new BaseException(MessageConstant.ORDER_REPETITION_ILLEGAL_ARGUMENT);
        }

        List<OrderDetail> orderDetails = orderDetailMapper.listByOrderId(id);
        orderDetails.forEach(orderDetail -> shoppingCartService.addShoppingCart(ShoppingCartDTO.builder()
                .dishId(orderDetail.getDishId())
                .setmealId(orderDetail.getSetmealId())
                .dishFlavor(orderDetail.getDishFlavor())
                .build()
        ));
    }
}
