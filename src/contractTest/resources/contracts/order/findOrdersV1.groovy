package contracts.order

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/orders") {
            queryParameters {
                parameter("size", value(stub(optional(anyNumber())), test(10)))
                parameter("page", value(stub(optional(anyNumber())), test(0)))
            }
        }
    }
    response {
        status 200
        headers {
            contentType "application/json"
        }
        body([
                number: 0,
                size: fromRequest().query("size"),
                totalPages: 1,
                totalElements: 2,
                content: [
                        [
                                id: anyNonBlankString(),
                                totalItems: 2,
                                totalAmount: 60.48,
                                placedAt: anyIso8601WithOffset(),
                                paidAt: null,
                                readyAt: null,
                                canceledAt: null,
                                status: "PLACED",
                                paymentMethod: "GATEWAY_BALANCE",
                                customer: [
                                        id: anyUuid(),
                                        firstName: "John",
                                        lastName: "Doe",
                                        document: "12345",
                                        email: "johndoe@email.com",
                                        phone: "1191234564"
                                ]
                        ],
                        [
                                id: anyNonBlankString(),
                                totalItems: 1,
                                totalAmount: 19.99,
                                placedAt: anyIso8601WithOffset(),
                                paidAt: null,
                                readyAt: null,
                                canceledAt: null,
                                status: "PLACED",
                                paymentMethod: "CREDIT_CARD",
                                customer: [
                                        id: anyUuid(),
                                        firstName: "Jane",
                                        lastName: "Roe",
                                        document: "54321",
                                        email: "janeroe@email.com",
                                        phone: "1187654321"
                                ]
                        ]
                ]
        ])
    }
}
