package com.simionescuandrei.inventoryTrackingTool.controller;

import com.simionescuandrei.inventoryTrackingTool.model.Item;
import com.simionescuandrei.inventoryTrackingTool.model.Warehouse;
import com.simionescuandrei.inventoryTrackingTool.service.ItemService;
import com.simionescuandrei.inventoryTrackingTool.service.WarehouseService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The rest controller for the warehouse class. All API calls are described in here */
@RestController
@RequestMapping("/warehouse")
@CrossOrigin
public class WarehouseController {

  @Autowired
  private WarehouseService warehouseService;
  @Autowired private ItemService itemService;

  //  @PostMapping("/")
  //  public String init() {
  //    Warehouse warehouse = new Warehouse(1L, "Stielt", "Delft");
  //    warehouseService.saveWarehouse(warehouse);
  //    return "New Warehouse added with ID:" + warehouse.getId();
  //  }

  /**
   * Adds a warehouse to the warehouse repository.
   *
   * @param warehouse - the warehouse to add
   * @return - String stating that the warehouse has been added
   */
  @PostMapping(
      value = "/add",
      consumes = {"application/json"})
  public String add(@RequestBody Warehouse warehouse) {
    warehouseService.saveWarehouse(warehouse);
    return "New Warehouse added with ID:" + warehouse.getId();
  }

  /**
   * Deletes a warehouse with the given ID from the repository.
   *
   * @param id - the ID of the warehouse to delete
   * @return - boolean indicating whether the removal was successful
   */
  @DeleteMapping("/{id}")
  public boolean delete(@PathVariable Long id) {
    return warehouseService.deleteWarehouse(id);
  }

