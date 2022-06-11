package library.engine;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import library.annotations.FindChild;
import library.annotations.Find;
import library.annotations.PageObject;

import java.lang.reflect.Field;

public class LocatorFactory {

    Page page;

    public LocatorFactory(Page page) {
        this.page = page;
    }

    public Locator createLocator(Field field, Object pageObjectInstance) {

        Class<?> clazz = field.getDeclaringClass();
        PageObject pageObjectAnnotation = clazz.getAnnotation(PageObject.class);
        Find findAnnotation = field.getAnnotation(Find.class);
        FindChild findChildAnnotation = field.getAnnotation(FindChild.class);

        if (pageObjectAnnotation.frame().length == 0) {
            if (findChildAnnotation == null) {
                return page.locator(findAnnotation.value());
            }

            Locator locator;
            try {
                Field depField = clazz.getField(findChildAnnotation.value());
                locator = (Locator) depField.get(pageObjectInstance);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e.getCause());
            }
            return locator.locator(findAnnotation.value());
        }

        FrameLocator frameLocator = page.frameLocator(pageObjectAnnotation.frame()[0]);

        if (pageObjectAnnotation.frame().length > 1) {
            for (int i = 1; i < pageObjectAnnotation.frame().length; i++) {
                frameLocator = frameLocator.frameLocator(pageObjectAnnotation.frame()[i]);
            }
        }

        return frameLocator.locator(findAnnotation.value());
    }
}
