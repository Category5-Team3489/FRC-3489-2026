package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class intake extends SubsystemBase {
  private final intakeIO parker;

  public intake(intakeIO given) {
    parker = given;
  }

  public Command spinTheStuff(double input) {
    return Commands.run(() -> parker.spinThatStuff(1));
  }

  public Command noSpin() {
    return Commands.run(() -> parker.stopMotors(), this);
  }
}
