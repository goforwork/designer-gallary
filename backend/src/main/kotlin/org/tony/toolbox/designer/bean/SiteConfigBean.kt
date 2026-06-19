package org.tony.toolbox.designer.bean


/**
 * 站点配置 - 领域对象
 */
data class SiteConfigBean(
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
    val socialLinks: List<SocialLink> = emptyList(),

    /** 页脚附加文本 */
    val footerText: String = "",

    /** 首页 Hero 配置 */
    val heroConfig: HeroConfig = HeroConfig(),

    /** 管理员用户名 */
    val adminUsername: String = "admin",

    /** 管理员密码（明文存储，方便后台查看） */
    val adminPassword: String = "",
) {
    /**
     * 社交媒体链接
     */
    data class SocialLink(
        /** 平台名称 */
        val name: String,

        /** 链接地址 */
        val url: String,
    )


    /**
     * 首页 Hero 配置
     */
    data class HeroConfig(
        /** 小标题 */
        val eyebrow: String = "",

        /** 主标题第一行 */
        val titleLine: String = "",

        /** 主标题强调词 */
        val titleEmphasis: String = "",

        /** 副标题第一行 */
        val subtitleLine1: String = "",

        /** 副标题第二行 */
        val subtitleLine2: String = "",

        /** 背景图片或视频 URL */
        val backgroundMedia: String = "",

        /** CTA 按钮文字 */
        val ctaText: String = "",

        /** CTA 跳转目标 */
        val ctaTarget: String = "",
    )
}
