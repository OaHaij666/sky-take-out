package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐数量
     * @param categoryId
     * @return
     */
    @Select("select count(*) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
    
    /**
     * 新增套餐
     * @param setmeal
     */
    void insert(Setmeal setmeal);
    
    /**
     * 套餐分页查询
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    
    /**
     * 根据id查询套餐
     */
    SetmealVO getById(Long id);
    
    /**
     * 更新套餐
     */
    void update(Setmeal setmeal);
    
    /**
     * 删除套餐
     */
    void delete(Long id);
    
    /**
     * 批量删除套餐
     */
    void deleteBatch(List<Long> ids);
    
    /**
     * 根据套餐id查询套餐状态
     */
    @Select("select status from setmeal where id = #{id}")
    Integer getStatusById(Long id);
    
    /**
     * 更新套餐状态
     */
    void updateStatus(Integer status, Long id);
}
