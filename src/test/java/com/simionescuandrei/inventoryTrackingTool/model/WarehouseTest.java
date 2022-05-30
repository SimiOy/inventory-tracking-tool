package com.simionescuandrei.inventoryTrackingTool.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WarehouseTest {

  private Warehouse wh1, wh2, wh3;
  private Map<Long, Integer> itemList1, itemList2;

  @BeforeEach
  void setup() {
    wh1 = new Warehouse(1L, "Amsterdam Depot", "Amsterdam");
    wh3 = new Warehouse(1L, "Amsterdam Depot", "Amsterdam");
    wh2 = new Warehouse(2L, "Rotterdam Depot", "Rotterdam");
    itemList1 = new HashMap<>();
    itemList2 = new HashMap<>();
    Long id = 1L;
    for(int i = 0; i < 9; i++, id++) {
      itemList1.put(id, i * 50);
      itemList2.put(id,i * 20);
    }
    wh1.setItems(itemList1);
    wh3.setItems(itemList1);
    wh2.setItems(itemList2);
  }

  @Test
  void notNull() {
    assertNotNull(wh1);
    assertNotNull(wh2);
  }

  @Test
  void getId() {
    assertEquals(1L,wh1.getId());
    assertEquals(2L,wh2.getId());
  }

  @Test
  void getName() {
    assertEquals("Amsterdam Depot",wh1.getName());
    assertEquals("Rotterdam Depot",wh2.getName());
  }

  @Test
  void getLocation() {
    assertEquals("Amsterdam",wh1.getLocation());
    assertEquals("Rotterdam",wh2.getLocation());
  }

  @Test
  void getItems() {
    assertEquals(itemList1,wh1.getItems());
    assertEquals(itemList2,wh2.getItems());
  }

  @Test
  void addItemWithStock() {
    Item item = new Item(3L,"pc",null,"yes",200);
    assertEquals("Something went wrong", wh1.addItemWithStock(item, 10));

    item = new Item(3L,"pc",60F,"yes",200);
    item.setAvailableStock(50);
    assertEquals("There is insufficient volume for this move", wh1.addItemWithStock(item, 60));
    assertEquals("Stock shouldn't be negative", wh1.addItemWithStock(item, -2));
    assertEquals("Successfully moved", wh1.addItemWithStock(item, 50));
    assertEquals(0,item.getAvailableStock());
  }

  @Test
  void removeItemWithStock() {
    Item item = new Item(3L,"pc",null,"yes",200);
    assertEquals("Something went wrong", wh1.removeItemWithStock(item, 10));

    item = new Item(10L,"pc",60F,"yes",200);
    item.setAvailableStock(50);
    assertEquals("This warehouse doesn't contain this item", wh1.removeItemWithStock(item, 60));
    item = new Item(5L,"pc",60F,"yes",200);
    assertEquals("Stock shouldn't be negative", wh1.removeItemWithStock(item, -2));
    assertEquals("Successfully removed", wh1.removeItemWithStock(item, 50));
    assertEquals(250,item.getAvailableStock());
  }

  @Test
  void testEquals() {
    assertEquals(wh1,wh3);
  }
}