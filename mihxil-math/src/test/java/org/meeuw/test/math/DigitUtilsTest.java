package org.meeuw.test.math;

import lombok.extern.java.Log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.meeuw.math.DigitUtils;

import static java.lang.Byte.toUnsignedInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.DigitUtils.fromInverseDigitsInBase;

@Log
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
    public void multiplyInverseDigits() {
        byte[] a1 = new byte[] {9, 9, 9, 9};
        byte[] sum = DigitUtils.multiplyInverseDigits((byte) 10, a1, a1);
        assertThat(fromInverseDigitsInBase((byte)  10, sum)).isEqualTo(9999L * 9999L);

    }


    @Test
    public void basic() {
        byte a = (byte) 200;
        byte b = (byte) 201;

        int c = toUnsignedInt(a) * toUnsignedInt(b);
        assertThat(c).isEqualTo(200 * 201);
    }

    @ParameterizedTest
    @CsvSource({
        "1,1",
        "10,a",
        "15,f",
        "20,k",
        "200,ğ"
    })
    public void digitToString(int digit, String s) {
        assertThat(DigitUtils.digitToString((byte) digit)).isEqualTo(s);
        assertThat(DigitUtils.stringToInverseDigits(s)).containsExactly((byte) digit);

    }



}
