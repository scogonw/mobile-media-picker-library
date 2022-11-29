package com.scogo.mediapicker.compose.core.request

import com.scogo.mediapicker.compose.core.callback.MediaPickerCallback
import com.scogo.mediapicker.compose.core.media.MediaPickerConfiguration
import java.util.*
import java.util.concurrent.ConcurrentHashMap

internal class PickerRequestWorker: PickerRequestWorkerCallback {

    private val workMap = ConcurrentHashMap<String, PickerRequest>()

    companion object {
        private var instance: PickerRequestWorker? = null
        fun getInstance(): PickerRequestWorker {
            if(instance == null) {
                synchronized(this) {
                    if(instance == null) {
                        instance = PickerRequestWorker()
                    }
                }
            }
            return instance!!
        }
    }

    override fun enqueue(
        config: MediaPickerConfiguration,
        callback: MediaPickerCallback
    ): String {
        val id = UUID.randomUUID().toString()
        val request = PickerRequest(
            id = id,
            data = PickerRequestData.create(id, config, callback)
        )
        workMap[id] = request
        return id
    }

    override fun dequeue(
        id: String
    ): String {
        if(workMap.contains(id)) {
            workMap.remove(id)
        }
        return id
    }

    override fun getWork(id: String?): PickerRequestData? {
        if(id == null) return null
        return if(workMap.containsKey(id)) {
            workMap[id]?.data
        }else {
           null
        }
    }

}