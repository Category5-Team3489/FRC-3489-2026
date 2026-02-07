package frc.robot.subsystems.turrent;

import com.ctre.phoenix6.controls.PositionDutyCycle;

public class turrentIOTalonFXS implements turrentIO {
  // Create motors
  private final com.ctre.phoenix6.hardware.TalonFXS topMotor;
  private final turrentIOInputs inputs = new turrentIOInputs();

  public turrentIOTalonFXS(int topMotorPort) {
    topMotor = new com.ctre.phoenix6.hardware.TalonFXS(topMotorPort);
  }

  @Override
  public void updateInputs(turrentIOInputs inputs) {
    inputs.topMotorCurrent = topMotor.getSupplyVoltage().getValueAsDouble();
    inputs.turrentAngle = topMotor.getPosition().getValueAsDouble();
  }

  @Override
  public void setTurrentAngle(double degrees) {
    double rotations =
        (degrees / 360.0)
            * inputs.gearRatio; // Convert degrees to rotations, accounting for gear ratio

    // Copied from the shooterIOTalonFX, check if this is correct
    PositionDutyCycle request = new PositionDutyCycle(rotations);
    topMotor.setControl(request);
  }
}
