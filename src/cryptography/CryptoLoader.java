package cryptography;

import configuration.Configuration;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/*
 * Author: 6439456
 */

public class CryptoLoader {
    private Object port;
    private Method cryptoMethod;

    public void createCryptographyMethod(CryptoAlgorithm cryptoAlgorithm, CryptoMethod method) {
        Object instance;
        try {
            URL[] urls = {new File(Configuration.instance.getCryptoAlgorithmPath(cryptoAlgorithm)).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, CryptoLoader.class.getClassLoader());
            Class clazz = Class.forName(cryptoAlgorithm.toString(), true, urlClassLoader);

            instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);
            cryptoMethod = port.getClass().getMethod(method.toString().toLowerCase(), String.class, File.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createCrackerMethod(CryptoAlgorithm cryptoAlgorithm) {
        Object instance;
        try {
            URL[] urls = {new File(Configuration.instance.getCrackerPath(cryptoAlgorithm)).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, CryptoLoader.class.getClassLoader());
            Class clazz = Class.forName(cryptoAlgorithm.toString() + "Cracker", true, urlClassLoader);

            instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);
            cryptoMethod = port.getClass().getMethod(CryptoMethod.DECRYPT.toString().toLowerCase(), String.class, File.class);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Object getPort() {
        return port;
    }

    public Method getCryptoMethod() {
        return cryptoMethod;
    }
}
