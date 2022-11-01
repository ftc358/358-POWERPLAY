package org.firstinspires.ftc.teamcode;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="test_Forward", group = "reborn")
public class rebornTestForward extends OpMode {
    rebornHardware robot = new rebornHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        //driving code
        //if (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
       // double pulleySpeed = .3;
        //double slowModifier = .3;
        //double r = Math.hypot(-gamepad1.left_stick_x, gamepad1.left_stick_y);

        //double robotAngle = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
        //double rightX = -gamepad1.right_stick_x;
        //double v1 = r * Math.cos(robotAngle) + rightX;
       // double v2 = r * Math.sin(robotAngle) - rightX;
       // double v3 = r * Math.sin(robotAngle) + rightX;
       // double v4 = r * Math.cos(robotAngle) - rightX;

        robot.frontLeft.setPower(1);
        robot.frontRight.setPower(1);
        robot.backLeft.setPower(1);
        robot.backRight.setPower(1);


        //we assign the power to the actual motors further down in the code, so that we are able to reduce the power using the left trigger.
    }


}
