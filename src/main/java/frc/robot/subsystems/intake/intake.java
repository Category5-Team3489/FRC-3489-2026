package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class intake extends SubsystemBase {
  private final IntakeIO parker;

  public intake(IntakeIO given){
    parker = given;
  }

  public Command spinTheStuff(double input) {
    return Commands.run(() -> parker.spinThatStuff());
  }

  public intake() {}

  public Command noSpin() {
    return Commands.run(() -> parker.stopMotors(), this);
  }
}
