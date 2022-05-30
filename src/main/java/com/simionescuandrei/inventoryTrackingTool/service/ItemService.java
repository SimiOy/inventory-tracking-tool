package com.simionescuandrei.inventoryTrackingTool.service;

import com.simionescuandrei.inventoryTrackingTool.model.Item;
import java.util.List;

/**
 * The item service interface declaring all methods that can be called for this Item. Respects CRUD
 * requirements
 */
public interface ItemService {
  Item saveItem(Item item);

  List<Item> getAllItems();

  boolean deleteItem(Long id);

  Item getItem(Long id);
}