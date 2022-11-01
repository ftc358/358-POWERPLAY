package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Any - Warehouse Yeet", group="Reborn Neutral")
//@Disabled
public class rebornWarehouse extends rebornDriving{
    @Override
    public void runOpMode (){
        robot.init(hardwareMap);

        telemetry.addData("Orientation", "Back towards warehouse. Close to warehouse");

        waitForStart();

        motorStop();

        move(.2, 'b', 36);
        motorStop();

    }
}
