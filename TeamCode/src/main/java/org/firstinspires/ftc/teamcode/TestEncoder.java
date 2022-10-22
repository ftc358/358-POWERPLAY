//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//@Autonomous
//public class TestEncoder extends LinearOpMode {
//
//    private DcMotor motor1;
//
//
//    public void runOpMode() throws InterruptedException {
//        // initializing motors
//        motor1 = hardwareMap.dcMotor.get("motor1");
//
//        motor1.setDirection(DcMotor.Direction.FORWARD);
//
//        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        // Send telemetry message to indicate successful Encoder reset
//        telemetry.addData("Starting at",  "%7d :%7d",
//                motor1.getCurrentPosition());
//        telemetry.update();
//
//        waitForStart();
//
//        while (opModeIsActive()) {
//
//        }
//}
