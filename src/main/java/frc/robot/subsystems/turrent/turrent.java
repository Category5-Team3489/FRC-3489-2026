package frc.robot.subsystems.turrent;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class turrent extends SubsystemBase {
  private turrentIO io;

  public turrent(turrentIO given) {
    io = given;
  }

  public void setTurrentAngle(double degrees) {
    io.setTurrentAngle(degrees);
  }
}
