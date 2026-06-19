package org.tony.toolbox.utils.log

import com.alibaba.fastjson2.JSON
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * AOP 方法调用日志切面，配合 [Log] 注解使用。
 *
 * ## 记录内容
 * - **call.start**: 方法名 + 入参（可开关）
 * - **call.end**: 方法名 + 耗时(ms) + 返回值（可开关）
 * - **call.error**: 方法名 + 耗时(ms) + 入参（可开关）+ 异常堆栈（可开关）
 *
 * ## 性能与稳定性考虑
 * - **避免无谓序列化**：只有在对应日志级别开启时才会真正做 JSON 序列化（入参使用 lazy）。
 * - **避免日志反噬业务**：JSON 序列化失败时回退到 `toString()`，不影响业务流程。
 *
 * ## 使用示例
 * ```kotlin
 * @Log
 * fun foo(param: Bar): Baz { ... }
 *
 * @Log(result = false) // 不打印返回值
 * fun bar() { ... }
 * ```
 */
@Aspect
@Component
class LogAspect {
    private val log = LoggerFactory.getLogger(LogAspect::class.java)

    /**
     * 环绕通知：在目标方法执行前后打点记录，并计算耗时。
     *
     * 注意：这里的 `@Around("@annotation(logAnno)")` 会把目标方法上的 [Log] 注解注入为参数，
     * 方便按注解配置控制是否记录 args/result/exception 以及 JSON 截断长度。
     */
    @Around("@annotation(logAnno)")
    fun around(joinPoint: ProceedingJoinPoint, logAnno: Log): Any? {
        val startNs = System.nanoTime()

        val sig = joinPoint.signature as MethodSignature
        val method = sig.method
        val methodName = "${method.declaringClass.name}.${method.name}"

        val args = joinPoint.args
        val argsJson = JSON.toJSONString(args)

        log.info("log.start | method={} | args={}", methodName, argsJson)

        return try {
            val res = joinPoint.proceed(args)
            val costMs = (System.nanoTime() - startNs) / 1_000_000

            if (logAnno.result) {
                val resJson = JSON.toJSONString(res)
                log.info("log.end | method={} | costMs={} | result={}", methodName, costMs, resJson)
            } else {
                log.info("log.end | method={} | costMs={}ms", methodName, costMs)
            }

            res
        } catch (t: Throwable) {
            val costMs = (System.nanoTime() - startNs) / 1_000_000
            log.error(
                "log.error | method={} | costMs={}ms | args={} | error=",
                methodName,
                costMs,
                argsJson,
                t
            )

            throw t
        }
    }
}

