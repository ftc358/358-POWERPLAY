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

package org.firstinspires.ftc.teamcode.LegacyCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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
@Disabled
//@Autonomous(name="Red - Far Duck Retrieval", group="Red Auton")
public class redfar extends LinearOpMode {
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

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();

        //vuforia stuff here

        //the below program assumes we start on blue team
        //drive horizontally towards wall, OR move forward to the wall
        //distance is how many inches you wanna go
        //ASSUMES BLUE TEAM CLOSE TO CAROUSEL, ITS FACING FORWARD TO CAROUSEL
        //strafe left a bit


        //move(1,'y',11);
        reset();

        //GO BACKWARDS TO WAREHOUSE. FACING TEAM CAROUSEL
        move(1,'b',36);
        reset();

        reset();


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
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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

                }
                motorStop();
                robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                break;

            case 'r':
                //to go right

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
                // to go left

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
                // go back to the right

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

