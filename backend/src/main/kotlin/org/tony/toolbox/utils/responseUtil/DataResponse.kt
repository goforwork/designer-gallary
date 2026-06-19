package org.tony.toolbox.utils.responseUtil

import com.alibaba.fastjson2.annotation.JSONField
import org.tony.toolbox.utils.errorUtil.ErrorCode


/**
 * 数据响应类
 */
class DataResponse (
    val ec: Int,
    val em: String,
    val data: Any? = null
) {
    companion object {
        fun success(data: Any?): DataResponse {
            return DataResponse(ErrorCode.SUCCESS.ec, ErrorCode.SUCCESS.em, data)
        }

        fun error(ec: Int, em: String, data: Any? = null): DataResponse {
            return DataResponse(ec, em, data)
        }
    }

    @JSONField(serialize = false)
    fun isSuccess(): Boolean {
        return ec == ErrorCode.SUCCESS.ec
    }
}