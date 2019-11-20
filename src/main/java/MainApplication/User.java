package MainApplication;

import Entities.Car;
import Server.Database;
import Server.TCPConnection;

public class User {

    public static void user(Database conn, TCPConnection tcpConnection) {
        while (tcpConnection.ping()) {
            String calledFunction = tcpConnection.receiveString();

            switch (calledFunction) {
                case "ADD CAR":
                    addCar(conn, tcpConnection);
                    break;
                case "FILTER CARS":
                    filterCars(conn, tcpConnection);
                    break;
                case "SELLING CAR":
                    sellingCar(conn, tcpConnection);
                    break;
                case "UPDATE ALL CARS":
                    updateAllCars(conn, tcpConnection);
                    break;
            }

        }
    }

    private static void addCar(Database conn, TCPConnection tcpConnection) {
        String jsonCar = tcpConnection.receiveString();
        Car car = new Car(jsonCar);
        tcpConnection.sendString(car.setObjectToDB(conn));
        return;
    }

    private static void filterCars(Database conn, TCPConnection tcpConnection) {
        String calledFunction = tcpConnection.receiveString();
        String jsonArray = "";
        Car car = new Car();
        switch (calledFunction) {
            case "ORDER":
                jsonArray = car.getFilteredCars(conn, "Под заказ");
                break;
            case "SOLD":
                jsonArray = car.getFilteredCars(conn, "Продано");
                break;
            case "IN STOCK":
                jsonArray = car.getFilteredCars(conn, "В наличии");
                break;
        }
        tcpConnection.sendString(jsonArray);
    }

    private static void sellingCar(Database conn, TCPConnection tcpConnection) {
        String sellingCarJSON = tcpConnection.receiveString();
        Car car = new Car();
        String result = car.sellingCar(conn, sellingCarJSON);
        tcpConnection.sendString(result);
    }

    private static void updateAllCars(Database conn, TCPConnection tcpConnection) {

    }

}
