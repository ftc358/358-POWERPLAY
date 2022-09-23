package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
public class danny_xdrive extends LinearOpMode{

    protected DcMotor motor1;
    protected DcMotor motor2;
    protected DcMotor motor3;
    protected DcMotor motor4;


    public void runOpMode() throws InterruptedException {
        //initializing motors/servos
        motor1 = hardwareMap.dcMotor.get("motor1");
        motor2 = hardwareMap.dcMotor.get("motor2");
        motor3 = hardwareMap.dcMotor.get("motor3");
        motor4 = hardwareMap.dcMotor.get("motor4");

        motor1.setDirection(DcMotor.Direction.REVERSE);
        motor2.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            //foward back left right
            motor1.setPower(gamepad1.left_stick_y);
            motor3.setPower(gamepad1.left_stick_y);
            motor2.setPower(-gamepad1.right_stick_y);
            motor4.setPower(-gamepad1.right_stick_y);


        }
    }
}