package com.inferno;


import java.io.IOException;

class SRSCollectionImpl extends Object implements SRSCollection{


    public SRSCollectionImpl() {

    }


    public boolean callFromSkype(String name) throws IOException {
        System.out.println("Inside Skype Call");
        Runtime.getRuntime().exec("C:\\Program Files (x86)\\Skype\\Phone\\Skype.exe /callto:"+name+"");
        return true;
    }


    public boolean bluetoothon() throws IOException {
        System.out.println("Inside Bluetooth");
        Runtime.getRuntime().exec("fsquirt");
        return true;
    }

    public boolean bluetoothoff() throws IOException {
        System.out.println("Inside Bluetooth");
        Runtime.getRuntime().exec("");
        return true;
    }

    public boolean getCarStatus() throws IOException {
        System.out.println("Inside Car Status");
        String url = "http://volkswagen-carnet.com/int/en/start/online-devices/mod_vehicle_health_report.html";
        Runtime.getRuntime().exec("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe "+url+" -incognito");
        return true;
    }

    @Override
    public boolean openMapsAndDirections(String command) throws IOException {
        System.out.println("Inside Maps Direction");
        String destination = command;
        double longitude = 0.00;
        double lattitude = 0.00;

        switch(destination){
            case "newyork":
                longitude = 40.71278;
                lattitude = -74.00594;
                break;
            case "boston":
                longitude = 42.36008;
                lattitude = -71.05888;
                break;
            case "tempe":
                longitude = 33.42551;
                lattitude = -111.94001;
                break;
            case "california":
                longitude = 36.77826;
                lattitude = -119.41793;
                break;
            case "seattle":
                longitude = 47.60621;
                lattitude = -122.33207;
                break;
            case "phoenix":
                longitude = 33.44838;
                lattitude = -112.07404;
                break;
            default:
                longitude = 25.76168;
                lattitude = -80.19179;
        }

        String url = "https://maps.google.com?saddr=Current+Location&daddr="+longitude+","+lattitude+"";
        Runtime.getRuntime().exec("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe "+url+" -incognito");
        return true;
    }

    @Override
    public boolean getFuelStations() throws IOException {
        System.out.println("Inside Maps Fuel Station");
        String url = "http://maps.google.com/?q=fuel+station";
        Runtime.getRuntime().exec("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe "+url+" -incognito");
        return true;
    }

    @Override
    public boolean playMusic() throws IOException {
        System.out.println("Inside Music");
        Runtime.getRuntime().exec("\"C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe\" c:\\playlist\\1.mp3");
        return true;
    }

    @Override
    public boolean getRestaurants() throws IOException {
        System.out.println("Inside Maps Restaurants");
        String url = "http://maps.google.com/?q=restaurants";
        Runtime.getRuntime().exec("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe "+url+" -incognito");
        return true;
    }
}
