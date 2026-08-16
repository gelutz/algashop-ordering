package contracts.shoppingcart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        headers {
            contentType 'application/json'
        }
        urlPath("/api/v1/shopping-carts/b551a5cf-7462-4751-bdb5-d1961359a4e2/items")
        body([
                productId: value(
                        test("39c36d6e-fc42-4e6a-9a49-a2a4900e4470"),
                        stub(anyUuid())
                ),
                quantity: value(
                        test(2),
                        stub(anyNumber())
                )
        ])
    }
    response {
        status 204
    }
}
