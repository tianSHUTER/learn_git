package com.example.demo;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;


public class XlassLoader extends ClassLoader {
    public static void main(String[] args) throws Exception {
      final String className ="Hello";
      final String methodName="hello";
        ClassLoader classLoader = new XlassLoader();
        Class<?> zClass = classLoader.loadClass(className);
        for (Method m: zClass.getDeclaredMethods()
             ) {
            System.out.println(zClass.getSimpleName()+""+m.getName());
        }
        Object instance = zClass.getDeclaredConstructor().newInstance();
        Method method = zClass.getMethod(methodName);
        method.invoke(instance);
    }

    @Override
    protected Class<?> findClass (String name) throws ClassNotFoundException{
    String resourcePath=name.replace(".","/");
    final String suffix =".xlass";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath + suffix);
        try{
            int length=inputStream.available();
            byte[] byteArray = new byte[length];
            inputStream.read(byteArray);
            byte[] decode = decode(byteArray);
            return defineClass(name,decode,0,decode.length);
        }catch (IOException e){
            throw new ClassNotFoundException(name,e);
        }finally {
            close(inputStream);
        }

    }

    private static byte[] decode(byte[] bytes){
        byte[] bytes1 = new byte[bytes.length];
        for (int i = 0 ; i <bytes.length;i++){
           bytes1[i]= (byte)(255-bytes1[i]);
        }
return bytes1;
    }

    private static void close(Closeable res){
        if (res!=null){
            try {
                res.close();
            }catch (IOException E)
            {
                E.printStackTrace();
            }
        }
    }
}
