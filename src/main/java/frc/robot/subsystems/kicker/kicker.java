package frc.robot.subsystems.kicker;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class kicker extends SubsystemBase {
  private final kickerIO io;

  public kicker(kickerIO module) {
    io = module;
  }

  @Override
  public void periodic() {
    // TODO Auto-generated method stub
    super.periodic();
  }

  public void spinMotor(double speed) {
    io.turnMotor(speed);
  }
}
