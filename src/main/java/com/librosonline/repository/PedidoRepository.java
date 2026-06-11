package com.librosonline.repository;

import com.librosonline.model.Pedido;
import com.librosonline.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Page<Pedido> findByUsuarioOrderByFechaDesc(Usuario usuario, Pageable pageable);
    Page<Pedido> findAllByOrderByFechaDesc(Pageable pageable);
}
