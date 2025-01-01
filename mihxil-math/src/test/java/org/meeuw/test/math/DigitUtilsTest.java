package org.meeuw.test.math;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.meeuw.math.AdicDigits;
import org.meeuw.math.DigitUtils;

import static java.lang.Byte.toUnsignedInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.DigitUtils.*;

@Log4j2
public class DigitUtilsTest {

    @Test
    public void shift() {
        AdicDigits a = AdicDigits.of("1234", "3457");
        assertThat(a.leftShift(3).toString()).isEqualTo("...1234 3457000");
        assertThat(a.rightShift(3).toString()).isEqualTo("...1234 3");
        assertThat(a.rightShift(4).toString()).isEqualTo("...1234 ");
        assertThat(a.rightShift(5).toString()).isEqualTo("...4123 ");
        assertThat(a.rightShift(6).toString()).isEqualTo("...3412 ");
        assertThat(a.rightShift(7).toString()).isEqualTo("...2341 ");
        assertThat(a.rightShift(8).toString()).isEqualTo("...1234 ");
        assertThat(a.rightShift(9).toString()).isEqualTo("...4123 ");

    }
    @Test
    public void shift2() {
        assertThat(AdicDigits.of("2", "").leftShift(1)
            .toString()).isEqualTo("...2 0");
    }

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
        AdicDigits product = multiplyAdicDigits(10, (byte)  9, a1);
        assertThat(DigitUtils.adicToString( 10, product)).isEqualTo("...9 01₁₀");
    }

    @Test
    public void multiplyAdicDigitsWithDigit2() {
        AdicDigits a1 = AdicDigits.of("5", "4");
        AdicDigits product = multiplyAdicDigits(10, (byte)  6, a1);
        assertThat(DigitUtils.adicToString( 10, product)).isEqualTo("...3 24₁₀");
    }

    @Test
    public void multiplyAdicDigitsWithDigit3() {
        AdicDigits a1 = AdicDigits.of("5", "4");
        AdicDigits product = multiplyAdicDigits(10, (byte)  7, a1);
        assertThat(DigitUtils.adicToString(10, product)).isEqualTo("...8 78₁₀");
    }


    @Test
    public void multiplyAdicDigitsWithDigit4() {
        AdicDigits a1 = AdicDigits.of("6", "");
        AdicDigits product = multiplyAdicDigits(10, (byte)  4, a1);
        assertThat(DigitUtils.adicToString(10, product)).isEqualTo("...6 4₁₀");
    }

    @Test
    public void multiplyAdicDigitsWithDigit5() {
        AdicDigits a1 = AdicDigits.of("2", "0");
        AdicDigits product = multiplyAdicDigits(10, (byte) 1, a1);
        assertThat(product.toString()).isEqualTo("...2 0");
    }

    @Test
    public void multiplyAdicDigitsWithDigitNonRepetitive() {
        AdicDigits a1 = AdicDigits.of("0", "5678");
        AdicDigits sum = multiplyAdicDigits((byte) 10, (byte)  5, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...0 28390₁₀");
    }

    @Test
    public void multiplyAdicDigitsWithDigitNonRepetitive2() {
        AdicDigits a1 = AdicDigits.of("0", "56780000");
        AdicDigits sum = multiplyAdicDigits(10, (byte)  1, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...0 56780000₁₀");
    }

    @Test
    public void sumAdicDigits() {
        AdicDigits a1 = AdicDigits.create(new byte[] {1}, new byte[] {});
        AdicDigits sum = DigitUtils.sumAdicDigits((byte) 10, a1, a1, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...3 ₁₀");
    }

    @Test
    public void sumAdicDigits2() {
        AdicDigits a1 = AdicDigits.create(new byte[] {1, 3}, new byte[] {});
        AdicDigits sum = DigitUtils.sumAdicDigits((byte) 10, a1, a1, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...93 ₁₀");
    }

      @Test
    public void sumAdicDigits3() {
        AdicDigits a1 = AdicDigits.of("0", "13314910");
        AdicDigits a2 = AdicDigits.of("0", "56780000");
        AdicDigits sum = DigitUtils.sumAdicDigits( 10, a1, a1, a1);
        assertThat(DigitUtils.adicToString(10, sum)).isEqualTo("...0 39944730₁₀");
    }


    @Test
    public void sumAdicDigitsWithOverflow() {
        AdicDigits a1 = AdicDigits.create(new byte[] {1, 4}, new byte[] {});
        AdicDigits sum = DigitUtils.sumAdicDigits((byte) 10, a1, a1, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...42 3₁₀");
    }


    @Test
    public void sumAdicDigitsWithOverflowAndDigits() {
        AdicDigits a1 = AdicDigits.create(new byte[] {1, 4}, new byte[] {1, 2, 3, 4});
        AdicDigits sum = DigitUtils.sumAdicDigits((byte) 10, a1, a1, a1);
        assertThat(DigitUtils.adicToString((byte) 10, sum)).isEqualTo("...42 963₁₀");
    }

    @Test
    public void sumAdicDigitsWithOverflowAndDigits2() {
        AdicDigits a1 = AdicDigits.ofRepetitive(      1, 2,               3).withDigits(1);
        AdicDigits a2 = AdicDigits.ofRepetitive(7, 1).withDigits(        3, 4);

        AdicDigits sum = DigitUtils.sumAdicDigits(10, a1, a2);
        assertThat(DigitUtils.adicToString(10, sum)).isEqualTo("...402948 365₁₀");
    }

    @Test
    public void sumAdicDigitsWithOverflowAndDigits3() {
        AdicDigits a1 = AdicDigits.ofRepetitive(      6, 7).withDigits(8, 9);
        AdicDigits a2 = AdicDigits.ofRepetitive(5).withDigits(        1, 6, 6, 6);

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
    public void multiplyRepetitiveNadics() {
        AdicDigits a1 = AdicDigits.of("1", "");
        AdicDigits a2 = AdicDigits.of("2", "");
        AdicDigits result  = multiplyAdicDigits(10, a1, a2);
        log.info("{} x {} = {}", a1, a2, result);
        assertThat(result.toString()).isEqualTo("...197530864 2");
    }

    @Test
    public void multiplyRepetitiveNadics2() {
        AdicDigits a1 = AdicDigits.of("5", "4");
        AdicDigits a2 = AdicDigits.of("6", "");
        AdicDigits result  = multiplyAdicDigits( 10, a1, a2);
        log.info("{} x {} = {}", a1, a2, result);
        assertThat(result.toString()).isEqualTo("...296 4");
    }

    @Test
    public void multiplyNonRepetitiveNadics() {
        AdicDigits a1 = AdicDigits.of("0", "12345");
        AdicDigits a2 = AdicDigits.of("0", "5678");
        AdicDigits result  = multiplyAdicDigits(10, a1, a2);
        log.info("{} x {} = {}", a1, a2, result);
        assertThat(result.toString()).isEqualTo("...0 70094910");
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
