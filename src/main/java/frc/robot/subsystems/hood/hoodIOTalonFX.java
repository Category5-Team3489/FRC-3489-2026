package frc.robot.subsystems.hood;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;

public class hoodIOTalonFX implements hoodIO {
  private static final double ANGLE_GEAR_RATIO = 9.0;
  private TalonFX motor;
  private final PositionDutyCycle positionRequest = new PositionDutyCycle(0.0);

  public hoodIOTalonFX(int motorID) {
    motor = new TalonFX(motorID);
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0.kP = 0.09;
    config.Slot0.kI = 0.0;
    config.Slot0.kD = 0.0;
    motor.getConfigurator().apply(config);
  }

  @Override
  public double getDegrees() {
    return motor.getPosition().getValueAsDouble();
  }

  @Override
  public void turnHood(double speed) {
    motor.set(speed);
  }

  @Override
  public void setHoodPos(double pos) {
    // if (motor.getPosition().getValueAsDouble() > pos + 2
    // || motor.getPosition().getValueAsDouble() < pos - 2) {
    motor.setControl(positionRequest.withPosition(pos));
    // }
  }

  @Override
  public void setHoodAngleDegrees(double degrees) {
    double targetRotations = ((70 + degrees) / 360.0) * ANGLE_GEAR_RATIO;
    motor.setControl(positionRequest.withPosition(targetRotations));
  }
}
