package com.weezam.challenge.user.adapter.in.rest.mapper;

import java.util.List;

public interface DomainMapper <D, E> {

    E toDomain(D dto);

    D toDto(E domain);

    List <E> toEntity(List<D> dtoList);

    List <D> toDto(List<E> domainList);
}