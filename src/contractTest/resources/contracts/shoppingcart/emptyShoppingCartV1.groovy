package contracts.shoppingcart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        url("/api/v1/shopping-carts/b551a5cf-7462-4751-bdb5-d1961359a4e2/items")
    }
    response {
        status 204
    }
}
