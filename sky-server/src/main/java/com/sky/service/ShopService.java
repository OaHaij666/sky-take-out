package com.sky.service;

/**
 * 店铺服务接口
 */
public interface ShopService {

    /**
     * 获取店铺营业状态
     * @return
     */
    Integer getStatus();

    /**
     * 设置店铺营业状态
     * @param status
     */
    void setStatus(Integer status);
}
