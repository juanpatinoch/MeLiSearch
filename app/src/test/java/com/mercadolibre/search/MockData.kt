package com.mercadolibre.search

import com.mercadolibre.search.model.dto.paging.PagingDto
import com.mercadolibre.search.model.dto.search.*
import java.util.*

object MockData {

    val mockDataQuery = UUID.randomUUID().toString().substring(0, 15)

    val mockDataLimit = Random().nextInt()

    val mockDataOffset = Random().nextInt()

    val mockSearchResultsDto = SearchResponseDto(
        paging = PagingDto(
            total = Random().nextInt(),
            offset = mockDataOffset,
            limit = mockDataLimit
        ),
        results = listOf(
            ResultsDto(
                id = UUID.randomUUID().toString().substring(0, 15),
                title = UUID.randomUUID().toString().substring(0, 15),
                price = Random().nextDouble(),
                originalPrice = Random().nextDouble(),
                currencyId = UUID.randomUUID().toString().substring(0, 15),
                permalink = UUID.randomUUID().toString().substring(0, 15),
                thumbnail = UUID.randomUUID().toString().substring(0, 15),
                installments = InstallmentsDto(
                    quantity = Random().nextInt(),
                    amount = Random().nextDouble(),
                    currencyId = UUID.randomUUID().toString().substring(0, 15)
                ),
                shipping = ShippingDto(
                    freeShipping = Random().nextBoolean()
                ),
                attributes = listOf(
                    AttributesDto(
                        name = UUID.randomUUID().toString().substring(0, 15),
                        valueName = UUID.randomUUID().toString().substring(0, 15)
                    )
                )
            )
        )

    )

}