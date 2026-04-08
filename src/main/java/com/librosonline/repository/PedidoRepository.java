package com.librosonline.repository;

import com.librosonline.model.Pedido;
import com.librosonline.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioOrderByFechaDesc(Usuario usuario);
    List<Pedido> findAllByOrderByFechaDesc();
}
