package org.meeuw.physics;

import org.meeuw.math.UncertainNumbers;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class PhysicalNumbers extends UncertainNumbers<PhysicalNumber> {

	public PhysicalNumbers(PhysicalNumber zero, PhysicalNumber one) {
		super(zero, one);
	}
}
