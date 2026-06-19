package org.tony.toolbox.designer.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.tony.toolbox.designer.config.DesignerStorageConfig
import org.tony.toolbox.utils.errorUtil.ErrorCode
import org.tony.toolbox.utils.exceptionUtil.CheckException
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID


/**
 * 文件上传 - 业务服务
 */
@Service
class UploadService(
    private val designerStorageConfig: DesignerStorageConfig,
) {

    /**
     * 允许的图片类型
     */
    private val allowedContentTypes = setOf(
        "image/jpeg",
        "image/png",
        "image/webp",
        "image/gif",
    )

    /**
     * 允许的文件扩展名
     */
    private val allowedExtensions = setOf("jpg", "jpeg", "png", "webp", "gif")

    /**
     * 最大文件大小：5MB
     */
    private val maxFileSize = 5L * 1024 * 1024


    /**
     * 上传 - 作品图片
     */
    fun uploadWorkImage(file: MultipartFile): String {
        validate(file)
        val extension = getExtension(file.originalFilename ?: "image.jpg")
        val filename = "${UUID.randomUUID()}.$extension"
        val targetPath = designerStorageConfig.workUploadsPath.resolve(filename)
        Files.copy(file.inputStream, targetPath)
        return "/uploads/works/$filename"
    }


    /**
     * 校验 - 文件
     */
    private fun validate(file: MultipartFile) {
        if (file.isEmpty) {
            throw CheckException(ErrorCode.PARAMETER_ERROR.ec, "上传文件为空")
        }
        if (file.size > maxFileSize) {
            throw CheckException(ErrorCode.PARAMETER_ERROR.ec, "文件大小不能超过 5MB")
        }
        val extension = getExtension(file.originalFilename ?: "")
        if (extension !in allowedExtensions) {
            throw CheckException(ErrorCode.PARAMETER_ERROR.ec, "仅支持 jpg、png、webp、gif 格式")
        }
        val contentType = file.contentType ?: ""
        if (contentType.isNotBlank() && contentType !in allowedContentTypes) {
            throw CheckException(ErrorCode.PARAMETER_ERROR.ec, "仅支持 jpg、png、webp、gif 格式")
        }
    }


    /**
     * 获取 - 文件扩展名
     */
    private fun getExtension(filename: String): String {
        val index = filename.lastIndexOf('.')
        return if (index >= 0 && index < filename.length - 1) {
            filename.substring(index + 1).lowercase()
        } else {
            "jpg"
        }
    }
}
