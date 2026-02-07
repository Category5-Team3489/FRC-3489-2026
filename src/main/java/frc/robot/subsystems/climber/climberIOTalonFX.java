package frc.robot.subsystems.climber;

public class climberIOTalonFX implements climberIO {
  private final climberIO.climberIOInputs inputs = new climberIO.climberIOInputs();
  private final com.ctre.phoenix6.hardware.TalonFX climbMotor;

  public climberIOTalonFX(int climbMotorPort) {
    climbMotor = new com.ctre.phoenix6.hardware.TalonFX(climbMotorPort);
  }

  @Override
  public void setClimberSpeed(double speed) {
    climbMotor.set(speed);
  }
}
