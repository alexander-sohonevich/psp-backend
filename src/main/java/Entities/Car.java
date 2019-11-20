package Entities;

import Server.Database;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Car {
    private String brand;
    private String model;
    private int yearOfIssue;
    private double cost;
    private String vinCode;
    private String bodyType;
    private String bodyColor;
    private int numberOfDoors;
    private String salon;
    private String salonColor;
    private String multimediaSystem;
    private double wheelDiameter;
    private String transmission;

    public Car() {
        this.brand = "";
        this.model = "";
        this.yearOfIssue = 0;
        this.cost = 0;
        this.vinCode = "";
        this.bodyType = "";
        this.bodyColor = "";
        this.numberOfDoors = 0;
        this.salon = "";
        this.salonColor = "";
        this.multimediaSystem = "";
        this.wheelDiameter = 0;
        this.transmission = "";
    }

    public Car(String brand, String model, int yearOfIssue, double cost, String vinCode,
               String bodyType, String bodyColor, int numberOfDoors, String salon,
               String salonColor, String multimediaSystem, double wheelDiameter, String transmission) {
        this.brand = brand;
        this.model = model;
        this.yearOfIssue = yearOfIssue;
        this.cost = cost;
        this.vinCode = vinCode;
        this.bodyType = bodyType;
        this.bodyColor = bodyColor;
        this.numberOfDoors = numberOfDoors;
        this.salon = salon;
        this.salonColor = salonColor;
        this.multimediaSystem = multimediaSystem;
        this.wheelDiameter = wheelDiameter;
        this.transmission = transmission;
    }

    public Car(String jsonString) {

        JSONObject carJson = new JSONObject(jsonString);

        String brand = carJson.getString("brand");
        String model = carJson.getString("model");
        String yearOfIssue = carJson.getString("yearOfIssue");
        String cost = carJson.getString("cost");
        String vinCode = carJson.getString("vinCode");
        String bodyType = carJson.getString("bodyType");
        String bodyColor = carJson.getString("bodyColor");
        String numberOfDoors = carJson.getString("numberOfDoors");
        String salon = carJson.getString("salon");
        String salonColor = carJson.getString("salonColor");
        String multimediaSystem = carJson.getString("multimediaSystem");
        String transmission = carJson.getString("transmission");
        String wheelDiameter = carJson.getString("wheelDiameter");


        this.brand = brand;
        this.model = model;
        this.yearOfIssue = Integer.parseInt(yearOfIssue);
        this.cost = Double.parseDouble(cost);
        this.vinCode = vinCode;
        this.bodyType = bodyType;
        this.bodyColor = bodyColor;
        this.numberOfDoors = Integer.parseInt(numberOfDoors);
        this.salon = salon;
        this.salonColor = salonColor;
        this.multimediaSystem = multimediaSystem;
        this.wheelDiameter = Double.parseDouble(wheelDiameter);
        this.transmission = transmission;
    }

    public String setObjectToDB(Database conn) {
        String checkingString = "";

        try {
            String query = "SELECT VIN_CODE FROM AUTO WHERE VIN_CODE='" + this.vinCode + "'";

            Statement statement = conn.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                if (rs.getString("VIN_CODE").equals(vinCode)) {
                    checkingString = "Ошибка! Такой VIN-код уже существует!";
                }
            }

            statement.close();
            rs.close();

            if (!checkingString.equals("")) {
                return checkingString;
            }

        } catch (SQLException s) {
            System.err.println(s);
            s.setNextException(s);
        }

        try {
            String query = "" +
                    "INSERT INTO AUTO " +
                    "VALUES ('%s', '%s', %s, %s, '%s'); " +
                    "INSERT INTO BODY " +
                    "VALUES ('%s', '%s', '%s', %s); " +
                    "INSERT INTO CAR_STATUS " +
                    "VALUES ('%s', '%s'); " +
                    "INSERT INTO SYSTEMS " +
                    "VALUES ('%s', '%s', '%s', '%s'); " +
                    "INSERT INTO TRANSMISSION " +
                    "VALUES ('%s', '%s', %s); ";

            String insertQuery = String.format(query,
                    brand, model, yearOfIssue, cost, vinCode,
                    bodyColor, vinCode, bodyType, numberOfDoors,
                    vinCode, "В наличии",
                    vinCode, salon, salonColor, multimediaSystem,
                    vinCode, transmission, wheelDiameter);

            Statement stmt = conn.getConnection().createStatement();
            Boolean okay = stmt.execute(insertQuery);

            if (okay) {
                checkingString = "Успешно";
            } else {
                checkingString = "Ошибка добавления. Попробуйте снова позже!";
            }

            stmt.close();
            return checkingString;
        } catch (SQLException s) {
            System.err.println(s);
            s.setNextException(s);
        }
        return "Ошибка";
    }

    public String getFilteredCars(Database conn, String filter) {

        String result = "[";

        String query = "SELECT a.BRAND, a.MODEL, a.YEAR_OF_ISSUE, a.COST, a.VIN_CODE,\n" +
                "b.BODY_TYPE, b.BODY_COLOR, b.NUMBER_OF_DOORS,\n" +
                "c.CAR_STATUS,\n" +
                "s.SALON, s.SALON_COLOR, s.MUTLIMEDIA_SYSTEM,\n" +
                "t.TRANSMISSION, t.WHEEL_DIAMETER \n" +
                "FROM auto a\n" +
                "JOIN body b ON a.VIN_CODE=b.VIN_CODE \n" +
                "JOIN car_status c ON a.VIN_CODE=c.VIN_CODE \n" +
                "JOIN systems s ON a.VIN_CODE=s.VIN_CODE \n" +
                "JOIN transmission t ON a.VIN_CODE=t.VIN_CODE;";

        query = query + "WHERE c.CAR_STATUS='" + filter + "'";

        try {
            Statement statement = conn.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Car filteredCar = new Car(
                        rs.getString("BRAND"),
                        rs.getString("MODEL"),
                        Integer.parseInt(rs.getString("YEAR_OF_ISSUE")),
                        Double.parseDouble(rs.getString("COST")),
                        rs.getString("VIN_CODE"),
                        rs.getString("BODY_TYPE"),
                        rs.getString("BODY_COLOR"),
                        Integer.parseInt(rs.getString("NUMBER_OF_DOORS")),
                        rs.getString("SALON"),
                        rs.getString("SALON_COLOR"),
                        rs.getString("MUTLIMEDIA_SYSTEM"),
                        Double.parseDouble(rs.getString("WHEEL_DIAMETER")),
                        rs.getString("TRANSMISSION"));
                result = result + filteredCar.toString() + ",";
            }

            result = result + "@";
            result = result.replace(",@", "]");

            statement.close();
            rs.close();
        } catch (SQLException s) {
            System.err.println(s);
            s.setNextException(s);
        }

        return result;
    }

    public String sellingCar(Database conn, String sellingCarJSON) {

        String query = "INSERT INTO SOLD_CARS " +
                "VALUES ('%s', '%s' , '%s', '%s')";

        JSONObject jsonObject = new JSONObject(sellingCarJSON);

        String vinCodeJSON = jsonObject.getString("vinCode");
        String buyerSurnameJSON = jsonObject.getString("buyerSurname");
        String buyerNameJSON = jsonObject.getString("buyerName");
        String buyerIDJSON = jsonObject.getString("buyerID");

        query = String.format(query, vinCodeJSON, buyerSurnameJSON, buyerNameJSON, buyerIDJSON);

        try {
            Statement statement = conn.getConnection().createStatement();
            Boolean rs = statement.execute(query);
            statement.close();

            if (rs) {
                return "Успешно";
            }
            else {
                return "Ошибка";
            }

        } catch (SQLException s) {
            System.err.println(s);
            s.setNextException(s);
        }

        return "Ошибка";
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYearOfIssue() {
        return yearOfIssue;
    }

    public void setYearOfIssue(int yearOfIssue) {
        this.yearOfIssue = yearOfIssue;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(String bodyColor) {
        this.bodyColor = bodyColor;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

    public String getSalonColor() {
        return salonColor;
    }

    public void setSalonColor(String salonColor) {
        this.salonColor = salonColor;
    }

    public String getMultimediaSystem() {
        return multimediaSystem;
    }

    public void setMultimediaSystem(String multimediaSystem) {
        this.multimediaSystem = multimediaSystem;
    }

    public double getWheelDiameter() {
        return wheelDiameter;
    }

    public void setWheelDiameter(double wheelDiameter) {
        this.wheelDiameter = wheelDiameter;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    @Override
    public String toString() {
        return "{ " +
                "\"brand\":  \"" + brand + "\", " +
                "\"model\":  \"" + model + " \", " +
                "\"yearOfIssue\": \" " + yearOfIssue + "  \", " +
                "\"cost\":  \"" + cost + "\", " +
                "\"vinCode\": \"" + vinCode + "\", " +
                "\"bodyType\": \"" + bodyType + "\", " +
                "\"bodyColor\": \"" + bodyColor + "\", " +
                "\"numberOfDoors\": \"" + numberOfDoors + " \", " +
                "\"salon\": \"" + salon + "\", " +
                "\"salonColor\": \"" + salonColor + "\", " +
                "\"multimediaSystem\": \"" + multimediaSystem + "\", " +
                "\"transmission\": \"" + transmission + "\", " +
                "\"wheelDiameter\": \"" + wheelDiameter + "\" " +
                "}";
    }

    @Test
    public void test() {
        Car car1 = new Car();

        System.out.println(car1.toString());

    }
}
