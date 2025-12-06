package com.sky.mapper;

import com.sky.entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ShopMapper {

    /**
     * 获取店铺状态
     * @return
     */
    @Select("select status from shop where id = 1")
    Integer getStatus();

    /**
     * 更新店铺状态
     * @param status
     */
    @Update("update shop set status = #{status}, update_time = now() where id = 1")
    void updateStatus(Integer status);

    /**
     * 初始化店铺信息
     * @param shop
     */
    void insert(Shop shop);
}
