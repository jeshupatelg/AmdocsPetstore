package com.amdocs.base;

import com.amdocs.dao.PetDao;

public class Pet {
	private final int petId;
    private final String petCategory;
    private final String petType;
    private final String color;
    private final int age;
    private double price;
    private boolean isVaccinated;
    private final String foodHabits;

    public Pet(int petId, String petCategory, String petType, String color, int age, double price, boolean isVaccinated, String foodHabits) {
        this.petId = petId;
        this.petCategory = petCategory;
        this.petType = petType;
        this.color = color;
        this.age = age;
        this.price = price;
        this.isVaccinated = isVaccinated;
        this.foodHabits = foodHabits;
    }

    public Pet(String petCategory, String petType, String color, int age, double price, boolean isVaccinated, String foodHabits) {
        this.petCategory = petCategory;
        this.petType = petType;
        this.color = color;
        this.age = age;
        this.price = price;
        this.isVaccinated = isVaccinated;
        this.foodHabits = foodHabits;
        this.petId = PetDao.getMaxPetId();
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVaccinated(boolean vaccinated) {
        isVaccinated = vaccinated;
    }

    public int getPetId() {
        return petId;
    }

    public String getPetCategory() {
        return petCategory;
    }

    public String getPetType() {
        return petType;
    }

    public String getColor() {
        return color;
    }

    public int getAge() {
        return age;
    }

    public double getPrice() {
        return price;
    }

    public boolean isVaccinated() {
        return isVaccinated;
    }

    public String getFoodHabits() {
        return foodHabits;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petId=" + petId +
                "   , petCategory='" + petCategory + '\'' +
                "   , petType='" + petType + '\'' +
                "   , color='" + color + '\'' +
                "   , age=" + age +
                "   , price=" + price +
                "   , isVaccinated=" + isVaccinated +
                ", foodHabits='" + foodHabits + '\'' +
                "}\n";
    }
}
