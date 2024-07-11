Sorry if i writed wrong, my main language is not english. I used translator in some parts.

### XG7Menus API Documentation

Welcome to the documentation for XG7Menus, an API designed for Spigot servers to create customizable menus efficiently.
XG7Menus provides a flexible framework for developers to implement various menu systems within Spigot plugins. 
Whether you're creating a lobby menu, An edit menu, administrative tools or storage. 
XG7Menus offers the best features to makes easy menu development.

#### Getting Started

To start using XG7Menus in your Spigot plugin, follow these steps:

1. **Include XG7Menus in Your Project**:
   - Add the XG7Menus library to your project's build path or include it as a Maven dependency.
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>com.github.DaviXG7</groupId>
  <artifactId>XG7MenusAPI</artifactId>
  <version>BETA-1.0.0</version>
</dependency>
```
2. **Include it in your main file**:
```java
//This will inicialize the XG7Menus
XG7Menus.inicialize(this);
```
3. **Create a Menu**:
   - Define menu layouts using intuitive methods provided by XG7Menus.
   - Populate menus with items using simple item management APIs.
4. **Handle the menu with click event**:
   - XG7Menus has a custom click event, here is an example:
```java
@EventHandler
public void onClick(MenuClickEvent event) {
   if (!event.getMenu().getId().equals("youid")) return;
   if (event.getSlot() == 0) {
      event.getMenu().updateInventory(
         new InventoryItem(
         Material.DIAMOND,
         "Changed item on slot 0",
         new ArrayList<>(),
         1, 
         //Will change the item with this slot
         0
         )
      );
      //Other methods
      event.getPlayer();
      //If menu is a PlayerMenu
      event.getLocation();
      event.getInventoryItem();
      //Menu click type
      event.getType();
   }
}
```
#### Example Usage
```java
//Create the menu
Menu menu = new Menu("id of the menu", "Title of the menu", /* size of the menu */ 27);
//Add the items
menu.addItems(
  new InventoryItem(Material.DIAMOND, "name of the item", Collections.singletonList("Lore of the item"), /* amount */ 1, /* slot */ 0)
);
//Open the menu
menu.open(player);
```
#### Other menus
```java
//Create the menu
Menu menu = new Menu("id of the menu", "Title of the menu", /* size of the menu */ 27);
//Create a player inventory menu
PlayerMenu playerMenu = new PlayerMenu("id of the menu");
//Create a simple menu pages
MenuPages pages = new MenuPages(menu1, menu2, ...);
//Create a list of item pages
ItemPages itemPages = new ItemPages("id", "title", /* size */27, items, new Menu.InventoryCoordinate(0,0), new Menu.InventoryCoordinate(9,5));
//Create a StorageMenu to Storage items in a json or database
StorageMenu storageMenu = new StorageMenu(/* Inicializes with empty inventory or a map with items or a inventory */);
```
Visit our [website](https://xg7plugins.com) for more.

Thank you for choosing XG7Menus. Start creating engaging menus for your Spigot server today!
