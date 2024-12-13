package org.meeuw.test.math;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.CsvSource;

import org.meeuw.math.DigitUtils;
import org.meeuw.math.DigitUtils.AdicDigits;

import static java.lang.Byte.toUnsignedInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.DigitUtils.fromInverseDigitsInBase;
import static org.meeuw.math.DigitUtils.multiplyAdicDigits;

@Log4j2
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
        AdicDigits a1 = AdicDigits.of("9", "89");
        AdicDigits sum = multiplyAdicDigits((byte) 10, (byte)  9, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...9 01₁₀");
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
    public void sumAdicDigitsWithOverflowAndDigits2() {
        AdicDigits a1 = AdicDigits.ofRepetitive(      1, 2,               3).digits(1);
        AdicDigits a2 = AdicDigits.ofRepetitive(7, 1).digits(        3, 4);

        AdicDigits sum = DigitUtils.sumAdicDigits(10, a1, a2);
        assertThat(DigitUtils.adicToString(10, sum)).isEqualTo("...402948 365₁₀");
    }

    @Test
    public void sumAdicDigitsWithOverflowAndDigits3() {
        AdicDigits a1 = AdicDigits.ofRepetitive(      6, 7).digits(8, 9);
        AdicDigits a2 = AdicDigits.ofRepetitive(5).digits(        1, 6, 6, 6);

        AdicDigits sum = DigitUtils.sumAdicDigits(10, a1, a2);
        assertThat(DigitUtils.adicToString(10, sum)).isEqualTo("...32 28455₁₀");
    }


    @Test
    public void multiplyInverseDigits() {
        byte[] a1 = new byte[] {9, 9, 9, 9};
        byte[] sum = DigitUtils.multiplyInverseDigits((byte) 10, a1, a1);
        assertThat(fromInverseDigitsInBase((byte)  10, sum)).isEqualTo(9999L * 9999L);

    }

    @Test
    public void multiplyRepetitivePadics() {
        AdicDigits a1 = AdicDigits.of("1", "");
        AdicDigits a2 = AdicDigits.of("2", "");
        AdicDigits result  = DigitUtils.multiplyPAdicDigits((byte) 10, a1, a2);
        log.info("{} x {} = {}", a1, a2, result);
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
        assertThat(DigitUtils.stringToDigits(s)).containsExactly((byte) digit);

    }



}
