package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;


@TeleOp(name="TeleOp")

public class ritateleop extends LinearOpMode{
    protected DcMotor lf;
    protected DcMotor lb;
    protected DcMotor rf;
    protected DcMotor rb;

    public void runOpMode() throws InterruptedException {
        //initializing motors
        lf=hardwareMap.dcMotor.get("lf");
        lb=hardwareMap.dcMotor.get("lb");
        rf=hardwareMap.dcMotor.get("rf");
        rb=hardwareMap.dcMotor.get("rb");

        waitForStart();

        while (opModeIsActive()) {
            lf.setPower(gamepad1.left_stick_y);
            lb.setPower(-gamepad1.left_stick_y);
            rf.setPower(gamepad1.left_stick_y);
            rb.setPower(-gamepad1.left_stick_y);
        }


    }
}

