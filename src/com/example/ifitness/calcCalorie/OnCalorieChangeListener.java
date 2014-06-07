package com.example.ifitness.calcCalorie;

import java.util.EventListener;

public interface OnCalorieChangeListener extends EventListener {
	public void onStepsChange(CalorieInfo calorieInfo);
	public void onCalorieChange(CalorieInfo calorieInfo);
	public void onTimeChange(CalorieInfo calorieInfo);
}
