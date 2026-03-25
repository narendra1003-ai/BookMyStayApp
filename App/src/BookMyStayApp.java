import java.util.HashMap;
import java.util.Map;

class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

abstract class Room {
    int beds;
    int size;
    double price;

    Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    abstract void display(int available);
}

class SingleRoom extends Room {
    SingleRoom() {
        super(1, 250, 1500.0);
    }

    void display(int available) {
        System.out.println("Single Room:");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
        System.out.println("Available: " + available);
        System.out.println();
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super(2, 400, 2500.0);
    }

    void display(int available) {
        System.out.println("Double Room:");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
        System.out.println("Available: " + available);
        System.out.println();
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super(3, 750, 5000.0);
    }

    void display(int available) {
        System.out.println("Suite Room:");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
        System.out.println("Available: " + available);
        System.out.println();
    }
}

class RoomSearchService {

    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        if (availability.getOrDefault("Single", 0) > 0) {
            singleRoom.display(availability.get("Single"));
        }

        if (availability.getOrDefault("Double", 0) > 0) {
            doubleRoom.display(availability.get("Double"));
        }

        if (availability.getOrDefault("Suite", 0) > 0) {
            suiteRoom.display(availability.get("Suite"));
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to the Hotel Booking Management System\n");

        RoomInventory inventory = new RoomInventory();

        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomSearchService service = new RoomSearchService();

        System.out.println("Room Search Results:\n");

        service.searchAvailableRooms(inventory, single, dbl, suite);
    }
}