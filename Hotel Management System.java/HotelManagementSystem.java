import java.util.*;

// =============================================
// ABSTRACTION - Abstract class
// =============================================
abstract class Room {
    private int roomNumber;       // ENCAPSULATION - private field
    private double pricePerNight;
    private boolean isBooked;

    public Room(int roomNumber, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    // ENCAPSULATION - Getters & Setters
    public int getRoomNumber()        { return roomNumber; }
    public double getPricePerNight()  { return pricePerNight; }
    public boolean isBooked()         { return isBooked; }
    public void setBooked(boolean b)  { this.isBooked = b; }

    // ABSTRACTION - Abstract method (subclass must implement)
    public abstract String getRoomType();
    public abstract String getAmenities();

    // POLYMORPHISM - Overriding toString
    @Override
    public String toString() {
        return String.format("[%s] Room %d | Price: %.0f/night | %s | %s",
                getRoomType(), roomNumber, pricePerNight,
                isBooked ? "BOOKED" : "AVAILABLE", getAmenities());
    }
}

// =============================================
// INHERITANCE - Standard Room extends Room
// =============================================
class StandardRoom extends Room {
    public StandardRoom(int roomNumber) {
        super(roomNumber, 2000);
    }

    @Override
    public String getRoomType()   { return "Standard"; }

    @Override
    public String getAmenities() { return "WiFi, TV, AC"; }
}

// =============================================
// INHERITANCE - Deluxe Room extends Room
// =============================================
class DeluxeRoom extends Room {
    public DeluxeRoom(int roomNumber) {
        super(roomNumber, 4500);
    }

    @Override
    public String getRoomType()   { return "Deluxe"; }

    @Override
    public String getAmenities() { return "WiFi, TV, AC, Mini-Bar, Sea View"; }
}

// =============================================
// INHERITANCE - Suite extends Room
// =============================================
class Suite extends Room {
    public Suite(int roomNumber) {
        super(roomNumber, 9000);
    }

    @Override
    public String getRoomType()   { return "Suite"; }

    @Override
    public String getAmenities() { return "WiFi, 2 TVs, Jacuzzi, Butler, Balcony"; }
}

// =============================================
// Booking class
// =============================================
class Booking {
    private static int counter = 1000;
    private int bookingId;
    private String guestName;
    private Room room;
    private int nights;

    public Booking(String guestName, Room room, int nights) {
        this.bookingId = ++counter;
        this.guestName = guestName;
        this.room = room;
        this.nights = nights;
        room.setBooked(true);
    }

    public int getBookingId()   { return bookingId; }
    public Room getRoom()       { return room; }

    public double getTotalCost() {
        return room.getPricePerNight() * nights;
    }

    @Override
    public String toString() {
        return String.format("Booking #%d | Guest: %s | %s Room %d | %d nights | Total: %.0f BDT",
                bookingId, guestName, room.getRoomType(),
                room.getRoomNumber(), nights, getTotalCost());
    }
}

// =============================================
// Hotel class - main management
// =============================================
class Hotel {
    private String name;
    private List<Room> rooms = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    public Hotel(String name) {
        this.name = name;
        // Add rooms
        rooms.add(new StandardRoom(101));
        rooms.add(new StandardRoom(102));
        rooms.add(new DeluxeRoom(201));
        rooms.add(new DeluxeRoom(202));
        rooms.add(new Suite(301));
    }

    public void showAllRooms() {
        System.out.println("\n===== " + name + " - All Rooms =====");
        // POLYMORPHISM - same list, different room types, each prints differently
        for (Room r : rooms) System.out.println(r);
    }

    public void showAvailableRooms() {
        System.out.println("\n===== Available Rooms =====");
        rooms.stream().filter(r -> !r.isBooked()).forEach(System.out::println);
    }

    public void bookRoom(int roomNumber, String guest, int nights) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNumber) {
                if (r.isBooked()) {
                    System.out.println("Room " + roomNumber + " is already booked!");
                } else {
                    Booking b = new Booking(guest, r, nights);
                    bookings.add(b);
                    System.out.println("✓ Booked! " + b);
                }
                return;
            }
        }
        System.out.println("Room " + roomNumber + " not found.");
    }

    public void checkout(int bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId() == bookingId) {
                b.getRoom().setBooked(false);
                bookings.remove(b);
                System.out.println("✓ Checkout done. Thanks for staying at " + name + "!");
                return;
            }
        }
        System.out.println("Booking #" + bookingId + " not found.");
    }

    public void showBookings() {
        System.out.println("\n===== Current Bookings =====");
        if (bookings.isEmpty()) System.out.println("No active bookings.");
        else bookings.forEach(System.out::println);
    }
}

// =============================================
// Main - Demo
// =============================================
public class HotelManagementSystem {
    public static void main(String[] args) {
        Hotel hotel = new Hotel("Dhaka Grand Hotel");

        hotel.showAllRooms();
        hotel.showAvailableRooms();

        System.out.println("\n===== Booking Rooms =====");
        hotel.bookRoom(101, "Rahim Mia", 3);
        hotel.bookRoom(201, "Karim Uddin", 2);
        hotel.bookRoom(301, "Sara Begum", 5);
        hotel.bookRoom(101, "New Guest", 1);  // Already booked!

        hotel.showBookings();
        hotel.showAllRooms();

        System.out.println("\n===== Checkout =====");
        hotel.checkout(1001);
        hotel.showAvailableRooms();
    }
}
