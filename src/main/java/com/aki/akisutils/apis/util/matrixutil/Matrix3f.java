package com.aki.akisutils.apis.util.matrixutil;

import sun.misc.Unsafe;

import java.nio.FloatBuffer;

public class Matrix3f {

	public float m00;
	public float m01;
	public float m02;
	public float m10;
	public float m11;
	public float m12;
	public float m20;
	public float m21;
	public float m22;

	public Matrix3f() {

	}

	public Matrix3f(Matrix3f matrix) {
		this.m00 = matrix.m00;
		this.m01 = matrix.m01;
		this.m02 = matrix.m02;
		this.m10 = matrix.m10;
		this.m11 = matrix.m11;
		this.m12 = matrix.m12;
		this.m20 = matrix.m20;
		this.m21 = matrix.m21;
		this.m22 = matrix.m22;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj != null && this.getClass() == obj.getClass()) {
			Matrix3f matrix = (Matrix3f) obj;
			return Float.compare(matrix.m00, this.m00) == 0
					&& Float.compare(matrix.m01, this.m01) == 0
					&& Float.compare(matrix.m02, this.m02) == 0
					&& Float.compare(matrix.m10, this.m10) == 0
					&& Float.compare(matrix.m11, this.m11) == 0
					&& Float.compare(matrix.m12, this.m12) == 0
					&& Float.compare(matrix.m20, this.m20) == 0
					&& Float.compare(matrix.m21, this.m21) == 0
					&& Float.compare(matrix.m22, this.m22) == 0;
		} else {
			return false;
		}
	}

	public int hashCode() {
		int i = this.m00 != 0.0F ? Float.floatToIntBits(this.m00) : 0;
		i = 31 * i + (this.m01 != 0.0F ? Float.floatToIntBits(this.m01) : 0);
		i = 31 * i + (this.m02 != 0.0F ? Float.floatToIntBits(this.m02) : 0);
		i = 31 * i + (this.m10 != 0.0F ? Float.floatToIntBits(this.m10) : 0);
		i = 31 * i + (this.m11 != 0.0F ? Float.floatToIntBits(this.m11) : 0);
		i = 31 * i + (this.m12 != 0.0F ? Float.floatToIntBits(this.m12) : 0);
		i = 31 * i + (this.m20 != 0.0F ? Float.floatToIntBits(this.m20) : 0);
		i = 31 * i + (this.m21 != 0.0F ? Float.floatToIntBits(this.m21) : 0);
		return 31 * i + (this.m22 != 0.0F ? Float.floatToIntBits(this.m22) : 0);
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("Matrix3f:\n");
		stringbuilder.append(this.m00);
		stringbuilder.append(" ");
		stringbuilder.append(this.m01);
		stringbuilder.append(" ");
		stringbuilder.append(this.m02);
		stringbuilder.append("\n");
		stringbuilder.append(this.m10);
		stringbuilder.append(" ");
		stringbuilder.append(this.m11);
		stringbuilder.append(" ");
		stringbuilder.append(this.m12);
		stringbuilder.append("\n");
		stringbuilder.append(this.m20);
		stringbuilder.append(" ");
		stringbuilder.append(this.m21);
		stringbuilder.append(" ");
		stringbuilder.append(this.m22);
		return stringbuilder.toString();
	}

	public void store(FloatBuffer buf) {
		store(MemoryUtil.getAddress(buf));
	}

	public void store(long addr) {
		Unsafe unsafe = UnsafeUtil.UNSAFE;
		unsafe.putFloat(addr, this.m00);
		unsafe.putFloat(addr + 4, this.m10);
		unsafe.putFloat(addr + 8, this.m20);
		unsafe.putFloat(addr + 12, this.m01);
		unsafe.putFloat(addr + 16, this.m11);
		unsafe.putFloat(addr + 20, this.m21);
		unsafe.putFloat(addr + 24, this.m02);
		unsafe.putFloat(addr + 28, this.m12);
		unsafe.putFloat(addr + 32, this.m22);
	}

	public void load(FloatBuffer buf) {
		load(MemoryUtil.getAddress(buf));
	}

	public void load(long addr) {
		Unsafe unsafe = UnsafeUtil.UNSAFE;
		this.m00 = unsafe.getFloat(addr);
		this.m10 = unsafe.getFloat(addr + 4);
		this.m20 = unsafe.getFloat(addr + 8);
		this.m01 = unsafe.getFloat(addr + 12);
		this.m11 = unsafe.getFloat(addr + 16);
		this.m21 = unsafe.getFloat(addr + 20);
		this.m02 = unsafe.getFloat(addr + 24);
		this.m12 = unsafe.getFloat(addr + 28);
		this.m22 = unsafe.getFloat(addr + 32);
	}

	public void setIdentity() {
		this.m00 = 1.0F;
		this.m01 = 0.0F;
		this.m02 = 0.0F;
		this.m10 = 0.0F;
		this.m11 = 1.0F;
		this.m12 = 0.0F;
		this.m20 = 0.0F;
		this.m21 = 0.0F;
		this.m22 = 1.0F;
	}

	public void adjugate() {
		float a00 = this.m00;
		float a01 = this.m01;
		float a02 = this.m02;
		float a10 = this.m10;
		float a11 = this.m11;
		float a12 = this.m12;
		float a20 = this.m20;
		float a21 = this.m21;
		float a22 = this.m22;
		this.m00 =  a11 * a22 - a12 * a21;
		this.m10 = -a10 * a22 - a12 * a20;
		this.m20 =  a10 * a21 - a11 * a20;
		this.m01 = -a01 * a22 - a02 * a21;
		this.m11 =  a00 * a22 - a02 * a20;
		this.m21 = -a00 * a21 - a01 * a20;
		this.m02 =  a01 * a12 - a02 * a11;
		this.m12 = -a00 * a12 - a02 * a10;
		this.m22 =  a00 * a11 - a01 * a10;
	}

	public float determinant() {
		float a00 = this.m00;
		float a01 = this.m01;
		float a02 = this.m02;
		float a10 = this.m10;
		float a11 = this.m11;
		float a12 = this.m12;
		float a20 = this.m20;
		float a21 = this.m21;
		float a22 = this.m22;
		float d00 = a11 * a22 - a12 * a21;
		float d01 = a10 * a22 - a12 * a20;
		float d02 = a10 * a21 - a11 * a20;
		return a00 * d00 - a01 * d01 + a02 * d02;
	}

	public void transpose() {
		float f = this.m10;
		this.m10 = this.m01;
		this.m01 = f;
		f = this.m20;
		this.m20 = this.m02;
		this.m02 = f;
		f = this.m21;
		this.m21 = this.m12;
		this.m12 = f;
	}

	public boolean invert() {
		float a00 = this.m00;
		float a01 = this.m01;
		float a02 = this.m02;
		float a10 = this.m10;
		float a11 = this.m11;
		float a12 = this.m12;
		float a20 = this.m20;
		float a21 = this.m21;
		float a22 = this.m22;
		float d00 = a11 * a22 - a12 * a21;
		float d01 = a10 * a22 - a12 * a20;
		float d02 = a10 * a21 - a11 * a20;
		float d = a00 * d00 - a01 * d01 + a02 * d02;
		if (d == 0.0F) {
			return false;
		}
		float f = 1.0F / d;
		this.m00 =  d00 * f;
		this.m10 = -d01 * f;
		this.m20 =  d02 * f;
		this.m01 = -(a01 * a22 - a02 * a21) * f;
		this.m11 =  (a00 * a22 - a02 * a20) * f;
		this.m21 = -(a00 * a21 - a01 * a20) * f;
		this.m02 =  (a01 * a12 - a02 * a11) * f;
		this.m12 = -(a00 * a12 - a02 * a10) * f;
		this.m22 =  (a00 * a11 - a01 * a10) * f;
		return true;
	}

	public void multiply(Matrix3f matrix) {
		float a00 = this.m00;
		float a01 = this.m01;
		float a02 = this.m02;
		float a10 = this.m10;
		float a11 = this.m11;
		float a12 = this.m12;
		float a20 = this.m20;
		float a21 = this.m21;
		float a22 = this.m22;
		float b00 = matrix.m00;
		float b01 = matrix.m01;
		float b02 = matrix.m02;
		float b10 = matrix.m10;
		float b11 = matrix.m11;
		float b12 = matrix.m12;
		float b20 = matrix.m20;
		float b21 = matrix.m21;
		float b22 = matrix.m22;
		this.m00 = a00 * b00 + a01 * b10 + a02 * b20;
		this.m01 = a00 * b01 + a01 * b11 + a02 * b21;
		this.m02 = a00 * b02 + a01 * b12 + a02 * b22;
		this.m10 = a10 * b00 + a11 * b10 + a12 * b20;
		this.m11 = a10 * b01 + a11 * b11 + a12 * b21;
		this.m12 = a10 * b02 + a11 * b12 + a12 * b22;
		this.m20 = a20 * b00 + a21 * b10 + a22 * b20;
		this.m21 = a20 * b01 + a21 * b11 + a22 * b21;
		this.m22 = a20 * b02 + a21 * b12 + a22 * b22;
	}

	public void multiply(float multiplier) {
		this.m00 *= multiplier;
		this.m01 *= multiplier;
		this.m02 *= multiplier;
		this.m10 *= multiplier;
		this.m11 *= multiplier;
		this.m12 *= multiplier;
		this.m20 *= multiplier;
		this.m21 *= multiplier;
		this.m22 *= multiplier;
	}

	public void add(Matrix3f matrix) {
		this.m00 += matrix.m00;
		this.m01 += matrix.m01;
		this.m02 += matrix.m02;
		this.m10 += matrix.m10;
		this.m11 += matrix.m11;
		this.m12 += matrix.m12;
		this.m20 += matrix.m20;
		this.m21 += matrix.m21;
		this.m22 += matrix.m22;
	}

	public void subtract(Matrix3f matrix) {
		this.m00 -= matrix.m00;
		this.m01 -= matrix.m01;
		this.m02 -= matrix.m02;
		this.m10 -= matrix.m10;
		this.m11 -= matrix.m11;
		this.m12 -= matrix.m12;
		this.m20 -= matrix.m20;
		this.m21 -= matrix.m21;
		this.m22 -= matrix.m22;
	}

	public void negate() {
		this.m00 = -this.m00;
		this.m01 = -this.m01;
		this.m02 = -this.m02;
		this.m10 = -this.m10;
		this.m11 = -this.m11;
		this.m12 = -this.m12;
		this.m20 = -this.m20;
		this.m21 = -this.m21;
		this.m22 = -this.m22;
	}

	public Matrix3f copy() {
		return new Matrix3f(this);
	}

	public static Matrix3f createIdentityMatrix() {
		Matrix3f matrix = new Matrix3f();
		matrix.m00 = 1.0F;
		matrix.m11 = 1.0F;
		matrix.m22 = 1.0F;
		return matrix;
	}

	public static Matrix3f createScaleMatrix(float x, float y, float z) {
		Matrix3f matrix = new Matrix3f();
		matrix.m00 = x;
		matrix.m11 = y;
		matrix.m22 = z;
		return matrix;
	}

	public static Matrix3f createRotateMatrix(Quaternion quaternion) {
		Matrix3f matrix = new Matrix3f();
		float xx = 2.0F * quaternion.x * quaternion.x;
		float yy = 2.0F * quaternion.y * quaternion.y;
		float zz = 2.0F * quaternion.z * quaternion.z;
		float xy = quaternion.x * quaternion.y;
		float yz = quaternion.y * quaternion.z;
		float zx = quaternion.z * quaternion.x;
		float xw = quaternion.x * quaternion.w;
		float yw = quaternion.y * quaternion.w;
		float zw = quaternion.z * quaternion.w;

		matrix.m00 = 1.0F - yy - zz;
		matrix.m11 = 1.0F - zz - xx;
		matrix.m22 = 1.0F - xx - yy;
		matrix.m10 = 2.0F * (xy + zw);
		matrix.m01 = 2.0F * (xy - zw);
		matrix.m20 = 2.0F * (zx - yw);
		matrix.m02 = 2.0F * (zx + yw);
		matrix.m21 = 2.0F * (yz + xw);
		matrix.m12 = 2.0F * (yz - xw);
		return matrix;
	}

	public static Matrix3f createRotateXMatrix(Quaternion quaternion) {
		Matrix3f matrix = new Matrix3f();
		float xx = 2.0F * quaternion.x * quaternion.x;
		float xw = quaternion.x * quaternion.w;

		matrix.m00 = 1.0F;
		matrix.m11 = 1.0F - xx;
		matrix.m22 = 1.0F - xx;
		matrix.m21 = 2.0F * xw;
		matrix.m12 = 2.0F * -xw;
		return matrix;
	}

	public static Matrix3f createRotateYMatrix(Quaternion quaternion) {
		Matrix3f matrix = new Matrix3f();
		float yy = 2.0F * quaternion.y * quaternion.y;
		float yw = quaternion.y * quaternion.w;

		matrix.m00 = 1.0F - yy;
		matrix.m11 = 1.0F;
		matrix.m22 = 1.0F - yy;
		matrix.m20 = 2.0F * -yw;
		matrix.m02 = 2.0F * yw;
		return matrix;
	}

	public static Matrix3f createRotateZMatrix(Quaternion quaternion) {
		Matrix3f matrix = new Matrix3f();
		float zz = 2.0F * quaternion.z * quaternion.z;
		float zw = quaternion.z * quaternion.w;

		matrix.m00 = 1.0F - zz;
		matrix.m11 = 1.0F - zz;
		matrix.m22 = 1.0F;
		matrix.m10 = 2.0F * zw;
		matrix.m01 = 2.0F * -zw;
		return matrix;
	}

	public void scale(float x, float y, float z) {
		this.m00 *= x;
		this.m10 *= x;
		this.m20 *= x;
		this.m01 *= y;
		this.m11 *= y;
		this.m21 *= y;
		this.m02 *= z;
		this.m12 *= z;
		this.m22 *= z;
	}

	public void rotate(Quaternion quaternion) {
		// setup rotation matrix
		float xx = 2.0F * quaternion.x * quaternion.x;
		float yy = 2.0F * quaternion.y * quaternion.y;
		float zz = 2.0F * quaternion.z * quaternion.z;
		float xy = quaternion.x * quaternion.y;
		float yz = quaternion.y * quaternion.z;
		float zx = quaternion.z * quaternion.x;
		float xw = quaternion.x * quaternion.w;
		float yw = quaternion.y * quaternion.w;
		float zw = quaternion.z * quaternion.w;

		float r00 = 1.0F - yy - zz;
		float r11 = 1.0F - zz - xx;
		float r22 = 1.0F - xx - yy;
		float r10 = 2.0F * (xy + zw);
		float r01 = 2.0F * (xy - zw);
		float r20 = 2.0F * (zx - yw);
		float r02 = 2.0F * (zx + yw);
		float r21 = 2.0F * (yz + xw);
		float r12 = 2.0F * (yz - xw);

		// multiply matrices
		float f00 = this.m00;
		float f01 = this.m01;
		float f02 = this.m02;
		float f10 = this.m10;
		float f11 = this.m11;
		float f12 = this.m12;
		float f20 = this.m20;
		float f21 = this.m21;
		float f22 = this.m22;

		this.m00 = f00 * r00 + f01 * r10 + f02 * r20;
		this.m01 = f00 * r01 + f01 * r11 + f02 * r21;
		this.m02 = f00 * r02 + f01 * r12 + f02 * r22;
		this.m10 = f10 * r00 + f11 * r10 + f12 * r20;
		this.m11 = f10 * r01 + f11 * r11 + f12 * r21;
		this.m12 = f10 * r02 + f11 * r12 + f12 * r22;
		this.m20 = f20 * r00 + f21 * r10 + f22 * r20;
		this.m21 = f20 * r01 + f21 * r11 + f22 * r21;
		this.m22 = f20 * r02 + f21 * r12 + f22 * r22;
	}

	public void rotateX(Quaternion quaternion) {
		// setup rotation matrix
		float xx = 2.0F * quaternion.x * quaternion.x;
		float xw = quaternion.x * quaternion.w;

		float r11 = 1.0F - xx;
		float r22 = 1.0F - xx;
		float r21 = 2.0F * xw;
		float r12 = 2.0F * -xw;

		// multiply matrices
		float f01 = this.m01;
		float f02 = this.m02;
		float f11 = this.m11;
		float f12 = this.m12;
		float f21 = this.m21;
		float f22 = this.m22;

		this.m01 = f01 * r11 + f02 * r21;
		this.m02 = f01 * r12 + f02 * r22;
		this.m11 = f11 * r11 + f12 * r21;
		this.m12 = f11 * r12 + f12 * r22;
		this.m21 = f21 * r11 + f22 * r21;
		this.m22 = f21 * r12 + f22 * r22;
	}

	public void rotateY(Quaternion quaternion) {
		// setup rotation matrix
		float yy = 2.0F * quaternion.y * quaternion.y;
		float yw = quaternion.y * quaternion.w;

		float r00 = 1.0F - yy;
		float r22 = 1.0F - yy;
		float r20 = 2.0F * -yw;
		float r02 = 2.0F * yw;

		// multiply matrices
		float f00 = this.m00;
		float f02 = this.m02;
		float f10 = this.m10;
		float f12 = this.m12;
		float f20 = this.m20;
		float f22 = this.m22;

		this.m00 = f00 * r00 + f02 * r20;
		this.m02 = f00 * r02 + f02 * r22;
		this.m10 = f10 * r00 + f12 * r20;
		this.m12 = f10 * r02 + f12 * r22;
		this.m20 = f20 * r00 + f22 * r20;
		this.m22 = f20 * r02 + f22 * r22;
	}

	public void rotateZ(Quaternion quaternion) {
		// setup rotation matrix
		float zz = 2.0F * quaternion.z * quaternion.z;
		float zw = quaternion.z * quaternion.w;

		float r00 = 1.0F - zz;
		float r11 = 1.0F - zz;
		float r10 = 2.0F * zw;
		float r01 = 2.0F * -zw;

		// multiply matrices
		float f00 = this.m00;
		float f01 = this.m01;
		float f10 = this.m10;
		float f11 = this.m11;
		float f20 = this.m20;
		float f21 = this.m21;

		this.m00 = f00 * r00 + f01 * r10;
		this.m01 = f00 * r01 + f01 * r11;
		this.m10 = f10 * r00 + f11 * r10;
		this.m11 = f10 * r01 + f11 * r11;
		this.m20 = f20 * r00 + f21 * r10;
		this.m21 = f20 * r01 + f21 * r11;
	}

}
