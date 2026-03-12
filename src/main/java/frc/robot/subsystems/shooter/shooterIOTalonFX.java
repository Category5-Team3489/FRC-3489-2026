package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.hardware.TalonFX;
import java.util.function.DoubleSupplier;

public class shooterIOTalonFX implements shooterIO {
  // Create motors
  private final TalonFX shooterMotor;
  private final TalonFX shootMotorOther; // Example of a second motor if needed

  public shooterIOTalonFX(int shooterMotorPort, int shooterMotorPortOther) {
    shooterMotor = new TalonFX(shooterMotorPort);
    shootMotorOther = new TalonFX(shooterMotorPortOther);
  }

  @Override
  public void updateInputs(shooterIOInputs inputs) {
    // TODO Auto-generated method stub
    // Phoenix6 StatusSignal APIs return signal objects; read numeric values
    inputs.topMotorCurrent = shooterMotor.getSupplyCurrent().getValueAsDouble();
    inputs.bottomMotorCurrent = shootMotorOther.getSupplyCurrent().getValueAsDouble();
    inputs.distanceToTarget = 0.0; // This would need a sensor to be implemented
  }

  @Override
  public void stopMotors() {
    // TODO Auto-generated method stub
    shooterMotor.set(0);
    shootMotorOther.set(0);
  }

  @Override
  public void setShootVoltageSupp(DoubleSupplier why) {
    shooterMotor.set(-why.getAsDouble());
    shootMotorOther.set(why.getAsDouble());
  }

  @Override
  public void shootBall(double speed) {
    // Check this code fs
    shooterMotor.set(-speed);
    shootMotorOther.set(speed);
  }

  @Override
  public void setShooterVoltage(double volts) {
    shooterMotor.setVoltage(volts);
  }
}
