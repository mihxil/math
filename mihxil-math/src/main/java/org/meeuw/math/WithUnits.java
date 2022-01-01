package org.meeuw.math;


/**
 * Simple interface to mark the 'units' for a certain object. The units in this case are just a simple string.
 *
 * The 'physics' module models units better, but this also may add unnecessary complexity in some cases.
 * @since 0.7
 */
public interface WithUnits {

    String getUnitsAsString();
}
