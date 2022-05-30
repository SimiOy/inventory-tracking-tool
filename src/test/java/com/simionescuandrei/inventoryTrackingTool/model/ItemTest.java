package com.simionescuandrei.inventoryTrackingTool.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemTest {

  private Item item;

  @BeforeEach
  void setup() {
    item = new Item(1L, "Keyboard", 120F, "fancy RGB", 1600);
  }

  @Test
  void notNull() {
    assertNotNull(item);
  }

  @Test
  void getStock() {
    assertEquals(1600, item.getStock());
  }

  @Test
  void getName() {
    assertEquals("Keyboard", item.getName());
  }

  @Test
  void getPrice() {
    assertEquals(120F, item.getPrice());
  }


  @Test
  void getDescription() {
    assertEquals("fancy RGB", item.getDescription());
  }


  @Test
  void getAvailableStock() {
    assertEquals(1600, item.getAvailableStock());
  }


  @Test
  void updateItem() {
    Item newItem = new Item(1L, "Controller", 60F, "for XBOX", 600);
    item.updateItem(newItem);
    assertEquals(item, newItem);
  }

  @Test
  void testEquals() {
    Item newItem = new Item(1L, "Keyboard", 120F, "fancy RGB", 1600);
    assertTrue(item.equals(newItem));
    newItem = null;
    assertFalse(item.equals(newItem));
    newItem = new Item(2L, "Keyboard", 120F, "fancy RGB", 1600);
    assertFalse(item.equals(newItem));
  }
}