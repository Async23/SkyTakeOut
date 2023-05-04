package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.exception.BaseException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderMapper;
import com.sky.properties.ShopProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import com.sky.service.OrderService;
import com.sky.utils.BaiduMapUtil;
import com.sky.utils.Location;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单
 */
@Slf4j
@Api(tags = "C 端订单接口")
@RequestMapping("/user/order")
@RestController("userOrderController")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private BaiduMapUtil baiduMapUtil;
    @Autowired
    private ShopProperties shopProperties;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        AddressBook orderAddress = addressBookService.getById(ordersSubmitDTO.getAddressBookId());
        String orderAddressStr = orderAddress.getProvinceName() + orderAddress.getCityName() + orderAddress.getDistrictName() + orderAddress.getDetail();

        Double distance = Double.valueOf(baiduMapUtil.getDistance(baiduMapUtil.getLocation(orderAddressStr)));
        if (distance > Double.valueOf(shopProperties.getDistance())) {
            // 订单超出配送范围
            throw new BaseException(MessageConstant.ORDER_OUT_OF_DELIVERY_RANGE);
        }
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);

        // 下单提醒测试
        /*List<OrderVO> orderVOList = orderMapper.listByCondition(OrdersPageQueryDTO.builder()
                .userId(BaseContext.getCurrentId())
                .build());
        if (orderVOList != null && orderVOList.size() > 0) {
            OrderVO order = orderVOList.get(0);
            String number = order.getNumber();
            Long orderId = order.getId();
            // orderService.paySuccess(number);
            Map<String, Object> map = new HashMap<>();
            // 消息类型， 1 表示来单提醒
            map.put("type", 1);
            map.put("orderId", orderId);
            map.put("content", "订单号： " + number);
            // 通过 WebSocket 实现来单提醒，向客户端浏览器推送消息
            webSocketServer.sendToAllClient(JSON.toJSONString(map));
        }*/
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 历史订单查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("历史订单查询：{}", ordersPageQueryDTO);
        PageResult ordersPageResult = orderService.historyOrders(ordersPageQueryDTO);

        return Result.success(ordersPageResult);
    }

    /**
     * 根据 id 查询订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> selectById(@PathVariable Long id) {
        OrderVO orderVO = orderService.selectById(id);

        return Result.success(orderVO);
    }

    /**
     * 用户取消订单
     *
     * @param id
     * @return
     */
    @ApiOperation("用户取消订单")
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id) {
        orderService.cancel(id);

        return Result.success();
    }

    /**
     * 再来一单
     *
     * @param id
     * @return
     */
    @ApiOperation("再来一单")
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable Long id) {
        log.info("再来一单 id:{}", id);
        orderService.repetition(id);

        return Result.success();
    }

    /**
     * ⽤户催单
     *
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("⽤户催单")
    public Result reminder(@PathVariable("id") Long id) {
        orderService.reminder(id);
        return Result.success();
    }

}
