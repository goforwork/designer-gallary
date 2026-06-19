package org.tony.toolbox.designer.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.tony.toolbox.designer.service.AuthService
import org.tony.toolbox.utils.exceptionUtil.CheckException


/**
 * 管理后台 - 登录拦截器
 */
@Component
class AdminAuthInterceptor(
    private val authService: AuthService,
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val user = authService.getCurrentUser(request)
        if (user == null) {
            response.status = 401
            response.contentType = "application/json;charset=UTF-8"
            response.writer.write(
                """{"success":false,"code":"UNAUTHORIZED","message":"请先登录","data":null}""",
            )
            return false
        }
        return true
    }
}
