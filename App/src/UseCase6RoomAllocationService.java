import java.util.*;

// Reservation Class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
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

// Booking Service
class BookingService {

    private Queue<Reservation> queue;
    private RoomInventory inventory;

    // Track allocated rooms (uniqueness)
    private Set<String> allocatedRoomIds;

    // Map room type → allocated IDs
    private HashMap<String, Set<String>> allocationMap;

    private int roomCounter = 100; // for generating IDs

    public BookingService(Queue<Reservation> queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.allocationMap = new HashMap<>();
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

                // Confirm booking
                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Room ID: " + roomId + "\n");

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
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println(" Book My Stay - Hotel Booking System");
        System.out.println(" Version 6.0");
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

        System.out.println("\nAll bookings processed successfully!");
    }
}