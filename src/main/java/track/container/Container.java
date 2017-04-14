package track.container;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import track.container.config.Bean;
import track.container.config.Property;
import track.container.config.InvalidConfigurationException;

import static track.container.config.ValueType.VAL;



/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {

    private List<Bean> beans;
    Map<String, Object> objByName;
    Map<String, Object> objByClassName;


    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) throws InvalidConfigurationException {
        this.beans = beans;
    }

    private Object makeObject(Bean bean) throws InvalidConfigurationException {
        Class clazz;
        try {
            clazz = Class.forName(bean.getClassName());
            Object obj = clazz.newInstance();

            for (Map.Entry<String, Property> entry : bean.getProperties().entrySet()) {
                Field field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                if (entry.getValue().getType() == VAL) {
                    switch (field.getType().toString()) {
                        case "byte":
                            field.set(obj, Byte.parseByte(entry.getValue().getValue()));
                            break;
                        case "short":
                            field.set(obj, Short.parseShort(entry.getValue().getValue()));
                            break;
                        case "char":
                            field.set(obj, entry.getValue().getValue().charAt(0));
                            break;
                        case "int":
                            field.set(obj, Integer.parseInt(entry.getValue().getValue()));
                            break;
                        case "long":
                            field.set(obj, Long.parseLong(entry.getValue().getValue()));
                            break;
                        case "float":
                            field.set(obj, Float.parseFloat(entry.getValue().getValue()));
                            break;
                        case "double":
                            field.set(obj, Double.parseDouble(entry.getValue().getValue()));
                            break;
                        case "boolean":
                            field.set(obj, Boolean.parseBoolean(entry.getValue().getValue()));
                            break;
                        case "String":
                            field.set(obj, entry.getValue().getValue());
                            break;
                        default:
                            break;
                    }
                } else {
                    field.set(obj, getById(entry.getValue().getValue()));
                }
            }
            return obj;
        } catch (Exception e) {
            throw new InvalidConfigurationException(e.getMessage());
        }
    }


    /**
     * Вернуть объект по имени бина из конфига
     * Например, Car car = (Car) container.getById("carBean")
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

}
