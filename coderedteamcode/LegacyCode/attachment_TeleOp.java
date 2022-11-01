package org.firstinspires.ftc.teamcode.LegacyCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Disabled
//@TeleOp(name="attachment_TeleOp")
public class attachment_TeleOp extends OpMode {
    mecanumHardware robot = new mecanumHardware();
    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        //driving code
        //if (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
        double pulleySpeed = .3;
        double slowModifier = .3;
        double r = Math.hypot(-gamepad1.left_stick_x, gamepad1.left_stick_y);

        double robotAngle = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
        double rightX = -gamepad1.right_stick_x;
        double v1 = r * Math.cos(robotAngle) + rightX;
        double v2 = r * Math.sin(robotAngle) - rightX;
        double v3 = r * Math.sin(robotAngle) + rightX;
        double v4 = r * Math.cos(robotAngle) - rightX;



        //we assign the power to the actual motors further down in the code, so that we are able to reduce the power using the left trigger.


        // attachment code
        telemetry.addLine();
        if (gamepad1.right_bumper) {
            robot.grabberServo.setPosition(mecanumHardware.grabber_max);
            telemetry.addData("Grabber Servo Position", mecanumHardware.grabber_max);
        }
        else if (gamepad1.left_bumper) {

            robot.grabberServo.setPosition(mecanumHardware.grabber_min);
            telemetry.addData("Grabber Servo Position", mecanumHardware.grabber_min);
        }
        else {
            telemetry.addData("Grabber Servo Position", "None");
        }

        if (gamepad1.y) {
            robot.carousel.setPower(0.5);
        }
        if (gamepad1.b) { 
            robot.carousel.setPower(0);
        }
        if (gamepad1.a) {
            robot.carousel.setPower(-0.5);
            telemetry.addData("Gamepad A:", true);
        }
        if (gamepad1.dpad_left) {
            robot.extenderServo.setPower(-1);
            telemetry.addData("Extender Power" , -1);
            telemetry.addData("DPAD: ", "Left");

        }
        else if (gamepad1.dpad_right) {
            robot.extenderServo.setPower(1);
            telemetry.addData("Extender Power" , 1);
            telemetry.addData("DPAD: ", "Right");
        }
        else {
            robot.extenderServo.setPower(0);
            telemetry.addData("Extender Power" , 0);
        }


        if (gamepad1.dpad_up) {
            robot.pulleyMotor0.setPower(pulleySpeed);
            robot.pulleyMotor1.setPower(pulleySpeed);

        } else if (gamepad1.dpad_down) {
            robot.pulleyMotor0.setPower(-pulleySpeed);
            robot.pulleyMotor1.setPower(-pulleySpeed);
        } else {
            robot.pulleyMotor0.setPower(0);
            robot.pulleyMotor1.setPower(0);
        }

        double newV1 = v1;
        double newV2 = v2;
        double newV3 = v3;
        double newV4 = v4;

        if (gamepad1.left_trigger>.1){
            newV1 = v1 * slowModifier;
            newV2 = v2 * slowModifier;
            newV3 = v3 * slowModifier;
            newV4 = v4 * slowModifier;

            telemetry.addData("Slow Mode", "ACTIVATE!!!!!");
        }

        //assign the power to the motors
        robot.frontLeft.setPower(-1 * newV1);
        robot.frontRight.setPower(-1 * newV2);
        robot.backLeft.setPower(-1 * newV3);
        robot.backRight.setPower(-1 * newV4);


        telemetry.addData("fLPower", -1 * newV1);
        telemetry.addData("fRPower", -1 * newV2);
        telemetry.addData("bLPower", -1 * newV3);
        telemetry.addData("bRPower", -1 * newV4);


        telemetry.addData("Encoder port 1 back left", robot.backLeft.getCurrentPosition());
        telemetry.addData("Encoder port 2 front right", robot.frontRight.getCurrentPosition());
        telemetry.addData("Encoder port 3 back right", robot.backRight.getCurrentPosition());
        telemetry.addData("Encoder port 4 back left", robot.backLeft.getCurrentPosition());

        telemetry.update();
    }
}
