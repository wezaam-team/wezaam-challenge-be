package com.weezam.challenge.user.adapter.out.persistence.mapper;

import java.util.List;

public interface EntityMapper<DOMAIN, ENTITY> {

    ENTITY toEntity(DOMAIN dto);

    DOMAIN toDomain(ENTITY domain);

    List <ENTITY> toEntity(List<DOMAIN> dtoList);

    List <DOMAIN> toDomain(List<ENTITY> domainList);
}