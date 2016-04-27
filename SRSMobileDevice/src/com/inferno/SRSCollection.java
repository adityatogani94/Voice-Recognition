package com.inferno;

import java.io.IOException;

public interface SRSCollection {
    public boolean callFromSkype(String command) throws IOException;
    public boolean bluetoothon() throws IOException;
    public boolean bluetoothoff() throws IOException;
    public boolean getCarStatus() throws IOException;
    boolean openMapsAndDirections(String command)throws IOException;
    boolean getFuelStations() throws IOException;
    boolean getRestaurants() throws IOException;
    boolean playMusic() throws IOException;
}
