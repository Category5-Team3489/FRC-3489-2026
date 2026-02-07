package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFXS;

public class shooterIOTalonFXS implements shooterIO {
  // Create motors
  private final TalonFXS angleMotor;
  private final TalonFXS shooterMotor;

  private final shooterIOInputs inputs = new shooterIOInputs();

  public shooterIOTalonFXS(int shooterMotorPort, int angleMotorPort) {
    angleMotor = new TalonFXS(angleMotorPort);
    shooterMotor = new TalonFXS(shooterMotorPort);
  }

  @Override
  public void updateInputs(shooterIOInputs inputs) {
    // TODO Auto-generated method stub
    // Phoenix6 StatusSignal APIs return signal objects; read numeric values
    inputs.topMotorCurrent = shooterMotor.getSupplyCurrent().getValueAsDouble();
    inputs.shootAngle = angleMotor.getPosition().getValueAsDouble() * 360.0;
    inputs.bottomMotorCurrent = angleMotor.getSupplyCurrent().getValueAsDouble();
    inputs.distanceToTarget = 0.0; // This would need a sensor to be implemented
  }

  @Override
  public void stopMotors() {
    // TODO Auto-generated method stub
    shooterMotor.set(0);
    angleMotor.set(0);
  }

  @Override
  public void shootBall(double speed) {
    // Check this code fs
    shooterMotor.set(speed);
  }

  @Override
  public void setShootAngle(double degrees) {
    // Check this code fs
    double rotations =
        (degrees / 360.0)
            * inputs.gearRatio; // Convert degrees to rotations, accounting for gear ratio
    PositionDutyCycle request = new PositionDutyCycle(rotations);
    angleMotor.setControl(request);
  }
}
