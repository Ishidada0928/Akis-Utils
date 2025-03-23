package com.aki.akisutils.apis.util.matrixutil;

public class MatrixStack {

	private Entry last = new Entry(Matrix4f.createIdentityMatrix(), Matrix3f.createIdentityMatrix());

	public void push() {
		this.last = new Entry(this.last);
	}

	public void pop() {
		if (this.last.parent == null) {
			throw new IllegalStateException();
		}
		this.last = this.last.parent;
	}

	public Entry last() {
		return this.last;
	}

	public Matrix4f modelMatrix() {
		return this.last.modelMatrix();
	}

	public Matrix3f normalMatrix() {
		return this.last.normalMatrix();
	}

	public void translate(float x, float y, float z) {
		this.last.translate(x, y, z);
	}

	public void scale(float x, float y, float z) {
		this.last.scale(x, y, z);
	}

	public void rotate(Quaternion quaternion) {
		this.last.rotate(quaternion);
	}

	public void rotateX(Quaternion quaternion) {
		this.last.rotateX(quaternion);
	}

	public void rotateY(Quaternion quaternion) {
		this.last.rotateY(quaternion);
	}

	public void rotateZ(Quaternion quaternion) {
		this.last.rotateZ(quaternion);
	}

	public static class Entry {

		public final Matrix4f modelMatrix;
		public final Matrix3f normalMatrix;
		private Entry parent;

		public Entry(Matrix4f positionMatrix, Matrix3f normalMatrix) {
			this.modelMatrix = positionMatrix;
			this.normalMatrix = normalMatrix;
		}

		public Entry(Entry parent) {
			this(parent.modelMatrix.copy(), parent.normalMatrix.copy());
			this.parent = parent;
		}

		public Matrix4f modelMatrix() {
			return this.modelMatrix;
		}

		public Matrix3f normalMatrix() {
			return this.normalMatrix;
		}

		public void translate(float x, float y, float z) {
			this.modelMatrix.translate(x, y, z);
		}

		public void scale(float x, float y, float z) {
			this.modelMatrix.scale(x, y, z);
			if (x == y && y == z) {
				if (x < 0.0F) {
					this.normalMatrix.negate();
				}
			} else {
				float f = Math.min(Math.min(Math.abs(x), Math.abs(y)), Math.abs(z));
				this.normalMatrix.scale(f / x, f / y, f / z);
			}
		}

		public void rotate(Quaternion quaternion) {
			this.modelMatrix.rotate(quaternion);
			this.normalMatrix.rotate(quaternion);
		}

		public void rotateX(Quaternion quaternion) {
			this.modelMatrix.rotateX(quaternion);
			this.normalMatrix.rotateX(quaternion);
		}

		public void rotateY(Quaternion quaternion) {
			this.modelMatrix.rotateY(quaternion);
			this.normalMatrix.rotateY(quaternion);
		}

		public void rotateZ(Quaternion quaternion) {
			this.modelMatrix.rotateZ(quaternion);
			this.normalMatrix.rotateZ(quaternion);
		}

	}

}
