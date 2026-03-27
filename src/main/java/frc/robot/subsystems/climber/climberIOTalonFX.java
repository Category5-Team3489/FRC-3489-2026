package frc.robot.subsystems.climber;

import com.ctre.phoenix6.controls.PositionDutyCycle;

public class climberIOTalonFX implements climberIO {
  private final double maxActPos = 20.5;
  private final double minActPos = 0.5;
  private final climberIO.climberIOInputs inputs = new climberIO.climberIOInputs();
  private final com.ctre.phoenix6.hardware.TalonFX climbMotor;

  public climberIOTalonFX(int climbMotorPort) {
    climbMotor = new com.ctre.phoenix6.hardware.TalonFX(climbMotorPort);
  }

  @Override
  public void setClimberSpeed(double speed) {
    double maxEncValue = 188;
    double minEncValue = 1;
    double applied;
    double position = climbMotor.getPosition().getValueAsDouble();
    if (speed > 0) {
      applied = speed;
      if (position >= 188) {
        applied = 0;
      }
    } else if (speed < 0) {
      applied = speed;
      if (position <= 1) {
        applied = 0;
      }
    } else {
      applied = speed;
    }
    // climbMotor.set(speed);
    climbMotor.set(applied);
  }

  @Override
  public void zeroClimber() {
    climbMotor.setPosition(0);
  }

  @Override
  public void toggleClimberPosition(boolean isExtended) {
    PositionDutyCycle dutyCycle = new PositionDutyCycle(isExtended ? minActPos : maxActPos);
    climbMotor.setControl(dutyCycle);
  }
}
