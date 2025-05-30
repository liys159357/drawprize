package com.ss.user.controller;


import com.ss.user.domain.dto.LoginFormDTO;
import com.ss.user.domain.po.User;
import com.ss.user.domain.vo.UserLoginVO;
import com.ss.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public UserLoginVO login(@RequestBody @Validated LoginFormDTO loginFormDTO){
        return userService.login(loginFormDTO);
    }
    /**
     * 用户注册接口
     */
    @ApiOperation("用户注册接口")
    @PostMapping("/register")
    public void register(@RequestBody @Validated User user){
        userService.register(user);
    }

    @ApiOperation("根据ID查询用户信息")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

}

