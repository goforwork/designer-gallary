package org.tony.toolbox.designer.bean


/**
 * 公开站点配置
 *
 * 用于用户端展示，不包含管理员账号等敏感信息。
 */
data class PublicSiteConfigBean(
    /** 站点标题 */
    val siteTitle: String = "",

    /** 站点描述 */
    val siteDescription: String = "",

    /** 品牌名 */
    val brandName: String = "",

    /** 联系邮箱 */
    val contactEmail: String = "",

    /** 联系电话 */
    val contactPhone: String = "",

    /** 社交媒体链接 */
    val socialLinks: List<SiteConfigBean.SocialLink> = emptyList(),

    /** 页脚附加文本 */
    val footerText: String = "",

    /** 首页 Hero 配置 */
    val heroConfig: SiteConfigBean.HeroConfig = SiteConfigBean.HeroConfig(),
) {
    companion object {
        /**
         * 从完整站点配置构建公开配置
         */
        fun from(config: SiteConfigBean): PublicSiteConfigBean {
            return PublicSiteConfigBean(
                siteTitle = config.siteTitle,
                siteDescription = config.siteDescription,
                brandName = config.brandName,
                contactEmail = config.contactEmail,
                contactPhone = config.contactPhone,
                socialLinks = config.socialLinks,
                footerText = config.footerText,
                heroConfig = config.heroConfig,
            )
        }
    }
}
