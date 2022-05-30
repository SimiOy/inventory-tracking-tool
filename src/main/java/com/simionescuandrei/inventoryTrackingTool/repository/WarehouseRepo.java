package com.simionescuandrei.inventoryTrackingTool.repository;

import com.simionescuandrei.inventoryTrackingTool.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The warehouse repository, where each warehouse object is mapped to an ID (LONG). */
@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {}
