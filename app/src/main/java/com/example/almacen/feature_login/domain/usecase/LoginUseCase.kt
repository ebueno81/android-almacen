package com.example.almacen.feature_login.domain.usecase

import com.example.almacen.feature_login.data.model.UserCredentials
import com.example.almacen.feature_login.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String)=
        repo.login(UserCredentials(username, password) )
}