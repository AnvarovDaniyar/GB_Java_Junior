package org.example.lesson2.HomeWork2;

public class Dog extends Animal{
    private String color;
    private double weight;

    public Dog() {
    }

    public Dog(String name, int age) {
        super(name, age);
    }

    public Dog(String name, int age, String color, double weight) {
        super(name, age);
        this.color = color;
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public double getWeight() {
        return weight;
    }

    public void makeSound() {
        System.out.println(super.getName() + " say -> Gav");
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name=" + super.getName() +
                ", age=" + super.getAge() +
                ", color='" + color +
                ", weight=" + weight +
                '}';
    }
}