package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class intake extends SubsystemBase {
  private final TalonFX parker = new TalonFX(22);

  public Command spinTheStuff(double input) {
    return Commands.run(() -> parker.set(input));
  }

  public intake() {}

  public Command noSpin() {
    return Commands.run(() -> parker.set(0), this);
  }
}
