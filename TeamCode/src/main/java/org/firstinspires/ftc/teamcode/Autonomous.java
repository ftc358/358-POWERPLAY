package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
public class Autonomous extends LinearOpMode {
    private static double col;
    private static double row;
    private static double width;
    private static double height;
    private static final String VUFORIA_KEY = "AX9Ef4T/////AAABmQ1E33Il6kCLgNHGUvIh1NUUcMgzpEU8XdP0cehzmSUTSjI3hmhSUbDqfUjP/2aA1Z7COoovNSxTBerLRCENWTSi8s8hbE7nVLldf8AZzpDymPGjs3SrlyCVood8TBIXdBkbP+u7tiYn30Sw7Iw9dHGk4I1imXgQjlOxOzZCsFv8JS2FVRaG8o9yhTOeyR4TNYKJmOOAwknEyU9j1EpZTa27wO3FdZexyyQTg0m+Cn1JJVC99FnLO9OjeLYQ5IMjrDJTrdSPn68T4YzTw776Zq3mwFgIwMl1FFLVkVORCneNdVfLEcRa5pzB6CILofXXX/rhjLjBKP1fCCKttYdhRu9hwQhgam/036uiC1/dRhiI";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);

        public void getposition(){
            if (opModeIsActive()) {
                while (opModeIsActive()) {
                    if (tfod != null) {
                        // getUpdatedRecognitions() will return null if no new information is available since
                        // the last time that call was made.
                        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                        if (updatedRecognitions != null) {
                            telemetry.addData("# Objects Detected", updatedRecognitions.size());

                            // step through the list of recognitions and display image position/size information for each one
                            // Note: "Image number" refers to the randomized image orientation/number
                            for (Recognition recognition : updatedRecognitions) {
                                col = (recognition.getLeft() + recognition.getRight()) / 2 ;
                                row = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                                width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                                height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;



                                telemetry.addData(""," ");
                                telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                                telemetry.addData("- Position (Row/Col)","%.0f / %.0f", row, col);
                                telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);
                            }
                            telemetry.update();
                        }

                    }
                }
            }
        }

}
