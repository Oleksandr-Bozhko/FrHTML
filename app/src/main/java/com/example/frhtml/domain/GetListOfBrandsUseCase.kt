package com.example.frhtml.domain

import com.example.frhtml.data.Brand


class GetListOfBrandsUseCase(private val repository: Repository) {
    operator fun invoke() =
        repository.getListOfBrands()
}
