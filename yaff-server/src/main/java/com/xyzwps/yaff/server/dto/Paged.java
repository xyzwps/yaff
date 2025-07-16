package com.xyzwps.yaff.server.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record Paged<D>(int page, int size, long total, List<D> data) {

    public static <D> Paged<D> of(Page<D> page) {
        return new Paged<>(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getContent()
        );
    }
}
