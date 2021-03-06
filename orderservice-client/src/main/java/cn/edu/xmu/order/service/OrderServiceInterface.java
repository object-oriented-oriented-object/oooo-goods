package cn.edu.xmu.order.service;

public interface OrderServiceInterface {
    Long getSkuIdByOrderItemId(Long orderItemId);

    Boolean isOrderItemBelongToUser(Long orderItemId, Long userId);
}
