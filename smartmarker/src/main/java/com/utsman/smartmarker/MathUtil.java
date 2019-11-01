package com.utsman.smartmarker;

public class MathUtil {

    public static double computeHeading(SmartLatLon from, SmartLatLon to) {
        double fromLat = Math.toRadians(from.getLatitude());
        double fromLng = Math.toRadians(from.getLongitude());
        double toLat = Math.toRadians(to.getLatitude());
        double toLng = Math.toRadians(to.getLongitude());
        double dLng = toLng - fromLng;
        double heading = Math.atan2(Math.sin(dLng) * Math.cos(toLat), Math.cos(fromLat) * Math.sin(toLat) - Math.sin(fromLat) * Math.cos(toLat) * Math.cos(dLng));
        return MathUtil.wrap(Math.toDegrees(heading));
    }

    private static double wrap(double n) {
        double min = -180.0D;
        double max = 180.0D;
        return n >= min && n < max ? n : mod(n - -180.0D, max - min) + min;
    }

    private static double mod(double x, double m) {
        return (x % m + m) % m;
    }
}
