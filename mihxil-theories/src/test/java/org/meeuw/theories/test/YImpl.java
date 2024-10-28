package org.meeuw.theories.test;

import java.util.List;

public class YImpl implements Y<String> {
    @Override
    public List<String> list() {
        return List.of("a", "b");
    }
}
