package com.livk.provider.biz.mapper

import com.livk.provider.api.domain.Users
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/**
 * @author livk
 */
@Mapper
interface UserMapper {
    @Select("select * from users")
    fun selectList(): List<Users>

    @Insert("insert info users (username,password) values (#{username},#{password})")
    fun insert(users: Users): Int

    @Delete("delete from users where id = #{id}")
    fun deleteById(@Param("id") id: Long): Int
}
