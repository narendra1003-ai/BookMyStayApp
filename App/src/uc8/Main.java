import java.util.HashMap;
import java.util.Map;

// Custom Exceptions
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

class InsufficientInventoryException extends Exception {
    public InsufficientInventoryException(String message) {
        super(message);
    }
}

class InvalidNightsException extends Exception {
    public InvalidNightsException(String message) {
        super(message);
    }
}

class InvalidGuestNameException extends Exception {
    public InvalidGuestNameException(String message) {
        super(message);
    }
}

// Validator
class BookingValidator {
    private static final String[] VALID_ROOM_TYPES = {"Standard", "Deluxe", "Suite"};
    private static final int MIN_NIGHTS = 1;
    private static final int MAX_NIGHTS = 30;

    public static void validateRoomType(String roomType) throws InvalidRoomTypeException {
        for (String valid : VALID_ROOM_TYPES) {
            if (valid.equals(roomType)) {
                return;
            }
        }
        throw new InvalidRoomTypeException("Invalid room type: " + roomType +
                ". Accepted types: Standard, Deluxe, Suite");
    }

    public static void validateNights(int nights) throws InvalidNightsException {
        if (nights < MIN_NIGHTS || nights > MAX_NIGHTS) {
            throw new InvalidNightsException("Invalid nights: " + nights +
                    ". Must be between " + MIN_NIGHTS + " and " + MAX_NIGHTS);
        }
    }

    public static void validateGuestName(String name) throws InvalidGuestNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidGuestNameException("Guest name cannot be empty");
        }
    }
}

// Room Inventory with validation
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 10);
        inventory.put("Deluxe", 5);
        inventory.put("Suite", 3);
    }

    public int getAvailability(String roomType) throws InvalidRoomTypeException {
        BookingValidator.validateRoomType(roomType);
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseInventory(String roomType, int quantity)
            throws InvalidRoomTypeException, InsufficientInventoryException {
        BookingValidator.validateRoomType(roomType);

        int current = inventory.getOrDefault(roomType, 0);

        if (quantity <= 0) {
            throw new InsufficientInventoryException("Quantity must be positive");
        }

        if (current < quantity) {
            throw new InsufficientInventoryException("Insufficient inventory for " + roomType +
                    ". Available: " + current + ", Requested: " + quantity);
        }

        inventory.put(roomType, current - quantity);
    }

    public void displayInventory() {
        System.out.println("=== Current     Room     Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Book My Stay: Use     Case 9 Error Handling & Validation ===\n");

        RoomInventory inventory = new RoomInventory();
        inventory.displayInventory();
        System.out.println();

        // Test 1: Valid booking
        System.out.println("Test 1: Valid booking");
        try {
            BookingValidator.validateRoomType("Deluxe");
            BookingValidator.validateNights(3);
            BookingValidator.validateGuestName("John Doe");
            inventory.decreaseInventory("Deluxe", 1);
            System.out.println("✓ Booking successful for John Doe - Deluxe, 3 nights");
        } catch (InvalidRoomTypeException | InsufficientInventoryException |
                 InvalidNightsException | InvalidGuestNameException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 2: Invalid room type
        System.out.println("Test 2: Invalid room type");
        try {
            BookingValidator.validateRoomType("Presidential");
            inventory.decreaseInventory("Presidential", 1);
            System.out.println("✓ Booking successful");
        } catch (InvalidRoomTypeException e) {
            System.out.println("✗ Error: " + e.getMessage());
        } catch (InsufficientInventoryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 3: Invalid nights
        System.out.println("Test 3: Invalid nights (too many)");
        try {
            BookingValidator.validateNights(45);
            System.out.println("✓ Booking successful");
        } catch (InvalidNightsException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 4: Empty guest name
        System.out.println("Test 4: Empty guest name");
        try {
            BookingValidator.validateGuestName("");
            System.out.println("✓ Booking successful");
        } catch (InvalidGuestNameException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 5: Insufficient inventory
        System.out.println("Test 5: Insufficient inventory");
        try {
            BookingValidator.validateRoomType("Suite");
            BookingValidator.validateNights(2);
            BookingValidator.validateGuestName("Jane Smith");
            inventory.decreaseInventory("Suite", 5);
            System.out.println("✓ Booking successful");
        } catch (InvalidRoomTypeException | InsufficientInventoryException |
                 InvalidNightsException | InvalidGuestNameException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 6: Another valid booking
        System.out.println("Test 6: Another valid booking");
        try {
            BookingValidator.validateRoomType("Standard");
            BookingValidator.validateNights(5);
            BookingValidator.validateGuestName("Alice Brown");
            inventory.decreaseInventory("Standard", 2);
            System.out.println("✓ Booking successful for Alice Brown - Standard, 5 nights");
        } catch (InvalidRoomTypeException | InsufficientInventoryException |
                 InvalidNightsException | InvalidGuestNameException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Final inventory status
        System.out.println("=== Final Room Inventory ===");
        inventory.displayInventory();
        System.out.println("\nSystem remained stable after all error scenarios.");
    }
}