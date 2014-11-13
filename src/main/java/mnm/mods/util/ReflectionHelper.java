package mnm.mods.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionHelper {

    public static Object getFieldValue(Class<?> cl, Object obj, String fieldName)
            throws IllegalAccessException, NoSuchFieldException {
        Field fd = cl.getField(fieldName);
        fd.setAccessible(true);
        return fd.get(obj);
    }

    public static Object getFieldValue(Class<?> cl, Object obj, String[] fieldNames)
            throws NoSuchFieldException, IllegalAccessException {
        NoSuchFieldException latest = null;
        for (String fieldName : fieldNames) {
            try {
                return getFieldValue(cl, obj, fieldName);
            } catch (NoSuchFieldException e) {
                latest = e;
            }
        }
        throw latest;

    }

    public static Object invokeMethod(Class<?> cl, Object obj, String methodName, Object[] args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?>[] parameterTypes = getParameterTypes(args);
        Method md = cl.getMethod(methodName, parameterTypes);
        md.setAccessible(true);
        return md.invoke(obj, args);
    }

    public static Object invokeMethod(Class<?> cl, Object obj, String[] methodNames, Object[] args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        NoSuchMethodException latest = null;
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

    private ReflectionHelper() {
    }
}
