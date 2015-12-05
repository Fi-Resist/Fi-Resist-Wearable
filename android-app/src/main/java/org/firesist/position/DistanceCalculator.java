package org.firesist.position;

import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;

public class DistanceCalculator {

	private FastFourierTransformer transformer;

	public DistanceCalculator() {
		transformer = new FastFourierTransformer(DftNormalization.STANDARD);
	}


	public double calculateDistance(Float[] acceleration, int sensorDelay) {

		double accel[] = new double[acceleration.length];
		for (int i = 0; i < acceleration.length; i++) {
			accel[i] = acceleration[i].doubleValue();
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

		// Return distance 
		return transformOutput[transformOutput.length - 1].getReal();
	}
}
