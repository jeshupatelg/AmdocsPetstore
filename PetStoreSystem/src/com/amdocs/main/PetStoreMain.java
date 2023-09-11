package com.amdocs.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.amdocs.base.Pet;
import com.amdocs.controller.PetController;
import com.amdocs.exception.PetException;

public class PetStoreMain {
	private static  BufferedReader br;
    private static PetController petController;

    public static void main(String[] args) throws  IOException, PetException {
        try {
            petController = new PetController();
        } catch (SQLException e) {
        	try {
        		throw new PetException("Unable to open the Pet Store Systems.");
        	}
        	catch(PetException e1) {
            return;}
        }
        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("  =====Welcome to the Pet Store======");
        loop : while(true){
            showMenu();
            int selection;
            try {
                selection = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
            	try {
            		throw new PetException("Please enter a valid selection.");
            	}
            	catch(PetException e1) {
            		continue;
            	}
            }
            switch (selection){
                case 1 -> addingPet();
                case 2 -> deletingPet();
                case 3 -> updatePetPrice();
                case 4 -> updatePetVaccinationStatus();
                case 5 -> updatePetPriceAndVaccinationStatus();
                case 6 -> petController.showAllPets().forEach(System.out::println);
                case 7 -> countPetsByCategory();
                case 8 -> searchByPrice();
                case 9 -> countByVaccinationStatusForPetType();
                case 10 -> {
                    break loop;
                }
                default -> System.out.println("Please enter a valid selection.");
            }
        }
    }

    /**
     * Prints the menu of operations to choose from.
     */
    private static void showMenu(){
        System.out.println("Select the operation to perform.");
        System.out.println("1.  Add new pet");
        System.out.println("2.  Delete pet details");
        System.out.println("3.  Update Pet price");
        System.out.println("4.  Update vaccination status");
        System.out.println("5.  Update Pet price and vaccination status");
        System.out.println("6.  List all pets");
        System.out.println("7.  Count pets by category");
        System.out.println("8.  Find pets by price range");
        System.out.println("9. Find pets by type and vaccination status");
        System.out.println("10. Exit");
        System.out.print("Enter your selection:- ");
    }

    /**
     * Gets petId as ip and returns it.
     * @return -> petId
     * @throws IOException -> problems with IO
     * @throws PetException 
     */
    private static int getPetId() throws IOException, PetException {
        int petId = -1;
        while(petId<0 && petId>-4){
            System.out.print("Enter PetId:- ");
            int temp=0;
            try{
                temp = Integer.parseInt(br.readLine());
                if(temp < 0)throw new NumberFormatException();
            }catch(NumberFormatException e){
            	try {
            		throw new PetException("\n!!! Enter a valid PetId !!!");
            	}
            	catch(PetException e1) {
            		petId--;
                	continue;
                }
            }
            petId = temp;
        }
        if(petId<0){
        	try {
        		throw new PetException("!!! Invalid PetId entered !!!\n    Stopping operation.");
        	}
        	catch(PetException e1) {
        		
        	}
        }
        return petId;
    }

    private static double getNewPrice() throws IOException, PetException {
        double newPrice = -1;
        while(newPrice<0 && newPrice>-4){
            System.out.print("Enter new price to update:- ");
            double temp=0;
            try{
                temp = Double.parseDouble(String.format("%.2f",Double.valueOf(br.readLine())));
                if(temp < 0)throw new NumberFormatException();
            }catch(NumberFormatException e){
            	try {
            		throw new PetException("\n!!! Enter a valid PetId !!!");
            	}
            	catch(PetException e1) {
            		newPrice--;
                	continue;
                }
            }
            newPrice = temp;
        }
        if(newPrice<0){
        	try {
        		throw new PetException("!!! Invalid price entered !!!\n    Stopping price update.");
        	}
        	catch(PetException e1) {}
        }
        return newPrice;
    }

