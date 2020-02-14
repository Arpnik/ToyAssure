package util;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ConvertGeneric{

    public static List<String> convert(Method[] gettersAndSetters)
    {
        List<String> res=new ArrayList<>();
        for(Method method:gettersAndSetters)
        {
            res.add(method.getName());
        }
        return res;
    }
    public static <T,From> T convert(From from,Class<T> To ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        T to=  To.newInstance();
        Method[] gettersAndSettersFrom = from.getClass().getMethods();
        Method[] gettersAndSettersTo =to.getClass().getMethods();
        List<String> toMethodList= convert(gettersAndSettersTo);
        for (int i = 0; i < gettersAndSettersFrom.length; i++)
        {
            String methodName = gettersAndSettersFrom[i].getName();
            if(methodName.startsWith("get"))
            {
                if(toMethodList.contains(methodName.replaceFirst("get","set")))
                {
                    to.getClass().getMethod(methodName.replaceFirst("get", "set") , gettersAndSettersFrom[i].getReturnType() ).invoke(to, gettersAndSettersFrom[i].invoke(from, null));
                }
            }
        }
        return (T) to;
    }}
