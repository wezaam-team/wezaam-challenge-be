package com.weezam.challenge.withdrawal.adapter.out.persistence.mapper;

import java.util.List;

public interface EntityMapper<DOMAIN, ENTITY> {

    ENTITY toEntity(DOMAIN domain);

    DOMAIN toDomain(ENTITY entity);

    List <ENTITY> toEntity(List<DOMAIN> domainList);

    List <DOMAIN> toDomain(List<ENTITY> entityList);
}