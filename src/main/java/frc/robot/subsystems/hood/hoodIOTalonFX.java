package frc.robot.subsystems.hood;

import com.ctre.phoenix6.hardware.TalonFX;

public class hoodIOTalonFX implements hoodIO {
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
}
