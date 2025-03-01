package utils

sealed class ConnectionState {
    data object Loading : ConnectionState()
    data class Error(val errorMessage: String) : ConnectionState()
    data class Success(val data: String) : ConnectionState()
    data object Default : ConnectionState()
}