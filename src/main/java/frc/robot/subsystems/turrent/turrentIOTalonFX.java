package frc.robot.subsystems.turrent;

import com.ctre.phoenix6.controls.PositionDutyCycle;

public class turrentIOTalonFX implements turrentIO {
  // Create motors
  private final com.ctre.phoenix6.hardware.TalonFX topMotor;
  private final turrentIOInputsAutoLogged inputs = new turrentIOInputsAutoLogged();

  public turrentIOTalonFX(int topMotorPort) {
    topMotor = new com.ctre.phoenix6.hardware.TalonFX(topMotorPort);
  }

  @Override
  public void updateInputs(turrentIOInputs inputs) {
    inputs.topMotorCurrent = topMotor.getSupplyVoltage().getValueAsDouble();
    inputs.turrentAngle = topMotor.getPosition().getValueAsDouble();
    inputs.turentTurn.setAngle(inputs.turrentAngle);
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
