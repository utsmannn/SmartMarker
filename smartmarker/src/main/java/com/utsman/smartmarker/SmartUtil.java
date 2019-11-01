package com.utsman.smartmarker;

class SmartUtil {

    static double getAngle(SmartLatLon fromSmartLatLon, SmartLatLon toSmartLatLon) {
        double heading = 0.0;
        if (fromSmartLatLon != toSmartLatLon) {
            heading = MathUtil.computeHeading(fromSmartLatLon, toSmartLatLon);
        }

        return heading;
    }
}
