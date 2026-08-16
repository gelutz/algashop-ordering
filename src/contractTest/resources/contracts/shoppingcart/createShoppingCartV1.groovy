package contracts.shoppingcart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        headers {
            accept 'application/json'
            contentType 'application/json'
        }
        urlPath("/api/v1/shopping-carts")
        body([
                customerId: value(
                        test("2c65f7f8-baf8-4b8f-9c1e-5210bf4a2c65"),
                        stub(anyUuid())
                )
        ])
    }
    response {
        status 201
        headers {
            contentType 'application/json'
        }
        body([
                id: anyUuid(),
                customerId: anyUuid(),
                totalItems: anyNumber(),
                totalAmount: anyNumber(),
                items: [
                        [
                                id: anyUuid(),
                                productId: anyUuid(),
                                name: anyNonBlankString(),
                                price: anyNumber(),
                                quantity: anyNumber(),
                                totalAmount: anyNumber(),
                                available: anyBoolean()
                        ]
                ]
        ])
    }
}
