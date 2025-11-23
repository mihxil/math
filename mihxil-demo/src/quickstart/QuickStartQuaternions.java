import static org.meeuw.math.abstractalgebra.quaternions.Quaternions.H_Q;

void main() {
    // Quaternions over rational numbers
    var a = H_Q.fromString("1 + 2i + 3j + 4k");
    var b = H_Q.fromString("2 - 2i + 3j - 4k");
    IO.println(a.times(b)); // prints 13 - 22i + 9j + 16k
}
