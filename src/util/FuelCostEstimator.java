// FuelCostEstimator: helps calculate fuel cost for trips or catches.
package util;

/**
 * Utility class for fuel cost calculation.
 * Add methods here as needed for trip/catch integration.
 */
public class FuelCostEstimator {
    /**
     * Calculates fuel cost given liters and price per liter.
     * @param liters Amount of fuel used
     * @param pricePerLiter Price per liter
     * @return Total fuel cost
     */
    // Calculate fuel cost: liters used * price per liter
    public static double estimateFuelCost(double liters, double pricePerLiter) {
        return liters * pricePerLiter;
    }
    // Add more methods as needed for trip/catch linkage
}


