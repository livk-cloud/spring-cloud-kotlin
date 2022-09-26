package com.livk.provider.biz.mapper;

import com.livk.provider.api.domain.Users;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * UserRepository
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@Mapper
public interface UserMapper {

    @Select("select * from users")
    List<Users> selectList();

    @Insert("insert info users (username,password) values (#{username},#{password})")
    int insert(Users users);

    @Delete("delete from users where id = #{id}")
    int deleteById(@Param("id") Long id);
}
