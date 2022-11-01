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
@Autonomous(name="LEGACY - Red - Camera Duck Retrieval", group="Blue Auton")
public class redclosewow extends LinearOpMode {
    public int x;
    public int y;

    /* Declare OpMode members. */
    mecanumHardware robot = new mecanumHardware();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 3.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    final double DESIRED_DISTANCE = 8.0; //  this is how close the camera should get to the target (inches)
    //  The GAIN constants set the relationship between the measured position error,
    //  and how much power is applied to the drive motors.  Drive = Error * Gain
    //  Make these values smaller for smoother control.
    final double SPEED_GAIN =   0.02 ;   //  Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double TURN_GAIN  =   0.01 ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MM_PER_INCH = 25.40 ;   //  Metric conversion
    private static final String VUFORIA_KEY = "AU2ZVVD/////AAABmWSnWzlvdECDh0CawWRMh50kTOol0b5i6u8TJ9mYkhAojzXIoOAOVA7kFQAmVX8CWYdcpRjhQUnpcWViN2ckinEOTF1xTzWbTv6pqSuYaUgSwNKQUy1nipKxdEpTCv6BW+17wHICNqIJVCblf5CCvgg79QnDk1G503iGNlmz8a9wRZIYadFQzWBK7Ps/sWMliCnRgUe5KAVfWAs/K+0DzveH/Gq82hE9E1GIXusodMsZJzGlmQKEWcIgfzuWYzzlYdJpw6eNPSVIK5lisdkBfkzJbsWSIsOuKJOzJMwB894qq7OB/nMJCaWT3qseZamRZKm0wpgVng4x0gW/ZccKzl/4jDDeuJOa2K4MSfCDTPn+";
    VuforiaLocalizer vuforia    = null;
    OpenGLMatrix targetPose     = null;
    String targetName           = "";

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        //vuforia
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

        boolean targetFound     = false;    // Set to true when a target is detected by Vuforia
        double  targetRange     = 0;        // Distance from camera to target in Inches
        double  targetBearing   = 0;        // Robot Heading, relative to target.  Positive degrees means target is to the right.
        double  drive           = 0;        // Desired forward power (-1 to +1)
        double  turn            = 0;        // Desired turning power (-1 to +1)
        //vuforia stuff here

        //the below program assumes we start on blue team
        //drive horizontally towards wall, OR move forward to the wall
        //distance is how many inches you wanna go
        //ASSUMES BLUE TEAM CLOSE TO CAROUSEL, ITS FACING FORWARD TO CAROUSEL
        //strafe left a bit
        reset();
        //STRAFE LEFT TO CAROUSEL, FACING TEAM WALL.
        move(0.8,'f',20);
        reset();

        robot.carousel.setPower(-0.5);
        sleep(2500);
        robot.carousel.setPower(0);
        //drive forward OR to backward
        reset();

        move(1,'r',23);


        reset();
        targetFound = false;
        while( !targetFound ) {
            move(1, 'f', 2);
            reset();
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
            telemetry.addData(">","HOLD Left-Bumper to Drive to Target\n");
            telemetry.addData("Target", " %s", targetName);
            telemetry.addData("Range",  "%5.1f inches", targetRange);
            telemetry.addData("Bearing","%3.0f degrees", targetBearing);
        } else {
            telemetry.addData(">","Drive using joystick to find target\n");
        }
        telemetry.update();
        //move(1,'b',2);
        move(1,'f',7);
        reset();
        move(1,'y',targetRange);

        //move(1,'y',11);

         //move(1,
        // \'f',1500);

             //move(1,'3',targetRange);

//        reset();
//        //move(1,'y',15);
//        reset();
        //y = strafef eleft

