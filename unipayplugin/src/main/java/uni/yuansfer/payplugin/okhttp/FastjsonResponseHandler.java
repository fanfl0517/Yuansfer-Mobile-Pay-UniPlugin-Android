package uni.yuansfer.payplugin.okhttp;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class FastjsonResponseHandler<T> implements IResponseHandler {

    private Class mParamClass;

    public FastjsonResponseHandler() {
        mParamClass = getSuperClassGenricType(getClass(), 0);
    }

    public final Class getParamClass() {
        return mParamClass;
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型
     *
     * @param clazz
     * @param index
     */
    private Class getSuperClassGenricType(Class clazz, int index)
            throws IndexOutOfBoundsException {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    public abstract void onSuccess(int statusCode, T response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }

}