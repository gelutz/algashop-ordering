package com.lutz.algashop.ordering.domain.order.vo;

import com.lutz.algashop.ordering.domain.commons.Document;
import com.lutz.algashop.ordering.domain.commons.FullName;
import com.lutz.algashop.ordering.domain.commons.Phone;
import lombok.Builder;

@Builder
public record Recipient(FullName fullName, Document document, Phone phone) {
}
