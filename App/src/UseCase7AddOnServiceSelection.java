import java.util.*;

// Reservation Class
class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = null;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
}

// Inventory Service
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseAvailability(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

// Add-On Service
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

// Add-On Service Manager
class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationToServices;

    public AddOnServiceManager() {
        this.reservationToServices = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        reservationToServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationToServices.get(reservationId).add(service);
    }

    public double getTotalCost(String reservationId) {
        List<AddOnService> services = reservationToServices.get(reservationId);
        if (services == null) return 0.0;
        return services.stream().mapToDouble(AddOnService::getCost).sum();
    }

    public void displayServices(String reservationId) {
        List<AddOnService> services = reservationToServices.get(reservationId);
        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for reservation " + reservationId);
        } else {
            System.out.println("Add-on services for " + reservationId + ":");
            for (AddOnService service : services) {
                System.out.println(" - " + service.getName() + ": $" + service.getCost());
            }
        }
    }
}

// Booking Service
class BookingService {

    private Queue<Reservation> queue;
    private RoomInventory inventory;

    // Track allocated rooms (uniqueness)
    private Set<String> allocatedRoomIds;

    // Map room type → allocated IDs
    private HashMap<String, Set<String>> allocationMap;

    private int roomCounter = 100; // for generating IDs
    private int reservationCounter = 1000; // for reservation IDs
    private List<Reservation> confirmedReservations;

    public BookingService(Queue<Reservation> queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.allocationMap = new HashMap<>();
        this.confirmedReservations = new ArrayList<>();
    }

    // Process booking requests
    public void processBookings() {

        System.out.println("\n----- Processing Bookings -----\n");

        while (!queue.isEmpty()) {

            Reservation r = queue.poll(); // FIFO

            String roomType = r.getRoomType();

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId;
                do {
                    roomId = roomType.substring(0, 2).toUpperCase() + (++roomCounter);
                } while (allocatedRoomIds.contains(roomId));

                // Store in set
                allocatedRoomIds.add(roomId);

                // Store in map
                allocationMap.putIfAbsent(roomType, new HashSet<>());
                allocationMap.get(roomType).add(roomId);

                // Update inventory
                inventory.decreaseAvailability(roomType);

                // Generate reservation ID
                String reservationId = "R" + (++reservationCounter);
                r.setReservationId(reservationId);

                // Add to confirmed
                confirmedReservations.add(r);

                // Confirm booking
                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Room ID: " + roomId);
                System.out.println("Reservation ID: " + reservationId + "\n");

            } else {
                System.out.println("Booking Failed (No availability) for: " + r.getGuestName());
            }
        }
    }

    // Display allocations
    public void displayAllocations() {
        System.out.println("\n----- Allocated Rooms -----");

        for (String type : allocationMap.keySet()) {
            System.out.println(type + " → " + allocationMap.get(type));
        }
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

// Main Class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println(" Book My Stay - Hotel Booking System");
        System.out.println(" Version 7.0");
        System.out.println("=======================================");

        // Create queue
        Queue<Reservation> queue = new LinkedList<>();

        queue.add(new Reservation("Alice", "Single Room"));
        queue.add(new Reservation("Bob", "Single Room"));
        queue.add(new Reservation("Charlie", "Single Room")); // should fail
        queue.add(new Reservation("David", "Suite Room"));

        // Create inventory
        RoomInventory inventory = new RoomInventory();

        // Booking service
        BookingService service = new BookingService(queue, inventory);

        // Process bookings
        service.processBookings();

        // Show allocated rooms
        service.displayAllocations();

        // Add-On Services
        AddOnServiceManager manager = new AddOnServiceManager();

        // Get confirmed reservations
        List<Reservation> confirmed = service.getConfirmedReservations();

        // Add services to reservations
        if (confirmed.size() > 0) {
            manager.addService(confirmed.get(0).getReservationId(), new AddOnService("Breakfast", 15.0));
            manager.addService(confirmed.get(0).getReservationId(), new AddOnService("Airport Shuttle", 25.0));
        }
        if (confirmed.size() > 1) {
            manager.addService(confirmed.get(1).getReservationId(), new AddOnService("Spa Package", 50.0));
        }
        if (confirmed.size() > 2) {
            manager.addService(confirmed.get(2).getReservationId(), new AddOnService("Late Checkout", 10.0));
        }

        // Display add-on services and costs
        System.out.println("\n----- Add-On Services -----\n");
        for (Reservation r : confirmed) {
            manager.displayServices(r.getReservationId());
            double totalCost = manager.getTotalCost(r.getReservationId());
            System.out.println("Total Add-On Cost: $" + totalCost + "\n");
        }

        System.out.println("All bookings and add-on services processed successfully!");
    }
}