package com.simionescuandrei.inventoryTrackingTool.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** Entity - Warehouse class. Holds information about a single warehouse and is stored on the db */
@Entity
public class Warehouse {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String location;

  // maps an item's id to the stock number available in the warehouse
  @ElementCollection(fetch = FetchType.EAGER)
  private Map<Long, Integer> items = new HashMap<>();

  /**
   * public constructor for the warehouse object.
   *
   * @param id - the ID of the warehouse to construct
   * @param name - the name of the warehouse
   * @param location - the location of the warehouse
   */
  public Warehouse(Long id, String name, String location) {
    this.id = id;
    this.name = name;
    this.location = location;
    this.items = new HashMap<>();
  }

  /**
   * public constructor for the warehouse object.
   *
   * @param id - the ID of the warehouse to construct
   * @param name - the name of the warehouse
   * @param location - the location of the warehouse
   * @param items - a map(id of item, quantity) of items present in that warehouse
   */
  public Warehouse(Long id, String name, String location, Map<Long, Integer> items) {
    this.id = id;
    this.name = name;
    this.location = location;
    this.items = items;
  }

  public Warehouse() {
    // for orm
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Map<Long, Integer> getItems() {
    return items;
  }

  public void setItems(Map<Long, Integer> items) {
    this.items = items;
  }

  /**
   * Updates a warehouse given information about another warehouse.
   *
   * @param warehouse - the warehouse to update to
   * @return - a boolean indicating whether the update was successful
   */
  public boolean updateWarehouse(Warehouse warehouse) {
    if (warehouse.getName() == null
        || warehouse.getLocation() == null
        || warehouse.getItems() == null) {
      return false;
    }
    this.name = warehouse.getName();
    this.location = warehouse.getLocation();
    this.setItems(warehouse.getItems());
    return true;
  }

  /**
   * Adds the specified item in this warehouse with a given stock.
   * By default, the method disallows addition of insufficient volume,
   * and has this set as a precondition
   * Negative addition (subtraction) is also disallowed for this method
   *
   * @param item - the item to add to the warehouse
   * @param stock - the stock of item to add
   * @return - a String indicating the status of the addition
   */
  public String addItemWithStock(Item item, Integer stock) {
    if (item.getId() == null
        || item.getPrice() == null
        || item.getDescription() == null
        || item.getStock() == null
        || item.getAvailableStock() == null) {
      return "Something went wrong";
    }
    if (stock > item.getAvailableStock()) {
      return "There is insufficient volume for this move";
    }
    if (stock < 0) {
      return "Stock shouldn't be negative";
    }

    Map<Long, Integer> map = this.getItems();
    if (map.containsKey(item.getId())) {
      // already in this warehouse
      // just update the stock

      map.put(item.getId(), this.getItems().get(item.getId()) + stock);
      this.setItems(map);
      item.setAvailableStock(item.getAvailableStock() - stock);
      return "Successfully moved";
    } else {
      // item new to warehouse
      map.put(item.getId(), stock);
      this.setItems(map);
      item.setAvailableStock(item.getAvailableStock() - stock);
      return "Successfully moved";
    }
  }

  /**
   * Removes the specified item from this warehouse with a given stock.
   * By default, the method disallows removal of an item that isn't in
   * the warehouse
   * Negative subtraction (addition) is also disallowed for this method
   *
   * @param item - the item to remove to the warehouse
   * @param stock - the stock of item to remove
   * @return - a String indicating the status of the removal
   */
  public String removeItemWithStock(Item item, Integer stock) {
    if (item.getId() == null
        || item.getPrice() == null
        || item.getDescription() == null
        || item.getStock() == null
        || item.getAvailableStock() == null) {
      return "Something went wrong";
    }
    if (!this.getItems().containsKey(item.getId())) {
      return "This warehouse doesn't contain this item";
    }
    if (stock < 0) {
      return "Stock shouldn't be negative";
    }

    if (stock > this.getItems().get(item.getId())) {
      stock = this.getItems().get(item.getId());
    }
    Map<Long, Integer> map = this.getItems();
    if (map.get(item.getId()) <= stock) {
      // remove it all together
      map.remove(item.getId());
      this.setItems(map);
      item.setAvailableStock(item.getAvailableStock() + stock);
      return "Successfully removed";
    } else {
      map.put(item.getId(), this.getItems().get(item.getId()) - stock);
      this.setItems(map);
      item.setAvailableStock(item.getAvailableStock() + stock);
      return "Successfully removed";
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Warehouse warehouse = (Warehouse) o;
    return Objects.equals(id, warehouse.id)
        && Objects.equals(name, warehouse.name)
        && Objects.equals(location, warehouse.location)
        && Objects.equals(items, warehouse.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, location, items);
  }
}
