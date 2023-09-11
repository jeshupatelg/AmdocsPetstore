package com.amdocs.controller;

import java.sql.SQLException;
import java.util.*;

import com.amdocs.base.Pet;
import com.amdocs.dao.PetDao;

public class PetController {
	private final PetDao petDao;
    private final List<Pet> list;
    private final Set<Integer> petIds;
    private static final Map<String, Integer> petCategories = new HashMap<>();

    public PetController() throws SQLException {
        petDao = new PetDao();
        list = petDao.showAllPets();
        petIds = new HashSet<>();
        for(Pet p : list){
            petIds.add(p.getPetId());
            petCategories.put(p.getPetCategory(),1+petCategories.getOrDefault(p.getPetCategory(),0));
        }
    }


    /**
     * Adds new Pet
     * @param p -> Pet to add
     * @return -> petId of new pet. <br>-1 if the operation fails.
     */
    public int addPetDetails(Pet p) {
        list.add(p);
        petIds.add(p.getPetId());
        petCategories.put(p.getPetCategory(),1+petCategories.getOrDefault(p.getPetCategory(),0));
        try {
            return petDao.addPetDetails(p);
        } catch (SQLException e) {
            list.remove(list.size()-1);
            petIds.remove(p.getPetId());
            petCategories.put(p.getPetCategory(), petCategories.get(p.getPetCategory()) - 1);
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Deletes pet with the given petId.
     * @param petId -> PetId for which pet details to delete.
     * @return -> true for pet deleted, false for no such pet.
     * @throws SQLException -> SQL problems
     */
    public boolean deletePetDetails(int petId) throws SQLException {
        if(!petIds.contains(petId))return false;
        petIds.remove(petId);
        Pet deleted = null;
        for(int i = 0;i<list.size();i++)
            if(list.get(i).getPetId() == petId){
                deleted = list.remove(i);
                break;
            }
        try {
            if(petDao.deletePetDetails(petId) == 1) {
                assert deleted != null;
                petCategories.put(deleted.getPetCategory(),petCategories.get(deleted.getPetCategory())-1);
            }
            else throw new SQLException();
        } catch (SQLException e) {
            petIds.add(petId);
            list.add(deleted);
            petCategories.put(deleted.getPetCategory(), petCategories.get(deleted.getPetCategory())+1);
            e.printStackTrace();
            throw e;//new SQLException(e);
        }
        return true;
    }

    /**
     * Updates the price in list and calls dao method to update in dB.
     * @param petId -> id for pet to update info
     * @param newPrice -> new Price
     * @return -> true if updated. false if petId not found.
     * @throws SQLException -> SQL Problems
     */
    public boolean updatePrice(int petId, double newPrice) throws SQLException {
        if(!petIds.contains(petId))return false;
        double oldPrice=0;
        Pet temp=null;
        for(Pet p : list){
            if(p.getPetId() == petId){
                temp = p;
                oldPrice = p.getPrice();
                p.setPrice(newPrice);
                break;
            }
        }
        try {
            assert temp != null;
            return petDao.updatePetPriceAndVaccinationStatus(petId,newPrice,temp.isVaccinated()) == 1;
        } catch (SQLException e) {
            temp.setPrice(oldPrice);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Updates the vaccination status of the pet with petId to true if false.
     * @param petId -> the pet whose vaccination status is to be updated.
     * @return -> true if the vaccination status is updated. false if it is not found.
     * @throws SQLException -> SQL problems
     */
    public boolean updateVaccinationStatus(int petId) throws SQLException {
        //List<Pet> list = petDao.getPetList();
        if(!petIds.contains(petId))return false;
        boolean oldVaccinationStatus = false;
        Pet temp = null;
        for(Pet p : list){
            if(p.getPetId() == petId){
                if(!p.isVaccinated()){
                    temp = p;
                    oldVaccinationStatus = p.isVaccinated();
                    p.setVaccinated(true);
                }
                break;
            }
        }
        try {
            assert temp != null;
            return petDao.updatePetPriceAndVaccinationStatus(petId,temp.getPrice(),true) == 1;
        } catch (SQLException e) {
            temp.setVaccinated(oldVaccinationStatus);
            e.printStackTrace();
            throw e;
        }
    }

    public boolean updatePriceAndVaccinationStatus(int petId,double newPrice) throws SQLException {
        if(!petIds.contains(petId))return false;
        boolean oldVaccinationStatus = false;
        double oldPrice = -1;
        Pet temp = null;
        for(Pet p : list){
            if(p.getPetId() == petId){
                if(!p.isVaccinated()){
                    temp = p;
                    oldPrice = p.getPrice();
                    oldVaccinationStatus = p.isVaccinated();
                    p.setVaccinated(true);
                    p.setPrice(newPrice);
                }
                break;
            }
        }
        try {
            return petDao.updatePetPriceAndVaccinationStatus(petId,newPrice,true) == 1;
        } catch (SQLException e) {
            assert temp != null;
            temp.setPrice(oldPrice);
            temp.setVaccinated(oldVaccinationStatus);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Gives a list of all pets.
     * @return -> list of all pets.
     */
    public List<Pet> showAllPets(){return list; }

    /**
     * Gives the no. of pets having the given category.
     * Uses the hashmap to get result.
     * @param category -> the category whose pet is to be counted.
     * @return -> No. of pets with that category.
     */
    public int countPetsByCategory(String category){
        return petDao.countPetsByCategory(category);
    }

    public static Map<String, Integer> getPetcategories() {
		return petCategories;
	}


	/**
     * Iterates the list of pets and returns a list of pets having price in the given range.
     * @param lowPrice -> Lower price in the range.
     * @param highPrice -> Higher price in the range.
     * @return -> List of pets in the price range.
     */
    public List<Pet> searchByPrice(double lowPrice, double highPrice){
        return petDao.searchByPrice(lowPrice,highPrice);
    }

    /**
     * Counts the no. of pets with given type and required vaccination status.
     * @param type -> Type
     * @param vaccinationStatus -> vaccination Status required.
     * @return -> count
     */
    public int countByVaccinationStatusForPetType(String type,boolean vaccinationStatus){
        return petDao.countByVaccinationStatusForPetType(type,vaccinationStatus);
    }

    public Set<String> getPetCategories(){
        return petCategories.keySet();
    }

    public Set<Integer> getPetIds() {
        return petIds;
    }
    
    public void exitSystem() {
    	try {
			petDao.exitSystem();
		} catch (SQLException e) {
		}
    }
}
