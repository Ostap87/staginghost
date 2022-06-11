package library.engine;

import java.lang.reflect.Field;

public interface FieldDecorator {

    Object decorate(Field field, Object pageObjectInstance);
}
