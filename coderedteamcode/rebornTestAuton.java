package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Single Operation", group="reborn")
//@Disabled
public class rebornTestAuton extends rebornDriving{
    @Override
    public void runOpMode (){
        robot.init(hardwareMap);

        waitForStart();
        strafeMove(20, false);

    }
}
