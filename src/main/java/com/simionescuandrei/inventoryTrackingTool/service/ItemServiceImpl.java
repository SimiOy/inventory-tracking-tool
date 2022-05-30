package com.simionescuandrei.inventoryTrackingTool.service;

import com.simionescuandrei.inventoryTrackingTool.model.Item;
import com.simionescuandrei.inventoryTrackingTool.repository.ItemRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** The item service implementation of all methods described in Item Service. */
@Service
public class ItemServiceImpl implements ItemService {

  @Autowired
  private ItemRepo itemRepo;

  /**
   * Saves an item in the repository.
   *
   * @param item - the item to save
   * @return - the item saved
   */
  @Override
  public Item saveItem(Item item) {
    return itemRepo.save(item);
  }

  /**
   * Retrieves an item from the repository.
   *
   * @param id - the ID of the item to retrieve
   * @return - the item retrieved
   */
  @Override
  public Item getItem(Long id) {
    Optional<Item> itemPolled = itemRepo.findById(id);
    if (itemPolled.isEmpty()) {
      return null;
    }
    return itemPolled.get();
  }

  /**
   * Deletes an item from the repository.
   *
   * @param id - the ID of the item to delete
   * @return - a boolean indicating whether the deletion was performed
   */
  @Override
  public boolean deleteItem(Long id) {
    Optional<Item> itemPolled = itemRepo.findById(id);
    if (itemPolled.isEmpty()) {
      return false;
    }
    try {
      itemRepo.delete(itemPolled.get());
    } catch (Exception e) {
      System.out.println("item is linked to a warehouse");
      return false;
    }
    return true;
  }

  /**
   * Gets a list of all items in the repository.
   *
   * @return - a list of all items
   */
  @Override
  public List<Item> getAllItems() {
    return itemRepo.findAll();
  }
}
