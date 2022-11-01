package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import java.util.List;

@Autonomous(name="Blue - Duck Detect and Place", group="Reborn Blue")
public class rebornBlueDuck extends rebornDriving {
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    public int x;
    public int y;

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    final double DESIRED_DISTANCE = 8.0; //  this is how close the camera should get to the target (inches)
    //  The GAIN constants set the relationship between the measured position error,
    //  and how much power is applied to the drive motors.  Drive = Error * Gain
    //  Make these values smaller for smoother control.
    final double SPEED_GAIN = 0.02;   //  Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double TURN_GAIN = 0.01;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)
    final double MM_PER_INCH = 25.40;   //  Metric conversion
    private static final String VUFORIA_KEY = "AU2ZVVD/////AAABmWSnWzlvdECDh0CawWRMh50kTOol0b5i6u8TJ9mYkhAojzXIoOAOVA7kFQAmVX8CWYdcpRjhQUnpcWViN2ckinEOTF1xTzWbTv6pqSuYaUgSwNKQUy1nipKxdEpTCv6BW+17wHICNqIJVCblf5CCvgg79QnDk1G503iGNlmz8a9wRZIYadFQzWBK7Ps/sWMliCnRgUe5KAVfWAs/K+0DzveH/Gq82hE9E1GIXusodMsZJzGlmQKEWcIgfzuWYzzlYdJpw6eNPSVIK5lisdkBfkzJbsWSIsOuKJOzJMwB894qq7OB/nMJCaWT3qseZamRZKm0wpgVng4x0gW/ZccKzl/4jDDeuJOa2K4MSfCDTPn+";
    OpenGLMatrix targetPose = null;
    String targetName = "";
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
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        initVuforia();
        initTfod();
        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1, 32.0/9.0);
        }
        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();
        //=======================
        //--actual code----------
        //=======================--------------------------------------------------------
        int duckLevel = duckDetection();
        telemetry.addData("Duck Pos", duckLevel);
        telemetry.update();
        telemetry.addData("Action", "Initial Move");
        telemetry.update();
        strafeMove(40, false);
        sleep(2000);
        int lateralMoveDistance = 25;
        int angleAmount = 95;
        switch(duckLevel){//moves to the alliance shipping hub based on what it reads
            case (1)://Warehouse close. Scoring level 1. Bottom
                levelLift('l');
                rotate(.5, 'r', angleAmount);
                move(.6, 'l', lateralMoveDistance-2);
                distanceMove(20, false);
                break;
            case (2)://Mid. Scoring level 2. Mid
                levelLift('m');
                rotate(.5, 'r', angleAmount);
                move(.6, 'l', lateralMoveDistance);
                distanceMove(25, false);
                break;
            case (3)://warehouse far. scoring level 3 top
                levelLift('t');
                rotate(.5, 'r', angleAmount);
                move(.6, 'l', lateralMoveDistance+2);
                distanceMove(45, false);
                break;
            default:
                break;
        }
        robot.clawServo.setPosition(0.5);
        sleep(200);
        robot.clawServo.setPosition(.15);
        sleep(200);
        robot.clawServo.setPosition(.5);
        move(.3, 'b', 10);
        //move(0.5,'b',10);
        if (duckLevel!=3) {
            levelLift('t');
        }
        rotate(0.5, 'r', 90);
        move(.3, 'l', 4);
        //this section is specifically to deliver ducks.
        move(0.4,'f',45);
        move (.2, 'r', 5);//untested
        robot.duckSpinner.setPower(-.1);
        sleep(2000);
        robot.duckSpinner.setPower(0);
        move(.3, 'b', 5);

        rotate(0.5, 'l',100);

        distanceMove(65, false);

        move(0.4,'r',10);
}
    private int duckDetection (){
        long programTime = System.currentTimeMillis();
        long waitTime = 5000L;
        while (opModeIsActive()) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Duck Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        if (recognition.getLabel().equalsIgnoreCase("Duck")) {
                            if (recognition.getLeft() >250.0) {
                                telemetry.addData("Duck Location", "Warehouse Far");
                                return 3;
                            } else if (recognition.getLeft() < 250.0) {
                                telemetry.addData("Duck Location", "Middle");
                                return 2;
                            }
                        }
                        i++;
                   }
                    telemetry.update();
                }
                else {
                    telemetry.addData("Duck Location", "Warehouse Close");
                    telemetry.addData("Target Time", programTime + waitTime);
                    telemetry.addData("Current Time", System.currentTimeMillis());
                    telemetry.update();
                    if (programTime + waitTime < System.currentTimeMillis()){
                        return 1;
                    }
                }
            }
        }
        return -1;
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}

