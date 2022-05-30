package com.simionescuandrei.inventoryTrackingTool.service;

import com.simionescuandrei.inventoryTrackingTool.model.Item;
import com.simionescuandrei.inventoryTrackingTool.model.Warehouse;
import java.util.List;

/**
 * The warehouse service interface declaring all methods that can be called for this Warehouse.
 * Respects CRUD requirements
 */
public interface WarehouseService {

  Warehouse saveWarehouse(Warehouse warehouse);

  List<Warehouse> getAllWarehouses();

  boolean deleteWarehouse(Long id);

  Warehouse getWarehouse(Long id);

  String addItemToWarehouse(Warehouse warehouse, Item item, Integer stock);

  String removeItemFromWarehouse(Warehouse warehouse, Item item, Integer stock);

  boolean moveItemToOtherWarehouse(
      Warehouse warehouseFrom, Warehouse warehouseTo, Item item, Integer stock);
}
