package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;

import org.springframework.web.bind.annotation.*;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    
    @Autowired DishService dishService;

    /**
     * 新增菜品
     */
    @ApiOperation("新增菜品")
    @PostMapping()
    public Result<String> addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品{}",dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     */
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> queryDish(DishPageQueryDTO dishPageQueryDTO){
        log.info("请求分页查询菜品，参数{}",dishPageQueryDTO);
        PageResult pageResult = dishService.queryDish(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 修改菜品
     */
    @ApiOperation("修改菜品")
    @PutMapping()
    public Result<String> updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品: {}", dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
    }
    
    /**
     * 批量删除菜品
     */
    @ApiOperation("批量删除菜品")
    @DeleteMapping()
    public Result<String> deleteBatch(@RequestParam List<Long> ids){
        log.info("批量删除菜品: {}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }
    
    /**
     * 根据id查询菜品
     */
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("根据id查询菜品: {}", id);
        DishVO dishVO = dishService.getDishById(id);
        return Result.success(dishVO);
    }
    
    /**
     * 根据分类id查询菜品
     */
    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result<List<Dish>> getDishByCategoryId(@RequestParam Long categoryId){
        log.info("根据分类id查询菜品: {}", categoryId);
        List<Dish> dishList = dishService.getDishByCategoryId(categoryId);
        return Result.success(dishList);
    }
    
    /**
     * 菜品起售停售
     */
    @ApiOperation("菜品起售停售")
    @PostMapping("/status/{status}")
    public Result<String> updateDishStatus(@PathVariable Integer status, @RequestParam Long id){
        log.info("菜品起售停售: id={}, status={}", id, status);
        dishService.updateDishStatus(status, id);
        return Result.success();
    }
}
