package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class intake extends SubsystemBase {
  private final intakeIO parker;
  private intakeIOInputsAutoLogged inputs = new intakeIOInputsAutoLogged();
  private boolean isSpinningIntake = false;
  private boolean isSpinningOuttake = false;

  public intake(intakeIO given) {
    parker = given;
  }

  @Override
  public void periodic() {
    // TODO Auto-generated method stub
    super.periodic();
    parker.updateInputs(inputs);
  }

  public Command spinTheStuff(double input) {
    return Commands.run(() -> parker.spinThatStuff(input));
  }

  public Command noSpin() {
    return Commands.run(() -> parker.stopMotors(), this);
  }

  public Command actuate(double speed) {
    return Commands.run(() -> parker.moveInorOut(speed));
  }

  public Command extend() {
    return Commands.run(() -> parker.extend(), this);
  }

  public Command retract() {
    return Commands.run(() -> parker.retract(), this);
  }

  public Command toggleSpinIntake() {
    return Commands.run(() -> parker.toggleSpinIntake(this.isSpinningIntake), this);
  }

  public Command toggleSpinOuttake() {
    return Commands.run(() -> parker.toggleSpinOuttake(this.isSpinningOuttake), this);
  }

  public boolean isSpinning() {
    return this.isSpinningIntake || this.isSpinningOuttake;
  }
}
