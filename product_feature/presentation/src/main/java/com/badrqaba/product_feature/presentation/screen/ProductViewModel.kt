package com.badrqaba.product_feature.presentation.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.badrqaba.core.util.api.Resource
import com.badrqaba.core.util.dispatcher.DefaultDispatchers
import com.badrqaba.core.util.dispatcher.DispatcherProvider
import com.badrqaba.core_ui.util.PRODUCT_ID_KEY
import com.badrqaba.core_ui.util.SEARCH_QUERY_KEY
import com.badrqaba.product_feature.domain.use_case.ProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val useCases: ProductUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val dispatcher: DispatcherProvider = DefaultDispatchers()
) : ViewModel() {

    private val _state: MutableStateFlow<ProductState> =
        MutableStateFlow(value = ProductState())
    val state: StateFlow<ProductState>
        get() = _state.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ProductState()
        )

    private var currentJob: Job? = null

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.GetProducts -> getProducts()
            is ProductEvent.OnNextPage -> onNextPage()
            is ProductEvent.LoadProduct -> loadProduct()
            is ProductEvent.SearchProducts -> searchProducts()
            is ProductEvent.SearchSuggestions -> searchSuggestions()
            is ProductEvent.OnQueryChange -> onQueryChange(query = event.query)
            is ProductEvent.ClearSearch -> onClearSearch()
        }
    }

    private fun onClearSearch() {
        _state.value = _state.value.copy(
            searchQuery = "",
            isSearching = false,
            suggestedProducts = emptyList()
        )
    }

    private fun onQueryChange(query: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = query
        _state.update { it.copy(searchQuery = query) }
        searchSuggestions()
    }

    @OptIn(FlowPreview::class)
    private fun searchSuggestions() {
        if (_state.value.searchQuery.isNotEmpty()) {
            currentJob?.cancel()
            currentJob = useCases
                .getSuggestedProducts(query = _state.value.searchQuery)
                .debounce(800)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> _state.update { it.copy(isSearching = true) }

                        is Resource.Success -> _state.update {
                            it.copy(
                                suggestedProducts = resource.data ?: emptyList(),
                                isSearching = false
                            )
                        }

                        is Resource.Error -> _state.update {
                            it.copy(
                                isSearching = false,
                                error = resource.error
                            )
                        }
                    }
                }
                .flowOn(dispatcher.io)
                .launchIn(viewModelScope)
        }
    }

    private fun searchProducts() {
        val searchQuery = savedStateHandle.get<String>(SEARCH_QUERY_KEY) ?: ""
        if (_state.value.currentPage <= _state.value.lastPage
            && searchQuery.isNotEmpty()
        ) {
            currentJob?.cancel()
            currentJob = useCases
                .searchProducts(
                    query = searchQuery,
                    page = _state.value.currentPage
                )
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> _state.value = _state.value.copy(
                            isPageLoading = true
                        )

                        is Resource.Success -> {
                            val newList =
                                (_state.value.products
                                        + (resource.data?.data ?: emptyList()))
                            _state.value = _state.value.copy(
                                searchResults = newList.distinctBy { it.id },
                                lastPage = resource.data?.lastPage ?: 1,
                                isPageLoading = false
                            )
                        }

                        is Resource.Error -> _state.value = _state.value.copy(
                            isPageLoading = false,
                            error = resource.error
                        )
                    }
                }
                .flowOn(dispatcher.io)
                .launchIn(viewModelScope)
        }
    }

    private fun loadProduct() {
        savedStateHandle.get<Long>(PRODUCT_ID_KEY)?.let { productId ->
            currentJob?.cancel()
            currentJob = useCases
                .getProduct(id = productId)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> _state.update { it.copy(isPageLoading = true) }

                        is Resource.Success -> _state.update {
                            it.copy(
                                product = resource.data,
                                isPageLoading = false
                            )
                        }

                        is Resource.Error -> _state.update {
                            it.copy(
                                isPageLoading = false,
                                error = resource.error
                            )
                        }
                    }
                }
                .flowOn(dispatcher.io)
                .launchIn(viewModelScope)

        }
    }

    private fun onNextPage() {
        _state.value = _state.value.copy(
            currentPage = _state.value.currentPage + 1
        )
    }

    private fun getProducts() {
        if (_state.value.currentPage <= _state.value.lastPage) {
            currentJob?.cancel()
            currentJob = useCases
                .getProducts(page = _state.value.currentPage)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> _state.value = _state.value.copy(
                            isPageLoading = true
                        )

                        is Resource.Success -> {
                            val newList =
                                (_state.value.products + (resource.data?.data ?: emptyList()))
                            _state.value = _state.value.copy(
                                products = newList.distinctBy { it.id },
                                lastPage = resource.data?.lastPage ?: 1,
                                isPageLoading = false
                            )
                        }

                        is Resource.Error -> _state.value = _state.value.copy(
                            isPageLoading = false,
                            error = resource.error
                        )
                    }
                }
                .flowOn(dispatcher.io)
                .launchIn(viewModelScope)
        }
    }
}