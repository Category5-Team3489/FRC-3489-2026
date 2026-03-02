package frc.robot.subsystems.turrent;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.vision.Vision;
import java.util.function.DoubleSupplier;

public class turrent extends SubsystemBase {
  private turrentIO io;
  private turrentIOInputsAutoLogged inputs = new turrentIOInputsAutoLogged();

  public turrent(turrentIO given) {
    io = given;
  }

  @Override
  public void periodic() {
    // TODO Auto-generated method stub
    io.updateInputs(inputs);
    super.periodic();
  }

  public void setTurrentAngle(double degrees) {
    io.setTurrentAngle(degrees);
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

  public Command lockToTarget(Vision eyes1, int cameraIndex, Vision eyes2, int cameraIndex2) {
    return Commands.run(
        () ->
            setTurrentAngle(
                (eyes1.getTargetX(cameraIndex).getDegrees()
                        + eyes2.getTargetX(cameraIndex2).getDegrees())
                    / 2),
        this);
  }
}
