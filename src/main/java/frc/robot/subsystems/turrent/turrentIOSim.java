package frc.robot.subsystems.turrent;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class turrentIOSim implements turrentIO {
    private final turrentIOInputs inputs = new turrentIOInputs();
    private DCMotorSim topMotor;
    private final PIDController anglePID = new PIDController(1.0, 0.0, 0.0);

    public turrentIOSim(double gearRatio) {
    

        // Simulate a motor with 1 CIM motor and a gear ratio of 1:1
        topMotor =
            new DCMotorSim(
                LinearSystemId.createDCMotorSystem(DCMotor.getCIM(1), 1.0, gearRatio), DCMotor.getCIM(1));
    }

    @Override
    public void updateInputs(turrentIOInputs inputs) {
        // Simulate some values for testing
        inputs.topMotorCurrent = 1.5; // Simulated current
        inputs.turrentAngle = 45.0; // Simulated angle
        inputs.gearRatio = 1.0; // Simulated gear ratio
    }

    @Override
    public void setTurrentAngle(double degrees) {
        double targetRad = Math.toRadians(degrees);
        double currentRad = topMotor.getAngularPositionRad();
        double outputVoltage = anglePID.calculate(currentRad, targetRad);
        outputVoltage = MathUtil.clamp(outputVoltage, -12.0, 12.0);
        topMotor.setInputVoltage(outputVoltage);
    }

}
