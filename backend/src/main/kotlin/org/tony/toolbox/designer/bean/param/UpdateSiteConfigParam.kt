package org.tony.toolbox.designer.bean.param

import org.tony.toolbox.designer.bean.SiteConfigBean


/**
 * 更新 - 站点配置参数
 */
data class UpdateSiteConfigParam(
    val siteTitle: String? = null,
    val siteDescription: String? = null,
    val brandName: String? = null,
    val contactEmail: String? = null,
    val contactPhone: String? = null,
    val socialLinks: List<SiteConfigBean.SocialLink>? = null,
    val footerText: String? = null,
    val heroConfig: SiteConfigBean.HeroConfig? = null,
    val adminUsername: String? = null,
    val adminPassword: String? = null,
)