    /**
     * Takes the pet details and creates an entry for such pet.
     * If unsupported entries are made more than thrice, operation rolls back.
     * @throws IOException -> problems with IO
     * @throws PetException 
     */
    private static void addingPet() throws IOException, PetException {
        System.out.println("     Enter Pet details :- ");
        System.out.print("1. Enter pet category:- ");
        String category = br.readLine().trim().toLowerCase();
        System.out.print("2. Enter pet type:- ");
        String type = br.readLine().trim().toLowerCase();
        System.out.print("3. Enter pet color:- ");
        String color = br.readLine().trim().toLowerCase();
        int age = -1;
        while(age<0 && age>-4){
            System.out.print("4. Enter pet age:- ");
            int temp=0;
            try{
                temp = Integer.parseInt(br.readLine());
                if(temp < 1)throw new NumberFormatException();
            }catch(NumberFormatException e){
            	try {
            		throw new PetException("\n!!! Enter a valid age !!!");
            	}
            	catch(PetException e1) {
            		age--;
                	continue;
                }
            }
            age = temp;
        }
        if(age<0){
        	try {
        		throw new PetException("!!! Invalid age entered !!!\n    Stopping pet addition.");
        	}
        	catch(PetException e1) {return;}
        }
        double price = -1.0;
        while(price<0 && price>-4){
            System.out.print("5. Enter pet price:- ");
            double temp=0;
            try{
                temp = Double.parseDouble(String.format("%.2f",Double.valueOf(br.readLine())));
                if(temp < 0)throw new NumberFormatException();
            }catch(NumberFormatException e){
            	try {
            		throw new PetException("\n!!! Enter a valid price !!!");
            	}
            	catch(PetException e1) {
            		price--;
                	continue;
                }
            }
            price = temp;
        }
        if(price<0){
        	try {
        		throw new PetException("!!! Invalid price entered !!!\n    Stopping pet addition.");
        	}
        	catch(PetException e1) {return;}
        }
        System.out.print("6. Enter vaccination status('Y' or 'N'):- ");
        String temp = br.readLine();
        boolean isVaccinated = temp.equals("Y") || temp.equals("y") || temp.equals("Yes") || temp.equals("yes");
        System.out.print("7. Enter pet's food habits:- ");
        String foodHabits = br.readLine();
        System.out.println("--- Please wait while we add the pet to Store Database ---");
        int res = petController.addPetDetails(new Pet(category,type,color,age,price,isVaccinated,foodHabits));
        try {
        if(res == -1)
        	throw new PetException("Pet not added due to technical issues.");
        else System.out.println("Pet added successfully with PetId = "+res);
        }
        catch(PetException e1) {}
    }

    /**
     * Takes the petId and deletes entry for such pet.
     * If unsupported entries are made more than thrice, operation rolls back.
     * @throws IOException -> problems with IO
     * @throws PetException 
     */
    private static void deletingPet() throws IOException, PetException {
        int petId = getPetId();
        if(petId < 0)return;
        System.out.println("--- Please wait while we delete the pet from Store Database ---");
        boolean res = false;
        try {
            res = petController.deletePetDetails(petId);
        } catch (SQLException e) {
        	try {
        		throw new PetException("Pet not deleted due to technical issues.");
        	}
        	catch(Exception e1) {return;}
        }
        try {
        if(res)System.out.println("Pet deleted successfully");
        else throw new PetException("No such Pet found.");
        }
        catch(PetException e1) {}
    }

    /**
     * Takes the petId and new price and updates price of entry for such pet.
     * If unsupported entries are made more than thrice, operation rolls back.
     * @throws IOException -> problems with IO
     * @throws PetException 
     */
    private static void updatePetPrice() throws IOException, PetException {
        int petId = getPetId();
        if(petId<0){
            return;
        }
        double newPrice = getNewPrice();
        if(newPrice<0){
            return;
        }
        System.out.println("--- Please wait while we update the price in Store Database ---");
        boolean res = false;
        try {
            res = petController.updatePrice(petId,newPrice);
        } catch (SQLException e) {
        	try {
            throw new PetException("Price not updated due to technical issues.\nPlease try again.");
        	}
        	catch(PetException e1) {
            return;}
        }
        try {
        if(res)System.out.println("Price updated successfully");
        else //System.out.println
        	throw new PetException("No such Pet found.");
        }
        catch(PetException e1) {}
    }

