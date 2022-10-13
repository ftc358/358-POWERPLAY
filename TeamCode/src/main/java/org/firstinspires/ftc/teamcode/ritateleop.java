package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;


@TeleOp

public class ritateleop extends LinearOpMode{
    protected DcMotor motor1;
    protected DcMotor motor2;
    protected DcMotor motor3;
    protected DcMotor motor4;

    public void runOpMode() throws InterruptedException {
        //initializing motors
        motor1=hardwareMap.dcMotor.get("motor1");
        motor2=hardwareMap.dcMotor.get("motor2");
        motor3=hardwareMap.dcMotor.get("motor3");
        motor4=hardwareMap.dcMotor.get("motor4");

        waitForStart();


        while (opModeIsActive()) {
            motor1.setPower(gamepad1.left_stick_y);
            motor3.setPower(gamepad1.left_stick_y);
            motor2.setPower(-gamepad1.left_stick_y);
            motor4.setPower(-gamepad1.left_stick_y);
        }


    }
}

