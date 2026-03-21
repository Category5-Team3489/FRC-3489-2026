package frc.robot.subsystems.indexer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class index extends SubsystemBase {
  private final indexIO io;

  public index(indexIO module) {
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

  public Command agitate() {
    return Commands.sequence(
        Commands.run(() -> spinMotor(0.6), this).withTimeout(0.5),
        Commands.run(() -> spinMotor(-0.6), this).withTimeout(0.5),
        Commands.runOnce(() -> spinMotor(0.0), this));
  }
}
