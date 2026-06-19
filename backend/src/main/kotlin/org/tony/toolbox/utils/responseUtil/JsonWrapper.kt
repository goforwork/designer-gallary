package org.tony.toolbox.utils.responseUtil

import com.alibaba.fastjson2.JSON
import org.slf4j.LoggerFactory
import org.tony.toolbox.utils.errorUtil.ErrorCode
import org.tony.toolbox.utils.exceptionUtil.CheckException
import org.tony.toolbox.utils.exceptionUtil.HighEfficientException
import org.tony.toolbox.utils.exceptionUtil.LowEfficientException
import java.util.concurrent.Callable


/**
 * json 包装器
 */
class JsonWrapper {
    companion object {
        private val log = LoggerFactory.getLogger(JsonWrapper::class.java)

        /**
         * 执行
         */
        fun <T> execute(callable: Callable<T>, from: String): String {
            try {
                val res = callable.call()

                // 记录 - 日志
                log.info(
                    "JsonWrapper.execute | from: {} | res: {}",
                    from,
                    JSON.toJSONString(res)
                )

                return JSON.toJSONString(DataResponse.success(res))
            } catch (e: CheckException) {
                return JSON.toJSONString(DataResponse.error(e.ec, e.em))
            } catch (e: LowEfficientException) {
                log.error("JsonWrapper.execute | 低效影响异常 | from: {} | error: ", from, e)
                return JSON.toJSONString(DataResponse.error(e.ec, e.em))
            } catch (e: HighEfficientException) {
                log.error("JsonWrapper.execute | 高效影响异常 | from: {} | error: ", from, e)
                return JSON.toJSONString(DataResponse.error(e.ec, e.em))
            } catch (e: Exception) {
                log.error("JsonWrapper.execute | 未知异常 | from: {} | error: ", from, e)
                return JSON.toJSONString(
                    DataResponse.error(
                        ErrorCode.UNKNOWN_ERROR.ec,
                        e.message ?: ErrorCode.UNKNOWN_ERROR.em
                    )
                )
            }
        }
    }
}