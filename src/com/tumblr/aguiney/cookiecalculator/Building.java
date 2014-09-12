package com.tumblr.aguiney.cookiecalculator;

public enum Building {
	CURSOR(15, 0.1),
	GRANDMA(100, 0.5),
	FARM(500, 4),
	FACTORY(3000, 10),
	MINE(10000, 40),
	SHIPMENT(40000, 100),
	ALCHEMY_LAB(200000, 400),
	PORTAL(1666666, 6666),
	TIME_MACHINE(123456789, 98765),
	ANTIMATTER_CONDENSER(3999999999L, 999999),
	PRISM(75000000000L, 10000000);
	
	private final double baseCost;
	// Singleton. I may incorporate cps functions in future updates.
	private final double baseCPS;
	private double multiplier = 1;
	
	Building(double baseCost, double baseCPS) {
		this.baseCost = baseCost;
		this.baseCPS = baseCPS;
	}
	
	// Returns the current price of the building.
	public double price(int buildingsOwned) {
		if (buildingsOwned < 0) return 0.0;
		double price = (baseCost * multiplier) * Math.pow(1.15, buildingsOwned);
		return Math.round(price);
	}
	
	// Returns the cumulative price of "target" buildings.
	public double cumulativePrice(int target) {
		double price = (baseCost * multiplier)
				* (Math.pow(1.15, target) - 1) / 0.15;
		return Math.round(price);
	}
	
	// Returns the refund value of "amount" buildings.
	public double refund(int amount, int buildingsOwned) {
		double refund = 0.0;
		if (amount > buildingsOwned) amount = buildingsOwned;
		
		for (int i = amount - 1; i >= 0; i--) {
			buildingsOwned--;
			refund += Math.floor(price(buildingsOwned) * 0.585);
		}
		return refund;
	}
	
	/* Returns the amount of buildings that can be purchased 
	 * with an amount of cookies. */
	public int quantity(long cookies, int buildingsOwned) {
		int quantity = 0;
		while (cookies >= price(buildingsOwned)) {
			cookies -= price(buildingsOwned);
			buildingsOwned++;
			quantity++;
		}
		return quantity;
	}
	
	// Getters.
	public double getBaseCost() {
		return baseCost;
	}
	
	public double getBaseCPS() {
		return baseCPS;
	}
	
	public double getMultiplier() {
		return multiplier;
	}
	
	public void setMultiplier(double newMultiplier) {
		multiplier = newMultiplier;
	}
}
