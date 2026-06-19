package org.tony.toolbox.designer.service

import org.springframework.stereotype.Service
import org.tony.toolbox.designer.bean.SiteConfigBean
import org.tony.toolbox.designer.bean.param.UpdateSiteConfigParam
import org.tony.toolbox.designer.dao.SiteConfigFileDao
import org.tony.toolbox.utils.errorUtil.ErrorCode
import org.tony.toolbox.utils.exceptionUtil.CheckException


/**
 * 站点配置 - 业务服务
 */
@Service
class SiteConfigService(
    private val siteConfigFileDao: SiteConfigFileDao,
) {

    /**
     * 获取 - 站点配置
     */
    fun getConfig(): SiteConfigBean {
        return siteConfigFileDao.readConfig()
    }


    /**
     * 更新 - 站点配置
     */
    fun updateConfig(param: UpdateSiteConfigParam): SiteConfigBean {
        val old = siteConfigFileDao.readConfig()

        // 校验 - 管理员密码
        val adminPassword = when {
            param.adminPassword == null -> old.adminPassword
            param.adminPassword.isEmpty() -> throw CheckException(
                ErrorCode.PARAMETER_ERROR.ec,
                "管理员密码不能为空",
            )
            param.adminPassword.length < 6 -> throw CheckException(
                ErrorCode.PARAMETER_ERROR.ec,
                "管理员密码长度不能少于 6 位",
            )
            else -> param.adminPassword
        }

        val updated = old.copy(
            siteTitle = param.siteTitle ?: old.siteTitle,
            siteDescription = param.siteDescription ?: old.siteDescription,
            brandName = param.brandName ?: old.brandName,
            contactEmail = param.contactEmail ?: old.contactEmail,
            contactPhone = param.contactPhone ?: old.contactPhone,
            socialLinks = param.socialLinks ?: old.socialLinks,
            footerText = param.footerText ?: old.footerText,
            heroConfig = param.heroConfig ?: old.heroConfig,
            adminUsername = param.adminUsername ?: old.adminUsername,
            adminPassword = adminPassword,
        )
        siteConfigFileDao.saveConfig(updated)
        return updated
    }
}
