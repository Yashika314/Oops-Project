
package oops.project;

import java.time.LocalDate;
import java.util.*;

// Enum for menu categories
enum MenuCategory {
    STARTER, MAIN_COURSE, DESSERT, BEVERAGE
}

// Record for immutable menu items
record MenuItem(String name, double price, MenuCategory category) {}

// Abstract sealed class for employees
sealed abstract class Employee permits Manager, Staff {
    private final String name;
    private final int id;

    protected Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public abstract void performDuty();
}

// Manager class
final class Manager extends Employee {
    public Manager(String name, int id) {
        super(name, id);
    }

    @Override
    public void performDuty() {
        System.out.println("Manager is generating the sales report.");
    }

    public void generateReport(List<Order> orders) {
        double totalSales = orders.stream().mapToDouble(Order::getTotalPrice).sum();
        System.out.println("Total Sales for " + LocalDate.now() + ": " + totalSales);
    }
}

// Staff class
final class Staff extends Employee {
    public Staff(String name, int id) {
        super(name, id);
    }

    @Override
    public void performDuty() {
        System.out.println("Staff is managing customer orders.");
    }
}

// Class to manage orders
class Order {
    private static int orderCounter = 0;
    private final int orderId;
    private final List<MenuItem> items;

    public Order() {
        this.orderId = ++orderCounter;
        this.items = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(MenuItem::price).sum();
    }

    public void printOrder() {
        System.out.println("Order ID: " + orderId);
        items.forEach(item -> System.out.println(item.name() + " - " + item.price()));
        System.out.println("Total: " + getTotalPrice());
    }
}

// Main application class
public class RestaurantManagementSystem {
    private final List<MenuItem> menu;
    private final List<Order> orders;

    public RestaurantManagementSystem() {
        menu = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public void addMenuItem(String name, double price, MenuCategory category) {
        menu.add(new MenuItem(name, price, category));
    }

    public void displayMenu() {
        System.out.println("--- Menu ---");
        menu.forEach(item -> System.out.println(item.name() + " - " + item.price() + " - " + item.category()));
    }

    public Order createOrder() {
        Order order = new Order();
        orders.add(order);
        return order;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RestaurantManagementSystem system = new RestaurantManagementSystem();

        // Adding default menu items
        system.addMenuItem("Soup", 5.99, MenuCategory.STARTER);
        system.addMenuItem("Steak", 15.99, MenuCategory.MAIN_COURSE);
        system.addMenuItem("Cake", 4.99, MenuCategory.DESSERT);
        system.addMenuItem("Coffee", 2.99, MenuCategory.BEVERAGE);

        boolean running = true;

        while (running) {
            System.out.println("\n--- Restaurant Management System ---");
            System.out.println("1. View Menu");
            System.out.println("2. Place Order");
            System.out.println("3. View Orders");
            System.out.println("4. Generate Sales Report");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> system.displayMenu();
                case 2 -> {
                    Order order = system.createOrder();
                    boolean addingItems = true;

                    while (addingItems) {
                        system.displayMenu();
                        System.out.print("Enter the name of the item to add (or 'done' to finish): ");
                        String itemName = scanner.nextLine();

                        if (itemName.equalsIgnoreCase("done")) {
                            addingItems = false;
                        } else {
                            Optional<MenuItem> menuItem = system.menu.stream()
                                .filter(item -> item.name().equalsIgnoreCase(itemName))
                                .findFirst();

                            if (menuItem.isPresent()) {
                                order.addItem(menuItem.get());
                                System.out.println("Item added to order.");
                            } else {
                                System.out.println("Item not found in menu.");
                            }
                        }
                    }

                    System.out.println("Your order:");
                    order.printOrder();
                }
                case 3 -> {
                    if (system.getOrders().isEmpty()) {
                        System.out.println("No orders placed yet.");
                    } else {
                        system.getOrders().forEach(Order::printOrder);
                    }
                }
                case 4 -> {
                    Manager manager = new Manager("Alice", 101);
                    manager.generateReport(system.getOrders());
                }
                case 5 -> {
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}