  /**
   * Updates a warehouse to the new specified warehouse in the request body.
   *
   * @param warehouse - the warehouse to update to
   * @param id - the id of the warehouse to update
   * @return - a response entity encapsulating a warehouse indicating the success or failure of the
   *     update (OK or NOT_FOUND)
   */
  @PutMapping("/{id}")
  public ResponseEntity<Warehouse> update(@RequestBody Warehouse warehouse, @PathVariable Long id) {
    try {
      Warehouse curWarehouse = warehouseService.getWarehouse(id);
      curWarehouse.updateWarehouse(warehouse);
      warehouseService.saveWarehouse(warehouse);
      return new ResponseEntity<>(HttpStatus.OK);

    } catch (NoSuchElementException e) {
      return new ResponseEntity<Warehouse>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Retrieves all warehouses from the repository.
   *
   * @return - a list of all warehouses in the repository
   */
  @GetMapping("/getAll")
  public List<Warehouse> getAll() {
    return warehouseService.getAllWarehouses();
  }

  /**
   * Retrieves a warehouse from the repository using a specified ID.
   *
   * @param id - the ID of the warehouse to remove
   * @return - a Response entity encapsulating a warehouse (can return OK or NOT_FOUND)
   */
  @GetMapping("/{id}")
  public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Long id) {
    Warehouse warehousePolled = warehouseService.getWarehouse(id);
    if (warehousePolled == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<Warehouse>(warehousePolled, HttpStatus.OK);
    }
  }

  /**
   * Adds an item with stock to a specified warehouse.
   *
   * @param warehouseId - the warehouse ID where the item should be placed
   * @param itemId - the ID of the item to place
   * @param stock - the stock to move there
   * @return - a response entity encapsulating a String which gives conclusive status of why/whether
   *     an item was added to the warehouse or not
   */
  @GetMapping("/addItem")
  public ResponseEntity<String> addItemToWarehouse(
      @RequestParam Long warehouseId, @RequestParam Long itemId, @RequestParam Integer stock) {

    Warehouse warehousePolled = warehouseService.getWarehouse(warehouseId);
    Item itemPolled = itemService.getItem(itemId);
    if (warehousePolled == null || itemPolled == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String successStatus = warehouseService.addItemToWarehouse(warehousePolled, itemPolled, stock);
    if (successStatus.equals("Successfully moved")) {
      warehouseService.saveWarehouse(warehousePolled);
      itemService.saveItem(itemPolled);
      return new ResponseEntity<>(successStatus, HttpStatus.OK);
    }
    return new ResponseEntity<>(successStatus, HttpStatus.BAD_REQUEST);
  }

  /**
   * Removes an item with stock from a specified warehouse.
   *
   * @param warehouseId - the warehouse ID where the item should be removed from
   * @param itemId - the ID of the item to remove
   * @param stock - the stock to remove from there
   * @return - a response entity encapsulating a String which gives conclusive status of why/whether
   *     an item was removed to the warehouse or not
   */
  @GetMapping("/removeItem")
  public ResponseEntity<String> removeItemFromWarehouse(
      @RequestParam Long warehouseId, @RequestParam Long itemId, @RequestParam Integer stock) {

    Warehouse warehousePolled = warehouseService.getWarehouse(warehouseId);
    Item itemPolled = itemService.getItem(itemId);
    if (warehousePolled == null || itemPolled == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String successStatus =
        warehouseService.removeItemFromWarehouse(warehousePolled, itemPolled, stock);
    if (successStatus.equals("Successfully removed")) {
      warehouseService.saveWarehouse(warehousePolled);
      itemService.saveItem(itemPolled);
      return new ResponseEntity<>(successStatus, HttpStatus.OK);
    }
    return new ResponseEntity<>(successStatus, HttpStatus.BAD_REQUEST);
  }

  /**
   * Moves an item between two warehouses with a given quantity (demand).
   *
   * @param warehouseFromId - the warehouse ID to move from
   * @param warehouseToId - the warehouse ID to move to
   * @param itemId - the ID of the item to move
   * @param stock - the quantity of the item to move
   * @return - a response entity encapsulating a boolean stating whether the move was successful or
   *     not (OK or NOT_FOUND can be returned)
   */
  @GetMapping("/moveItem")
  public ResponseEntity<Boolean> moveItemToWarehouse(
      @RequestParam Long warehouseFromId,
      @RequestParam Long warehouseToId,
      @RequestParam Long itemId,
      @RequestParam Integer stock) {
    Warehouse warehouseFromPolled = warehouseService.getWarehouse(warehouseFromId);
    Warehouse warehouseToPolled = warehouseService.getWarehouse(warehouseToId);
    Item itemPolled = itemService.getItem(itemId);
    if (warehouseFromPolled == null || itemPolled == null || warehouseToPolled == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    boolean moveSuccessful =
        warehouseService.moveItemToOtherWarehouse(
            warehouseFromPolled, warehouseToPolled, itemPolled, stock);
    if (moveSuccessful) {
      warehouseService.saveWarehouse(warehouseFromPolled);
      warehouseService.saveWarehouse(warehouseToPolled);
      return new ResponseEntity<>(true, HttpStatus.OK);
    }
    return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
  }

  /**
   * Retrieves a list of all items present in a specified warehouse.
   *
   * @param warehouseId - the warehouse ID to request the list of items from
   * @return - a list of items encapsulated in a response entity (can return OK or NOT_FOUND)
   */
  @GetMapping("/getItems")
  public ResponseEntity<List<Item>> getAllItemsInWarehouse(@RequestParam Long warehouseId) {
    Warehouse warehousePolled = warehouseService.getWarehouse(warehouseId);
    if (warehousePolled == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    List<Item> items =
        warehousePolled.getItems().keySet().stream()
            .map(x -> itemService.getItem(x))
            .collect(Collectors.toList());
    for (Item item : items) {
      Integer currentStockOfItemInWarehouse = warehousePolled.getItems().get(item.getId());
      item.setAvailableStock(currentStockOfItemInWarehouse);
    }
    return new ResponseEntity<>(items, HttpStatus.OK);
  }
}
