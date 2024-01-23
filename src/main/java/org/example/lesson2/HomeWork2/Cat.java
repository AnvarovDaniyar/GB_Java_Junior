package org.example.lesson2.HomeWork2;

public class Cat extends Animal{
    public Cat() {
    }
    private String color;
    public Cat(String name, int age) {
        super(name, age);
    }

    public Cat(String name, int age, String color) {
        super(name, age);
        this.color = color;
    }

    public String getColor() {
        return color;
    }


    public void somersaults(){
        System.out.println(super.getName() + "does a somersault");
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name=" + super.getName() +
                ", age=" + super.getAge() +
                ", color='" + color +
                "} ";
    }
}