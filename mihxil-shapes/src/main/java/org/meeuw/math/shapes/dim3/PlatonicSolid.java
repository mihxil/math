package org.meeuw.math.shapes.dim3;

public enum PlatonicSolid {
    TETRAHEDRON(3, 3),
    CUBE(4, 3),
    OCTAHEDRON(3, 4),
    DODECAHEDRON(5, 3),
    ICOSAHEDRON(3, 5);

    private final int faces;
    private final int vertices;
    private final int edges;

    private final int p;
    private final int q;


    PlatonicSolid(int p, int q) {
        this.faces = 4 * q / (4 - (p - 2) * (q - 2));
        this.vertices = 4 * p / (4 - (p - 2) * (q - 2));
        this.edges = 2 * p * q / (4 - (p - 2) * (q - 2));
        this.p = p;
        this.q = q;
    }

    public int faces() {
        return faces;
    }

    public int vertices() {
        return vertices;
    }

    public int edges() {
        return edges;
    }

    /**
     * Number of edges per face
     */
    public int p() {
        return p;
    }
    /**
     * Number of faces meeting at each vertex
     */
    public int q() {
        return q;
    }

    public String toString() {
        return String.format("{%d,%d}", p, q);
    }
}
