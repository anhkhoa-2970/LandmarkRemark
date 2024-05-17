package com.test.landmarkremark.data.base


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.greenrobot.eventbus.EventBus
import com.test.landmarkremark.R
import com.test.landmarkremark.utils.Constants
import com.test.landmarkremark.utils.Utils.getString
import com.test.landmarkremark.utils.Utils.isNetworkAvailable

fun <E, M> resultFlow(
    networkCall: suspend () -> NetworkResult<E>,
    mapper: Mapper<E, M>?
): Flow<NetworkResult<M>> = flow {
    emit(NetworkResult.Loading())
    processResultFlow(networkCall, mapper)
}.flowOn(Dispatchers.IO)

private suspend fun <E, M> FlowCollector<NetworkResult<M>>.processResultFlow(
    networkCall: suspend () -> NetworkResult<E>,
    mapper: Mapper<E, M>?
) {
    if (!isNetworkAvailable()) {
        emit(
            NetworkResult.Failure(
                message = getString(R.string.please_check_your_network),
                code = 9999
            )
        )
    } else {
        when (val responseStatus = networkCall.invoke()) {
            is NetworkResult.Success -> {
                emit(
                    NetworkResult.Success(
                        responseStatus.message,
                        responseStatus.data?.let { mapper?.fromEntity(it) }
                    )
                )
            }

            is NetworkResult.Failure -> {
                when (responseStatus.code) {
                    Constants.CODE_EXPIRED_ACCOUNT, Constants.CODE_EXPIRED_TOKEN -> {
                        EventBus.getDefault().post(Constants.MessageEvent(Constants.EventType.CLEAR_DATA_GO_TO_LOGIN))
                    }

                    else -> {
                        emit(
                            NetworkResult.Failure(
                                message = responseStatus.message,
                                code = responseStatus.code
                            )
                        )
                    }
                }
            }

            else -> {}
        }
    }

}