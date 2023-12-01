package com.example.frhtml.domain

import javax.inject.Inject


class GetListOfBrandsUseCase  @Inject constructor(private val repository: Repository) {
    operator fun invoke() =
        repository.getListOfBrands()
}
