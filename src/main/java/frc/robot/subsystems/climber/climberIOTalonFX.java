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
    climbMotor.set(speed);
  }

  @Override
  public void toggleClimberPosition(boolean isExtended) {
    PositionDutyCycle dutyCycle = new PositionDutyCycle(isExtended ? minActPos : maxActPos);
    climbMotor.setControl(dutyCycle);
  }
}
