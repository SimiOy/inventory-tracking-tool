package com.simionescuandrei.inventoryTrackingTool.repository;

import com.simionescuandrei.inventoryTrackingTool.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The repository of all items (mapped by a Long ID). */
@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {}
