package org.meeuw.physics;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public interface Dimension {

    @Min(0) @Max(7) int ordinal();
}
