package com.sky.constant;

/**
 * 信息提示常量类
 */
public class MessageConstant {

    public static final String PASSWORD_ERROR = "密码错误";
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String ACCOUNT_LOCKED = "账号被锁定";
    public static final String UNKNOWN_ERROR = "未知错误";
    public static final String USER_NOT_LOGIN = "用户未登录";
    public static final String ALREADY_EXISTS = "账号已存在";

    /**
     * 店铺状态
     */
    public static final String UPDATE_SHOP_STATUS_ILLEGAL_ARGUMENT = "修改店铺状态参数有误";

    /**
     * 分类
     */
    public static final String CATEGORY_QUERY_BY_TYPE_ILLEGAL_ARGUMENT = "根据类型查询分类参数有误";
    public static final String CATEGORY_START_OR_STOP_ILLEGAL_ARGUMENT = "启用、禁用分类参数有误";
    public static final String CATEGORY_QUERY_BY_ID_ILLEGAL_ARGUMENT = "根据ID查询分类参数有误";
    public static final String CATEGORY_QUERY_PAGE_ILLEGAL_ARGUMENT = "分类分页查询参数有误";
    public static final String CATEGORY_BE_RELATED_BY_SETMEAL = "当前分类关联了套餐,不能删除";
    public static final String CATEGORY_BE_RELATED_BY_DISH = "当前分类关联了菜品,不能删除";
    public static final String CATEGORY_INSERT_ILLEGAL_ARGUMENT = "新增分类参数有误";
    public static final String CATEGORY_UPDATE_ILLEGAL_ARGUMENT = "修改分类参数有误";
    public static final String CATEGORY_DELETE_ILLEGAL_ARGUMENT = "删除分类参数有误";

    /**
     * 菜品
     */
    public static final String DISH_ON_SALE = "启售中的菜品不能删除";
    // 已修改：原为 "当前菜品关联了套餐,不能删除"
    public static final String DISH_BE_RELATED_BY_SETMEAL = "关联了套餐的菜品不能删除";
    public static final String DISH_QUERY_PAGE_ILLEGAL_ARGUMENT = "菜品分页查询参数有误";
    public static final String DISH_QUERY_ILLEGAL_ARGUMENT = "菜品查询参数有误";
    public static final String DISH_INSERT_ILLEGAL_ARGUMENT = "新增菜品参数有误";
    public static final String DISH_UPDATE_ILLEGAL_ARGUMENT = "修改菜品参数有误";
    public static final String DISH_DELETE_ILLEGAL_ARGUMENT = "删除菜品参数有误";
    public static final String DISH_START_OR_STOP_ILLEGAL_ARGUMENT = "启售、停售菜品状态参数有误";

    /**
     * 套餐
     */
    public static final String SETMEAL_START_OR_STOP_ILLEGAL_ARGUMENT = "启售、停售套餐参数有误";
    public static final String SETMEAL_QUERY_PAGE_ILLEGAL_ARGUMENT = "套餐分页查询参数有误";
    public static final String SETMEAL_ENABLE_FAILED = "套餐内包含未启售菜品，无法启售";
    public static final String SETMEAL_INSERT_ILLEGAL_ARGUMENT = "新增套餐参数有误";
    public static final String SETMEAL_UPDATE_ILLEGAL_ARGUMENT = "修改套餐参数有误";
    public static final String SETMEAL_QUERY_ILLEGAL_ARGUMENT = "套餐查询参数有误";
    public static final String SETMEAL_DELETE_ILLEGAL_ARGUMENT = "删除套餐参数有误";
    public static final String SETMEAL_ON_SALE = "启售中的套餐不能删除";

    /**
     * 购物车
     */
    public static final String SHOPPING_CART_IS_NULL = "购物车数据为空，不能下单";
    public static final String SHOPPING_CART_DELETE_ILLEGAL = "删除购物车中商品参数有误";

    public static final String ADDRESS_BOOK_IS_NULL = "用户地址为空，不能下单";
    public static final String LOGIN_FAILED = "登录失败";
    public static final String UPLOAD_FAILED = "文件上传失败";
    public static final String PASSWORD_EDIT_FAILED = "密码修改失败";

    /**
     * 订单
     */
    public static final String ORDER_STATUS_ERROR = "订单状态错误";
    public static final String ORDER_NOT_FOUND = "订单不存在";
    public static final String ORDER_QUERY_PAGE_ILLEGAL_ARGUMENT = "订单分页查询参数有误";
    public static final String ORDER_QUERY_BY_ID_ILLEGAL_ARGUMENT = "订单 id 查询参数有误";
    public static final String ORDER_CANCLE_ILLEGAL_ARGUMENT = "取消订单参数有误";
    public static final String ORDER_REJECTION_ILLEGAL_ARGUMENT = "拒绝订单参数有误";


}
