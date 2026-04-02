package uc8;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Reservation {
    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final int nights;
    private final LocalDateTime bookedAt;

    public Reservation(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.bookedAt = LocalDateTime.now();
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNights() { return nights; }
    public LocalDateTime getBookedAt() { return bookedAt; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%d nights) at %s",
                reservationId, guestName, roomType, nights, bookedAt);
    }
}

class BookingHistory {
    private final List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // return unmodifiable list to avoid external modification
    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public int count() {
        return reservations.size();
    }
}

class BookingReportService {
    public void printBookingHistory(List<Reservation> reservations) {
        System.out.println("=== Booking History (chronological) ===");
        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void printSummary(List<Reservation> reservations) {
        System.out.println("=== Booking Report Summary ===");
        System.out.println("Total confirmed bookings: " + reservations.size());

        long totalNights = reservations.stream()
                .mapToInt(Reservation::getNights)
                .sum();
        System.out.println("Total booked nights: " + totalNights);

        reservations.stream()
                .map(Reservation::getRoomType)
                .distinct()
                .forEach(room -> System.out.println("- Room Type: " + room));
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Book My Stay: Use Case 8 Booking History & Reporting ===");

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("R001", "Alice Smith", "Deluxe", 3));
        history.addReservation(new Reservation("R002", "Bob Johnson", "Standard", 2));
        history.addReservation(new Reservation("R003", "Cathy Lee", "Suite", 5));

        // Admin requests history and report
        reportService.printBookingHistory(history.getReservations());
        reportService.printSummary(history.getReservations());

        // Verify original history remains unchanged after reporting
        System.out.println("History count (final): " + history.count());
    }
}
