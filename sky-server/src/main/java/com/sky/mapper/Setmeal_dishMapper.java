package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Setmeal_dishMapper {
    
    /**
     * 批量插入套餐菜品关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
    
    /**
     * 根据套餐id查询套餐菜品关系
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
    
    /**
     * 根据套餐id删除套餐菜品关系
     */
    void deleteBySetmealId(Long setmealId);
    
    /**
     * 根据套餐id批量删除套餐菜品关系
     */
    void deleteBySetmealIds(List<Long> setmealIds);
}
