package com.example.javafx_vibe.javafx_vibe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.CSVWriter;

import java.net.URL;

import java.util.ResourceBundle;

import com.fazecast.jSerialComm.SerialPort;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.io.*;





public class MainController implements Initializable
{
    private static boolean stopFlag = false;
    @FXML
    private MenuItem Exit;
    private static SerialPort comPort;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private Button btnChart;


    @FXML
    private Spinner<Integer> time;
    @FXML
    private Spinner<Integer> intensity;
    private int currentTimeValue;
    private int currentIntensityValue;

//    Mac version
//    protected static SerialPort macArduinoPort = SerialPort.getCommPort("/dev/tty.usbserial-1130");
//    protected static SerialPort macArduinoPort = SerialPort.getCommPort("/dev/tty.usbmodem11301");

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        setComPort(SerialPort.getCommPort("/dev/tty.usbmodem11301"));
        SpinnerValueFactory<Integer> timeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 240);
        timeValueFactory.setValue(0);
        time.setValueFactory(timeValueFactory);
        time.valueProperty().addListener((observableValue, integer, t1) -> setCurrentTimeValue(time.getValue()));
        SpinnerValueFactory<Integer> intensityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        timeValueFactory.setValue(0);
        intensity.setValueFactory(intensityValueFactory);
        intensity.valueProperty().addListener((observableValue, integer, t1) -> setCurrentIntensityValue(intensity.getValue()));
        comPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0,0 );
        comPort.openPort();
//        setComPort(SerialPort.getCommPort("/dev/tty.usbmodem11301"));
//        SpinnerValueFactory<Integer> timeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120);
//        timeValueFactory.setValue(0);
//        time.setValueFactory(timeValueFactory);
//        time.valueProperty().addListener((observableValue, integer, t1) -> setCurrentTimeValue(time.getValue()));
//        SpinnerValueFactory<Integer> intensityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
//        timeValueFactory.setValue(0);
//        intensity.setValueFactory(intensityValueFactory);
//        intensity.valueProperty().addListener((observableValue, integer, t1) -> setCurrentIntensityValue(intensity.getValue()));
//        comPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
//        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0,0 );
//        comPort.openPort();
    }


    @FXML
    void handle_btnStart(ActionEvent event) throws IOException {
        handleMotorStart();
        SerialPort comPort = ArduinoUtils.findArduinoPort();
        setComPort(comPort);

        String filePath = "accelerometer_data.csv";
        CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath));

        long startTime = System.currentTimeMillis();

        Thread dataThread = new Thread(() -> {
            try {
                InputStream inputStream = comPort.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while (!stopFlag) {
                    // Wait for a small delay before reading the next line of data
                    Thread.sleep(33); // Adjust the delay value as needed
                    // Process the line of data (e.g., split it into x, y, z values)
                    var line = bufferedReader.readLine();
                    System.out.println(line);
                    long currentTime = System.currentTimeMillis();
                    double elapsedTime = (double) (currentTime - startTime) / 1000;
                    try {
                        var accelerationData = AccelerationData.from_arduino(line, elapsedTime);
                        csvWriter.writeNext(accelerationData.toCsvStrings());
                    } catch (Exception e) {
                        System.out.println("Failed to parse arduino data: " + e);
                    }
                }
                inputStream.close();
                inputStreamReader.close();
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        dataThread.start();
    }

    @FXML
    void handle_btnStop(ActionEvent event) throws IOException {
        System.out.println("\nStop button clicked");
        stopFlag = true;

        OutputStream outputStream1 = comPort.getOutputStream();
        String customOutput = "t";
//        String output = "s:3:1";
        outputStream1.write(customOutput.getBytes());
        outputStream1.flush();
    }

    @FXML
    void handleMotorStart() throws IOException
    {
        String timeStr = Integer.toString(currentTimeValue);
        String intensityStr = Integer.toString(currentIntensityValue);
        System.out.println(timeStr);
        System.out.println(intensityStr);
        OutputStream outputStream1 = comPort.getOutputStream();
        String customOutput = "\"s:" + timeStr + ":" + intensityStr + "\"";
//        String output = "s:3:1";
        System.out.println(customOutput);
        outputStream1.write(customOutput.getBytes());
        outputStream1.flush();
    }


    void setComPort(SerialPort comPort){
        this.comPort = comPort;
    }

    public static void closeComPort(){
        comPort.closePort();

    }

    public int getCurrentTimeValue()
    {
        return currentTimeValue;
    }

    public void setCurrentTimeValue(int currentTimeValue)
    {
        this.currentTimeValue = currentTimeValue;
    }

    public int getCurrentIntensityValue()
    {
        return currentIntensityValue;
    }

    public void setCurrentIntensityValue(int currentIntensityValue)
    {
        this.currentIntensityValue = currentIntensityValue;
    }
}




