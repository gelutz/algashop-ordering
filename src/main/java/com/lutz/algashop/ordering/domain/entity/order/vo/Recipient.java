package com.lutz.algashop.ordering.domain.entity.order.vo;

import com.lutz.algashop.ordering.domain.entity.customer.vo.Document;
import com.lutz.algashop.ordering.domain.entity.customer.vo.FullName;
import com.lutz.algashop.ordering.domain.entity.customer.vo.Phone;
import lombok.Builder;

@Builder
public record Recipient(FullName fullName, Document document, Phone phone) {
}
