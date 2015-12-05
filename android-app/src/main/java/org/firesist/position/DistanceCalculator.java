package org.firesist.position;

import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import java.util.ArrayList;

public class DistanceCalculator {

	private FastFourierTransformer transformer;

	public DistanceCalculator() {
		transformer = new FastFourierTransformer(DftNormalization.STANDARD);
	}


	public double calculateDistance(ArrayList<Float> acceleration, int sensorDelay) {

		// apache math FFT uses doubles instead of floats
		// (as it should)
		double accel[] = new double[acceleration.size()];
		for (int i = 0; i < acceleration.size(); i++) {
			accel[i] = acceleration.get(i)
				.doubleValue();
		}

		// FFT the acceleration
		Complex transformOutput[] = transformer.transform(accel, TransformType.FORWARD);

		double omega = 0;

		// divide by (-omega^2)
		for (int i = 0; i < transformOutput.length; i++) {
			omega = i * sensorDelay;
			transformOutput[i] = new Complex(transformOutput[i].getReal() / -(omega * omega));
		}

		// FFT^-1
		transformOutput = transformer.transform(accel, TransformType.INVERSE);

		double displacement = 0;
		for (int i = 0; i < transformOutput.length; i++) {
			displacement += Math.abs(transformOutput[i].getReal());
		}

		// Return distance 
		return displacement;
	}
}
