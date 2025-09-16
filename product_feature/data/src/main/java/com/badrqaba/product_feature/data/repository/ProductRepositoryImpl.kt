package com.badrqaba.product_feature.data.repository

import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.data.mapper.toImageEntity
import com.badrqaba.core.data.mapper.toPaginationResponse
import com.badrqaba.core.data.mapper.toProduct
import com.badrqaba.core.data.mapper.toProductEntity
import com.badrqaba.core.data.mapper.toProductPreview
import com.badrqaba.core.data.remote.dto.ProductDTO
import com.badrqaba.core.data.remote.dto.ProductPreviewDTO
import com.badrqaba.core.data.remote.dto.pagination.PaginationResponseDTO
import com.badrqaba.core.domain.model.PaginationResponse
import com.badrqaba.core.domain.model.Product
import com.badrqaba.core.domain.model.ProductPreview
import com.badrqaba.core.util.api.ApiError
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.Resource
import com.badrqaba.core.util.api.networkBoundResource
import com.badrqaba.product_feature.domain.repository.ProductRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl @Inject constructor(
    database: PocketGoodsDatabase,
    private val apiService: ApiService
) : ProductRepository {

    private val productDao = database.productDao()
    private val imageDao = database.imageDao()

    override fun getProducts(page: Int): Flow<Resource<PaginationResponse<ProductPreview>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val response = apiService.rawGet<PaginationResponseDTO<ProductPreviewDTO>>(
                    url = "products/page=$page",
                )

                emit(
                    Resource.Success(
                        data = response.toPaginationResponse {
                            it.toProductPreview()
                        }
                    ))
            } catch (exp: Exception) {
                emit(Resource.Error(error = exp))
            }
        }
    }

    override fun getProduct(id: Long): Flow<Resource<Product?>> {
        return networkBoundResource(
            databaseQuery = {
                productDao.getProduct(productId = id)
                    .map { it?.toProduct() }
            },
            apiCall = {
                try {
                    apiService.get<ProductDTO>(
                        url = "product/details/${id}"
                    )
                } catch (exp: Exception) {
                    throw ApiError(message = exp.message ?: "Unknown error")
                }
            },
            shouldFetch = { true },
            saveApiCallResult = { productDTO ->
                productDao.insertProduct(product = productDTO.toProductEntity())

                productDTO.media.map { imageDTO ->
                    imageDao.insertImage(
                        imageDTO.toImageEntity(
                            modelId = productDTO.id
                        )
                    )
                }
            }
        )
    }

    override fun getSuggestedProducts(query: String): Flow<Resource<List<ProductPreview>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val response = apiService.post<List<ProductPreviewDTO>, Map<String, String>>(
                    url = "product/search-suggestions",
                    body = mapOf("search_query" to query),
                    headers = mapOf("Content-Type" to "application/x-www-form-urlencoded")
                )

                emit(
                    Resource.Success(
                    data = response.map { it.toProductPreview() }
                ))

            } catch (exp: Exception) {
                emit(Resource.Error(error = exp))
            }
        }
    }

    override fun searchProducts(
        query: String,
        page: Int
    ): Flow<Resource<PaginationResponse<ProductPreview>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val response =
                    apiService.rawPost<PaginationResponseDTO<ProductPreviewDTO>, Map<String, String>>(
                        url = "product/search?=${page}",
                        body = mapOf("search_query" to query),
                        headers = mapOf("Content-Type" to "application/x-www-form-urlencoded")
                    )

                emit(
                    Resource.Success(
                        data = response.toPaginationResponse {
                            it.toProductPreview()
                        }
                    )
                )

            } catch (exp: Exception) {
                emit(Resource.Error(error = exp))
            }
        }
    }

}