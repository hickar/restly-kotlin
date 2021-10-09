package com.hickar.restly.ui.requestList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hickar.restly.models.Request
import com.hickar.restly.repository.room.RequestRepository
import kotlinx.coroutines.runBlocking

class RequestListViewModel(
    private val repository: RequestRepository,
) : ViewModel() {

    val requests: MutableLiveData<MutableList<Request>> = MutableLiveData()

    init {
        refreshRequests()
    }

    suspend fun createNewDefaultRequest(): Long {
        val newRequest = Request()
        val newRequestId = repository.insert(newRequest)

        refreshRequests()
        return newRequestId
    }

    fun refreshRequests() {
        runBlocking {
            requests.value = repository.getAll().toMutableList()
        }
    }

    fun deleteRequest(position: Int) {
        runBlocking {
            repository.delete(requests.value!![position])
            requests.value?.removeAt(position)
            requests.value = requests.value
        }
    }
}