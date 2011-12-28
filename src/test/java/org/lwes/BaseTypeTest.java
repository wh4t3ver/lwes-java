package org.lwes;

import org.junit.Test;

public class BaseTypeTest {
    @SuppressWarnings("unused")
    @Test(expected = IllegalStateException.class)
    public void inconsistentConstructor() {
        new BaseType(FieldType.INT16.name, FieldType.UINT16.token);
    }
}
