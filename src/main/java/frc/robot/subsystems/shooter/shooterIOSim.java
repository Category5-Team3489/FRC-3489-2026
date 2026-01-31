package frc.robot.subsystems.shooter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class shooterIOSim implements shooterIO {
  // Simulated motors
  private final DCMotorSim angleMotorSim;
  private final DCMotorSim shooterMotorSim;

  // PID Controllers for simulation
  private final PIDController anglePID;
  private final PIDController shooterPID;

  public shooterIOSim() {
    // Initialize simulated motors
    angleMotorSim =
        new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getCIM(1), 1.0, 0.02), DCMotor.getCIM(1));
    shooterMotorSim =
        new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getCIM(2), 1.0, 0.02), DCMotor.getCIM(2));

    // Initialize PID controllers
    anglePID = new PIDController(1.0, 0.0, 0.0);
    shooterPID = new PIDController(1.0, 0.0, 0.0);
  }

  @Override
  public void updateInputs(shooterIOInputs inputs) {
    // Advance sim by one timestep (20ms) then read values
    shooterMotorSim.update(0.02);
    angleMotorSim.update(0.02);

    inputs.topMotorCurrent = shooterMotorSim.getCurrentDrawAmps();
    // convert radians to degrees for shooter angle
    inputs.shootAngle = Math.toDegrees(angleMotorSim.getAngularPositionRad());
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
    double targetRad = Math.toRadians(degrees);
    double currentRad = angleMotorSim.getAngularPositionRad();
    double outputVoltage = anglePID.calculate(currentRad, targetRad);
    outputVoltage = MathUtil.clamp(outputVoltage, -12.0, 12.0);
    angleMotorSim.setInputVoltage(outputVoltage);
  }
}
