package com.simionescuandrei.inventoryTrackingTool.controller;

import com.simionescuandrei.inventoryTrackingTool.model.Item;
import com.simionescuandrei.inventoryTrackingTool.service.ItemService;
import java.util.List;
import java.util.NoSuchElementException;
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
import org.springframework.web.bind.annotation.RestController;

/** The rest controller for the item class. All API calls are described in here */
@RestController
@RequestMapping("/item")
@CrossOrigin
public class ItemController {

  @Autowired private ItemService itemService;

  /**
   * Adds a new item to the repository of items.
   *
   * @param item - the item to add
   * @return - A string indicating that an item was successfully added
   */
  @PostMapping("/add")
  public String add(@RequestBody Item item) {
    item.setAvailableStock(item.getStock());
    itemService.saveItem(item);
    return "New Item added with ID:" + item.getId();
  }

  /**
   * Deletes an item from the repository.
   *
   * @param id - the id of the item to delete
   * @return - a boolean indicating whether the deletion was successful
   */
  @DeleteMapping("/{id}")
  public boolean delete(@PathVariable Long id) {
    return itemService.deleteItem(id);
  }

  /**
   * Updates an item from the repository with new values.
   *
   * @param item - the item we have to match it too
   * @param id - the id of the item to be updated
   * @return - A response entity encapsulating an item (OK or NOT_FOUND)
   */
  @PutMapping("/{id}")
  public ResponseEntity<Item> update(@RequestBody Item item, @PathVariable Long id) {
    try {
      Item curItem = itemService.getItem(id);
      item.setAvailableStock(
          (curItem.getAvailableStock() == null ? 0 : curItem.getAvailableStock())
              + ((item.getStock() == null ? 0 : item.getStock())
                  - (curItem.getStock() == null ? 0 : curItem.getStock())));
      boolean updateSuccessful = curItem.updateItem(item);
      if (updateSuccessful) {
        itemService.saveItem(item);
        return new ResponseEntity<>(HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    } catch (NoSuchElementException e) {
      return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Retrieves all the items from the repository.
   *
   * @return - a list of all items in the database
   */
  @GetMapping("/getAll")
  public List<Item> getAll() {
    return itemService.getAllItems();
  }

  /**
   * Retrieves an item using its ID.
   *
   * @param id - the ID of the item to retrieve
   * @return - a Response Entity encapsulating an Item (OK or NOT_FOUND)
   */
  @GetMapping("/{id}")
  public ResponseEntity<Item> getItemById(@PathVariable Long id) {
    Item itemPolled = itemService.getItem(id);
    if (itemPolled == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<Item>(itemPolled, HttpStatus.OK);
    }
  }
}
