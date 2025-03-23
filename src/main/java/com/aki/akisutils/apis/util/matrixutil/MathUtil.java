package com.aki.akisutils.apis.util.matrixutil;

import java.util.function.DoubleUnaryOperator;

public class MathUtil {

	private static DoubleUnaryOperator SIN = Math::sin;
	private static DoubleUnaryOperator COS = Math::cos;

	public static void setSinFunc(DoubleUnaryOperator sinFunc) {
		if (sinFunc == null)
			throw new IllegalArgumentException();
		SIN = sinFunc;
	}

	public static void setCosFunc(DoubleUnaryOperator cosFunc) {
		if (cosFunc == null)
			throw new IllegalArgumentException();
		COS = cosFunc;
	}

	public static double sin(double a) {
		return SIN.applyAsDouble(a);
	}

	public static double cos(double a) {
		return COS.applyAsDouble(a);
	}

}
