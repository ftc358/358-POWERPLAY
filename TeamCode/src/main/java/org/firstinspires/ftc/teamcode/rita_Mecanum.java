package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;


@TeleOp

public class rita_Mecanum extends LinearOpMode{
    protected DcMotor motor1;
    protected DcMotor motor2;
    protected DcMotor motor3;
    protected DcMotor motor4;



    public void movedriveTrain(){
        double vertical = 0;
        double horizontal = 0;
        double pivot = 0;

        vertical=-gamepad1.left_stick_y;
        horizontal=gamepad1.left_stick_x;
        pivot=gamepad1.right_stick_x;

        motor1.setPower(pivot+(-vertical+horizontal));
        motor2.setPower(pivot+(-vertical-horizontal));
        motor3.setPower(pivot+(-vertical-horizontal));
        motor4.setPower(pivot+(-vertical+horizontal));

    }


    public void runOpMode() throws InterruptedException {
        //initializing motors
        motor1=hardwareMap.dcMotor.get("motor1");
        motor2=hardwareMap.dcMotor.get("motor2");
        motor3=hardwareMap.dcMotor.get("motor3");
        motor4=hardwareMap.dcMotor.get("motor4");

        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor2.setDirection(DcMotorSimple.Direction.REVERSE);

        }


    }
}


