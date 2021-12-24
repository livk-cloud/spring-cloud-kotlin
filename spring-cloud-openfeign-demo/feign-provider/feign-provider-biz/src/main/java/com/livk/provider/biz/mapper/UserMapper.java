package com.livk.provider.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.livk.provider.api.domain.Users;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * UserRepository
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@Mapper
public interface UserMapper extends BaseMapper<Users> {
}
