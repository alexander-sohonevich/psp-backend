package MainApplication;

import Entities.Car;
import Server.Database;
import Server.TCPConnection;
import com.google.gson.Gson;

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
                case "UPDATE CAR":
                    updateCar(conn, tcpConnection);
                    break;
                case "ORDER CAR":
                    orderCar(conn, tcpConnection);
                    break;
            }

        }
    }

    private static void addCar(Database conn, TCPConnection tcpConnection) {
        Gson gson = new Gson();

        Car car = gson.fromJson(tcpConnection.receiveString(), Car.class);

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
            case "ORDERED":
                jsonArray = car.getFilteredCars(conn, "Заказан");
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

    private static void updateCar(Database conn, TCPConnection tcpConnection) {
        Gson gson = new Gson();
        Car car = gson.fromJson(tcpConnection.receiveString(), Car.class);
        String updateTable = tcpConnection.receiveString();
        String result = car.updateCar(conn, updateTable);
        tcpConnection.sendString(result);
    }

    private static void orderCar(Database conn, TCPConnection tcpConnection) {
        Car car = new Car();

        String result = car.orderCar(conn, tcpConnection.receiveString());

        tcpConnection.sendString(result);
    }

}
