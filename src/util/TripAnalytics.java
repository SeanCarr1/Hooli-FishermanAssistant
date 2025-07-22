// TripAnalytics: provides helper methods to analyze trip and catch data for stats and charts.
package util;

import model.TripRecord;
import model.CatchRecord;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TripAnalytics {
    // Get total fuel used from a list of trips
    public static double getTotalFuel(List<TripRecord> trips) {
        return trips.stream().mapToDouble(TripRecord::getFuelUsed).sum();
    }

    // Get average efficiency (catch per fuel) from trips and catches
    public static double getAverageEfficiency(List<TripRecord> trips, List<CatchRecord> catches) {
        if (trips.isEmpty() || catches.isEmpty()) return 0;
        double totalCatch = 0;
        for (TripRecord trip : trips) {
            if (trip.getCatchIds() != null) {
                for (String id : trip.getCatchIds()) {
                    for (CatchRecord cr : catches) {
                        String uniqueId = cr.getDate() + ":" + cr.getFishType() + ":" + cr.getAmount();
                        if (uniqueId.equals(id)) {
                            totalCatch += cr.getAmount();
                        }
                    }
                }
            }
        }
        double totalFuel = getTotalFuel(trips);
        return totalFuel > 0 ? totalCatch / totalFuel : 0;
    }

    // Get the most used boat type from trips
    public static String getMostUsedBoatType(List<TripRecord> trips) {
        Map<String, Integer> freq = new HashMap<>();
        for (TripRecord t : trips) {
            freq.put(t.getBoatType(), freq.getOrDefault(t.getBoatType(), 0) + 1);
        }
        return freq.entrySet().stream().max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey).orElse("N/A");
    }

    // Estimate total CO2 emissions from trips (2.68 kg CO2 per liter)
    public static double getEstimatedCO2(List<TripRecord> trips) {
        double totalFuel = getTotalFuel(trips);
        return totalFuel * 2.68;
    }
}
