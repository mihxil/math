package org.meeuw.test.math;

import org.junit.jupiter.api.Test;

import org.meeuw.math.DigitUtils;
import org.meeuw.math.DigitUtils.AdicDigits;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.DigitUtils.fromInverseDigitsInBase;
import static org.meeuw.math.DigitUtils.multiplyAdicDigits;

public class DigitUtilsTest {


    @Test
    public void sumInverseDigits() {
        byte[] a = new byte[] {6, 7, 8}; // 876
        byte[] b = new byte[] {5, 4, 7, 3}; // 3745

        byte[] sum = DigitUtils.sumInverseDigits((byte) 10, a, b);
        assertThat(fromInverseDigitsInBase((byte)  10, sum)).isEqualTo(3745 + 876);
    }

    @Test
    public void sumInverseDigits2() {
        byte[] a1 = new byte[] {9, 9, 9, 9};
        byte[] sum = DigitUtils.sumInverseDigits((byte) 10, a1, a1, a1, a1, a1, a1, a1, a1, a1, a1, a1, a1, a1, a1, a1, a1);
        assertThat(fromInverseDigitsInBase((byte)  10, sum)).isEqualTo(16 * 9999L);
    }

    @Test
    public void multiplyInverseDigitsWithDigit() {
        byte[] a1 = new byte[] {9, 9, 9, 9};
        byte[] sum = DigitUtils.multiplyInverseDigits((byte) 10, (byte) 9, a1);
        assertThat(fromInverseDigitsInBase((byte)  10, sum)).isEqualTo(9 * 9999L);
    }


    @Test
    public void multiplyAdicDigitsWithDigit() {
        AdicDigits a1 = new AdicDigits(new byte[] {9}, new byte[] {8, 8});
        AdicDigits sum = multiplyAdicDigits((byte) 10, (byte)  9, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("9̅2₁₀");
    }

    @Test
    public void sumAdicDigits() {
        AdicDigits a1 = new AdicDigits(new byte[] {1}, new byte[] {});
        AdicDigits sum = DigitUtils.sumAdicDigits((byte) 10, a1, a1, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...3 ₁₀");
    }

    @Test
    public void sumAdicDigits2() {
        AdicDigits a1 = new AdicDigits(new byte[] {1, 3}, new byte[] {});
        AdicDigits sum = DigitUtils.sumAdicDigits((byte) 10, a1, a1, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...93 ₁₀");
    }


    @Test
    public void sumAdicDigitsWithOverflow() {
        AdicDigits a1 = new AdicDigits(new byte[] {1, 4}, new byte[] {});
        AdicDigits sum = DigitUtils.sumAdicDigits((byte) 10, a1, a1, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...42 3₁₀");
    }


     @Test
    public void sumAdicDigitsWithOverflowAndDigits() {
        AdicDigits a1 = new AdicDigits(new byte[] {1, 4}, new byte[] {1, 2, 3, 4});
        AdicDigits sum = DigitUtils.sumAdicDigits((byte) 10, a1, a1, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...24 2963₁₀");
    }


    @Test
    public void multiplyInverseDigits() {
        byte[] a1 = new byte[] {9, 9, 9, 9};
        byte[] sum = DigitUtils.multiplyInverseDigits((byte) 10, a1, a1);
        assertThat(fromInverseDigitsInBase((byte)  10, sum)).isEqualTo(9999L * 9999L);

    }





}
