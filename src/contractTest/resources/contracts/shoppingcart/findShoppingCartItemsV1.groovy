package contracts.shoppingcart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept 'application/json'
        }
        url("/api/v1/shopping-carts/b551a5cf-7462-4751-bdb5-d1961359a4e2/items")
    }
    response {
        status 200
        headers {
            contentType 'application/json'
        }
        body([
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
