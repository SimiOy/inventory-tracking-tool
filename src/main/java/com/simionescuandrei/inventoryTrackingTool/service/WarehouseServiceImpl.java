package com.simionescuandrei.inventoryTrackingTool.service;

import com.simionescuandrei.inventoryTrackingTool.model.Item;
import com.simionescuandrei.inventoryTrackingTool.model.Warehouse;
import com.simionescuandrei.inventoryTrackingTool.repository.ItemRepo;
import com.simionescuandrei.inventoryTrackingTool.repository.WarehouseRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** The warehouse service implementation of all methods described in Warehouse Service. */
@Service
public class WarehouseServiceImpl implements WarehouseService {

  @Autowired
  private WarehouseRepo warehouseRepo;
  @Autowired private ItemRepo itemRepo;

  /**
   * Saves a warehouse on the repository.
   *
   * @param warehouse - the warehouse to save
   * @return - the warehouse saved
   */
  @Override
  public Warehouse saveWarehouse(Warehouse warehouse) {
    return warehouseRepo.save(warehouse);
  }

  /**
   * Retrieves a list of all warehouses present in this repository.
   *
   * @return - a list of all warehouses
   */
  @Override
  public List<Warehouse> getAllWarehouses() {
    return warehouseRepo.findAll();
  }

  /**
   * Deletes a warehouse from the repository.
   *
   * @param id - the ID of the warehouse to delete
   * @return - a boolean indicating whether the deletion was successful
   */
  @Override
  public boolean deleteWarehouse(Long id) {
    Optional<Warehouse> warehousePolled = warehouseRepo.findById(id);
    if (warehousePolled.isEmpty()) {
      return false;
    }
    Warehouse wh = warehousePolled.get();
    List<Item> activeItems = new ArrayList<>();
    for (Long itemId : wh.getItems().keySet()) {
      Optional<Item> item = itemRepo.findById(itemId);
      if (item.isPresent()) {
        activeItems.add(item.get());
      }
    }
    for (Item item : activeItems) {
      addItemToWarehouse(wh, item, -wh.getItems().get(item.getId()));
      itemRepo.save(item);
    }
    warehouseRepo.delete(wh);
    return true;
  }

  /**
   * Retrieves a warehouse with the specified ID from the repository.
   *
   * @param id - the warehouse to get
   * @return - the warehouse or null if not present
   */
  @Override
  public Warehouse getWarehouse(Long id) {
    Optional<Warehouse> warehousePolled = warehouseRepo.findById(id);
    if (warehousePolled.isEmpty()) {
      return null;
    }
    return warehousePolled.get();
  }

  /**
   * Adds a specified item with quantity to a warehouse.
   *
   * @param warehouse - the warehouse to add to
   * @param item - the item to add
   * @param stock - the quantity of item to add in
   * @return - a String indicating the status of the addition (successful, or if not, why)
   */
  @Override
  public String addItemToWarehouse(Warehouse warehouse, Item item, Integer stock) {
    return warehouse.addItemWithStock(item, stock);
  }

  /**
   * Removes a specified item with quantity from a warehouse.
   *
   * @param warehouse - the warehouse to remove from
   * @param item - the item to remove
   * @param stock - the quantity of item to remove
   * @return - a String indicating the status of the removal (successful, or if not, why)
   */
  @Override
  public String removeItemFromWarehouse(Warehouse warehouse, Item item, Integer stock) {
    return warehouse.removeItemWithStock(item, stock);
  }

  /**
   * Moves an item with quantity between two warehouses. This method first removes the specified
   * quantity of item from the origin warehouse, then adds the same quantity of item to the other
   * warehouse. If something goes wrong in the process, both warehouses go back to their initial
   * state
   *
   * @param warehouseFrom - the warehouse to move from
   * @param warehouseTo - the warehouse to move to
   * @param item - the item to move
   * @param stock - the quantity of item to move between them
   * @return - a boolean indicating whether the move was successful or not
   */
  @Override
  public boolean moveItemToOtherWarehouse(
      Warehouse warehouseFrom, Warehouse warehouseTo, Item item, Integer stock) {

    Integer stockOfItemInWarehouse = warehouseFrom.getItems().get(item.getId());
    if (stockOfItemInWarehouse == null) {
      return false;
    }

    // can't move more stock than what a warehouse currently has
    if (stock >= stockOfItemInWarehouse) {
      stock = stockOfItemInWarehouse;
    }
    String removalDone = warehouseFrom.removeItemWithStock(item, stock);
    // removal went wrong, no need to add to the other warehouse
    if (!removalDone.equals("Successfully removed")) {
      return false;
    }
    String additionDone = warehouseTo.addItemWithStock(item, stock);
    // successfully moved from one side to another
    if (additionDone.equals("Successfully moved")) {
      return true;
    }
    // removal was successful, but addition was not
    // get back to previous state
    warehouseFrom.addItemWithStock(item, stock);
    return false;
  }
}