    /**
     * Takes the petId updates vaccination status of entry for such pet.
     * If unsupported entries are made more than thrice, operation rolls back.
     * @throws IOException -> problems with IO
     * @throws PetException 
     */
    private static void updatePetVaccinationStatus() throws IOException, PetException {
        int petId = getPetId();
        if(petId<0){
            return;
        }
        boolean res = false;
        try {
            res = petController.updateVaccinationStatus(petId);
        } catch (SQLException e) {
            //System.out.println
        	try {
        		throw new PetException("Vaccination not updated due to technical issues.\nPlease try again.");
        	}
        	catch(PetException e1) {return;}
        }
        try {
        if(res)System.out.println("Vaccination updated successfully");
        else //System.out.println
        	throw new PetException("No such Pet found.");
        }
        catch(PetException e) {}
    }

    /**
     * Takes the petId and new Price and updates price and vaccination status of entry for such pet.
     * If unsupported entries are made more than thrice, operation rolls back.
     * @throws IOException -> problems with IO
     * @throws PetException 
     */
    private static void updatePetPriceAndVaccinationStatus() throws IOException, PetException {
        int petId = getPetId();
        if(petId<0){
            return;
        }
        double newPrice = getNewPrice();
        if(newPrice<0){
            return;
        }
        boolean res = false;
        try {
            res = petController.updatePriceAndVaccinationStatus(petId,newPrice);
        } catch (SQLException e) {
            //System.out.println
        	try {
        		throw new PetException("Price and vaccination not updated due to technical issues.\nPlease try again.");
        	}
        	catch(PetException e1) {return;}
        }
        try {
        	if(res)System.out.println("Updated successfully");
        	else //System.out.println
        		throw new PetException("No such Pet found.");
        }
        catch(PetException e1) {}
    }

    /**
     * Takes pet Category and returns no. of such pets.
     * @throws IOException-> problems with IO
     */
    private static void countPetsByCategory() throws IOException {
        System.out.print("Enter pet category:- ");
        String category = br.readLine().trim().toLowerCase();
        System.out.println("The no. of pets with '"+category+"' pet category is "+petController.countPetsByCategory(category));
    }

    /**
     * Takes lower and higher price to form a range.
     * Gives the list of all pets whose price lies between them.
     * @throws IOException -> Problems with IO
     * @throws PetException 
     */
    private static void searchByPrice() throws IOException, PetException {
        double lowPrice = Double.NEGATIVE_INFINITY;
        while(lowPrice == Double.NEGATIVE_INFINITY){
            System.out.print("Enter lower pet price range:- ");
            double temp=0;
            try{
                temp = Double.parseDouble(String.format("%.2f",Double.valueOf(br.readLine())));
            }catch(NumberFormatException e){
            	try {
                //System.out.println
            		throw new PetException("\n!!! Enter a valid price !!!");
            	}
            	catch(PetException e1) {
            		continue;
        		}
            }
            lowPrice = temp;
        }
        double highPrice = Double.NEGATIVE_INFINITY;
        int tries = 0;
        while(highPrice == Double.NEGATIVE_INFINITY && tries < 3){
            System.out.print("Enter higher pet price range:- ");
            double temp=0;
            try{
                temp = Double.parseDouble(String.format("%.2f",Double.valueOf(br.readLine())));
                if(temp<lowPrice){
                	try {
                		throw new PetException("Higher price in range should be more or equal to lower price.");
                	}
                	catch(PetException e1) {
                		tries++;
                        continue;
                	}
                }
            }catch(NumberFormatException e){
            	try {
            		throw new PetException("Enter a valid price.");
            	}
            	catch(PetException e1) {
            		tries++;
                    continue;
            	}
            }
            highPrice = temp;
        }
        if(tries >= 3){
        	try {
        		throw new PetException("Invalid price entered. Stopping the operation.");
        	}
        	catch(PetException e1) {return;}
        }
        System.out.println("Following are the pets with price between "+lowPrice+" and "+highPrice+" :-");
        petController.searchByPrice(lowPrice,highPrice).forEach(System.out::println);
    }

    private static void countByVaccinationStatusForPetType() throws IOException {
        System.out.print("Enter the pet type:- ");
        String type = br.readLine().trim().toLowerCase();
        System.out.print("Do you want the pet to be vaccinated ?(Y for yes)");
        String temp = br.readLine();
        boolean isVaccinated = temp.equals("Y") || temp.equals("y") || temp.equals("Yes") || temp.equals("yes");
        System.out.println("The no. of pets of type "+type+" with required vaccination status is "+petController.countByVaccinationStatusForPetType(type,isVaccinated));
    }
}
