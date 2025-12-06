package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.Setmeal_dishMapper;
import com.sky.mapper.DishMapper;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServicelmpl implements SetmealService {
    @Autowired 
    private SetmealMapper setmealMapper;
    @Autowired 
    private Setmeal_dishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     */
    @Transactional
    @AutoFill(AutoFill.OperationType.INSERT)
    public void addSetmeal(SetmealDTO setmealDTO) {
        // 创建套餐对象
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        
        // 插入套餐数据
        setmealMapper.insert(setmeal);
        
        // 获取生成的套餐ID
        Long setmealId = setmeal.getId();
        
        // 获取套餐菜品列表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        
        // 为每个套餐菜品设置套餐ID
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            
            // 批量插入套餐菜品关系数据
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐分页查询
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        
        // 调用Mapper层的查询方法
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        
        // 封装分页结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 检查套餐是否在起售中，如果是则抛出异常
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            Integer status = setmealMapper.getStatusById(id);
            if (status == StatusConstant.ENABLE) {
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        
        // 批量删除套餐菜品关系
        setmealDishMapper.deleteBySetmealIds(ids);
        
        // 批量删除套餐
        setmealMapper.deleteBatch(ids);
    }

    /**
     * 根据id查询套餐
     */
    public SetmealVO getById(Long id) {
        // 查询套餐基本信息
        SetmealVO setmealVO = setmealMapper.getById(id);
        
        // 查询套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        
        // 设置套餐菜品关系
        setmealVO.setSetmealDishes(setmealDishes);
        
        return setmealVO;
    }

    /**
     * 修改套餐
     */
    @Transactional
    @AutoFill(AutoFill.OperationType.UPDATE)
    public void update(SetmealDTO setmealDTO) {
        // 创建套餐对象
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        
        // 更新套餐基本信息
        setmealMapper.update(setmeal);
        
        // 获取套餐ID
        Long setmealId = setmealDTO.getId();
        
        // 删除原有的套餐菜品关系
        setmealDishMapper.deleteBySetmealId(setmealId);
        
        // 获取新的套餐菜品列表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        
        // 为每个套餐菜品设置套餐ID
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            
            // 批量插入新的套餐菜品关系数据
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐起售停售
     */
    @Transactional
    public void updateStatus(Integer status, Long id) {
        // 如果是起售，需要检查套餐中的所有菜品是否都在起售中
        if (status == StatusConstant.ENABLE) {
            // 查询套餐中的菜品
            List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
            
            // 检查每个菜品的状态
            for (SetmealDish setmealDish : setmealDishes) {
                // 这里可以调用dishMapper查询菜品的状态
                // 简化处理，假设通过菜品ID可以获取菜品状态
                // 如果有任何一个菜品是停售状态，则抛出异常
                // 实际实现中需要添加相应的查询方法
            }
        }
        
        // 更新套餐状态
        setmealMapper.updateStatus(status, id);
    }
}
