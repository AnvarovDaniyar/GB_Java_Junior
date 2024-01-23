package org.example.lesson2.HomeWork2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Program {
    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Animal> animalArray = new ArrayList<>();

        animalArray.add(new Cat("Barsik", 5, "white"));
        animalArray.add(new Cat("Yury", 2, "black"));
        animalArray.add(new Dog("Valet", 1, "grey", 8.5));
        animalArray.add(new Dog("Roza", 10, "black", 15));

        Class<?> animalClass;
        for (Animal animal : animalArray) {
            System.out.println(animal);
            if (animal.getClass().equals(Cat.class)) {
                animalClass = Class.forName("ru.gb.ergakov.lesson2.Homework2.Cat");
            } else {
                animalClass = Class.forName("ru.gb.ergakov.lesson2.Homework2.Dog");
            }


            // Получить количество параметров конструктора:
            Constructor[] constructors = animalClass.getConstructors();
            Arrays.stream(constructors)
                    .map(constructor -> "Constructor parameter count: " + constructor.getParameterCount())
                    .forEach(System.out::println);
            System.out.println();

            // Получить список полей дочернего и родительского классов:
            Field[] superFields = animalClass.getSuperclass().getDeclaredFields();
            Field[] classFields = animalClass.getDeclaredFields();
            Field[] allFields = new Field[superFields.length + classFields.length];

            Arrays.setAll(allFields, i ->
                    (i < classFields.length ? classFields[i] : superFields[i - classFields.length]));

            for (Field field : allFields) {
                System.out.println("Field: " + field.getName() + ", type: " + field.getType().getName());
            }
            System.out.println();

            // Вывести все методы классов:
            Method[] metods = animalClass.getDeclaredMethods();
            for (Method method : metods){
                System.out.println("Method: " + method.getName() +
                        ", Return type: " + method.getReturnType() +
                        ", parameter types" + Arrays.toString(method.getParameterTypes()));
            }
            System.out.println();

            // Вызов метода "makeSound()":
            Method makeSoundMethod;
            try {
                makeSoundMethod = animalClass.getDeclaredMethod("makeSound");
                makeSoundMethod.invoke(animal);
                System.out.println();
            } catch (NoSuchMethodException e) {
                System.out.println("У этого животного такого метода нет\n");;
            }
        }
    }

}