package com.hickar.restly.mappers

import com.google.gson.GsonBuilder
import com.hickar.restly.models.*
import com.hickar.restly.repository.models.RequestDTO

class RequestMapper : Mapper<Request, RequestDTO> {
    private val gson = GsonBuilder().create()

    override fun toDTO(request: Request): RequestDTO {
        return RequestDTO(
            request.id,
            request.method,
            request.name,
            request.url,
            gson.toJson(request.queryParams),
            gson.toJson(request.headers),
            gson.toJson(request.body)
        )
    }

    override fun toDTOList(requests: List<Request>): List<RequestDTO> {
        val dtoList: MutableList<RequestDTO> = mutableListOf()
        for (request in requests) {
            dtoList.add(toDTO(request))
        }

        return dtoList
    }

    override fun toEntity(request: RequestDTO): Request {
        val queryParams = if (request.queryParams.isNotEmpty()) {
            gson.fromJson(request.queryParams, Array<RequestQueryParameter>::class.java).toList()
        } else {
            listOf()
        }

        val headers = if (request.headers.isNotEmpty()) {
            gson.fromJson(request.headers, Array<RequestHeader>::class.java).toList()
        } else {
            listOf()
        }

        return Request(
            request.id,
            request.method,
            request.name,
            request.url,
            queryParams,
            headers,
            gson.fromJson(request.body, RequestBody::class.java)
        )
    }

    override fun toEntityList(requests: List<RequestDTO>): List<Request> {
        val entityList: MutableList<Request> = mutableListOf()
        for (request in requests) {
            entityList.add(toEntity(request))
        }

        return entityList
    }
}