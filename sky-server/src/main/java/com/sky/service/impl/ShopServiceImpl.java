package com.sky.service.impl;

import com.sky.entity.Shop;
import com.sky.mapper.ShopMapper;
import com.sky.service.ShopService;
import com.sky.context.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 店铺服务实现类
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    /**
     * 获取店铺营业状态
     * @return
     */
    @Override
    public Integer getStatus() {
        Integer status = shopMapper.getStatus();
        // 如果没有找到数据，初始化店铺信息
        if (status == null) {
            Shop shop = Shop.builder()
                    .name("苍穹外卖")
                    .status(1) // 默认营业状态
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .createUser(BaseContext.getCurrentId())
                    .updateUser(BaseContext.getCurrentId())
                    .build();
            shopMapper.insert(shop);
            return 1;
        }
        return status;
    }

    /**
     * 设置店铺营业状态
     * @param status
     */
    @Override
    @Transactional
    public void setStatus(Integer status) {
        shopMapper.updateStatus(status);
    }
}
