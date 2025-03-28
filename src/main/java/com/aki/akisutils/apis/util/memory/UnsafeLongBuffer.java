package com.aki.akisutils.apis.util.memory;

import java.nio.LongBuffer;

public class UnsafeLongBuffer extends UnsafeNIOBuffer<LongBuffer> {

	public UnsafeLongBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.LONG.toByte(capacity));
	}

	public long getLongCapacity() {
		return PrimitiveInfo.LONG.fromByte(getCapacity());
	}

	@Override
	protected LongBuffer createBuffer() {
		return NIOBufferUtil.asLongBuffer(getAddress(), getLongCapacity());
	}

}
