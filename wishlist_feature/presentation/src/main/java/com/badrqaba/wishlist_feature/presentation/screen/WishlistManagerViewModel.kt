package com.badrqaba.wishlist_feature.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.badrqaba.core.domain.model.WishlistItem
import com.badrqaba.core.util.api.Resource
import com.badrqaba.core.util.dispatcher.DefaultDispatchers
import com.badrqaba.core.util.dispatcher.DispatcherProvider
import com.badrqaba.wishlist_feature.domain.use_case.WishlistUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistManagerViewModel @Inject constructor(
    private val useCases: WishlistUseCases,
    private val dispatchers: DispatcherProvider = DefaultDispatchers()
) : ViewModel() {

    private val _state = MutableStateFlow(WishlistManagerState())
    val state: StateFlow<WishlistManagerState> = _state

    private var currentJob: Job? = null

    fun onEvent(event: WishlistManagerScreenEvent) {
        when (event) {
            is WishlistManagerScreenEvent.CloseDeleteConfirmationModal -> closeDeleteConfirmationModal()

            is WishlistManagerScreenEvent.DeleteWishlistItem -> deleteWishlistItem(
                userId = event.userId,
                onSuccess = event.onSuccess,
                onError = event.onError
            )

            is WishlistManagerScreenEvent.GetWishlistItems -> getWishlistItems(userId = event.userId)

            is WishlistManagerScreenEvent.ShowDeleteConfirmationModal -> showDeleteConfirmationModal(
                wishlistItem = event.wishlistItem
            )
        }
    }

    private fun showDeleteConfirmationModal(wishlistItem: WishlistItem) {
        _state.update {
            it.copy(selectedWishlistItem = wishlistItem)
        }
    }

    private fun getWishlistItems(userId: Long) {
        currentJob?.cancel()
//        currentJob = viewModelScope.launch(context = dispatchers.io) {
        currentJob = useCases
            .getEntireWishlist(userId = userId)
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> _state.value = _state.value.copy(
                        isPageLoading = true
                    )

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isPageLoading = false,
                            wishlist = resource.data ?: emptyList()
                        )
                    }

                    is Resource.Error -> _state.value = _state.value.copy(
                        isPageLoading = false,
                        error = resource.error
                    )
                }
            }
            .flowOn(dispatchers.io)
            .launchIn(viewModelScope)
//        }
    }

    private fun deleteWishlistItem(
        userId: Long,
        onSuccess: (String) -> Unit,
        onError: (Throwable?, String) -> Unit
    ) {
        val item = _state.value.selectedWishlistItem ?: return

        val productId = item.productId
        val productTitle = item.productDetail?.title.orEmpty()

        if (productId == null) {
            onError(null, productTitle)
            return
        }

        _state.update { it.copy(isDeleting = true) }

        currentJob?.cancel()
        currentJob = viewModelScope.launch(dispatchers.io) {
            val result = runCatching {
                useCases.toggleWishlist(
                    userId = userId,
                    productId = productId
                )
            }

            result
                .onSuccess {
                    onSuccess(productTitle)
                    _state.update {
                        it.copy(
                            selectedWishlistItem = null
                        )
                    }
                }
                .onFailure { throwable ->
                    onError(throwable, productTitle)
                }

            _state.update { it.copy(isDeleting = false) }
        }
    }

    private fun closeDeleteConfirmationModal() {
        _state.update {
            it.copy(selectedWishlistItem = null)
        }
    }
}