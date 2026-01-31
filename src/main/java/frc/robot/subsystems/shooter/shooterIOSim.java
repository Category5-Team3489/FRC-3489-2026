package frc.robot.subsystems.shooter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class shooterIOSim implements shooterIO {
    // Simulated motors
    private final DCMotorSim angleMotorSim;
    private final DCMotorSim shooterMotorSim;

    // PID Controllers for simulation
    private final PIDController anglePID;
    private final PIDController shooterPID;

    public shooterIOSim() {
        // Initialize simulated motors
        angleMotorSim = new DCMotorSim(DCMotor.getCIM(1), 1.0, 0.02);
        shooterMotorSim = new DCMotorSim(DCMotor.getCIM(2), 1.0, 0.02);

        // Initialize PID controllers
        anglePID = new PIDController(1.0, 0.0, 0.0);
        shooterPID = new PIDController(1.0, 0.0, 0.0);
    }

    @Override
    public void updateInputs(shooterIOInputs inputs) {
        inputs.topMotorCurrent = shooterMotorSim.getCurrentDrawAmps();
        inputs.shootAngle = angleMotorSim.getPositionRotations() * 360.0;
        inputs.bottomMotorCurrent = angleMotorSim.getCurrentDrawAmps();
        inputs.distanceToTarget = 0.0; // This would need a sensor to be implemented
    }

    @Override
    public void stopMotors() {
        shooterMotorSim.setInputVoltage(0);
        angleMotorSim.setInputVoltage(0);
    }  

    @Override
    public void shootBall(double speed) {
        double voltage = MathUtil.clamp(speed * 12.0, -12.0, 12.0);
        shooterMotorSim.setInputVoltage(voltage);
    }

    @Override
    public void setShootAngle(double degrees) {
        double targetRotations = degrees / 360.0;
        double currentRotations = angleMotorSim.getPositionRotations();
        double outputVoltage = anglePID.calculate(currentRotations, targetRotations);
        outputVoltage = MathUtil.clamp(outputVoltage, -12.0, 12.0);
        angleMotorSim.setInputVoltage(outputVoltage);
    }
}