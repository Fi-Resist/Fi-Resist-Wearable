package org.firesist.position;

import org.apache.commons.math3.transform.FastFourierTransformer;
import android.util.Log;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import java.util.ArrayList;
import java.util.Arrays;
public class DistanceCalculator {

	private FastFourierTransformer transformer;
	private double storedDisplacement;

	public DistanceCalculator() {
		transformer = new FastFourierTransformer(DftNormalization.STANDARD);
		storedDisplacement = 0;
	}

	public void clearStoredDisplacement() {
		storedDisplacement = 0;
	}

	public double calculateDistance(ArrayList<Float> acceleration, double sensorDelay) {
		int actualEnd = acceleration.size();
		int len = acceleration.size();

		if ((acceleration.size() & (acceleration.size() - 1)) != 0) {
			len =  (int) Math.pow(2, (32 - Integer.numberOfLeadingZeros(acceleration.size() - 1)));
		} 


		// apache math FFT uses doubles instead of floats
		// (as it should)
		double accel[] = new double[len];
		for (int i = 0; i < acceleration.size(); i++) {
			accel[i] = acceleration.get(i)
				.doubleValue();
		}


		// FFT the acceleration
		Complex transformOutput[] = transformer.transform(accel, TransformType.FORWARD);

		Complex omega;
		double noise = sensorDelay / 1000;

		// divide by (-omega^2)
		for (int i = 0; i < transformOutput.length; i++) {
			if (i > 0) {
				omega = transformOutput[i].multiply(-2 * Math.PI * sensorDelay);
				omega = omega.multiply(omega);
				transformOutput[i] = transformOutput[i].divide(omega);
			}
		}

		// FFT^-1
		transformOutput = transformer.transform(transformOutput, TransformType.INVERSE);

		double displacement = transformOutput[actualEnd - 1].subtract(transformOutput[1]).abs();
		storedDisplacement += transformOutput[actualEnd - 1].abs();

		// Return distance 
		return storedDisplacement;
	}
}
