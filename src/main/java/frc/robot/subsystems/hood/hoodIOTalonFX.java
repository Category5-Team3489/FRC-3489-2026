package frc.robot.subsystems.hood;

import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;

public class hoodIOTalonFX implements hoodIO {
  private static final double ANGLE_GEAR_RATIO = 6.0;
  private TalonFX motor;

  public hoodIOTalonFX(int motorID) {
    motor = new TalonFX(motorID);
  }

  @Override
  public void turnHood(double speed) {
    motor.set(speed);
  }

  @Override
  public void setHoodPos(double pos) {
    motor.setPosition(pos);
  }

  @Override
  public void setHoodAngleDegrees(double degrees) {
    double targetRotations = (degrees / 360.0) * ANGLE_GEAR_RATIO;
    motor.setControl(new PositionDutyCycle(targetRotations));
  }
}
