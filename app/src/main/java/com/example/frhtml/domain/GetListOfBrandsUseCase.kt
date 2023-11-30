package com.example.frhtml.domain


class GetListOfBrandsUseCase (private val repository: Repository) {
    operator fun invoke() =
        repository.getListOfBrands()
}
