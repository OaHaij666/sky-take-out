package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.mapper.DishFlavorMapper;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品功能
     */
    @Transactional
    @AutoFill(AutoFill.OperationType.INSERT)
    @Override
    public void addDish(DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        
        // 插入菜品信息
        dishMapper.addDish(dish);
        
        // 获取生成的菜品id
        Long dishId = dish.getId();
        
        // 处理口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });
            // 批量插入口味信息
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 分页查询菜品
     */
    @Override
    public PageResult queryDish(DishPageQueryDTO dishPageQueryDTO){
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //调用pagehelper帮助分页

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改菜品
     */
    @Transactional
    @AutoFill(AutoFill.OperationType.UPDATE)
    @Override
    public void updateDish(DishDTO dishDTO) {
        // 更新菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateDish(dish);
        
        // 删除原有的口味信息
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        
        // 处理新的口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dishDTO.getId());
            });
            // 批量插入口味信息
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 批量删除菜品
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        // 批量删除菜品信息
        dishMapper.deleteBatch(ids);
        
        // 删除相关的口味信息
        ids.forEach(dishId -> {
            dishFlavorMapper.deleteByDishId(dishId);
        });
    }

    /**
     * 根据id查询菜品
     */
    @Override
    public DishVO getDishById(Long id) {
        // 查询菜品信息
        Dish dish = dishMapper.getDishById(id);
        
        // 查询菜品口味信息
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        
        // 构建返回对象
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        
        return dishVO;
    }

    /**
     * 根据分类id查询菜品
     */
    @Override
    public List<Dish> getDishByCategoryId(Long categoryId) {
        return dishMapper.getDishByCategoryId(categoryId);
    }

    /**
     * 菜品起售停售
     */
    @AutoFill(AutoFill.OperationType.UPDATE)
    @Override
    public void updateDishStatus(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.updateDish(dish);
    }
}
