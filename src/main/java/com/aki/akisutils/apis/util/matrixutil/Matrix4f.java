package com.aki.akisutils.apis.util.matrixutil;

import sun.misc.Unsafe;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class Matrix4f {
	public float m00;
	public float m01;
	public float m02;
	public float m03;
	public float m10;
	public float m11;
	public float m12;
	public float m13;
	public float m20;
	public float m21;
	public float m22;
	public float m23;
	public float m30;
	public float m31;
	public float m32;
	public float m33;

	public Matrix4f() {

	}

	public Matrix4f(Matrix4f matrix) {
		this.m00 = matrix.m00;
		this.m01 = matrix.m01;
		this.m02 = matrix.m02;
		this.m03 = matrix.m03;
		this.m10 = matrix.m10;
		this.m11 = matrix.m11;
		this.m12 = matrix.m12;
		this.m13 = matrix.m13;
		this.m20 = matrix.m20;
		this.m21 = matrix.m21;
		this.m22 = matrix.m22;
		this.m23 = matrix.m23;
		this.m30 = matrix.m30;
		this.m31 = matrix.m31;
		this.m32 = matrix.m32;
		this.m33 = matrix.m33;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj != null && this.getClass() == obj.getClass()) {
			Matrix4f matrix = (Matrix4f) obj;
			return Float.compare(matrix.m00, this.m00) == 0
					&& Float.compare(matrix.m01, this.m01) == 0
					&& Float.compare(matrix.m02, this.m02) == 0
					&& Float.compare(matrix.m03, this.m03) == 0
					&& Float.compare(matrix.m10, this.m10) == 0
					&& Float.compare(matrix.m11, this.m11) == 0
					&& Float.compare(matrix.m12, this.m12) == 0
					&& Float.compare(matrix.m13, this.m13) == 0
					&& Float.compare(matrix.m20, this.m20) == 0
					&& Float.compare(matrix.m21, this.m21) == 0
					&& Float.compare(matrix.m22, this.m22) == 0
					&& Float.compare(matrix.m23, this.m23) == 0
					&& Float.compare(matrix.m30, this.m30) == 0
					&& Float.compare(matrix.m31, this.m31) == 0
					&& Float.compare(matrix.m32, this.m32) == 0
					&& Float.compare(matrix.m33, this.m33) == 0;
		} else {
			return false;
		}
	}

	public int hashCode() {
		int i = this.m00 != 0.0F ? Float.floatToIntBits(this.m00) : 0;
		i = 31 * i + (this.m01 != 0.0F ? Float.floatToIntBits(this.m01) : 0);
		i = 31 * i + (this.m02 != 0.0F ? Float.floatToIntBits(this.m02) : 0);
		i = 31 * i + (this.m03 != 0.0F ? Float.floatToIntBits(this.m03) : 0);
		i = 31 * i + (this.m10 != 0.0F ? Float.floatToIntBits(this.m10) : 0);
		i = 31 * i + (this.m11 != 0.0F ? Float.floatToIntBits(this.m11) : 0);
		i = 31 * i + (this.m12 != 0.0F ? Float.floatToIntBits(this.m12) : 0);
		i = 31 * i + (this.m13 != 0.0F ? Float.floatToIntBits(this.m13) : 0);
		i = 31 * i + (this.m20 != 0.0F ? Float.floatToIntBits(this.m20) : 0);
		i = 31 * i + (this.m21 != 0.0F ? Float.floatToIntBits(this.m21) : 0);
		i = 31 * i + (this.m22 != 0.0F ? Float.floatToIntBits(this.m22) : 0);
		i = 31 * i + (this.m23 != 0.0F ? Float.floatToIntBits(this.m23) : 0);
		i = 31 * i + (this.m30 != 0.0F ? Float.floatToIntBits(this.m30) : 0);
		i = 31 * i + (this.m31 != 0.0F ? Float.floatToIntBits(this.m31) : 0);
		i = 31 * i + (this.m32 != 0.0F ? Float.floatToIntBits(this.m32) : 0);
		return 31 * i + (this.m33 != 0.0F ? Float.floatToIntBits(this.m33) : 0);
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("Matrix4f:\n");
		stringbuilder.append(this.m00);
		stringbuilder.append(" ");
		stringbuilder.append(this.m01);
		stringbuilder.append(" ");
		stringbuilder.append(this.m02);
		stringbuilder.append(" ");
		stringbuilder.append(this.m03);
		stringbuilder.append("\n");
		stringbuilder.append(this.m10);
		stringbuilder.append(" ");
		stringbuilder.append(this.m11);
		stringbuilder.append(" ");
		stringbuilder.append(this.m12);
		stringbuilder.append(" ");
		stringbuilder.append(this.m13);
		stringbuilder.append("\n");
		stringbuilder.append(this.m20);
		stringbuilder.append(" ");
		stringbuilder.append(this.m21);
		stringbuilder.append(" ");
		stringbuilder.append(this.m22);
		stringbuilder.append(" ");
		stringbuilder.append(this.m23);
		stringbuilder.append("\n");
		stringbuilder.append(this.m30);
		stringbuilder.append(" ");
		stringbuilder.append(this.m31);
		stringbuilder.append(" ");
		stringbuilder.append(this.m32);
		stringbuilder.append(" ");
		stringbuilder.append(this.m33);
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
		unsafe.putFloat(addr + 12, this.m30);
		unsafe.putFloat(addr + 16, this.m01);
		unsafe.putFloat(addr + 20, this.m11);
		unsafe.putFloat(addr + 24, this.m21);
		unsafe.putFloat(addr + 28, this.m31);
		unsafe.putFloat(addr + 32, this.m02);
		unsafe.putFloat(addr + 36, this.m12);
		unsafe.putFloat(addr + 40, this.m22);
		unsafe.putFloat(addr + 44, this.m32);
		unsafe.putFloat(addr + 48, this.m03);
		unsafe.putFloat(addr + 52, this.m13);
		unsafe.putFloat(addr + 56, this.m23);
		unsafe.putFloat(addr + 60, this.m33);
	}

	public void load(FloatBuffer buf) {
		load(MemoryUtil.getAddress(buf));
	}

	public void load(long addr) {
		Unsafe unsafe = UnsafeUtil.UNSAFE;
		this.m00 = unsafe.getFloat(addr);
		this.m10 = unsafe.getFloat(addr + 4);
		this.m20 = unsafe.getFloat(addr + 8);
		this.m30 = unsafe.getFloat(addr + 12);
		this.m01 = unsafe.getFloat(addr + 16);
		this.m11 = unsafe.getFloat(addr + 20);
		this.m21 = unsafe.getFloat(addr + 24);
		this.m31 = unsafe.getFloat(addr + 28);
		this.m02 = unsafe.getFloat(addr + 32);
		this.m12 = unsafe.getFloat(addr + 36);
		this.m22 = unsafe.getFloat(addr + 40);
		this.m32 = unsafe.getFloat(addr + 44);
		this.m03 = unsafe.getFloat(addr + 48);
		this.m13 = unsafe.getFloat(addr + 52);
		this.m23 = unsafe.getFloat(addr + 56);
		this.m33 = unsafe.getFloat(addr + 60);
	}

	public FloatBuffer toFloatBuffer() {
		FloatBuffer buff = ByteBuffer.allocateDirect(16 * 8).order(ByteOrder.nativeOrder()).asFloatBuffer();
		buff.put((float) m00).put((float) m10).put((float) m20).put((float) m30);
		buff.put((float) m01).put((float) m11).put((float) m21).put((float) m31);
		buff.put((float) m02).put((float) m12).put((float) m22).put((float) m32);
		buff.put((float) m03).put((float) m13).put((float) m23).put((float) m33);
		buff.flip();
		return buff;
	}

	public DoubleBuffer toDoubleBuffer() {
		DoubleBuffer buff = ByteBuffer.allocateDirect(16 * 8).order(ByteOrder.nativeOrder()).asDoubleBuffer();
		buff.put(m00).put(m10).put(m20).put(m30);
		buff.put(m01).put(m11).put(m21).put(m31);
		buff.put(m02).put(m12).put(m22).put(m32);
		buff.put(m03).put(m13).put(m23).put(m33);
		buff.flip();
		return buff;
	}

	public void setIdentity() {
		this.m00 = 1.0F;
		this.m01 = 0.0F;
		this.m02 = 0.0F;
		this.m03 = 0.0F;
		this.m10 = 0.0F;
		this.m11 = 1.0F;
		this.m12 = 0.0F;
		this.m13 = 0.0F;
		this.m20 = 0.0F;
		this.m21 = 0.0F;
		this.m22 = 1.0F;
		this.m23 = 0.0F;
		this.m30 = 0.0F;
		this.m31 = 0.0F;
		this.m32 = 0.0F;
		this.m33 = 1.0F;
	}

	public void adjugate() {
		float a00 = this.m00;
		float a01 = this.m01;
		float a02 = this.m02;
		float a03 = this.m03;
		float a10 = this.m10;
		float a11 = this.m11;
		float a12 = this.m12;
		float a13 = this.m13;
		float a20 = this.m20;
		float a21 = this.m21;
		float a22 = this.m22;
		float a23 = this.m23;
		float a30 = this.m30;
		float a31 = this.m31;
		float a32 = this.m32;
		float a33 = this.m33;
		float d001 = a00 * a11 - a01 * a10;
		float d002 = a00 * a12 - a02 * a10;
		float d003 = a00 * a13 - a03 * a10;
		float d012 = a01 * a12 - a02 * a11;
		float d013 = a01 * a13 - a03 * a11;
		float d023 = a02 * a13 - a03 * a12;
		float d101 = a20 * a31 - a21 * a30;
		float d102 = a20 * a32 - a22 * a30;
		float d103 = a20 * a33 - a23 * a30;
		float d112 = a21 * a32 - a22 * a31;
		float d113 = a21 * a33 - a23 * a31;
		float d123 = a22 * a33 - a23 * a32;
		this.m00 =  (a11 * d123 - a12 * d113 + a13 * d112);
		this.m10 = -(a10 * d123 - a12 * d103 + a13 * d102);
		this.m20 =  (a10 * d113 - a11 * d103 + a13 * d101);
		this.m30 = -(a10 * d112 - a11 * d102 + a12 * d101);
		this.m01 = -(a01 * d123 - a02 * d113 + a03 * d112);
		this.m11 =  (a00 * d123 - a02 * d103 + a03 * d102);
		this.m21 = -(a00 * d113 - a01 * d103 + a03 * d101);
		this.m31 =  (a00 * d112 - a01 * d102 + a02 * d101);
		this.m02 =  (a31 * d023 - a32 * d013 + a33 * d012);
		this.m12 = -(a30 * d023 - a32 * d003 + a33 * d002);
		this.m22 =  (a30 * d013 - a31 * d003 + a33 * d001);
		this.m32 = -(a30 * d012 - a31 * d002 + a32 * d001);
		this.m03 = -(a21 * d023 - a22 * d013 + a23 * d012);
		this.m13 =  (a20 * d023 - a22 * d003 + a23 * d002);
		this.m23 = -(a20 * d013 - a21 * d003 + a23 * d001);
		this.m33 =  (a20 * d012 - a21 * d002 + a22 * d001);
	}

	public float determinant() {
		float a00 = this.m00;
		float a01 = this.m01;
		float a02 = this.m02;
		float a03 = this.m03;
		float a10 = this.m10;
		float a11 = this.m11;
		float a12 = this.m12;
		float a13 = this.m13;
		float a20 = this.m20;
		float a21 = this.m21;
		float a22 = this.m22;
		float a23 = this.m23;
		float a30 = this.m30;
		float a31 = this.m31;
		float a32 = this.m32;
		float a33 = this.m33;
		float d001 = a00 * a11 - a01 * a10;
		float d002 = a00 * a12 - a02 * a10;
		float d003 = a00 * a13 - a03 * a10;
		float d012 = a01 * a12 - a02 * a11;
		float d013 = a01 * a13 - a03 * a11;
		float d023 = a02 * a13 - a03 * a12;
		float d101 = a20 * a31 - a21 * a30;
		float d102 = a20 * a32 - a22 * a30;
		float d103 = a20 * a33 - a23 * a30;
		float d112 = a21 * a32 - a22 * a31;
		float d113 = a21 * a33 - a23 * a31;
		float d123 = a22 * a33 - a23 * a32;
		return d001 * d123 - d002 * d113 + d003 * d112 + d012 * d103 - d013 * d102 + d023 * d101;
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
		f = this.m30;
		this.m30 = this.m03;
		this.m03 = f;
		f = this.m31;
		this.m31 = this.m13;
		this.m13 = f;
		f = this.m32;
		this.m32 = this.m23;
		this.m23 = f;
	}

	public boolean invert() {
		float a00 = this.m00;
		float a01 = this.m01;
		float a02 = this.m02;
		float a03 = this.m03;
		float a10 = this.m10;
		float a11 = this.m11;
		float a12 = this.m12;
		float a13 = this.m13;
		float a20 = this.m20;
		float a21 = this.m21;
		float a22 = this.m22;
		float a23 = this.m23;
		float a30 = this.m30;
		float a31 = this.m31;
		float a32 = this.m32;
		float a33 = this.m33;
		float d001 = a00 * a11 - a01 * a10;
		float d002 = a00 * a12 - a02 * a10;
		float d003 = a00 * a13 - a03 * a10;
		float d012 = a01 * a12 - a02 * a11;
		float d013 = a01 * a13 - a03 * a11;
		float d023 = a02 * a13 - a03 * a12;
		float d101 = a20 * a31 - a21 * a30;
		float d102 = a20 * a32 - a22 * a30;
		float d103 = a20 * a33 - a23 * a30;
		float d112 = a21 * a32 - a22 * a31;
		float d113 = a21 * a33 - a23 * a31;
		float d123 = a22 * a33 - a23 * a32;
		float d = d001 * d123 - d002 * d113 + d003 * d112 + d012 * d103 - d013 * d102 + d023 * d101;
		if (d == 0.0F) {
			return false;
		}
		float f = 1.0F / d;
		this.m00 =  (a11 * d123 - a12 * d113 + a13 * d112) * f;
		this.m10 = -(a10 * d123 - a12 * d103 + a13 * d102) * f;
		this.m20 =  (a10 * d113 - a11 * d103 + a13 * d101) * f;
		this.m30 = -(a10 * d112 - a11 * d102 + a12 * d101) * f;
		this.m01 = -(a01 * d123 - a02 * d113 + a03 * d112) * f;
		this.m11 =  (a00 * d123 - a02 * d103 + a03 * d102) * f;
		this.m21 = -(a00 * d113 - a01 * d103 + a03 * d101) * f;
		this.m31 =  (a00 * d112 - a01 * d102 + a02 * d101) * f;
		this.m02 =  (a31 * d023 - a32 * d013 + a33 * d012) * f;
		this.m12 = -(a30 * d023 - a32 * d003 + a33 * d002) * f;
		this.m22 =  (a30 * d013 - a31 * d003 + a33 * d001) * f;
		this.m32 = -(a30 * d012 - a31 * d002 + a32 * d001) * f;
		this.m03 = -(a21 * d023 - a22 * d013 + a23 * d012) * f;
		this.m13 =  (a20 * d023 - a22 * d003 + a23 * d002) * f;
		this.m23 = -(a20 * d013 - a21 * d003 + a23 * d001) * f;
		this.m33 =  (a20 * d012 - a21 * d002 + a22 * d001) * f;
		return true;
	}

	public void multiply(Matrix4f matrix) {
		float a00 = this.m00;
		float a01 = this.m01;
		float a02 = this.m02;
		float a03 = this.m03;
		float a10 = this.m10;
		float a11 = this.m11;
		float a12 = this.m12;
		float a13 = this.m13;
		float a20 = this.m20;
		float a21 = this.m21;
		float a22 = this.m22;
		float a23 = this.m23;
		float a30 = this.m30;
		float a31 = this.m31;
		float a32 = this.m32;
		float a33 = this.m33;
		float b00 = matrix.m00;
		float b01 = matrix.m01;
		float b02 = matrix.m02;
		float b03 = matrix.m03;
		float b10 = matrix.m10;
		float b11 = matrix.m11;
		float b12 = matrix.m12;
		float b13 = matrix.m13;
		float b20 = matrix.m20;
		float b21 = matrix.m21;
		float b22 = matrix.m22;
		float b23 = matrix.m23;
		float b30 = matrix.m30;
		float b31 = matrix.m31;
		float b32 = matrix.m32;
		float b33 = matrix.m33;
		this.m00 = a00 * b00 + a01 * b10 + a02 * b20 + a03 * b30;
		this.m01 = a00 * b01 + a01 * b11 + a02 * b21 + a03 * b31;
		this.m02 = a00 * b02 + a01 * b12 + a02 * b22 + a03 * b32;
		this.m03 = a00 * b03 + a01 * b13 + a02 * b23 + a03 * b33;
		this.m10 = a10 * b00 + a11 * b10 + a12 * b20 + a13 * b30;
		this.m11 = a10 * b01 + a11 * b11 + a12 * b21 + a13 * b31;
		this.m12 = a10 * b02 + a11 * b12 + a12 * b22 + a13 * b32;
		this.m13 = a10 * b03 + a11 * b13 + a12 * b23 + a13 * b33;
		this.m20 = a20 * b00 + a21 * b10 + a22 * b20 + a23 * b30;
		this.m21 = a20 * b01 + a21 * b11 + a22 * b21 + a23 * b31;
		this.m22 = a20 * b02 + a21 * b12 + a22 * b22 + a23 * b32;
		this.m23 = a20 * b03 + a21 * b13 + a22 * b23 + a23 * b33;
		this.m30 = a30 * b00 + a31 * b10 + a32 * b20 + a33 * b30;
		this.m31 = a30 * b01 + a31 * b11 + a32 * b21 + a33 * b31;
		this.m32 = a30 * b02 + a31 * b12 + a32 * b22 + a33 * b32;
		this.m33 = a30 * b03 + a31 * b13 + a32 * b23 + a33 * b33;
	}

	public void multiply(float multiplier) {
		this.m00 *= multiplier;
		this.m01 *= multiplier;
		this.m02 *= multiplier;
		this.m03 *= multiplier;
		this.m10 *= multiplier;
		this.m11 *= multiplier;
		this.m12 *= multiplier;
		this.m13 *= multiplier;
		this.m20 *= multiplier;
		this.m21 *= multiplier;
		this.m22 *= multiplier;
		this.m23 *= multiplier;
		this.m30 *= multiplier;
		this.m31 *= multiplier;
		this.m32 *= multiplier;
		this.m33 *= multiplier;
	}

	public void add(Matrix4f matrix) {
		this.m00 += matrix.m00;
		this.m01 += matrix.m01;
		this.m02 += matrix.m02;
		this.m03 += matrix.m03;
		this.m10 += matrix.m10;
		this.m11 += matrix.m11;
		this.m12 += matrix.m12;
		this.m13 += matrix.m13;
		this.m20 += matrix.m20;
		this.m21 += matrix.m21;
		this.m22 += matrix.m22;
		this.m23 += matrix.m23;
		this.m30 += matrix.m30;
		this.m31 += matrix.m31;
		this.m32 += matrix.m32;
		this.m33 += matrix.m33;
	}

	public void subtract(Matrix4f matrix) {
		this.m00 -= matrix.m00;
		this.m01 -= matrix.m01;
		this.m02 -= matrix.m02;
		this.m03 -= matrix.m03;
		this.m10 -= matrix.m10;
		this.m11 -= matrix.m11;
		this.m12 -= matrix.m12;
		this.m13 -= matrix.m13;
		this.m20 -= matrix.m20;
		this.m21 -= matrix.m21;
		this.m22 -= matrix.m22;
		this.m23 -= matrix.m23;
		this.m30 -= matrix.m30;
		this.m31 -= matrix.m31;
		this.m32 -= matrix.m32;
		this.m33 -= matrix.m33;
	}

	public void negate() {
		this.m00 = -this.m00;
		this.m01 = -this.m01;
		this.m02 = -this.m02;
		this.m03 = -this.m03;
		this.m10 = -this.m10;
		this.m11 = -this.m11;
		this.m12 = -this.m12;
		this.m13 = -this.m13;
		this.m20 = -this.m20;
		this.m21 = -this.m21;
		this.m22 = -this.m22;
		this.m23 = -this.m23;
		this.m30 = -this.m30;
		this.m31 = -this.m31;
		this.m32 = -this.m32;
		this.m33 = -this.m33;
	}

	public static Matrix4f perspective(double fov, float aspectRatio, float nearPlane, float farPlane) {
		float f = (float) (1.0D / Math.tan(Math.toRadians(fov) * 0.5D));
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = f / aspectRatio;
		matrix.m11 = f;
		matrix.m22 = (farPlane + nearPlane) / (nearPlane - farPlane);
		matrix.m32 = -1.0F;
		matrix.m23 = 2.0F * farPlane * nearPlane / (nearPlane - farPlane);
		return matrix;
	}

	public static Matrix4f orthographic(float width, float height, float nearPlane, float farPlane) {
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = 2.0F / width;
		matrix.m11 = 2.0F / height;
		float f = farPlane - nearPlane;
		matrix.m22 = -2.0F / f;
		matrix.m33 = 1.0F;
		matrix.m03 = -1.0F;
		matrix.m13 = -1.0F;
		matrix.m23 = -(farPlane + nearPlane) / f;
		return matrix;
	}

	public Matrix4f copy() {
		return new Matrix4f(this);
	}

	public static Matrix4f createIdentityMatrix() {
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = 1.0F;
		matrix.m11 = 1.0F;
		matrix.m22 = 1.0F;
		matrix.m33 = 1.0F;
		return matrix;
	}

	public static Matrix4f createScaleMatrix(float x, float y, float z) {
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = x;
		matrix.m11 = y;
		matrix.m22 = z;
		matrix.m33 = 1.0F;
		return matrix;
	}

	public static Matrix4f createTranslateMatrix(float x, float y, float z) {
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = 1.0F;
		matrix.m11 = 1.0F;
		matrix.m22 = 1.0F;
		matrix.m33 = 1.0F;
		matrix.m03 = x;
		matrix.m13 = y;
		matrix.m23 = z;
		return matrix;
	}

	public static Matrix4f createRotateMatrix(Quaternion quaternion) {
		Matrix4f matrix = new Matrix4f();
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
		matrix.m33 = 1.0F;
		matrix.m10 = 2.0F * (xy + zw);
		matrix.m01 = 2.0F * (xy - zw);
		matrix.m20 = 2.0F * (zx - yw);
		matrix.m02 = 2.0F * (zx + yw);
		matrix.m21 = 2.0F * (yz + xw);
		matrix.m12 = 2.0F * (yz - xw);
		return matrix;
	}

	public static Matrix4f createRotateXMatrix(Quaternion quaternion) {
		Matrix4f matrix = new Matrix4f();
		float xx = 2.0F * quaternion.x * quaternion.x;
		float xw = quaternion.x * quaternion.w;

		matrix.m00 = 1.0F;
		matrix.m11 = 1.0F - xx;
		matrix.m22 = 1.0F - xx;
		matrix.m33 = 1.0F;
		matrix.m21 = 2.0F * xw;
		matrix.m12 = 2.0F * -xw;
		return matrix;
	}

	public static Matrix4f createRotateYMatrix(Quaternion quaternion) {
		Matrix4f matrix = new Matrix4f();
		float yy = 2.0F * quaternion.y * quaternion.y;
		float yw = quaternion.y * quaternion.w;

		matrix.m00 = 1.0F - yy;
		matrix.m11 = 1.0F;
		matrix.m22 = 1.0F - yy;
		matrix.m33 = 1.0F;
		matrix.m20 = 2.0F * -yw;
		matrix.m02 = 2.0F * yw;
		return matrix;
	}

	public static Matrix4f createRotateZMatrix(Quaternion quaternion) {
		Matrix4f matrix = new Matrix4f();
		float zz = 2.0F * quaternion.z * quaternion.z;
		float zw = quaternion.z * quaternion.w;

		matrix.m00 = 1.0F - zz;
		matrix.m11 = 1.0F - zz;
		matrix.m22 = 1.0F;
		matrix.m33 = 1.0F;
		matrix.m10 = 2.0F * zw;
		matrix.m01 = 2.0F * -zw;
		return matrix;
	}

	public void translate(float x, float y, float z) {
		this.m03 += this.m00 * x + this.m01 * y + this.m02 * z;
		this.m13 += this.m10 * x + this.m11 * y + this.m12 * z;
		this.m23 += this.m20 * x + this.m21 * y + this.m22 * z;
		this.m33 += this.m30 * x + this.m31 * y + this.m32 * z;
	}

	public void scale(float x, float y, float z) {
		this.m00 *= x;
		this.m10 *= x;
		this.m20 *= x;
		this.m30 *= x;
		this.m01 *= y;
		this.m11 *= y;
		this.m21 *= y;
		this.m31 *= y;
		this.m02 *= z;
		this.m12 *= z;
		this.m22 *= z;
		this.m32 *= z;
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
		float f30 = this.m30;
		float f31 = this.m31;
		float f32 = this.m32;

		this.m00 = f00 * r00 + f01 * r10 + f02 * r20;
		this.m01 = f00 * r01 + f01 * r11 + f02 * r21;
		this.m02 = f00 * r02 + f01 * r12 + f02 * r22;
		this.m10 = f10 * r00 + f11 * r10 + f12 * r20;
		this.m11 = f10 * r01 + f11 * r11 + f12 * r21;
		this.m12 = f10 * r02 + f11 * r12 + f12 * r22;
		this.m20 = f20 * r00 + f21 * r10 + f22 * r20;
		this.m21 = f20 * r01 + f21 * r11 + f22 * r21;
		this.m22 = f20 * r02 + f21 * r12 + f22 * r22;
		this.m30 = f30 * r00 + f31 * r10 + f32 * r20;
		this.m31 = f30 * r01 + f31 * r11 + f32 * r21;
		this.m32 = f30 * r02 + f31 * r12 + f32 * r22;
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
		float f31 = this.m31;
		float f32 = this.m32;

		this.m01 = f01 * r11 + f02 * r21;
		this.m02 = f01 * r12 + f02 * r22;
		this.m11 = f11 * r11 + f12 * r21;
		this.m12 = f11 * r12 + f12 * r22;
		this.m21 = f21 * r11 + f22 * r21;
		this.m22 = f21 * r12 + f22 * r22;
		this.m31 = f31 * r11 + f32 * r21;
		this.m32 = f31 * r12 + f32 * r22;
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
		float f30 = this.m30;
		float f32 = this.m32;

		this.m00 = f00 * r00 + f02 * r20;
		this.m02 = f00 * r02 + f02 * r22;
		this.m10 = f10 * r00 + f12 * r20;
		this.m12 = f10 * r02 + f12 * r22;
		this.m20 = f20 * r00 + f22 * r20;
		this.m22 = f20 * r02 + f22 * r22;
		this.m30 = f30 * r00 + f32 * r20;
		this.m32 = f30 * r02 + f32 * r22;
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
		float f30 = this.m30;
		float f31 = this.m31;

		this.m00 = f00 * r00 + f01 * r10;
		this.m01 = f00 * r01 + f01 * r11;
		this.m10 = f10 * r00 + f11 * r10;
		this.m11 = f10 * r01 + f11 * r11;
		this.m20 = f20 * r00 + f21 * r10;
		this.m21 = f20 * r01 + f21 * r11;
		this.m30 = f30 * r00 + f31 * r10;
		this.m31 = f30 * r01 + f31 * r11;
	}

}
