package org.firstinspires.ftc.teamcode.LegacyCode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

// Hardware class for the Greenhill 3 testbot

public class mecanumHardware
{
    /* Public OpMode members. */
    public DcMotor  frontLeft   = null, frontRight  = null, backLeft   = null, backRight  = null;
    public DcMotor  pulleyMotor0 = null, pulleyMotor1=null, carousel = null;
    public CRServo extenderServo = null;
    public Servo grabberServo = null;

    public static final double grabber_min = 0;
    public static final double grabber_max = 0.75;

    /* Constructor */
    public mecanumHardware(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap hwMap) {
        // Define and Initialize Devices
        frontLeft  = hwMap.get(DcMotor.class, "front_left");
        frontRight = hwMap.get(DcMotor.class, "front_right");
        backLeft   = hwMap.get(DcMotor.class, "back_left");
        backRight  = hwMap.get(DcMotor.class, "back_right");

        pulleyMotor1  = hwMap.get(DcMotor.class, "pulley1");
        pulleyMotor0  = hwMap.get(DcMotor.class, "pulley0");
        carousel = hwMap.get(DcMotor.class, "motor_carousel");
        extenderServo = hwMap.get(CRServo.class,"extender"); //3 port

        grabberServo = hwMap.get(Servo.class, "grabber"); //2 port on expansion

        // Set Direction
        frontLeft.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        frontRight.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        backLeft.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        backRight.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        pulleyMotor1.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        pulleyMotor0.setDirection(DcMotor.Direction.REVERSE);// Set to REVERSE if using AndyMark motors
        carousel.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        pulleyMotor1.setPower(0);
        pulleyMotor0.setPower(0);
        carousel.setPower(0);
        extenderServo.setPower(0);
        // RUN TO POSITION
        // Set all motors to run with encoders if applicable.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pulleyMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pulleyMotor0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        carousel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // Set servo to min position
        grabberServo.setPosition(grabber_min);



    }
}

