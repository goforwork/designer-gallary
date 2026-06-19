package org.tony.toolbox.designer.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.tony.toolbox.designer.bean.param.LoginParam
import org.tony.toolbox.designer.service.AuthService
import org.tony.toolbox.utils.responseUtil.JsonWrapper


/**
 * 认证 - API 控制器
 */
@RestController
@RequestMapping("/api/admin/auth")
class AuthController(
    private val authService: AuthService,
) {

    /**
     * 登录
     */
    @PostMapping("/login")
    fun login(
        @RequestBody param: LoginParam,
        request: HttpServletRequest,
    ): String {
        return JsonWrapper.execute(
            callable = { authService.login(param, request) },
            from = "AuthController.login",
        )
    }


    /**
     * 登出
     */
    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): String {
        return JsonWrapper.execute(
            callable = {
                authService.logout(request)
                "ok"
            },
            from = "AuthController.logout",
        )
    }


    /**
     * 当前用户
     */
    @GetMapping("/me")
    fun me(request: HttpServletRequest): String {
        return JsonWrapper.execute(
            callable = { authService.getCurrentUser(request) },
            from = "AuthController.me",
        )
    }
}
