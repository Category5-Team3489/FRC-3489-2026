package frc.robot.subsystems.climber;

public class climberIOTalonFXS implements climberIO {
  private final climberIOInputsAutoLogged inputs = new climberIOInputsAutoLogged();
  private final com.ctre.phoenix6.hardware.TalonFXS climbMotor;

  public climberIOTalonFXS(int climbMotorPort) {
    climbMotor = new com.ctre.phoenix6.hardware.TalonFXS(climbMotorPort);
  }

  @Override
  public void setClimberSpeed(double speed) {
    climbMotor.set(speed);
  }
}
