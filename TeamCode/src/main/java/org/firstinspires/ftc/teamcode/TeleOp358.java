package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TeleOp358")

public class TeleOp358 extends LinearOpMode {

    Hardware358 robot = null;

    public void runOpMode() throws InterruptedException {
        // initializing motors
        robot.init();

        waitForStart();

        while (opModeIsActive()) {
            robot.lf.setPower(-gamepad1.left_stick_y);
            robot.lb.setPower(gamepad1.left_stick_y);
            robot.rf.setPower(gamepad1.left_stick_y);
            robot.rb.setPower(-gamepad1.left_stick_y);
        }


    }
}

