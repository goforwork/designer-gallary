package org.tony.toolbox.utils.log

/**
 * 方法日志注解：用于 AOP 统一记录入参、出参、耗时与异常。
 *
 * - 默认开启：args/result/exception
 * - 建议用于：对外接口、关键业务方法、链路关键节点
 * - 不建议用于：高频低价值方法（避免日志/序列化开销）
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Log(
    /** 是否记录返回值 */
    val result: Boolean = true,
)

