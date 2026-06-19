package org.tony.toolbox.designer.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.tony.toolbox.designer.bean.PublicSiteConfigBean
import org.tony.toolbox.designer.service.CategoryService
import org.tony.toolbox.designer.service.SiteConfigService
import org.tony.toolbox.designer.service.WorkService
import org.tony.toolbox.utils.responseUtil.JsonWrapper


/**
 * 公开 - API 控制器
 */
@RestController
@RequestMapping("/api/public")
class PublicController(
    private val workService: WorkService,
    private val categoryService: CategoryService,
    private val siteConfigService: SiteConfigService,
) {

    /**
     * 列表 - 作品
     */
    @GetMapping("/works")
    fun listWorks(@RequestParam(required = false) category: String?): String {
        val categoryId = category?.let { categoryService.getPublicCategory(it).id }
        return JsonWrapper.execute(
            callable = { workService.listPublicWorks(categoryId) },
            from = "PublicController.listWorks",
        )
    }


    /**
     * 详情 - 作品
     */
    @GetMapping("/works/{slug}")
    fun getWork(@PathVariable slug: String): String {
        return JsonWrapper.execute(
            callable = { workService.getPublicWork(slug) },
            from = "PublicController.getWork",
        )
    }


    /**
     * 列表 - 分类
     */
    @GetMapping("/categories")
    fun listCategories(): String {
        return JsonWrapper.execute(
            callable = { categoryService.listPublicCategories() },
            from = "PublicController.listCategories",
        )
    }


    /**
     * 详情 - 分类
     */
    @GetMapping("/categories/{slug}")
    fun getCategory(@PathVariable slug: String): String {
        return JsonWrapper.execute(
            callable = { categoryService.getPublicCategory(slug) },
            from = "PublicController.getCategory",
        )
    }


    /**
     * 获取 - 站点配置（公开，不含管理员凭证）
     */
    @GetMapping("/site-config")
    fun getSiteConfig(): String {
        return JsonWrapper.execute(
            callable = { PublicSiteConfigBean.from(siteConfigService.getConfig()) },
            from = "PublicController.getSiteConfig",
        )
    }
}
