package io.github.GuaMaia.domain.repository;

import io.github.GuaMaia.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsPedido extends JpaRepository<ItemPedido, Integer> {
}