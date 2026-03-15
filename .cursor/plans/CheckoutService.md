I now have a thorough understanding of the entire codebase. Here's my plan:

---

## Plano de Implementação - CheckoutService

### Arquivos a criar (3 arquivos novos)

#### 1. `ShoppingCartCantProceedToCheckoutException`
**Caminho:** `src/main/java/com/lutz/algashop/ordering/domain/exception/shoppingCart/ShoppingCartCantProceedToCheckoutException.java`

- Extende `DomainException`
- Mensagem descritiva indicando que o carrinho não pode prosseguir para checkout (pode incluir o `ShoppingCartId`)
- Segue o padrão das outras exceções do pacote `shoppingCart`

#### 2. `CheckoutService`
**Caminho:** `src/main/java/com/lutz/algashop/ordering/domain/service/CheckoutService.java`

- Anotado com `@DomainService` (para ser detectado como bean via `DomainServiceScanConfig`)
- Método `checkout(ShoppingCart, Billing, Shipping, PaymentMethod)` que:
    1. **Valida** o carrinho: se `shoppingCart.containsUnavailableItems()` ou `shoppingCart.isEmpty()`, lança `ShoppingCartCantProceedToCheckoutException`
    2. **Cria** o pedido via `Order.draft(shoppingCart.customerId())`
    3. **Configura** o pedido: `order.changeBilling(billing)`, `order.changeShipping(shipping)`, `order.changePaymentMethod(paymentMethod)`
    4. **Itera** sobre `shoppingCart.items()`, para cada `ShoppingCartItem` cria um `Product` (usando o builder) com os dados do item (`productId`, `name`, `price`, `available`) e chama `order.addItem(product, item.quantity())`
    5. **Finaliza**: `order.place()` e `shoppingCart.empty()`
    6. **Retorna** o `Order`

**Nota importante sobre a ordem:** `changeShipping` e `changeBilling` devem ser chamados antes de `addItem`, pois `Order.addItem` chama `product.verifyIfIsInStock()` e `recalculateTotals()`, mas o `place()` verifica se shipping/billing/paymentMethod não são nulos. A chamada a `changeShipping` também valida `expectedDeliveryDate`, então precisa ser feita enquanto a order estiver DRAFT.

#### 3. `CheckoutServiceTest`
**Caminho:** `src/test/java/com/lutz/algashop/ordering/domain/service/CheckoutServiceTest.java`

Usando **AssertJ** (`assertThat`, `assertThatThrownBy`) para todas as asserções, com `@Nested` + `@DisplayName` seguindo o estilo do projeto.

**Cenários de teste:**

| Teste | Descrição |
|---|---|
| **Carrinho válido - pedido criado corretamente** | Cria um `ShoppingCart` via `startShopping()`, adiciona 2 produtos (in stock), chama `checkout`. Verifica: order não nulo, status PLACED, customerId correto, paymentMethod correto, billing correto, shipping correto. |
| **Carrinho válido - itens transferidos** | Verifica que os `OrderItem`s no pedido retornado correspondem aos itens do carrinho (quantidade, preço, productId). Usa `Money` para comparações. |
| **Carrinho válido - carrinho esvaziado** | Após checkout, verifica `shoppingCart.isEmpty()` é true e `shoppingCart.items()` está vazio. |
| **Carrinho com itens indisponíveis** | Adiciona item, depois `refreshItem` com `inStock(false)`. Verifica que lança `ShoppingCartCantProceedToCheckoutException`. Verifica que o carrinho NÃO foi esvaziado. |
| **Carrinho vazio** | `ShoppingCart.startShopping(customerId)` sem adicionar itens. Verifica que lança `ShoppingCartCantProceedToCheckoutException`. |
| **Uso de Money** | Todas as asserções de valores monetários usam `Money` (ex: `assertThat(orderItem.price()).isEqualTo(new Money("25.00"))`) em vez de `BigDecimal`. |

**Estrutura do teste:**
```java
class CheckoutServiceTest {
    private CheckoutService sut = new CheckoutService();

    // helpers para criar Billing, Shipping, etc. usando OrderTestBuilder

    @Nested @DisplayName("checkout com carrinho válido")
    class ValidCartCheckout {
        @Test void shouldReturnPlacedOrder() { ... }
        @Test void shouldTransferItemsCorrectly() { ... }
        @Test void shouldEmptyShoppingCart() { ... }
    }

    @Nested @DisplayName("checkout com itens indisponíveis")
    class UnavailableItems {
        @Test void shouldThrowException() { ... }
        @Test void shouldNotEmptyCart() { ... }
    }

    @Nested @DisplayName("checkout com carrinho vazio")
    class EmptyCart {
        @Test void shouldThrowException() { ... }
    }
}
```

### Resumo das alterações

| # | Tipo | Arquivo | Descrição |
|---|---|---|---|
| 1 | **Novo** | `domain/exception/shoppingCart/ShoppingCartCantProceedToCheckoutException.java` | Nova exceção de domínio |
| 2 | **Novo** | `domain/service/CheckoutService.java` | Domain service com `@DomainService` |
| 3 | **Novo** | Test: `domain/service/CheckoutServiceTest.java` | Testes unitários com AssertJ |

Nenhum arquivo existente precisa ser modificado.

---

Deseja que eu prossiga com a implementação?