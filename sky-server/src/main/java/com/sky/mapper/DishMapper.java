package com.sky.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     */
    Integer addDish(Dish dish);

    /**
     * 菜品分页查询
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
    
    /**
     * 更新菜品
     */
    void updateDish(Dish dish);
    
    /**
     * 根据id删除菜品
     */
    void deleteDish(Long id);
    
    /**
     * 根据id查询菜品
     */
    Dish getDishById(Long id);
    
    /**
     * 根据分类id查询菜品
     */
    List<Dish> getDishByCategoryId(Long categoryId);
    
    /**
     * 批量删除菜品
     */
    void deleteBatch(List<Long> ids);
}
