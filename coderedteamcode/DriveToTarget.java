package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

@Autonomous (name = "driveToTarget")
public class DriveToTarget extends rebornDriving {

    private static final String VUFORIA_KEY = "AU2ZVVD/////AAABmWSnWzlvdECDh0CawWRMh50kTOol0b5i6u8TJ9mYkhAojzXIoOAOVA7kFQAmVX8CWYdcpRjhQUnpcWViN2ckinEOTF1xTzWbTv6pqSuYaUgSwNKQUy1nipKxdEpTCv6BW+17wHICNqIJVCblf5CCvgg79QnDk1G503iGNlmz8a9wRZIYadFQzWBK7Ps/sWMliCnRgUe5KAVfWAs/K+0DzveH/Gq82hE9E1GIXusodMsZJzGlmQKEWcIgfzuWYzzlYdJpw6eNPSVIK5lisdkBfkzJbsWSIsOuKJOzJMwB894qq7OB/nMJCaWT3qseZamRZKm0wpgVng4x0gW/ZccKzl/4jDDeuJOa2K4MSfCDTPn+";
    private static final float mmPerInch        = 25.4f;
    private static final float mmTargetHeight   = 6 * mmPerInch;          // the height of the center of the target image above the floor
    private static final float halfField        = 72 * mmPerInch;
    private static final float halfTile         = 12 * mmPerInch;
    private static final float oneAndHalfTile   = 36 * mmPerInch;

    // Class Members
    private OpenGLMatrix lastLocation   = null;
    private VuforiaLocalizer vuforia    = null;
    private VuforiaTrackables targets   = null ;
    private WebcamName webcamName       = null;

    private boolean targetVisible       = false;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        // We also indicate which camera we wish to use.
        parameters.cameraName = webcamName;

        // Turn off Extended tracking.  Set this true if you want Vuforia to track beyond the target.
        parameters.useExtendedTracking = false;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        //get information from FreightFrenzy list
        targets = this.vuforia.loadTrackablesFromAsset("FreightFrenzy");
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targets);
    }
}
