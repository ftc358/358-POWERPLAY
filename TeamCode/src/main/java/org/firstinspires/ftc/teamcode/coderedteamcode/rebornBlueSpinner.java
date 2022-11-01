/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.LegacyCode.mecanumHardware;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

//@Disabled
@Autonomous(name="Blue - Duck Spinner", group="Reborn Blue")

//NEED TO CORRECT THE MOVEMENT AND ORIENTATION BEFORE RUNNING. IT YEETS ITSELF FORWARD WHEN RUN
public class rebornBlueSpinner extends rebornDriving {
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
    VuforiaLocalizer vuforia = null;
    OpenGLMatrix targetPose = null;
    String targetName = "";

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        telemetry.addData("orientation", "Front toward caroselrebornBlueSpinner. Second tile");
        telemetry.update();
        //====================================
        //-----------VUFORIA INIT-------------
        //====================================

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        // Turn off Extended tracking.  Set this true if you want Vuforia to track beyond the target.
        parameters.useExtendedTracking = false;

        // Connect to the camera we are to use.  This name must match what is set up in Robot Configuration
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the trackable objects from the Assets file, and give them meaningful names
        VuforiaTrackables targetsFreightFrenzy = this.vuforia.loadTrackablesFromAsset("FreightFrenzy");
        targetsFreightFrenzy.get(0).setName("Blue Storage");
        targetsFreightFrenzy.get(1).setName("Blue Alliance Wall");
        targetsFreightFrenzy.get(2).setName("Red Storage");
        targetsFreightFrenzy.get(3).setName("Red Alliance Wall");

        // Start tracking targets in the background
        targetsFreightFrenzy.activate();



        waitForStart();

        boolean targetFound = false;    // Set to true when a target is detected by Vuforia
        double targetRange = 0;        // Distance from camera to target in Inches
        double targetBearing = 0;        // Robot Heading, relative to target.  Positive degrees means target is to the right.
        double drive = 0;        // Desired forward power (-1 to +1)
        double turn = 0;        // Desired turning power (-1 to +1)
        //vuforia stuff here


        //=============================
        //--------ACTUAL PROGRAM-------
        //=============================


        motorStop();

        move(.6, 'l', 3);
        //STRAFE LEFT TO CAROUSEL, FACING TEAM WALL.
        move(0.3, 'f', 17);
        sleep(150);
        move(.05, 'f', 1);

        //spin the duck
        robot.duckSpinner.setPower(-.1);
        sleep(500);
//        move(.1, 'f', 2);
        sleep(1500);
        robot.duckSpinner.setPower(0);
        motorStop();

        move(.6, 'b', 10);
        move(.6, 'l', 10);

        //move right
        rotate(1, 'l', 100);

        //continue until the image is seen
        targetFound = false;
        while (!targetFound) {
            //move a little each time the image is not seen
            move(.6, 'f', 2);

            //looks for the image
            for (VuforiaTrackable trackable : targetsFreightFrenzy) {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    targetPose = ((VuforiaTrackableDefaultListener) trackable.getListener()).getVuforiaCameraFromTarget();
                    //MOVE BACKWARD LOOKING FOR THE PICTURE
                    // if we have a target, process the "pose" to determine the position of the target relative to the robot.
                    if (targetPose != null) {
                        targetFound = true;
                        targetName = trackable.getName();
                        VectorF trans = targetPose.getTranslation();

                        // Extract the X & Y components of the offset of the target relative to the robot
                        double targetX = trans.get(0) / MM_PER_INCH; // Image X axis
                        double targetY = trans.get(2) / MM_PER_INCH; // Image Z axis

                        // target range is based on distance from robot position to origin (right triangle).
                        targetRange = Math.hypot(targetX, targetY);

                        // target bearing is based on angle formed between the X axis and the target range line
                        targetBearing = Math.toDegrees(Math.asin(targetX / targetRange));

                        break;  // jump out of target tracking loop if we find a target.
                    }
                }
            }
        }

        // Tell the driver what we see, and what to do.
        if (targetFound) {
            telemetry.addData(">", "HOLD Left-Bumper to Drive to Target\n");
            telemetry.addData("Target", " %s", targetName);
            telemetry.addData("Range", "%5.1f inches", targetRange);
            telemetry.addData("Bearing", "%3.0f degrees", targetBearing);
        } else {
            telemetry.addData(">", "Drive using joystick to find target\n");
        }
        telemetry.update();
        //move(1,'b',2);
//        move(1, 'r', 7);
        reset();
        move(1, 'r', targetRange-.5);


    }
}

