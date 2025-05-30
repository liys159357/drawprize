package com.ss.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ss.user.domain.dto.LoginFormDTO;
import com.ss.user.domain.po.User;
import com.ss.user.domain.vo.UserLoginVO;


/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
public interface IUserService extends IService<User> {


    UserLoginVO login(LoginFormDTO loginFormDTO);



    void register(User user);


    User getUserById(Long id);

}
