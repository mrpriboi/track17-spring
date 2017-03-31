package track.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
<<<<<<< HEAD
import java.util.HashMap;
=======
>>>>>>> master

import track.container.config.Bean;
import track.container.config.InvalidConfigurationException;
import track.container.config.Property;
import track.container.config.ValueType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {
<<<<<<< HEAD
    private Map<String, Object> objByName;
    private Map<String, Object> objByClassName;
    List<Bean> beans;

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) {
        objByName = new HashMap<>();
        objByClassName = new HashMap<>();
        this.beans = beans;
    }

    private Object makeObject(Bean bean) throws InvalidConfigurationException {
        try {
            objByName.put(bean.getId(), Class.forName(bean.getClassName()).newInstance());
            objByClassName.put(bean.getClassName(), objByName.get(bean.getId()));
=======

    Map<String, Object> map = new HashMap<>();

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) throws Exception {

    }

    public static void main(String[] args) throws Exception {
>>>>>>> master

            Object obj = getById(bean.getId());
            Class cl = obj.getClass();
            for (Property property : bean.getProperties().values()) {
                StringBuilder name = new StringBuilder(property.getName());
                Field currentField = cl.getDeclaredField(name.toString());
                name.setCharAt(0, Character.toUpperCase(name.charAt(0)));
                Method setMethod = cl.getDeclaredMethod("set" + name, currentField.getType());
                if (property.getType().equals(ValueType.VAL)) {
                    if (isPrimitive(currentField.getType().toString())) {
                        setMethod.invoke(obj, Integer.parseInt(property.getValue()));
                    } else {
                        throw new Exception("Invalid type");
                    }
                } else {
                    setMethod.invoke(obj, getByClass(currentField.getType().getTypeName()));
                }
            }
            return obj;
        } catch (Exception e) {
            throw new InvalidConfigurationException(e.getMessage());
        }
    }

    /**
     *  Вернуть объект по имени бина из конфига
     *  Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) throws InvalidConfigurationException {
        if (objByName.containsKey(id)) {
            return objByName.get(id);
        } else {
            for (Bean bean : beans) {
                if (bean.getId().equals(id)) {
                    return makeObject(bean);
                }
            }
            return null;
        }
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("track.container.beans.Car")
     */
    public Object getByClass(String className) throws InvalidConfigurationException {
        if (objByClassName.containsKey(className)) {
            return objByClassName.get(className);
        } else {
            for (Bean bean : beans) {
                if (bean.getClassName().equals(className)) {
                    return makeObject(bean);
                }
            }
            return null;
        }
    }

    private boolean isPrimitive(String type) {
        return type.equals("int") || type.equals("boolean") || type.equals("short") ||
                type.equals("long") || type.equals("char") || type.equals("byte") ||
                type.equals("float") || type.equals("double") || type.equals("String");
    }

}
