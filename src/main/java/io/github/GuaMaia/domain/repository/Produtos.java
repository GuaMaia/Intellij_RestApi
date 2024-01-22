package io.github.GuaMaia.domain.repository;

import io.github.GuaMaia.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}