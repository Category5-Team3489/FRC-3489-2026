package frc.robot.subsystems.shooter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import java.util.function.DoubleSupplier;

public class shooterIOSim implements shooterIO {
  private final DCMotorSim shooterMotorSim;
  private double appliedVolts = 0.0;

  public shooterIOSim() {
    shooterMotorSim =
        new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getCIM(2), 1.0, 0.02), DCMotor.getCIM(2));
  }

  @Override
  public void setShootVoltageSupp(DoubleSupplier why) {
    setShooterVoltage(why.getAsDouble());
  }

  @Override
  public void updateInputs(shooterIOInputs inputs) {
    shooterMotorSim.update(0.02);
    inputs.topMotorCurrent = Math.abs(shooterMotorSim.getCurrentDrawAmps());
    inputs.bottomMotorCurrent = Math.abs(shooterMotorSim.getCurrentDrawAmps());
    inputs.topVelocityRotationsPerSecond = shooterMotorSim.getAngularVelocityRPM() / 60.0;
    inputs.bottomVelocityRotationsPerSecond = shooterMotorSim.getAngularVelocityRPM() / 60.0;
    inputs.appliedVolts = appliedVolts;
    inputs.distanceToTarget = 0.0;
  }

  @Override
  public void setShooterVoltage(double volts) {
    appliedVolts = MathUtil.clamp(volts, -12.0, 12.0);
    shooterMotorSim.setInputVoltage(appliedVolts);
  }

  @Override
  public void stopMotors() {
    setShooterVoltage(0.0);
  }

  @Override
  public void shootBall(double speed) {
    setShooterVoltage(speed * 12.0);
  }
}
