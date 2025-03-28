package com.aki.akisutils.apis.util.memory;

import java.nio.IntBuffer;

public class UnsafeIntBuffer extends UnsafeNIOBuffer<IntBuffer> {

	public UnsafeIntBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.INT.toByte(capacity));
	}

	public long getIntCapacity() {
		return PrimitiveInfo.INT.fromByte(getCapacity());
	}

	@Override
	protected IntBuffer createBuffer() {
		return NIOBufferUtil.asIntBuffer(getAddress(), getIntCapacity());
	}

}
