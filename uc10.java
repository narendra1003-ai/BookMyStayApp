import java.util.*;

public class UseCase10BookingCancellation {

    // ------------------ Inventory ---------------------
    static class InventoryManager {
        private Map<String, Integer> roomInventory = new HashMap<>();
        private Stack<String> releasedRooms = new Stack<>();

        public InventoryManager() {
            roomInventory.put("DELUXE", 5);
            roomInventory.put("SUITE", 3);
        }

        public boolean incrementInventory(String roomType) {
            if (!roomInventory.containsKey(roomType)) return false;
            roomInventory.put(roomType, roomInventory.get(roomType) + 1);
            return true;
        }

        public Stack<String> getReleasedRooms() {
            return releasedRooms;
        }

        public void printInventory() {
            System.out.println("\n--- Updated Inventory ---");
            for (String type : roomInventory.keySet()) {
                System.out.println(type + " : " + roomInventory.get(type));
            }
        }
    }

    // ------------------ Booking -----------------------
    static class Booking {
        String bookingId;
        String guestName;
        String roomType;
        String roomId;
        boolean cancelled;

        Booking(String bId, String guest, String type, String rId) {
            this.bookingId = bId;
            this.guestName = guest;
            this.roomType = type;
            this.roomId = rId;
            this.cancelled = false;
        }
    }

    // ------------------ Cancellation Service -----------------------
    static class CancellationService {

        private Map<String, Booking> bookingHistory;
        private InventoryManager inventory;

        CancellationService(Map<String, Booking> bookingHistory, InventoryManager inventory) {
            this.bookingHistory = bookingHistory;
            this.inventory = inventory;
        }

        public void cancelBooking(String bookingId) {

            System.out.println("\nCancelling Booking: " + bookingId);

            // Step 1: Validate booking
            if (!bookingHistory.containsKey(bookingId)) {
                System.out.println("❌ ERROR: Booking does not exist!");
                return;
            }

            Booking b = bookingHistory.get(bookingId);

            if (b.cancelled) {
                System.out.println("❌ ERROR: Booking is already cancelled!");
                return;
            }

            // Step 2: Push room ID to rollback stack
            inventory.getReleasedRooms().push(b.roomId);
            System.out.println("➡ Room ID pushed to rollback stack: " + b.roomId);

            // Step 3: Restore inventory
            inventory.incrementInventory(b.roomType);
            System.out.println("➡ Inventory restored for room type: " + b.roomType);

            // Step 4: Mark booking as cancelled
            b.cancelled = true;
            System.out.println("➡ Booking marked as CANCELLED");

            // Step 5: System state restored
            System.out.println("✔ Cancellation Completed Successfully!");
        }
    }

    // ------------------ Main Driver -----------------------
    public static void main(String[] args) {

        InventoryManager inventory = new InventoryManager();
        Map<String, Booking> bookingHistory = new HashMap<>();

        // Sample confirmed bookings
        bookingHistory.put("B001", new Booking("B001", "Arun", "DELUXE", "D101"));
        bookingHistory.put("B002", new Booking("B002", "Meera", "SUITE", "S201"));
        bookingHistory.put("B003", new Booking("B003", "John", "DELUXE", "D102"));

        CancellationService cancelService = new CancellationService(bookingHistory, inventory);

        System.out.println("=== USE CASE 10: Booking Cancellation & Rollback ===");

        // Simulating cancellation requests
        cancelService.cancelBooking("B002");     // VALID
        cancelService.cancelBooking("B002");     // INVALID - Already cancelled
        cancelService.cancelBooking("B010");     // INVALID - Does not exist

        // Display final system state
        inventory.printInventory();

        System.out.println("\n--- Rollback Stack (LIFO) ---");
        System.out.println(inventory.getReleasedRooms());
    }
}
