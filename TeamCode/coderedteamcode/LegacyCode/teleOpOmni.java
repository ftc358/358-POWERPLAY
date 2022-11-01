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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.LegacyCode.omni;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

//@TeleOp(name="teleOpOmni", group="teleop")
@Disabled
public class teleOpOmni extends OpMode {

    omni robot = new omni();
    // Declare OpMode members.
    private float drive = 1f;
    //private float BRDrive = 1f;

    @Override
    public void init()
    {
        //Initialize the hardware variables.
        //The init() method of the hardware class does all the work here
        robot.init(hardwareMap);

    }

    @Override
    public void loop()

    {
        if (gamepad1.left_stick_y > 0.4){
            robot.motor2.setPower(drive);
            robot.motor3.setPower(drive);
        }
        else if (gamepad1.left_stick_y < -0.4){
            robot.motor2.setPower(-drive);
            robot.motor3.setPower(-drive);
        }
        else
        {
            robot.motor2.setPower(0);
            robot.motor3.setPower(0);
        }

        if (gamepad1.left_stick_x > 0.4){
            robot.motor0.setPower(drive);
            robot.motor1.setPower(drive);
        }
        else if (gamepad1.left_stick_x < -0.4){
            robot.motor0.setPower(-drive);
            robot.motor1.setPower(-drive);
        }
        else {
            robot.motor0.setPower(0);
            robot.motor1.setPower(0);
        }
      //  mecanumMove();
    }

    /* public void mecanumMove()
    {
        //variableswrw3
        double r = Math.hypot(-gamepad1.left_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
        double rightX = -gamepad1.right_stick_x;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        robot.frontLeft.setPower(-drive * v1);
        robot.frontRight.setPower(-drive * v2);
        robot.backLeft.setPower(-drive * v3);
        robot.backRight.setPower(-drive * v4);

        telemetry.addData("fLPower", -drive * v1);
        telemetry.addData("fRPower", -drive * v2);
        telemetry.addData("bLPower", -drive * v3);
        telemetry.addData("bRPower", -drive * v4);

        telemetry.addData("Encoder port 1 back left",  robot.backLeft.getCurrentPosition());
        telemetry.addData("Encoder port 2 front right", robot.frontRight.getCurrentPosition());
        telemetry.addData("Encoder port 3 back right", robot.backRight.getCurrentPosition());
        telemetry.addData("Encoder port 4 back left", robot.backLeft.getCurrentPosition());


        telemetry.update();


    }*/
}
