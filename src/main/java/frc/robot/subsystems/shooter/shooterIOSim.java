package frc.robot.subsystems.shooter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import java.util.function.DoubleSupplier;

public class shooterIOSim implements shooterIO {
  private static final double ANGLE_GEAR_RATIO = 6.0;
  // Simulated motors
  private final DCMotorSim angleMotorSim;
  private final DCMotorSim shooterMotorSim;
  private double hoodCalibrationOffsetDeg = 0.0;

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
  public void setShootVoltageSupp(DoubleSupplier why) {
    shooterMotorSim.setInputVoltage(why.getAsDouble());
  }

  @Override
  public void setHoodSpeed(double speed) {
    angleMotorSim.setInputVoltage(speed * 12);
  }

  // Local dashboard visualization (do not include in AutoLog inputs)
  private final Mechanism2d turnMechanism = new Mechanism2d(1, 1);
  private final MechanismRoot2d root = turnMechanism.getRoot("shooter root", 0, 0);
  private final MechanismLigament2d shooterTurn =
      root.append(new MechanismLigament2d("shooter direction", 1, 0));

  @Override
  public void updateInputs(shooterIOInputs inputs) {
    // Advance sim by one timestep (20ms) then read values
    shooterMotorSim.update(0.02);
    angleMotorSim.update(0.02);

    inputs.topMotorCurrent = shooterMotorSim.getCurrentDrawAmps();
    // convert radians to degrees for shooter angle
    inputs.gearRatio = ANGLE_GEAR_RATIO;
    inputs.shootAngle =
        Math.toDegrees(angleMotorSim.getAngularPositionRad()) + hoodCalibrationOffsetDeg;
    inputs.bottomMotorCurrent = angleMotorSim.getCurrentDrawAmps();
    inputs.distanceToTarget = 0.0; // This would need a sensor to be implemented
    shooterTurn.setAngle(inputs.shootAngle);
  }

  @Override
  public void setShooterVoltage(double volts) {
    shooterMotorSim.setInputVoltage(volts);
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
    double targetRad = Math.toRadians(degrees - hoodCalibrationOffsetDeg);
    double currentRad = angleMotorSim.getAngularPositionRad();
    double outputVoltage = anglePID.calculate(currentRad, targetRad);
    outputVoltage = MathUtil.clamp(outputVoltage, -12.0, 12.0);
    angleMotorSim.setInputVoltage(outputVoltage);
  }

  @Override
  public void zeroHoodCalibration() {
    hoodCalibrationOffsetDeg = -Math.toDegrees(angleMotorSim.getAngularPositionRad());
  }

  @Override
  public void addHoodCalibrationOffset(double deltaDegrees) {
    hoodCalibrationOffsetDeg += deltaDegrees;
  }

  @Override
  public double getHoodCalibrationOffset() {
    return hoodCalibrationOffsetDeg;
  }
}
