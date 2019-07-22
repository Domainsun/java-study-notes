package com.domain.hello.mybatisplus.service.impl;

import com.domain.hello.mybatisplus.entity.TbContent;
import com.domain.hello.mybatisplus.mapper.TbContentMapper;
import com.domain.hello.mybatisplus.service.ITbContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author domain
 * @since 2019-07-19
 */
@Service
public class TbContentServiceImpl extends ServiceImpl<TbContentMapper, TbContent> implements ITbContentService {

}
