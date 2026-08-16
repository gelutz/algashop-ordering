package contracts.shoppingcart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/shopping-carts/03ff97bf-c376-41c6-85ed-79b4ffd86e2c")
    }
    response {
        status 404
        body([
                instance: fromRequest().path(),
                type: "/errors/not-found",
                title: "Not found"
        ])
    }
}
