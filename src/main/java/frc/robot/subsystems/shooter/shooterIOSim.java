package frc.robot.subsystems.shooter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import java.util.function.DoubleSupplier;

public class shooterIOSim implements shooterIO {
  private final DCMotorSim shooterMotorSim;

  public shooterIOSim() {
    shooterMotorSim =
        new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getCIM(2), 1.0, 0.02), DCMotor.getCIM(2));
  }

  @Override
  public void setShootVoltageSupp(DoubleSupplier why) {
    shooterMotorSim.setInputVoltage(why.getAsDouble());
  }

  @Override
  public void updateInputs(shooterIOInputs inputs) {
    shooterMotorSim.update(0.02);
    inputs.topMotorCurrent = shooterMotorSim.getCurrentDrawAmps();
    inputs.bottomMotorCurrent = shooterMotorSim.getCurrentDrawAmps();
    inputs.distanceToTarget = 0.0;
  }

  @Override
  public void setShooterVoltage(double volts) {
    shooterMotorSim.setInputVoltage(volts);
  }

  @Override
  public void stopMotors() {
    shooterMotorSim.setInputVoltage(0);
  }

  @Override
  public void shootBall(double speed) {
    shooterMotorSim.setInputVoltage(MathUtil.clamp(speed * 12.0, -12.0, 12.0));
  }
}
