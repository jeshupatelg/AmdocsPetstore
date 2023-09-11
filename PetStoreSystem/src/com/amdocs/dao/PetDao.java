package com.amdocs.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.amdocs.base.Pet;
import com.amdocs.controller.PetController;

public class PetDao {
	private static int maxPetId;

    /**
     * maxPetId stores the max petId. Use to get petId for new pet.
     * @return -> incremented petId for new pet addition
     */
    public static int getMaxPetId() {
        return ++maxPetId;
    }

    private List<Pet> list;
    private PreparedStatement addPetStatement;
    private PreparedStatement deletePetStatement;
    private PreparedStatement updatePetStatement;
    private final DBConnection dbConnection;

    public PetDao() throws SQLException {
    	dbConnection = DBConnection.getDBConnection();
        list = new ArrayList<>();
        String getList = "select petId,petCategory,petType,color,age,price,isVaccinated,foodHabits from pet";
        Statement st = dbConnection.getConn().createStatement();
        ResultSet rs = st.executeQuery(getList);
        while(rs.next()){
            Pet p = getPetFromResult(rs);
            maxPetId = Math.max(maxPetId,p.getPetId());
            list.add(p);
        }
        st.close();
        String preparedAdd = "insert into pet values(?,?,?,?,?,?,?,?)";
        addPetStatement = dbConnection.getConn().prepareStatement(preparedAdd);
        String preparedDelete = "delete from pet where petId=?";
        deletePetStatement = dbConnection.getConn().prepareStatement(preparedDelete);
        String preparedUpdate = "update pet set price=?,isVaccinated=? where petId=?";
        updatePetStatement = dbConnection.getConn().prepareStatement(preparedUpdate);
    }

    /**
     * Returns list of Pets
     * @return list of pets
     */
    public List<Pet> showAllPets() {
        return list;
    }

    /**
     * Creates pet object from the dB result row.
     * @param rs -> ResultSet
     * @return Pet object
     * @throws SQLException -> SQL problems
     */
    private Pet getPetFromResult(ResultSet rs) throws SQLException {
        return new Pet(rs.getInt(1),
                rs.getString(2).toLowerCase(),
                rs.getString(3).toLowerCase(),
                rs.getString(4).toLowerCase(),
                rs.getInt(5),
                rs.getDouble(6),
                rs.getBoolean(7),
                rs.getString(8));
    }

    /**
     * Adds Pet provided to the list of pets and adds it to the dB
     * @param p -> Pet to add
     * @return petId or -1 if not added.
     * @throws SQLException -> SQL Problems
     */
    public int addPetDetails(Pet p) throws SQLException {
        addPetStatement.setInt(1,p.getPetId());
        addPetStatement.setString(2,p.getPetCategory());
        addPetStatement.setString(3,p.getPetType());
        addPetStatement.setString(4,p.getColor());
        addPetStatement.setInt(5,p.getAge());
        addPetStatement.setDouble(6,p.getPrice());
        addPetStatement.setString(7, String.valueOf(p.isVaccinated()?'1':'0'));
        addPetStatement.setString(8,p.getFoodHabits());
        return addPetStatement.executeUpdate()==1 ? p.getPetId() : -1;
    }

    /**
     * Deletes the Pet with petId. The pet with petId must be present in list.Check before invocation.
     * @param petId -> id of pet to delete
     * @return -> no. of rows deleted
     * @throws SQLException -> SQL problems
     */
    public int deletePetDetails(int petId) throws SQLException {
        deletePetStatement.setInt(1,petId);
        return deletePetStatement.executeUpdate();
    }

    /**
     * Updates the info in dB. Doesn't update in list. To be done prior or later.
     * @param petId -> id to update info.
     * @param newPrice -> new price
     * @param vaccinationStatus -> vaccination status to set.
     * @return -> no. of rows updated.
     * @throws SQLException -> SQL problems
     */
    public int updatePetPriceAndVaccinationStatus(int petId, double newPrice, boolean vaccinationStatus) throws SQLException {
        updatePetStatement.setDouble(1,newPrice);
        updatePetStatement.setString(2, String.valueOf(vaccinationStatus?'1':'0'));
        updatePetStatement.setInt(3,petId);
        return updatePetStatement.executeUpdate();
    }
    
    public List<Pet> searchByPrice(double lowPrice,double highPrice){
    	List<Pet> res = new ArrayList<>();
        list.forEach(p -> {
            if(p.getPrice()<=highPrice && p.getPrice()>=lowPrice)res.add(p);
        });
        return res;
    }
    
    public int countByVaccinationStatusForPetType(String type,boolean vaccinationStatus){
    	int res = 0;
        for(Pet p : list){
            if(p.getPetType().equals(type) && (p.isVaccinated() == vaccinationStatus))res++;
        }
        return res;
    }
    
    /**
     * Gives the no. of pets having the given category.
     * Uses the hashmap to get result.
     * @param category -> the category whose pet is to be counted.
     * @return -> No. of pets with that category.
     */
    public int countPetsByCategory(String category){
        return PetController.getPetcategories().getOrDefault(category,0);
    }
    
    public void exitSystem() throws SQLException {
    	dbConnection.closeConnection();
    	addPetStatement.close();
    	deletePetStatement.close();
    	updatePetStatement.close();
    }
}
