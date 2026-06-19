package org.tony.toolbox.designer.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.tony.toolbox.designer.bean.param.CreateCategoryParam
import org.tony.toolbox.designer.bean.param.CreateWorkParam
import org.tony.toolbox.designer.bean.param.UpdateCategoryParam
import org.tony.toolbox.designer.bean.param.UpdateSiteConfigParam
import org.tony.toolbox.designer.bean.param.UpdateWorkParam
import org.tony.toolbox.designer.service.CategoryService
import org.tony.toolbox.designer.service.SiteConfigService
import org.tony.toolbox.designer.service.UploadService
import org.tony.toolbox.designer.service.WorkService
import org.tony.toolbox.utils.responseUtil.JsonWrapper


/**
 * 管理后台 - API 控制器
 */
@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val workService: WorkService,
    private val categoryService: CategoryService,
    private val siteConfigService: SiteConfigService,
    private val uploadService: UploadService,
) {

    /**
     * 列表 - 作品
     */
    @GetMapping("/works")
    fun listWorks(): String {
        return JsonWrapper.execute(
            callable = { workService.listAllWorks() },
            from = "AdminController.listWorks",
        )
    }


    /**
     * 详情 - 作品
     */
    @GetMapping("/works/{id}")
    fun getWork(@PathVariable id: String): String {
        return JsonWrapper.execute(
            callable = { workService.getWork(id) },
            from = "AdminController.getWork",
        )
    }


    /**
     * 创建 - 作品
     */
    @PostMapping("/works")
    fun createWork(@RequestBody param: CreateWorkParam): String {
        return JsonWrapper.execute(
            callable = { workService.createWork(param) },
            from = "AdminController.createWork",
        )
    }


    /**
     * 更新 - 作品
     */
    @PatchMapping("/works/{id}")
    fun updateWork(
        @PathVariable id: String,
        @RequestBody param: UpdateWorkParam,
    ): String {
        return JsonWrapper.execute(
            callable = { workService.updateWork(id, param) },
            from = "AdminController.updateWork",
        )
    }


    /**
     * 删除 - 作品
     */
    @DeleteMapping("/works/{id}")
    fun deleteWork(@PathVariable id: String): String {
        return JsonWrapper.execute(
            callable = { workService.deleteWork(id) },
            from = "AdminController.deleteWork",
        )
    }


    /**
     * 列表 - 分类
     */
    @GetMapping("/categories")
    fun listCategories(): String {
        return JsonWrapper.execute(
            callable = { categoryService.listAllCategories() },
            from = "AdminController.listCategories",
        )
    }


    /**
     * 详情 - 分类
     */
    @GetMapping("/categories/{id}")
    fun getCategory(@PathVariable id: String): String {
        return JsonWrapper.execute(
            callable = { categoryService.getCategory(id) },
            from = "AdminController.getCategory",
        )
    }


    /**
     * 创建 - 分类
     */
    @PostMapping("/categories")
    fun createCategory(@RequestBody param: CreateCategoryParam): String {
        return JsonWrapper.execute(
            callable = { categoryService.createCategory(param) },
            from = "AdminController.createCategory",
        )
    }


    /**
     * 更新 - 分类
     */
    @PatchMapping("/categories/{id}")
    fun updateCategory(
        @PathVariable id: String,
        @RequestBody param: UpdateCategoryParam,
    ): String {
        return JsonWrapper.execute(
            callable = { categoryService.updateCategory(id, param) },
            from = "AdminController.updateCategory",
        )
    }


    /**
     * 删除 - 分类
     */
    @DeleteMapping("/categories/{id}")
    fun deleteCategory(@PathVariable id: String): String {
        return JsonWrapper.execute(
            callable = { categoryService.deleteCategory(id) },
            from = "AdminController.deleteCategory",
        )
    }


    /**
     * 获取 - 站点配置
     */
    @GetMapping("/site-config")
    fun getSiteConfig(): String {
        return JsonWrapper.execute(
            callable = { siteConfigService.getConfig() },
            from = "AdminController.getSiteConfig",
        )
    }


    /**
     * 更新 - 站点配置
     */
    @PatchMapping("/site-config")
    fun updateSiteConfig(@RequestBody param: UpdateSiteConfigParam): String {
        return JsonWrapper.execute(
            callable = { siteConfigService.updateConfig(param) },
            from = "AdminController.updateSiteConfig",
        )
    }


    /**
     * 上传 - 图片
     */
    @PostMapping("/upload")
    fun uploadImage(@RequestParam("file") file: MultipartFile): String {
        return JsonWrapper.execute(
            callable = { uploadService.uploadWorkImage(file) },
            from = "AdminController.uploadImage",
        )
    }
}
