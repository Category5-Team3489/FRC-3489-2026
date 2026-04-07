package frc.robot.subsystems.turrent;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.Logger;

public class turrent extends SubsystemBase {
  private turrentIO io;
  private turrentIOInputsAutoLogged inputs = new turrentIOInputsAutoLogged();

  public turrent(turrentIO given) {
    io = given;
  }

  public double posToDeg(DoubleSupplier pos) {
    return ((0.13265) * pos.getAsDouble()) - 0.047;
  }

  public double degToPos(DoubleSupplier deg) {
    return ((7.53851) * deg.getAsDouble()) + 0.359449;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Turrent", inputs);
    super.periodic();
  }

  public void setTurrentAngle(DoubleSupplier degrees) {
    io.setTurrentAngle(degrees.getAsDouble());
  }

  public void resetTurrentAngle() {
    io.resetTurrentAngle();
  }

  public Command turnTurrentYAY(double speedy) {
    return Commands.run(() -> io.turnTurrent(speedy), this);
  }

  // Supplier-based overload so the joystick is sampled each scheduler cycle.
  public Command turnTurrent(DoubleSupplier speedSupplier) {
    return Commands.run(() -> io.turnTurrent(speedSupplier.getAsDouble()), this);
  }
}
