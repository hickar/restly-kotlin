package com.hickar.restly.repository.room

import androidx.annotation.WorkerThread
import com.hickar.restly.models.Collection
import com.hickar.restly.models.RequestDirectory
import com.hickar.restly.models.RequestItem
import com.hickar.restly.repository.dao.CollectionDao
import com.hickar.restly.repository.dao.CollectionRemoteSource
import com.hickar.restly.repository.dao.RequestGroupDao
import com.hickar.restly.repository.dao.RequestItemDao
import com.hickar.restly.repository.mappers.CollectionMapper
import com.hickar.restly.repository.mappers.RequestGroupMapper
import com.hickar.restly.repository.mappers.RequestItemMapper
import com.hickar.restly.repository.models.CollectionRemoteDTO
import com.hickar.restly.services.SharedPreferencesHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionRepository @Inject constructor(
    private val collectionMapper: CollectionMapper,
    private val requestItemMapper: RequestItemMapper,
    private val requestGroupMapper: RequestGroupMapper,
    private val collectionDao: CollectionDao,
    private val requestItemDao: RequestItemDao,
    private val requestGroupDao: RequestGroupDao,
    private val collectionRemoteSource: CollectionRemoteSource,
    private val prefs: SharedPreferencesHelper,
) {
    @WorkerThread
    suspend fun getAllCollections(forceUpdate: Boolean = false): List<Collection> {
        if (forceUpdate && prefs.getPostmanUserInfo() != null) {
            val token = prefs.getPostmanApiKey()
            val collections = collectionRemoteSource.getCollections(token)
            saveRemoteCollections(collections)
        }

        return collectionMapper.toEntityList(collectionDao.getAll())
    }

    private suspend fun saveRemoteCollections(collections: List<CollectionRemoteDTO>) {
//        for (collection in collections) {
//            collectionDao.insert(
//                CollectionDTO(
//                    collection.id,
//                    collection.name,
//                    collection.description,
//                    collection.owner,
//                    null
//                )
//            )
//
//            for (request in collection.items) {
//                requestDao.insert(requestItemMapper.toDTO(request))
//            }
//        }
    }

    @WorkerThread
    suspend fun getCollectionById(id: String): Collection? {
        val collectionDto = collectionDao.getById(id) ?: return null
        return collectionMapper.toEntity(collectionDto)
    }

    @WorkerThread
    suspend fun insertCollection(collection: Collection) {
        return collectionDao.insert(collectionMapper.toDTO(collection))
    }

    @WorkerThread
    suspend fun updateCollection(collection: Collection) {
        return collectionDao.update(collectionMapper.toDTO(collection))
    }

    @WorkerThread
    suspend fun deleteCollection(collection: Collection) {
        return collectionDao.delete(collectionMapper.toDTO(collection))
    }

    @WorkerThread
    suspend fun getRequestGroupById(id: String): RequestDirectory? {
        val requestGroupDto = requestGroupDao.getById(id) ?: return null
        val subgroups = mutableListOf<RequestDirectory>()

        requestGroupDao.getByParentId(requestGroupDto.id).forEach { subgroupDto ->
            val subgroup = getRequestGroupById(subgroupDto.id)
            if (subgroup != null) subgroups.add(subgroup)
        }

        val requests = requestItemMapper.toEntityList(requestItemDao.getByGroupId(requestGroupDto.id))

        return RequestDirectory(
            id = requestGroupDto.id,
            name = requestGroupDto.name,
            description = requestGroupDto.description,
            requests = requests.toMutableList(),
            subgroups = subgroups.toMutableList(),
            parentId = requestGroupDto.parentId
        )
    }

    @WorkerThread
    suspend fun insertRequestGroup(requestGroup: RequestDirectory) {
        requestGroupDao.insert(requestGroupMapper.toDTO(requestGroup))
    }

    @WorkerThread
    suspend fun updateRequestGroup(requestGroup: RequestDirectory) {
        return requestGroupDao.update(requestGroupMapper.toDTO(requestGroup))
    }

    @WorkerThread
    suspend fun deleteRequestGroup(requestGroup: RequestDirectory) {
        requestGroupDao.deleteById(requestGroup.id)
        requestGroupDao.deleteGroupsByParentId(requestGroup.id)
        requestItemDao.deleteRequestsByGroupId(requestGroup.id)
        for (subgroup in requestGroup.subgroups) {
            deleteRequestGroup(subgroup)
        }
    }

    @WorkerThread
    suspend fun getRequestItemById(id: String): RequestItem {
        return requestItemMapper.toEntity(requestItemDao.getById(id))
    }

    @WorkerThread
    suspend fun insertRequestItem(request: RequestItem) {
        return requestItemDao.insert(requestItemMapper.toDTO(request))
    }

    @WorkerThread
    suspend fun updateRequestItem(request: RequestItem) {
        return requestItemDao.update(requestItemMapper.toDTO(request))
    }

    @WorkerThread
    suspend fun deleteRequestItem(request: RequestItem) {
        return requestItemDao.delete(requestItemMapper.toDTO(request))
    }

    @WorkerThread
    suspend fun deleteRequestsByCollectionId(id: String) {
        return requestItemDao.deleteRequestsByGroupId(id)
    }

//    @WorkerThread
//    suspend fun saveAllToRemote() {
//        if (prefs.getRestlyUserInfo() != null) {
//            val token = prefs.getRestlyJwt()
//            val collections = collectionDao.getAll()
//            val collectionDTOs = mutableListOf<CollectionRemoteDTO>()
//
//            for (collection in collections) {
//                val requests = requestMapper.toEntityList(requestDao.getByCollectionId(collection.id))
//
//                collectionDTOs.add(
//                    CollectionRemoteDTO(
//                        collection.id,
//                        collection.name,
//                        collection.description,
//                        collection.owner,
//                        requests
//                    )
//                )
//            }
//
//            collectionRemoteSource.postCollections(token, collectionDTOs)
//        }
//    }
}