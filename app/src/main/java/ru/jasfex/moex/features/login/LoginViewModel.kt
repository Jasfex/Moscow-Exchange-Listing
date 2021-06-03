package ru.jasfex.moex.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.jasfex.moex.preferences.UserPreferencesRepository

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collect {
                _uiState.value = if (it.agreementSigned) LoginUiState.Authorized else LoginUiState.NotAuthorized
            }
        }
    }

    fun onAuthorize() {
        viewModelScope.launch {
            userPreferencesRepository.updateAgreementSigned(true)
        }
    }

}

class LoginViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userPreferencesRepository = userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}