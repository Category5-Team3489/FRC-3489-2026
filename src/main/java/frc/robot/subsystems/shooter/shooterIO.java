package frc.robot.subsystems.shooter;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class shooterIO extends SubsystemBase{
    private final Inputs inputs = new Inputs();
    private final int index = 0;

    private static class Inputs {
        @Override
        public String toString() {
            return "Inputs{}";
        }
    }

    private void updateInputs(Inputs inputs) {
        // Populate inputs from hardware here.
    }

    @Override
    public void periodic() {
        updateInputs(inputs);
        // Replace or reintroduce your logger here; using System.out.println for a safe default.
        System.out.println("Drive/Module" + Integer.toString(index) + " " + inputs);
    }
}

