import static org.meeuw.math.abstractalgebra.quaternions.Quaternions.H_Q;

void main() {
    // Quaternions over rational numbers
    var a = H_Q.fromString("1 + 2i + 3j + 4k");
    var b = H_Q.fromStrings("1/2",  "-2",  "3",  "4");
    IO.println(a.times(b)); // prints -⁴¹⁄₂ - i - ²³⁄₂j + 18k
}
