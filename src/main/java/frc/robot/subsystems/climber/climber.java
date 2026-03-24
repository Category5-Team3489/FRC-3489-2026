package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

public class climber extends SubsystemBase {
  private climberIO io;
  private boolean isExtended = false;

  public climber(climberIO given) {
    io = given;
  }

  @Override
  public void periodic() {
    // TODO Auto-generated method stub
    super.periodic();
  }

  public Command moveClimbMotor(DoubleSupplier speed) {
    return Commands.run(() -> io.setClimberSpeed(speed.getAsDouble()), this);
  }

  public Command toggleClimberPosition() {
    return Commands.run(() -> io.toggleClimberPosition(this.isExtended), this);
  }
}
