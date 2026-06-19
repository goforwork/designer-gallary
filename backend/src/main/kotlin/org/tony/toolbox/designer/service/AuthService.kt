package org.tony.toolbox.designer.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import org.tony.toolbox.designer.bean.param.LoginParam
import org.tony.toolbox.utils.errorUtil.ErrorCode
import org.tony.toolbox.utils.exceptionUtil.CheckException


/**
 * 认证 - 业务服务
 *
 * 管理员账号信息存储在站点配置中（site-config.json），
 * 密码使用明文存储，方便在管理后台查看和修改。
 */
@Service
class AuthService(
    private val siteConfigService: SiteConfigService,
) {

    /**
     * 登录
     */
    fun login(param: LoginParam, request: HttpServletRequest): Map<String, String> {
        val config = siteConfigService.getConfig()
        if (param.username != config.adminUsername || param.password != config.adminPassword) {
            throw CheckException(ErrorCode.DATA_INVALID.ec, "用户名或密码错误")
        }
        request.session.setAttribute(SESSION_KEY, config.adminUsername)
        return mapOf("username" to config.adminUsername, "role" to "admin")
    }


    /**
     * 登出
     */
    fun logout(request: HttpServletRequest) {
        request.session.invalidate()
    }


    /**
     * 获取 - 当前登录用户
     */
    fun getCurrentUser(request: HttpServletRequest): Map<String, String>? {
        val username = request.session.getAttribute(SESSION_KEY) as? String ?: return null
        val config = siteConfigService.getConfig()
        return if (config.adminUsername == username) {
            mapOf("username" to username, "role" to "admin")
        } else {
            null
        }
    }


    companion object {
        const val SESSION_KEY = "designer_admin_user"
    }
}
