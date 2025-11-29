package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品
     */
    void addDish(DishDTO dishDTO);
    
    /**
     * 分页查询菜品
     */
    PageResult queryDish(DishPageQueryDTO dishPageQueryDTO);
    
    /**
     * 修改菜品
     */
    void updateDish(DishDTO dishDTO);
    
    /**
     * 批量删除菜品
     */
    void deleteBatch(List<Long> ids);
    
    /**
     * 根据id查询菜品
     */
    DishVO getDishById(Long id);
    
    /**
     * 根据分类id查询菜品
     */
    List<Dish> getDishByCategoryId(Long categoryId);
    
    /**
     * 菜品起售停售
     */
    void updateDishStatus(Integer status, Long id);
} 
