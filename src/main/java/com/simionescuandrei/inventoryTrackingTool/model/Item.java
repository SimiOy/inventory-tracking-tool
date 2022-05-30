package com.simionescuandrei.inventoryTrackingTool.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** Entity - Item class. Holds information about a single item and is stored on the db */
@Entity
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private Float price;
  private String description;
  private Integer stock;
  private Integer availableStock;

  /**
   * Constructor for Item.
   *
   * @param id - the ID used to identify it in the db
   * @param name - the name of the item
   * @param price - the price of the item
   * @param description - the description of the item
   * @param stock - the stock of the item (total stock)
   */
  public Item(Long id, String name, Float price, String description, Integer stock) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.description = description;
    this.stock = stock;
    this.availableStock = stock;
  }

  public Item() {
    // for orm
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getAvailableStock() {
    return availableStock;
  }

  public void setAvailableStock(Integer availableStock) {
    this.availableStock = availableStock;
  }

  /**
   * Method that updates an item with the information from another item.
   *
   * @param item - the item to update to
   * @return - if the update went through
   */
  public boolean updateItem(Item item) {
    if (item.getName() == null
        || item.getPrice() == null
        || item.getDescription() == null
        || item.getStock() == null
        || item.getAvailableStock() == null) {
      return false;
    }

    this.name = item.getName();
    this.price = item.getPrice();
    this.description = item.getDescription();
    int previousStock = this.stock;
    this.stock = item.getStock();
    this.availableStock += (this.stock - previousStock);
    return true;
  }

  @Override
  public String toString() {
    return "Item{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", price="
        + price
        + ", description='"
        + description
        + '\''
        + ", stock="
        + stock
        + ", availableStock="
        + availableStock
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    return Objects.equals(id, item.id)
        && Objects.equals(name, item.name)
        && Objects.equals(price, item.price)
        && Objects.equals(description, item.description)
        && Objects.equals(stock, item.stock)
        && Objects.equals(availableStock, item.availableStock);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, description, stock, availableStock);
  }
}
