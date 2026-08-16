package contracts.order

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        headers {
            accept 'application/json'
            contentType 'application/vnd.order-with-shopping-cart.v1+json'
        }
        urlPath("/api/v1/orders")
        body([
                shoppingCartId: value(
                        test("277297bf-e586-4389-9f21-b3ce0c3f6580"),
                        stub(anyUuid())
                ),
                paymentMethod: value(
                        test("GATEWAY_BALANCE"),
                        stub(nonBlank())
                ),
                shipping: [
                        recipient: [
                                firstName: value(test("John"), stub(nonBlank())),
                                lastName: value(test("Doe"), stub(nonBlank())),
                                document: value(test("12345"), stub(nonBlank())),
                                phone: value(test("5511912341234"), stub(nonBlank()))
                        ],
                        address: [
                                street: value(test("Bourbon Street"), stub(nonBlank())),
                                number: value(test("2000"), stub(nonBlank())),
                                complement: value(test("apt 122"), stub(nonBlank())),
                                neighborhood: value(test("North Ville"), stub(nonBlank())),
                                city: value(test("Yostfort"), stub(nonBlank())),
                                state: value(test("South Carolina"), stub(nonBlank())),
                                zipCode: value(test("12321"), stub(nonBlank()))
                        ]
                ],
                billing: [
                        firstName: value(test("John"), stub(nonBlank())),
                        lastName: value(test("Doe"), stub(nonBlank())),
                        document: value(test("12345"), stub(nonBlank())),
                        email: value(test("johndoe@email.com"), stub(nonBlank())),
                        phone: value(test("5511912341234"), stub(nonBlank())),
                        address: [
                                street: value(test("Bourbon Street"), stub(nonBlank())),
                                number: value(test("2000"), stub(nonBlank())),
                                complement: value(test("apt 122"), stub(nonBlank())),
                                neighborhood: value(test("North Ville"), stub(nonBlank())),
                                city: value(test("Yostfort"), stub(nonBlank())),
                                state: value(test("South Carolina"), stub(nonBlank())),
                                zipCode: value(test("12321"), stub(nonBlank()))
                        ]
                ]
        ])
    }
    response {
        status 201
        headers {
            contentType 'application/json'
        }
        body([
                id: "01226N0640J7Q",
                customer: [
                        id: anyUuid(),
                        firstName: "John",
                        lastName: "Doe",
                        document: "12345",
                        email: "johndoe@email.com",
                        phone: "1191234564"
                ],
                totalItems: 2,
                totalAmount: 60.48,
                placedAt: anyIso8601WithOffset(),
                canceledAt: null,
                paidAt: null,
                readyAt: null,
                status: "PLACED",
                paymentMethod: "GATEWAY_BALANCE",
                shipping: [
                        cost: 20.5,
                        expectedDate: anyDate(),
                        recipient: [
                                firstName: "John",
                                lastName: "Doe",
                                document: "12345",
                                phone: "5511912341234"
                        ],
                        address: [
                                street: "Bourbon Street",
                                number: "2000",
                                complement: "apt 122",
                                neighborhood: "North Ville",
                                city: "Yostfort",
                                state: "South Carolina",
                                zipCode: "12321"
                        ]
                ],
                billing: [
                        firstName: "John",
                        lastName: "Doe",
                        document: "12345",
                        phone: "5511912341234",
                        address: [
                                street: "Bourbon Street",
                                number: "2000",
                                complement: "apt 122",
                                neighborhood: "North Ville",
                                city: "Yostfort",
                                state: "South Carolina",
                                zipCode: "12321"
                        ]
                ],
                items: [
                        [
                                id: anyNonBlankString(),
                                productId: anyUuid(),
                                orderId: "01226N0640J7Q",
                                price: 19.99,
                                productName: "Notebook Dive Gamer X11",
                                quantity: 2,
                                totalAmount: 39.98
                        ]
                ]
        ])
    }
}
