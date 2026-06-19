package org.tony.toolbox.designer.dao

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.TypeReference
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


/**
 * JSON 文件 DAO 基类
 */
abstract class JsonFileDao<T>(
    private val filePath: () -> Path,
    private val type: Type,
) {
    private val writeLock = ReentrantLock()


    /**
     * 读取 - 全部数据
     */
    open fun readAll(): T {
        val path = filePath()
        if (!Files.exists(path)) {
            return defaultValue()
        }
        val json = Files.readString(path, StandardCharsets.UTF_8)
        if (json.isBlank()) {
            return defaultValue()
        }
        return JSON.parseObject(json, type)
    }


    /**
     * 写入 - 全部数据（原子替换）
     */
    open fun writeAll(data: T) {
        writeLock.withLock {
            val path = filePath()
            Files.createDirectories(path.parent)
            val tempPath = path.parent.resolve(path.fileName.toString() + ".tmp")
            val json = JSON.toJSONString(data)
            Files.writeString(tempPath, json, StandardCharsets.UTF_8)
            try {
                Files.move(tempPath, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
            } catch (_: Exception) {
                Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }


    /**
     * 默认值
     */
    protected abstract fun defaultValue(): T


    companion object {
        /**
         * 构建 TypeReference
         */
        inline fun <reified T> typeRef(): TypeReference<T> = object : TypeReference<T>() {}
    }
}
