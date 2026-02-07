package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class climber extends SubsystemBase {
  private climberIO io;

  public climber(climberIO given) {
    io = given;
  }

  public void moveClimbMotor(double speed) {
    io.setClimberSpeed(speed);
  }
}
