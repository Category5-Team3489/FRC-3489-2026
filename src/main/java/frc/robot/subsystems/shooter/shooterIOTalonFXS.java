package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.hardware.TalonFXS;
import java.util.function.DoubleSupplier;

public class shooterIOTalonFXS implements shooterIO {
  // Create motors
  private final TalonFXS shooterMotor;

  private final shooterIOInputs inputs = new shooterIOInputs();

  public shooterIOTalonFXS(int shooterMotorPort) {
    shooterMotor = new TalonFXS(shooterMotorPort);

    CurrentLimitsConfigs limits = new CurrentLimitsConfigs();
    limits.SupplyCurrentLimitEnable = true;
    limits.SupplyCurrentLimit = 45;
    shooterMotor.getConfigurator().apply(limits);
  }

  @Override
  public void setShooterVoltage(double volts) {
    shooterMotor.setVoltage(volts);
  }

  @Override
  public void updateInputs(shooterIOInputs inputs) {
    // Phoenix6 StatusSignal APIs return signal objects; read numeric values
    inputs.topMotorCurrent = shooterMotor.getSupplyCurrent().getValueAsDouble();
    inputs.bottomMotorCurrent = shooterMotor.getSupplyCurrent().getValueAsDouble();
    inputs.distanceToTarget = 0.0; // This would need a sensor to be implemented
  }

  @Override
  public void setShootVoltageSupp(DoubleSupplier why) {
    shooterMotor.setVoltage(why.getAsDouble());
  }

  @Override
  public void stopMotors() {
    // TODO Auto-generated method stub
    shooterMotor.set(0);
  }

  @Override
  public void shootBall(double speed) {
    // Check this code fs
    shooterMotor.set(speed);
  }
}
