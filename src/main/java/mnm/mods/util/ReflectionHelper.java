package mnm.mods.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class to help with reflection.
 */
public final class ReflectionHelper {

    /**
     * Gets the value of a field using a class and object.
     *
     * @param cl The class
     * @param obj The object
     * @param fieldName The field name
     * @return The value
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static Object getFieldValue(Class<?> cl, Object obj, String fieldName)
            throws IllegalAccessException, NoSuchFieldException {
        Field fd = cl.getDeclaredField(fieldName);
        fd.setAccessible(true);
        return fd.get(obj);
    }

    /**
     * Gets the value of a field using a class, object, and possible field
     * names. Useful when dealing with obfuscated classes.
     *
     * @param cl The class
     * @param obj The object
     * @param fieldNames Possible field names
     * @return The value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Class<?> cl, Object obj, String[] fieldNames)
            throws NoSuchFieldException, IllegalAccessException {
        NoSuchFieldException latest = new NoSuchFieldException("This shouldn't happen");
        for (String fieldName : fieldNames) {
            try {
                return getFieldValue(cl, obj, fieldName);
            } catch (NoSuchFieldException e) {
                latest = e;
            }
        }
        throw latest;

    }

    /**
     * Sets the value of a field using a class and object.
     *
     * @param cl The class
     * @param obj The object or null if static
     * @param value The value to set
     * @param fieldName The name of the field
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Class<?> cl, Object obj, Object value, String fieldName)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field fd = cl.getDeclaredField(fieldName);
        fd.setAccessible(true);
        fd.set(obj, value);
    }

    /**
     * Sets the value of a field using a class, object, and possible field
     * names.
     *
     * @param cl The class
     * @param obj The object or null if static
     * @param value The new value
     * @param fieldNames Possible field names
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Class<?> cl, Object obj, Object value, String[] fieldNames)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        NoSuchFieldException latest = new NoSuchFieldException("This shouldn't happen");
        for (String fieldName : fieldNames) {
            try {
                setFieldValue(cl, obj, value, fieldName);
                return;
            } catch (NoSuchFieldException e) {
                latest = e;
            }
        }
        throw latest;

    }

    /**
     * Invokes a method using a class, object, and arguments.
     *
     * @param cl The class
     * @param obj The object
     * @param methodName The name
     * @param args The arguments
     * @return The return value
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Class<?> cl, Object obj, String methodName, Object[] args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?>[] parameterTypes = getParameterTypes(args);
        Method md = cl.getDeclaredMethod(methodName, parameterTypes);
        md.setAccessible(true);
        return md.invoke(obj, args);
    }

    /**
     * Invokes a method using a class, object, arguments, and possible method
     * names. Useful when dealing with obfuscated classes.
     *
     * @param cl The class
     * @param obj The object
     * @param methodNames Possible method names
     * @param args The arguments
     * @return The return value
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Class<?> cl, Object obj, String[] methodNames, Object[] args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        NoSuchMethodException latest = new NoSuchMethodException("This shouldn't happen");
        for (String methodName : methodNames) {
            try {
                return invokeMethod(cl, obj, methodName, args);
            } catch (NoSuchMethodException e) {
                latest = e;
            }
        }
        throw latest;
    }

    private static Class<?>[] getParameterTypes(Object... args) {
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        return types;
    }

    private ReflectionHelper() {}

}
