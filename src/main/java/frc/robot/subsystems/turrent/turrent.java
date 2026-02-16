package frc.robot.subsystems.turrent;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.vision.Vision;

public class turrent extends SubsystemBase {
  private turrentIO io;
  // private turrentIOInputsAutoLogged inputs = new turrentIOInputsAutoLogged();

  public turrent(turrentIO given) {
    io = given;
  }

  // @Override
  // public void periodic() {
  //   // TODO Auto-generated method stub
  //   // io.updateInputs(inputs);
  //   super.periodic();
  // }

  public void setTurrentAngle(double degrees) {
    io.setTurrentAngle(degrees);
  }

  public Command turnTurrentYAY(double speedy) {
    return Commands.run(() -> io.turnTurrent(speedy), this);
  }

  public Command lockToTarget(Vision eyes, int cameraIndex) {
    return Commands.run(() -> setTurrentAngle((eyes.getTargetX(cameraIndex).getDegrees())), this);
  }
}
