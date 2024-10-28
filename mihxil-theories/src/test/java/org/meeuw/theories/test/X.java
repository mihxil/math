package org.meeuw.theories.test;

import java.util.List;

public interface X<E> {

    List<E> list();

    default E first() {
        return list().get(0);
    }
}
