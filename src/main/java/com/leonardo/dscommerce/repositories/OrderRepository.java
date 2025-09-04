package com.leonardo.dscommerce.repositories;

import com.leonardo.dscommerce.entities.Order;
import com.leonardo.dscommerce.entities.User;
import com.leonardo.dscommerce.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}