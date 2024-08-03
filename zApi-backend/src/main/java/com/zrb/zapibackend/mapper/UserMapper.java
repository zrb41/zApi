package com.zrb.zapibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zrb.zapicommon.model.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
