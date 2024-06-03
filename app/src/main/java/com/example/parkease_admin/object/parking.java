package com.example.parkease_admin.object;

public class parking {
    String ParkingSpaceID;
    String currentUser;
    boolean status;
    double longitude, latitude;
    double price;

    public parking() {
    }
    public parking(String parkingSpaceID, String currentUser, boolean status, double longitude, double latitude, double price) {
        ParkingSpaceID = parkingSpaceID;
        this.currentUser = currentUser;
        this.status = status;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getParkingSpaceID() {
        return ParkingSpaceID;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setParkingSpaceID(String parkingSpaceID) {
        ParkingSpaceID = parkingSpaceID;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
