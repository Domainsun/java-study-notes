package com.domain.hello.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.domain.hello.mybatisplus.entity.TbUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author domain
 * @since 2019-07-18
 */
@Repository
public interface TbUserMapper extends BaseMapper<TbUser> {

     List<TbUser> getAll();
     /**
      * <p>
      * 查询 : 根据state状态查询用户列表，分页显示
      * 注意!!: 如果入参是有多个,需要加注解指定参数名才能在xml中取值
      * </p>
      *
      * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
      * @param state 状态
      * @return 分页对象
      */
     IPage<TbUser> selectPageVo(Page page, @Param("city") String city);

}
