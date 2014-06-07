package com.example.ifitness.calcCalorie;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SensorListener implements SensorEventListener {
	private CalorieInfo mCalorieInfo;
	private float[] preCoordinate;
	private double currentTime = 0, lastTime = 0;
	private float WALKING_THRESHOLD = 20;

	public SensorListener(CalorieInfo calorieInfo) {
		this.mCalorieInfo = calorieInfo;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		if (arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			analyseDate(arg0.values);
		}
	}

	public void analyseDate(float[] values) {
		currentTime = System.currentTimeMillis();
		mCalorieInfo.setTime(null);
		if (currentTime - lastTime > 200) {
			if (preCoordinate == null) {
				preCoordinate = new float[3];

			} else {
				int angle = calculateAngle(values, preCoordinate);
				if (angle >= WALKING_THRESHOLD) {
					int steps = mCalorieInfo.getSteps();
					mCalorieInfo.setSteps(++steps);
				}
			}
			for (int i = 0; i < 3; i++) {
				preCoordinate[i] = values[i];
			}
			lastTime = currentTime;
		}
	}

	public int calculateAngle(float[] newPoints, float[] oldPoints) {
		int angle = 0;
		float vectorProduct = 0;
		float newMold = 0;
		float oldMold = 0;
		for (int i = 0; i < 3; i++) {
			vectorProduct += newPoints[i] * oldPoints[i];
			newMold += newPoints[i] * newPoints[i];
			oldMold += oldPoints[i] * oldPoints[i];
		}
		newMold = (float) Math.sqrt(newMold);
		oldMold = (float) Math.sqrt(oldMold);

		float cosineAngle = (float) (vectorProduct / (newMold * oldMold));

		float fange = (float) Math.toDegrees(Math.acos(cosineAngle));
		angle = (int) fange;
		return angle;
	}

}