        motorStop();
    }

    public void motorStop() {
        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backLeft.setPower(0);
        robot.backRight.setPower(0);
        robot.carousel.setPower(0);
        reset();
    }
    public void reset(){
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    public void move(double power, char direction, double distance) {
        double ticks = COUNTS_PER_INCH * distance/3;
//        double ticks = 7.5* distance;
        switch(direction){
            case 'f':
                //to go forward

                //set target position
                robot.frontLeft.setTargetPosition((int)ticks);
                robot.backLeft.setTargetPosition((int)(ticks));
                robot.frontRight.setTargetPosition((int)(ticks));
                robot.backRight.setTargetPosition((int)(ticks));
                //set run to position
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                //set drive power for forward
                robot.frontLeft.setPower(power);
                robot.frontRight.setPower(power);
                robot.backLeft.setPower(power);
                robot.backRight.setPower(power);

                while (robot.frontLeft.isBusy() && robot.backLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy())
                {

                }
                motorStop();
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                break;

            case 'b':
                //setting power of motors to go backward


                //set target position
                robot.frontLeft.setTargetPosition((int) -ticks);
                robot.backLeft.setTargetPosition((int) -ticks);
                robot.frontRight.setTargetPosition((int) -ticks);
                robot.backRight.setTargetPosition((int) -ticks);
                //set run to position
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                //set drive power for forward
                robot.frontLeft.setPower(-power);
                robot.frontRight.setPower(-power);
                robot.backLeft.setPower(-power);
                robot.backRight.setPower(-power);

                while (robot.frontLeft.isBusy() && robot.backLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy())
                {
                    telemetry.clear();
                    telemetry.addData("Front Left Pos", robot.frontLeft.getCurrentPosition());
                    telemetry.addData("Front Right Pos", robot.frontRight.getCurrentPosition());
                    telemetry.addData("Back Left Pos", robot.backLeft.getCurrentPosition());
                    telemetry.addData("Back Right (Mephistopheles) Pos", robot.backRight.getCurrentPosition());
                    telemetry.update();

                }
                motorStop();
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                break;

            case 'r':
                //to turn clockwise

            robot.frontLeft.setTargetPosition((int)ticks);
            robot.backLeft.setTargetPosition((int)ticks);
            robot.frontRight.setTargetPosition((int)-ticks);
            robot.backRight.setTargetPosition((int)-ticks);
            //set run to position
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //set drive power for forward
            robot.frontLeft.setPower(power);
            robot.frontRight.setPower(power);
            robot.backLeft.setPower(-power);
            robot.backRight.setPower(-power);

            while (robot.frontLeft.isBusy() && robot.backLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy())
            {

            }
            motorStop();
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            break;
            case 'l':
                // to turn counter clockwise

            robot.frontLeft.setTargetPosition((int)ticks);
            robot.backLeft.setTargetPosition((int) -ticks);
            robot.frontRight.setTargetPosition((int)-ticks);
            robot.backRight.setTargetPosition((int) ticks);
            //set run to position
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //set drive power for forward
            robot.frontLeft.setPower(-power);
            robot.frontRight.setPower(power);
            robot.backLeft.setPower(-power);
            robot.backRight.setPower(power);

            while (robot.frontLeft.isBusy() && robot.backLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy())
            {
                telemetry.clear();
                telemetry.addData("Front Left Pos", robot.frontLeft.getCurrentPosition());
                telemetry.addData("Front Right Pos", robot.frontRight.getCurrentPosition());
                telemetry.addData("Back Left Pos", robot.backLeft.getCurrentPosition());
                telemetry.addData("Back Right (Mephistopheles) Pos", robot.backRight.getCurrentPosition());
                telemetry.update();
            }
            motorStop();
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            break;
            case 'x':
                //to strafe right


            //set target position
            robot.frontLeft.setTargetPosition((int) ticks);
            robot.backLeft.setTargetPosition((int)-ticks);
            robot.frontRight.setTargetPosition((int)-ticks);
            robot.backRight.setTargetPosition((int) ticks);
            //set run to position
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //set drive power for forward
            robot.frontLeft.setPower(power);
            robot.frontRight.setPower(-power);
            robot.backLeft.setPower(-power);
            robot.backRight.setPower(power);

            while (robot.frontLeft.isBusy() && robot.backLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy())
            {

            }
            motorStop();
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            break;
            case 'y' :
                // to strafe left

            //set target position
            robot.frontLeft.setTargetPosition((int)-ticks);
            robot.backLeft.setTargetPosition((int)ticks);
            robot.frontRight.setTargetPosition((int)ticks);
            robot.backRight.setTargetPosition((int)-ticks);
            //set run to position
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //set drive power for forward
            robot.frontLeft.setPower(-power);
            robot.frontRight.setPower(power);
            robot.backLeft.setPower(power);
            robot.backRight.setPower(-power);

            while (robot.frontLeft.isBusy() && robot.backLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy())
            {

            }
            motorStop();
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            break;

            default:
                motorStop();
        }
    }
    public void diagonal(double power, char direction, long distance){
        double ticks = 1120/7.5 * distance;
        switch(direction) {
            case '1':
                //forward right

                //set target position

                robot.frontLeft.setTargetPosition((int) (ticks));
                robot.backLeft.setTargetPosition(0);
                robot.frontRight.setTargetPosition(0);
                robot.backRight.setTargetPosition((int) ticks);
                //set run to position
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                //set drive power for forward
                robot.frontLeft.setPower(power);
                robot.frontRight.setPower(0);
                robot.backLeft.setPower(0);
                robot.backRight.setPower(power);

                while (robot.frontLeft.isBusy() && robot.backLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy()) {

                }
                motorStop();
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                break;

            case '2':
                //forward left

                //set target position

                robot.frontLeft.setTargetPosition(0);
                robot.backLeft.setTargetPosition((int) ticks);
                robot.frontRight.setTargetPosition((int) ticks);
                robot.backRight.setTargetPosition(0);
                //set run to position
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                //set drive power for forward
                robot.frontLeft.setPower(0);
                robot.frontRight.setPower(power);
                robot.backLeft.setPower(power);
                robot.backRight.setPower(0);

                while (robot.frontLeft.isBusy() && robot.backLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy()) {

                }
                motorStop();
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


                break;
            case '3':
                // go back right

                robot.frontLeft.setTargetPosition(0);
                robot.backLeft.setTargetPosition((int) -ticks);
                robot.frontRight.setTargetPosition((int) -ticks);
                robot.backRight.setTargetPosition(0);
                //set run to position
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                //set drive power for forward
                robot.frontLeft.setPower(0);
                robot.frontRight.setPower(-power);
                robot.backLeft.setPower(-power);
                robot.backRight.setPower(0);

                while (robot.frontLeft.isBusy() && robot.backLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy()) {

                }
                motorStop();
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


                break;

            case '4':
//back left
                robot.frontLeft.setTargetPosition((int) -ticks);
                robot.backLeft.setTargetPosition(0);
                robot.frontRight.setTargetPosition(0);
                robot.backRight.setTargetPosition((int) -ticks);
                //set run to position
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                //set drive power for forward
                robot.frontLeft.setPower(-power);
                robot.frontRight.setPower(0);
                robot.backLeft.setPower(0);
                robot.backRight.setPower(-power);

                while (robot.frontLeft.isBusy() && robot.backLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy()) {

                }
                motorStop();
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


                break;
            default:
                motorStop();

            }
            }
        }

