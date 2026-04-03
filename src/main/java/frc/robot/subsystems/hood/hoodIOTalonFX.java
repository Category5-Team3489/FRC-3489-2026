package frc.robot.subsystems.hood;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;

public class hoodIOTalonFX implements hoodIO {
  private static final double DEGREES_PER_ROTATION = 3.48;
  private static final double DEGREES_OFFSET = 63.035;
  private TalonFX motor;
  private final PositionDutyCycle positionRequest = new PositionDutyCycle(0.0);
  private final double maxActPos = 20.5;
  private final double minActPos = 0.5;
  private boolean isExtended = false;

  public hoodIOTalonFX(int motorID) {
    motor = new TalonFX(motorID);
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0.kP = 0.09;
    config.Slot0.kI = 0.0;
    config.Slot0.kD = 0.0;
    motor.getConfigurator().apply(config);
  }

  @Override
  public double getPos() {
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
    double targetRotations = (degrees - DEGREES_OFFSET) / DEGREES_PER_ROTATION;
    motor.setControl(positionRequest.withPosition(targetRotations));
  }

  @Override
  public void toggleHoodPosition() {
    PositionDutyCycle dutyCycle = new PositionDutyCycle(isExtended ? minActPos : maxActPos);
    this.motor.setControl(dutyCycle);
    isExtended = !isExtended;
  }
}
